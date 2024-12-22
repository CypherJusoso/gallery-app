package com.sopas.gallery.sopas_gallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.LikeDislike;
import com.sopas.gallery.sopas_gallery.service.interfac.ILikeDislikeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/like-dislike")
public class LikeDislikeController {

    @Autowired
    private ILikeDislikeService likeDislikeService;

    @PostMapping("/add/{imageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Response> addLikeDislike(@RequestBody LikeDislike likeDislike, @PathVariable Long imageId) {
        
        Response response = likeDislikeService.addLikeDislike(likeDislike, imageId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/likes/{imageId}")
    public ResponseEntity<Response> getLikesByImageId(@PathVariable Long imageId) {
        
        Response response = likeDislikeService.getLikesByImageId(imageId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/dislikes/{imageId}")
    public ResponseEntity<Response> getDislikesByImageId(@PathVariable Long imageId) {
        
        Response response = likeDislikeService.getDislikesByImageId(imageId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
}
