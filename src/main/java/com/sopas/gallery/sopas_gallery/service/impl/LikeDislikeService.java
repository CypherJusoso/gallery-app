package com.sopas.gallery.sopas_gallery.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.sopas.gallery.sopas_gallery.dto.LightLikeDislikeDTO;
import com.sopas.gallery.sopas_gallery.dto.LikeDislikeDTO;
import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.entity.LikeDislike;
import com.sopas.gallery.sopas_gallery.entity.User;
import com.sopas.gallery.sopas_gallery.exception.OurException;
import com.sopas.gallery.sopas_gallery.repository.ImageRepository;
import com.sopas.gallery.sopas_gallery.repository.LikeDislikeRepository;
import com.sopas.gallery.sopas_gallery.repository.UserRepository;
import com.sopas.gallery.sopas_gallery.service.interfac.ILikeDislikeService;
import com.sopas.gallery.sopas_gallery.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LikeDislikeService implements ILikeDislikeService {

    @Autowired
    private LikeDislikeRepository likeDislikeRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ImageRepository imageRepository;

    @Override
    @Transactional
    public Response addLikeDislike(LikeDislike likeDislike, Long imageId) {
        Response response = new Response();

        String authUsername = getCurrentUsername();

        if(authUsername == null){
            response.setStatusCode(403);
            response.setMessage("User is not authenticated.");
            return response;
        }

        User user = userRepository.findByUsername(authUsername)
            .orElseThrow(()-> new OurException("User Not Found"));
            log.info("User retrieved from SecurityContextHolder {}", user.getUsername());

        try {

            Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new OurException("Image Not Found"));

            LikeDislike existingReaction = likeDislikeRepository.findByUserIdAndImageId(user.getId(), imageId);
            
            if(existingReaction != null){
            
                if(existingReaction.getLiked().equals(likeDislike.getLiked())){
                    likeDislikeRepository.delete(existingReaction);
                    response.setStatusCode(200);
                    response.setMessage("Reaction deleted successfully");
                    return response;
                } else {
                    existingReaction.setLiked(likeDislike.getLiked());
                    likeDislikeRepository.save(existingReaction);
                    response.setStatusCode(200);
                    response.setMessage("Reaction updated successfully.");
                    return response;
        }
        }else {
            likeDislike.setUser(user);
            likeDislike.setImage(image);
            likeDislikeRepository.save(likeDislike);
            log.info("User at the end from SecurityContextHolder 4{}", user.getUsername());

            LightLikeDislikeDTO likeDislikeDTO = Utils.mapLikeDislikeEntityToLightDto(likeDislike);
            log.info("User at the end from SecurityContextHolder 3{}", user.getUsername());

            response.setData(likeDislikeDTO);
            response.setStatusCode(201);
            log.info("User at the end from SecurityContextHolder2 {}", user.getUsername());

            response.setMessage("Reaction saved successfully");
            log.info("User at the end from SecurityContextHolder {}", user.getUsername());

        }

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } 
        catch (Exception e) {
            log.error("Unexpected error: ", e);
            log.info("User at the catch from SecurityContextHolder {}", user.getId());
            response.setStatusCode(500);
            response.setMessage("Error al guardar la reacción: " + e.getMessage());
            throw e; // Re-lanza la excepción si es necesario
        }
        return response;
    }
    

    @Override
    @Transactional(readOnly = true)
    public Response getLikesByImageId(Long imageId) {
        Response response = new Response();
        try {
            Long likeCount = likeDislikeRepository.countByImageIdAndLiked(imageId, true);
            response.setStatusCode(200);
            response.setMessage("Likes fetched successfully");
            response.setData(likeCount);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Fething Likes: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Response getDislikesByImageId(Long imageId) {
        Response response = new Response();
        try {
            Long likeCount = likeDislikeRepository.countByImageIdAndLiked(imageId, false);
            response.setStatusCode(200);
            response.setMessage("Dislikes fetched successfully");
            response.setData(likeCount);        
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Fething Dislikes: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteLikeDislike(Long likeDislikeId) {
        Response response = new Response();

        try {
            if(likeDislikeRepository.existsById(likeDislikeId)){
                likeDislikeRepository.deleteById(likeDislikeId);
                response.setStatusCode(200);
                response.setMessage("Reaction successfully deleted");
            }else{
                response.setStatusCode(404);
                response.setMessage("Reacción no encontrada.");    
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Fetching Dislikes: " + e.getMessage());
        }
        return response;
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
