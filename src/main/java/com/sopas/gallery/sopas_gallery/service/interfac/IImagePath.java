package com.sopas.gallery.sopas_gallery.service.interfac;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface IImagePath {
    ResponseEntity<Resource> getImage(String username, Long imageId);
}
