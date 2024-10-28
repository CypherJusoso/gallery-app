package com.sopas.gallery.sopas_gallery.service.interfac;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.LikeDislike;

public interface ILikeDislikeService {

    Response addLikeDislike(LikeDislike likeDislike);

    Response getLikesByImageId(Long imageId);

    Response getDislikesByImageId(Long imageId);

    Response deleteLikeDislike(Long likeDislikeId);
}
