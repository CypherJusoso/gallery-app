package com.sopas.gallery.sopas_gallery.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sopas.gallery.sopas_gallery.dto.CommentDTO;
import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.Comment;
import com.sopas.gallery.sopas_gallery.exception.OurException;
import com.sopas.gallery.sopas_gallery.repository.CommentRepository;
import com.sopas.gallery.sopas_gallery.service.interfac.ICommentService;
import com.sopas.gallery.sopas_gallery.utils.Utils;

import java.util.List;

import jakarta.validation.ConstraintViolationException;

public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override    
    @Transactional
    public Response addComent(Comment comment) {
        Response response = new Response();

        if(comment == null  || comment.getContent().isBlank()){
            response.setStatusCode(400);
            response.setMessage("Your comment cannot be empty.");
            return response;
        }
        try {
            commentRepository.save(comment);
            response.setStatusCode(201);
            response.setMessage("Comment added successfully");

        }catch(ConstraintViolationException e){
            response.setStatusCode(400);
            response.setMessage("Validation error: " + e.getMessage());
        } 
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving comment: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Response getCommentsByImageId(Long imageId) {
        Response response = new Response();

        try {
            List<Comment> comments = commentRepository.findByImageId(imageId);
            List<CommentDTO> commentDTOs = Utils.mapCommentListEntityToCommentListDTO(comments);
            response.setStatusCode(200);
            response.setMessage("Comments Found Successfully");
            response.setCommentsList(commentDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching comments " + e.getMessage());
        
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Response getCommentsByUserId(Long userId) {
        Response response = new Response();
        try {
            List<Comment> comments = commentRepository.findByUserId(userId);
            List<CommentDTO> commentDTOs = Utils.mapCommentListEntityToCommentListDTO(comments);
            response.setStatusCode(200);
            response.setMessage("Comments Found Successfully");
            response.setCommentsList(commentDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching comments " + e.getMessage());
        
        }
        return response;
    }

    @Override
    public Response deleteComment(Long commentId) {
Response response = new Response();

        try {
            commentRepository.findById(commentId).orElseThrow(()-> new OurException("Comment Not Found"));
            commentRepository.deleteById(commentId);
            response.setStatusCode(200);
            response.setMessage("Comment Successfully Deleted");
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
         catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Deleting the Comment " + e.getMessage());
        }
        return response;
    }

}
