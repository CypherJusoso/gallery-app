package com.sopas.gallery.sopas_gallery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.entity.LikeDislike;
import com.sopas.gallery.sopas_gallery.entity.User;



public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {

    List<LikeDislike> findByImage(Image image);

    List<LikeDislike> findByUser(User user);

    long countByImageAndIsLikeTrue(Image image);
    
    long countByImageAndIsLikeFalse(Image image);

    LikeDislike findByUserIdAndImageId(Long id, Long id2);

    List<LikeDislike> findByImageIdAndIsLike(Long imageId, Boolean isLike);

}
