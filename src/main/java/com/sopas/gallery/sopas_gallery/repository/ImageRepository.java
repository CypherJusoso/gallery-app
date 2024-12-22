package com.sopas.gallery.sopas_gallery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.entity.Tag;


public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUserId(Long userId);

    @Query("SELECT DISTINCT i FROM Image i JOIN i.tags t WHERE t IN :tags GROUP BY i.id HAVING COUNT (DISTINCT t) = :tagCount")
    List<Image> findByTags(@Param("tags") List<Tag> tags, @Param("tagCount") long tagCount);

    List<Image> findAllByOrderByUploadDateDesc();

    @Query("SELECT i FROM Image i JOIN i.tags t WHERE t.id = :tagId")
    List<Image> findByTagId(@Param("tagId") Long tagId);
    
    @Query("SELECT COUNT(i) FROM Image i JOIN i.tags t WHERE t.id = :tagId")
    int countImagesByTagId(@Param("tagId") Long tagId);
}
