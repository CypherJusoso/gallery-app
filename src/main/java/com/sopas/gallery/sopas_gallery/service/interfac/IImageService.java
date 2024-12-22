package com.sopas.gallery.sopas_gallery.service.interfac;


import org.springframework.web.multipart.MultipartFile;

import com.sopas.gallery.sopas_gallery.dto.Response;



public interface IImageService {

    Response uploadImage(MultipartFile photo, String tagsInput);

    Response getImagesByTags(String tags);

    Response getImageById(Long imageId);

    Response deleteImage(Long imageId);

    Response getAllImages();
 
    Response updateImage(Long imageId, MultipartFile photo, String tagsInput);

    int countImagesByTag(Long tagId);
}
