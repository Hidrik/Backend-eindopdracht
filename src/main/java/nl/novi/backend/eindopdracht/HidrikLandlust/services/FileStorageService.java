package nl.novi.backend.eindopdracht.HidrikLandlust.services;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void init();
    String save(MultipartFile file);
    Resource load(String filename);
    void delete(String filename);
    void deleteAll();
    Stream<Path> loadAll();
}