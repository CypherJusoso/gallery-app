package com.sopas.gallery.sopas_gallery.service.interfac;

import java.util.List;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.entity.Tag;



public interface IImageService {

    Response uploadImage(Image image);

    Response getImagesByUserId(Long userId);

    Response getImagesByTags(List<Tag> tags);

    Response getImageById(Long imageId);

    Response deleteImage(Long imageId);

    Response getAllImages();

    //Title, Tags
    Response updateImage(Long imageId, String title, String tags);

}
