package nl.novi.backend.eindopdracht.HidrikLandlust.utils;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    void init();
    Path createDirectory(Long componentId);
    String save(MultipartFile file, Long componentId);
    Resource load(String fileName, Long componentId);
    void delete(String fileName, Long componentId);
    void deleteAll();
    Boolean fileExists(String fileName, Long componentId);
    /*  Stream<Path> loadAll();*/
}