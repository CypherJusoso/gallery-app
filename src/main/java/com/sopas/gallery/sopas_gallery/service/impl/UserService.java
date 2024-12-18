package com.sopas.gallery.sopas_gallery.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopas.gallery.sopas_gallery.dto.ImageDTO;
import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.dto.UserDTO;
import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.entity.Role;
import com.sopas.gallery.sopas_gallery.entity.User;
import com.sopas.gallery.sopas_gallery.exception.OurException;
import com.sopas.gallery.sopas_gallery.repository.ImageRepository;
import com.sopas.gallery.sopas_gallery.repository.RoleRepository;
import com.sopas.gallery.sopas_gallery.repository.UserRepository;
import com.sopas.gallery.sopas_gallery.service.interfac.IUserService;
import com.sopas.gallery.sopas_gallery.utils.Utils;



@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    @Transactional
    public Response register(User user) {
        
        Response response = new Response();

        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        optionalRoleUser.ifPresent(roles::add);

        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToDto(savedUser);
            response.setStatusCode(201);
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Response getAllUsers() {
        Response response = new Response();

        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userListDTO = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("successfull");
            response.setUsersList(userListDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());       
         }
         return response;
    }

    @Override
    public Response deleteUser(Long userId) {
        Response response = new Response();

        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new OurException("User Not Found."));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("User Succesfully Deleted");
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } 
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Deleting User " + e.getMessage());
        }
        return response;
    }

 @Override
    @Transactional(readOnly = true)
    public Response getImagesByUserId(Long userId) {
        Response response = new Response();
        try {
           User user = userRepository.findById(userId).orElseThrow(()-> new OurException("User Not Found."));
           List<Image> images = imageRepository.findByUserId(userId);
           user.setImages(images);
           UserDTO userDTO = Utils.mapUserEntityToDtoPlusImages(user);
           List<ImageDTO> imageDTOs = Utils.mapImageListEntityToImageListDTO(images);
           response.setStatusCode(200);
           response.setMessage("Succesfully Found Images");
           response.setImagesList(imageDTOs);
           response.setUser(userDTO);
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
         catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a New Image " + e.getMessage());
        }
        return response;
        }

    @Override
    public Response getUserById(Long userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new OurException("User Not Found."));
            UserDTO userDTO = Utils.mapUserEntityToDto(user);
            response.setStatusCode(200);
            response.setMessage("User Found Successfully");
            response.setUser(userDTO);
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
         catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Finding User " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {

        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email).orElseThrow(()-> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToDto(user);
            response.setStatusCode(200);
            response.setMessage("User Found Successfully");
            response.setUser(userDTO);
        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
         catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Finding User " + e.getMessage());
        }   
        return response;
     }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
