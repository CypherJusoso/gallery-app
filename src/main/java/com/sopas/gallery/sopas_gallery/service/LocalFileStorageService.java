package com.sopas.gallery.sopas_gallery.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService {

    private final String baseUploadDir = "images/";
    private final long MAX_FILE_SIZE_MB = 5 * 1024 * 1024; //5MB en bytes

    private final String[] ALLOWED_MIME_TYPES = {"image/jpeg", "image/png", "image/webp"};

    public LocalFileStorageService(){
        createBaseUploadDirIfNotExists();
    }

    //Creo el directorio si no existe
    private void createBaseUploadDirIfNotExists(){
        File directory = new File(baseUploadDir);
        if(!directory.exists()){
            directory.mkdirs();
        }
    }

    public String saveImage(MultipartFile file, String userName) throws java.io.IOException{

        //Validar archivo
        String contentType = file.getContentType();
        if(!isAllowedMimeType(contentType)){
            throw new IllegalArgumentException("Only JPEG, PNG, and WEBP image formats are allowed.");
        }

        if(file.getSize() > MAX_FILE_SIZE_MB){
            throw new IllegalArgumentException("File size exceeds the limit of 5MB.");
        }

        //Genero el directorio único para el usuario
        String userDir = baseUploadDir + userName.toString() + '/';
        createUserDirIfNotExists(userDir);

        //Generar nombre único para el archivo
        String fileName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(userDir + fileName);

        //Salvo los archivos en el directorio local
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }
    
    private void createUserDirIfNotExists(String userDir){
        File directory = new File(userDir);
        if(!directory.exists()){
            directory.mkdirs();
        }
    }
    private boolean isAllowedMimeType(String mimeType){
        for (String allowedMimeType : ALLOWED_MIME_TYPES){
            if(allowedMimeType.equals(mimeType)){
                return true;
            }
        }
        return false;
    }
}
