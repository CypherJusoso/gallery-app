package com.sopas.gallery.sopas_gallery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.dto.TagDTO;
import com.sopas.gallery.sopas_gallery.entity.Tag;
import com.sopas.gallery.sopas_gallery.entity.User;
import com.sopas.gallery.sopas_gallery.exception.OurException;
import com.sopas.gallery.sopas_gallery.repository.TagRepository;
import com.sopas.gallery.sopas_gallery.repository.UserRepository;
import com.sopas.gallery.sopas_gallery.service.interfac.ITagService;
import com.sopas.gallery.sopas_gallery.utils.Utils;



import jakarta.validation.ConstraintViolationException;

public class TagService implements ITagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Response addTag(Tag tag) {
       Response response = new Response();
        try {
            tagRepository.save(tag);
            response.setStatusCode(201);
            response.setMessage("Tag added successfully");

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
    public Response getAllTags() {
        Response response = new Response();
        try {
           List<Tag> tagList = tagRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
           List<TagDTO> tagListDtos = Utils.mapTagListEntityToTagListDTO(tagList);
           response.setStatusCode(200);
           response.setMessage("Tags Found Successfully");
           response.setTagsList(tagListDtos);            
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Getting the Tags " + e.getMessage());
        }
        return response;
        }  

    @Override
    public Response getTagById(Long tagId) {
        Response response = new Response();
        try {
           Tag tag = tagRepository.findById(tagId).orElseThrow(()-> new OurException("Tag Not Found"));
           TagDTO tagDTO = Utils.mapTagEntityToDto(tag);
           response.setStatusCode(200);
           response.setMessage("Tag Found Successfully");
           response.setTag(tagDTO);            
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Fetching Tag " + e.getMessage());
        }
        return response;
        }

    @Override
    public Response getTagByName(String name) {
        Response response = new Response();
        try {
           Tag tag = tagRepository.findByName(name).orElseThrow(()-> new OurException("Tag Not Found"));
           TagDTO tagDTO = Utils.mapTagEntityToDto(tag);
           response.setStatusCode(200);
           response.setMessage("Tag Found Successfully");
           response.setTag(tagDTO);            
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Fetching Tag " + e.getMessage());
        }
        return response;
        }

    @Override
    public Response deleteTag(Long tagId) {
        Response response = new Response();
        try {
            tagRepository.findById(tagId).orElseThrow(()-> new OurException("Tag Not Found"));

            if(!isAdmin(getCurrentUsername())){
                response.setStatusCode(403);
                response.setMessage("You do not have permission to delete this tag.");
                return response;
            }
            tagRepository.deleteById(tagId);
            response.setStatusCode(200);
            response.setMessage("Tag Successfully Deleted");
            
        }catch(OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
         catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Deleting the Image " + e.getMessage());
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
