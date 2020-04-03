package com.gft.assignment.tradecapture.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
@AllArgsConstructor
public class PathMonitorExecutor implements CommandLineRunner {
    private ExecutorService executorService;

    private PathMonitor pathMonitor;

    @Override
    public void run(String... args) throws Exception {
        executorService.submit(pathMonitor);
    }

}
