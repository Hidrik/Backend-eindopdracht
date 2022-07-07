package nl.novi.backend.eindopdracht.hidriklandlust.utils;

import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.BadRequestException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.InternalFailureException;
import nl.novi.backend.eindopdracht.hidriklandlust.exceptions.RecordNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageImpl implements FileStorage {

    private final Path root = Paths.get("uploads");
    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new InternalFailureException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Path createDirectory(Long componentId) {
        Path directory = Paths.get("uploads", String.valueOf(componentId));
        if (!Files.exists(directory)) {
            try {
                Files.createDirectory(directory);
            } catch (IOException e) {
                throw new InternalFailureException("Could not initialize folder for upload!");
            }
        }

        return directory;
    }

    @Override
    public String save(MultipartFile file, Long componentId) {
        Path directory = createDirectory(componentId);
        try {
            Files.copy(file.getInputStream(), directory.resolve(file.getOriginalFilename()));
            return directory.resolve(file.getOriginalFilename()).toString();
        } catch (Exception e) {
            throw new InternalFailureException("Could not store the file. Error: " + e.getMessage());
        }
    }
    @Override
    public Resource load(String fileName, Long componentId) {
        Path directory = createDirectory(componentId);
        try {
            Path file = directory.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new InternalFailureException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new InternalFailureException("Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(String fileName, Long componentId) {
        Path directory = createDirectory(componentId);
        Path path = directory.resolve(fileName);
        File file = path.toFile();
        if (!file.exists()) throw new RecordNotFoundException("Cant find file with filename: " + fileName);
        if (!file.delete()) throw new BadRequestException("Cant delete file with filename " + fileName);
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Boolean fileExists(String fileName, Long componentId) {
        Path directory = createDirectory(componentId);
        Path file = directory.resolve(fileName);

        return Files.exists(file);
    }


}
