package com.sopas.gallery.sopas_gallery.service.interfac;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.Tag;



public interface IImageService {

    Response uploadImage(MultipartFile photo, String tagsInput);

    Response getImagesByUserId(Long userId);

    Response getImagesByTags(List<Tag> tags);

    Response getImageById(Long imageId);

    Response deleteImage(Long imageId);

    Response getAllImages();

 
    Response updateImage(Long imageId, MultipartFile photo, String tagsInput);

}
