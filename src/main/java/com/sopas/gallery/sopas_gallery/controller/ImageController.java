package com.sopas.gallery.sopas_gallery.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.service.interfac.IImageService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private IImageService imageService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Response> uploadNewImage(
        @RequestPart(value = "photo", required = true) MultipartFile photo,
        @RequestParam(required = true) String tagsInput
        ) {
        Response response = imageService.uploadImage(photo, tagsInput);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
    @PutMapping("/update/{imageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Response> updateImage(
        @PathVariable Long imageId,
        @RequestPart(value = "photo", required = true) MultipartFile photo,
        @RequestParam(required = true) String tagsInput
        ) {
        Response response = imageService.updateImage(imageId, photo, tagsInput);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    

    @GetMapping("/all")
    public ResponseEntity<Response> getAllImages() {
       Response response = imageService.getAllImages();
       return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/image-by-id/{imageId}")
    public ResponseEntity<Response> getImageById(@PathVariable Long imageId) {
       Response response = imageService.getImageById(imageId);
       return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/by-tags")
    public ResponseEntity<Response> getImagesByTags(@RequestParam String tags) {
        Response response = imageService.getImagesByTags(tags);
       return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{imageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Response> deleteImage(@PathVariable Long imageId){
        Response response = imageService.deleteImage(imageId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("image-count-by-tag/{tagId}")
    public ResponseEntity<Response> countImageByTag(@PathVariable Long tagId) {
        Response response = new Response();
        try {
            int imageCount = imageService.countImagesByTag(tagId);
            response.setStatusCode(200);
            response.setMessage("Image count found successfully");
            response.setData(imageCount);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Fetching Image Count: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }
}
