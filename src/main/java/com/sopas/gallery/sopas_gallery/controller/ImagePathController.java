package com.sopas.gallery.sopas_gallery.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopas.gallery.sopas_gallery.service.interfac.IImagePath;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("image-path")
public class ImagePathController {

    @Autowired
    private IImagePath imagePathService;

    @GetMapping("/{username}/{imageId:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String username, @PathVariable Long imageId) {
        return imagePathService.getImage(username, imageId);
    }
}
