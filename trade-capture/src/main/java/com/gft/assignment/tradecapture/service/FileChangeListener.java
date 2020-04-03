package com.gft.assignment.tradecapture.service;

import java.nio.file.Path;

public interface FileChangeListener {
    void fileCreated(Path path);
    void fileModified(Path path);
}
