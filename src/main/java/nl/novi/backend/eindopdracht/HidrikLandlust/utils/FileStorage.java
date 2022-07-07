package nl.novi.backend.eindopdracht.HidrikLandlust.utils;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorage {
    void init();
    Path createDirectory(Long componentId);
    String save(MultipartFile file, Long componentId);
    Resource load(String fileName, Long componentId);
    void delete(String fileName, Long componentId);
    void deleteAll();
    Boolean fileExists(String fileName, Long componentId);
}