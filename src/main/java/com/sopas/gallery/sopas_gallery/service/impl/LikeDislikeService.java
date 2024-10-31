package com.sopas.gallery.sopas_gallery.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.sopas.gallery.sopas_gallery.dto.LikeDislikeDTO;
import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.LikeDislike;
import com.sopas.gallery.sopas_gallery.repository.LikeDislikeRepository;
import com.sopas.gallery.sopas_gallery.service.interfac.ILikeDislikeService;
import com.sopas.gallery.sopas_gallery.utils.Utils;

public class LikeDislikeService implements ILikeDislikeService {

    @Autowired
    private LikeDislikeRepository likeDislikeRepository;

    @Override
    @Transactional
    public Response addLikeDislike(LikeDislike likeDislike) {
        Response response = new Response();

        try {
            LikeDislike existing = likeDislikeRepository.findByUserIdAndImageId(likeDislike.getUser().getId(), likeDislike.getImage().getId());
            if(existing != null){
            if(existing.getIsLike().equals(likeDislike.getIsLike())){
                likeDislikeRepository.delete(existing);
                response.setStatusCode(200);
                response.setMessage("Reaction deleted successfully");
                return response;
            }else {
            existing.setIsLike(likeDislike.getIsLike());
            likeDislikeRepository.save(existing);
            response.setStatusCode(200);
            response.setMessage("Reaction updated successfully.");
            return response;
        }
        }else {
            likeDislikeRepository.save(likeDislike);
            response.setStatusCode(201);
            response.setMessage("Reaction saved successfully");
        }

        } catch (Exception e) {
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
            List<LikeDislike> likes = likeDislikeRepository.findByImageIdAndIsLike(imageId, true);
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
            List<LikeDislike> likes = likeDislikeRepository.findByImageIdAndIsLike(imageId, false);
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
            response.setMessage("Error Fething Dislikes: " + e.getMessage());
        }
        return response;
    }
}
