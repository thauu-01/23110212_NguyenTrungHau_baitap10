package baitap10.service;

import baitap10.config.StorageProperties;
import baitap10.exception.StorageException;
import baitap10.exception.StorageFileNotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageServiceImpl implements IStorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String getSorageFilename(MultipartFile file, String id) {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        return "p" + id + "." + ext;
    }

    @Override
    public void store(MultipartFile file, String storeFilename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(storeFilename))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new StorageException("Failed to store file: ", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            throw new StorageFileNotFoundException("Cannot read file: " + filename);
        } catch (Exception e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void delete(String storeFilename) {
        try {
            Path destinationFile = rootLocation.resolve(Paths.get(storeFilename)).normalize().toAbsolutePath();
            Files.delete(destinationFile);
        } catch (Exception e) {
            throw new StorageException("Failed to delete file: ", e);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            System.out.println(rootLocation.toString());
        } catch (Exception e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}