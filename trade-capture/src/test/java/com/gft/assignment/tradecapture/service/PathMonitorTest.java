package com.gft.assignment.tradecapture.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PathMonitorTest {
    @Test
    void shouldNotifyListenersWhenNewFileIsCreated() throws IOException {
        FileChangeListener listener = mock(FileChangeListener.class);
        String dirName = "pathmonitor";
        Path tempDirectory = Files.createTempDirectory(dirName);
        Executors.newSingleThreadExecutor().submit(new PathMonitor(List.of(listener), tempDirectory));

        Path tempFilePath = Files.createTempFile(tempDirectory, "newtestfile", null);
        verify(listener, timeout(50)).fileCreated(eq(tempFilePath));
        verifyNoMoreInteractions(listener);

        Files.delete(tempFilePath);
        Files.delete(tempDirectory);
    }

    @Test
    void shouldNotifyListenersWhenFileIsModified() throws IOException {
        String dirName = "pathmonitor";
        Path tempDirectory = Files.createTempDirectory(dirName);

        Path tempFilePath = Files.createTempFile(tempDirectory, "newfileformodification", null);
        FileChangeListener listener = mock(FileChangeListener.class);
        Executors.newSingleThreadExecutor().submit(new PathMonitor(List.of(listener), tempDirectory));

        Files.writeString(tempFilePath, "test content");
        verify(listener, timeout(50).atLeastOnce()).fileModified(eq(tempFilePath));
        verifyNoMoreInteractions(listener);

        Files.delete(tempFilePath);
        Files.delete(tempDirectory);
    }
}