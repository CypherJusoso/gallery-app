package com.sopas.gallery.sopas_gallery.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.repository.ImageRepository;
import com.sopas.gallery.sopas_gallery.service.interfac.IImagePath;

@Service
public class ImagePath implements IImagePath{
  private final ResourceLoader resourceLoader;
    private final ImageRepository imageRepository;

    @Autowired
    public ImagePath(ResourceLoader resourceLoader, ImageRepository imageRepository) {
        this.resourceLoader = resourceLoader;
        this.imageRepository = imageRepository;
    }
    @Override
    public ResponseEntity<Resource> getImage(String username, Long imageId) {
        Optional<Image> imageOptional = imageRepository.findById(imageId);
        if(!imageOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }

        Image image = imageOptional.get();

        String photoDir = image.getPhotoDir();

        Resource imageResource = resourceLoader.getResource("file:" + photoDir);

        if(imageResource.exists() && imageResource.isReadable()){
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
