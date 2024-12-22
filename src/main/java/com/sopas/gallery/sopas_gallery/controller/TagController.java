package com.sopas.gallery.sopas_gallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.Tag;
import com.sopas.gallery.sopas_gallery.service.interfac.ITagService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private ITagService tagService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Response> addTag(@Valid @RequestBody Tag tag) {
    
        Response response = tagService.addTag(tag);
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllTags() {
        Response response = tagService.getAllTags();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
    @GetMapping("/get-by-id/{tagId}")
    public ResponseEntity<Response> getTagById(@PathVariable Long tagId) {
        Response response = tagService.getTagById(tagId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("get-tag-by-name")
    public ResponseEntity<Response> getTagByName(@RequestParam String tagName) {
        Response response = tagService.getTagByName(tagName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{tagId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response> deleteTagById(@PathVariable Long tagId) {
        Response response = tagService.deleteTag(tagId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}