package com.sopas.gallery.sopas_gallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.dto.UpdateCommentRequest;
import com.sopas.gallery.sopas_gallery.entity.Comment;
import com.sopas.gallery.sopas_gallery.service.interfac.ICommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @PostMapping("/add/{imageId}")
    public ResponseEntity<Response> addComement(@PathVariable Long imageId, @RequestBody Comment comment) {
    
        Response response = commentService.addComent(imageId, comment);
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("get-by-image/{imageId}")
    public ResponseEntity<Response> getCommentsByImageId(@PathVariable Long imageId) {
        
        Response response = commentService.getCommentsByImageId(imageId);

        return ResponseEntity.status(response.getStatusCode()).body(response);

    }
    
    @GetMapping("get-by-user/{userId}")
    public ResponseEntity<Response> getCommentsByUserId(@PathVariable Long userId) {
               
        Response response = commentService.getCommentsByUserId(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("delete/{commentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Response> deleteCommentById(@PathVariable Long commentId){
        Response response = commentService.deleteComment(commentId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{commentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Response> updateComment(
        @PathVariable Long commentId,
        @RequestBody(required = true) UpdateCommentRequest request
        ) {
        Response response = commentService.updateComment(commentId, request.getNewContent());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
