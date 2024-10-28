package com.sopas.gallery.sopas_gallery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.entity.Tag;



public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    List<Tag> findByImages(List<Image> images);

    List<Tag> findByNameContainingIgnoreCase(String namePart);
}
