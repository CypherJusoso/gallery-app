package com.sopas.gallery.sopas_gallery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopas.gallery.sopas_gallery.entity.Comment;
import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.entity.User;



public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByImage(Image image);

    List<Comment> findByUser(User user);
}
