package com.sopas.gallery.sopas_gallery.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.sopas.gallery.sopas_gallery.dto.LikeDislikeDTO;
import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.LikeDislike;
import com.sopas.gallery.sopas_gallery.entity.User;
import com.sopas.gallery.sopas_gallery.exception.OurException;
import com.sopas.gallery.sopas_gallery.repository.LikeDislikeRepository;
import com.sopas.gallery.sopas_gallery.repository.UserRepository;
import com.sopas.gallery.sopas_gallery.service.interfac.ILikeDislikeService;
import com.sopas.gallery.sopas_gallery.utils.Utils;

@Service
public class LikeDislikeService implements ILikeDislikeService {

    @Autowired
    private LikeDislikeRepository likeDislikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Response addLikeDislike(LikeDislike likeDislike) {
        Response response = new Response();

        String authUsername = getCurrentUsername();

        if(authUsername == null){
            response.setStatusCode(403);
            response.setMessage("User is not authenticated.");
            return response;
        }

        User user = userRepository.findByUsername(authUsername).orElseThrow(()-> new OurException("User Not Found"));
        try {
            LikeDislike existingReaction = likeDislikeRepository.findByUserIdAndImageId(likeDislike.getUser().getId(), likeDislike.getImage().getId());
            if(existingReaction != null){
            if(existingReaction.getLiked().equals(likeDislike.getLiked())){
                likeDislikeRepository.delete(existingReaction);
                response.setStatusCode(200);
                response.setMessage("Reaction deleted successfully");
                return response;
            }else {
            existingReaction.setLiked(likeDislike.getLiked());
            likeDislikeRepository.save(existingReaction);
            response.setStatusCode(200);
            response.setMessage("Reaction updated successfully.");
            return response;
        }
        }else {
            likeDislike.setUser(user);
            likeDislikeRepository.save(likeDislike);
            LikeDislikeDTO likeDislikeDTO = Utils.mapLikeDislikeEntityToDTO(likeDislike);
            response.setLikeDislike(likeDislikeDTO);
            response.setStatusCode(201);
            response.setMessage("Reaction saved successfully");
        }

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } 
         catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al guardar la reacción: " + e.getMessage());        
        }
        return response;
    }
    

    @Override
    @Transactional(readOnly = true)
    public Response getLikesByImageId(Long imageId) {
        Response response = new Response();
        try {
            List<LikeDislike> likes = likeDislikeRepository.findByImageIdAndLiked(imageId, true);
            response.setStatusCode(200);
            response.setMessage("Likes fetched successfully");
            List<LikeDislikeDTO> likeDislikeDTO = Utils.mapLikeDislikeListEntityToLikeDislikeListDTO(likes);
            response.setLikeDislikeList(likeDislikeDTO);
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
            List<LikeDislike> likes = likeDislikeRepository.findByImageIdAndLiked(imageId, false);
            response.setStatusCode(200);
            response.setMessage("Dislikes fetched successfully");
            List<LikeDislikeDTO> likeDislikeDTO = Utils.mapLikeDislikeListEntityToLikeDislikeListDTO(likes);
            response.setLikeDislikeList(likeDislikeDTO);
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
