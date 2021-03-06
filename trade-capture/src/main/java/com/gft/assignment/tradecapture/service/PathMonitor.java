package com.gft.assignment.tradecapture.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@Slf4j
@Service
public class PathMonitor implements Runnable {
    private final List<FileChangeListener> fileChangeListeners;
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys = new HashMap<>(1);

    private boolean running;

    public PathMonitor(List<FileChangeListener> fileChangeListeners, @Value("${app.monitor.dir}") Path dir)
            throws IOException {
        this.fileChangeListeners = fileChangeListeners;
        watcher = FileSystems.getDefault().newWatchService();
        register(dir);
    }

    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
        keys.put(key, dir);
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            log.debug("Waiting for directory event...");
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                running = false;
                break;
            }
            Path dir = keys.get(key);
            if (dir == null) {
                log.warn("Ignoring watch key for unknown location {}", key.watchable());
                continue;
            }

            List<WatchEvent<?>> watchEvents = key.pollEvents();
            log.debug("Encountered {} pending event(s) for processing", watchEvents.size());
            //Watch service fires events really early thus making occasional IOException
            tryWaitForIOFinish();
            processPendingEvents(dir, watchEvents);

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {

                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
        running = false;
    }

    private void tryWaitForIOFinish() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            log.debug("Waiting interrupted...");
            Thread.currentThread().interrupt();
        }
    }

    private void processPendingEvents(Path dir, List<WatchEvent<?>> watchEvents) {
        watchEvents.stream()
                .filter(e -> e.kind() != OVERFLOW)
                .map(this::cast)
                .forEach(e -> processEvent(e.kind(), dir.resolve(e.context())));
    }

    private void processEvent(WatchEvent.Kind<Path> kind, Path path) {
        log.info("Processing new FS event {} {}", kind.name(), path);

        if (ENTRY_MODIFY.equals(kind)) {
            fileChangeListeners.forEach(listener -> listener.fileModified(path));
        } else if (ENTRY_CREATE.equals(kind)) {
            fileChangeListeners.forEach(listener -> listener.fileCreated(path));
        }
    }

    @SuppressWarnings("unchecked")
    private WatchEvent<Path> cast(WatchEvent<?> event) {
        return (WatchEvent<Path>) event;
    }
}
