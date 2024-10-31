package com.sopas.gallery.sopas_gallery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopas.gallery.sopas_gallery.entity.Comment;





public interface CommentRepository extends JpaRepository<Comment, Long> {
   
    List<Comment> findByImageId(Long imageId);
    List<Comment> findByUserId(Long userId);
}
