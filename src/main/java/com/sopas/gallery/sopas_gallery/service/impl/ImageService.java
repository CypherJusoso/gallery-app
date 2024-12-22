package com.sopas.gallery.sopas_gallery.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sopas.gallery.sopas_gallery.dto.ImageDTO;
import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.entity.Tag;
import com.sopas.gallery.sopas_gallery.entity.User;
import com.sopas.gallery.sopas_gallery.exception.OurException;
import com.sopas.gallery.sopas_gallery.repository.ImageRepository;
import com.sopas.gallery.sopas_gallery.repository.TagRepository;
import com.sopas.gallery.sopas_gallery.repository.UserRepository;
import com.sopas.gallery.sopas_gallery.service.LocalFileStorageService;
import com.sopas.gallery.sopas_gallery.service.interfac.IImageService;
import com.sopas.gallery.sopas_gallery.utils.Utils;

import java.time.LocalDateTime;

@Service
public class ImageService implements IImageService {

    @Autowired
    private ImageRepository imageRepository;
   
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LocalFileStorageService fileStorageService;

    @Autowired
    private TagRepository tagRepository;

    @Override
    @Transactional
    public Response uploadImage( MultipartFile photo, String tagsInput) {
        Response response = new Response();

        if(photo == null || photo.isEmpty()){
            response.setStatusCode(400);
            response.setMessage("No image file provided or file is empty.");
            return response;
        }
        
            String currentUserName = getCurrentUsername();
            if(currentUserName == null){
                response.setStatusCode(403);
                response.setMessage("User is not authenticated.");
                return response;
            }    

            Optional<User> userOptional = userRepository.findByUsername(currentUserName);

            if(!userOptional.isPresent()){
                response.setStatusCode(404);
                response.setMessage("User Not Found");
                return response;
            }
            
            try {
                User user = userOptional.get();
                String photoDir = fileStorageService.saveImage(photo, user.getUsername());

        Image image = new Image();
        image.setUser(user);
        image.setPhotoDir(photoDir);
        image.setUploadDate(LocalDateTime.now());

        // Procesar y asignar etiquetas
        List<Tag> tags = processTags(tagsInput);
        image.setTags(tags);

        // Guardar imagen y mapear DTO
        imageRepository.save(image);
        ImageDTO imageDTO = Utils.mapImageEntityToDto(image);

        // Configurar respuesta exitosa
        response.setStatusCode(201);
        response.setMessage("Image successfully created.");
        response.setImageDTO(imageDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a New Image " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Response getImagesByTags(String tags) {
        Response response = new Response();


        if(tags== null || tags.isEmpty()){
            response.setStatusCode(400);
            response.setMessage("No Tags Provided.");
            return response;
        }
        try {
            List<Tag> tagList = findTags(tags);
            List<Image> images = imageRepository.findByTags(tagList, tagList.size());
            
            if(images.isEmpty()){
                response.setStatusCode(404);
                response.setMessage("No images found for the provided tags.");
                return response;
            }
            List<ImageDTO> imageDTOs = Utils.mapImageListEntityToImageListDTO(images);
            response.setStatusCode(200);
            response.setMessage("Images found");
            response.setImagesList(imageDTOs);
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
         catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Fetching Images " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getImageById(Long imageId) {
        Response response = new Response();
        try {
           Image image = imageRepository.findById(imageId).orElseThrow(()-> new OurException("Image Not Found"));
           ImageDTO imageDTO = Utils.mapImageEntityToDto(image);
           response.setStatusCode(200);
           response.setMessage("Image Found Successfully");
           response.setImageDTO(imageDTO);            
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a New Image " + e.getMessage());
        }
        return response;
        }

    @Override
    @Transactional
    public Response deleteImage(Long imageId) {
        Response response = new Response();

        try {
           Image image = imageRepository.findById(imageId).orElseThrow(()-> new OurException("Image Not Found"));
            
            String authUsername = getCurrentUsername();

            if(!image.getUser().getUsername().equals(authUsername) && !isAdmin(authUsername)){
                response.setStatusCode(403);
                response.setMessage("You do not have permission to delete this image.");
                return response;
            }
            
            imageRepository.deleteById(imageId);
            response.setStatusCode(200);
            response.setMessage("Image Successfully Deleted");
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
         catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Deleting the Image " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Response getAllImages() {
        Response response = new Response();
        try {
           List<Image> imageList = imageRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
           List<ImageDTO> imageListDTO = Utils.mapImageListEntityToImageListDTO(imageList);
           response.setStatusCode(200);
           response.setMessage("Images Found Successfully");
           response.setImagesList(imageListDTO);            
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a New Image " + e.getMessage());
        }
        return response;
        }    

    @Override
    @Transactional
    public Response updateImage(Long imageId, MultipartFile photo, String tagsInput) {
        Response response = new Response();

        try {
            Image image = imageRepository.findById(imageId).orElseThrow(()-> new OurException("Image Not Found"));

            String currentUsername = getCurrentUsername();
            if(currentUsername == null){
                response.setStatusCode(403);
                response.setMessage("User is not authenticated.");
                return response;
            }    

            if(!image.getUser().getUsername().equals(currentUsername) && !isAdmin(currentUsername)){
                response.setStatusCode(403);
                response.setMessage("You do not have permission to delete this image.");
                return response;
            }
            String imagePath = null;
            if(photo != null && !photo.isEmpty()){
                imagePath = fileStorageService.saveImage(photo, currentUsername);
            }
            if(tagsInput != null){
            List<Tag> tags = processTags(tagsInput);
        image.setTags(tags);
            }
            if(imagePath != null){
                image.setPhotoDir(imagePath);
            }
            Image updatedImage = imageRepository.save(image);
            ImageDTO imageDTO = Utils.mapImageEntityToDto(updatedImage);
            response.setStatusCode(200);
            response.setMessage("Image Updated Successfully");
            response.setImageDTO(imageDTO);

        }catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Updating the Image " + e.getMessage());
        }
        return response;
    }

    @Override
    public int countImagesByTag(Long tagId) {
        try {
             return imageRepository.countImagesByTagId(tagId);
        } catch (Exception e) {
            throw new RuntimeException("Error Fetching Image Count: " + e.getMessage());
        }
    }

    private List<Tag> processTags(String tagsInput){
        List<Tag> tags = new ArrayList<>();
        if(tagsInput != null && !tagsInput.trim().isEmpty()){
            String[] tagNames = tagsInput.split("\\s+");
            for(String tagName : tagNames){
                String trimmedTag = tagName.trim();
                if(!trimmedTag.isEmpty()){
                    Tag tag = tagRepository.findByName(trimmedTag)
                    .orElseGet(()-> {
                        Tag newTag = new Tag();
                        newTag.setName(trimmedTag);
                        return tagRepository.save(newTag);
                    });
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    private List<Tag> findTags(String tagsInput) {
        List<Tag> tags = new ArrayList<>();
        if (tagsInput != null && !tagsInput.trim().isEmpty()) {
            String[] tagNames = tagsInput.split("\\s+");
            for (String tagName : tagNames) {
                String trimmedTag = tagName.trim();
                if (!trimmedTag.isEmpty()) {
                    tagRepository.findByName(trimmedTag).ifPresent(tags::add);
                }
            }
        }
        return tags;
    }

    private String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AnonymousAuthenticationToken) {
        return null;
    }
    return authentication.getName();
    }

    private boolean isAdmin(String username){
        return userRepository.findByUsername(username)
        .map(User::isAdmin)
        .orElse(false);
    }

   
}
