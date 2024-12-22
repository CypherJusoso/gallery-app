package com.sopas.gallery.sopas_gallery.utils;


import java.util.stream.Collectors;

import com.sopas.gallery.sopas_gallery.dto.CommentDTO;
import com.sopas.gallery.sopas_gallery.dto.ImageDTO;
import com.sopas.gallery.sopas_gallery.dto.LightCommentDTO;
import com.sopas.gallery.sopas_gallery.dto.LightImageDTO;
import com.sopas.gallery.sopas_gallery.dto.LightLikeDislikeDTO;
import com.sopas.gallery.sopas_gallery.dto.LightUserDTO;
import com.sopas.gallery.sopas_gallery.dto.LikeDislikeDTO;
import com.sopas.gallery.sopas_gallery.dto.RoleDTO;
import com.sopas.gallery.sopas_gallery.dto.TagDTO;
import com.sopas.gallery.sopas_gallery.dto.UserDTO;
import com.sopas.gallery.sopas_gallery.entity.Comment;
import com.sopas.gallery.sopas_gallery.entity.Image;
import com.sopas.gallery.sopas_gallery.entity.LikeDislike;
import com.sopas.gallery.sopas_gallery.entity.Role;
import com.sopas.gallery.sopas_gallery.entity.Tag;
import com.sopas.gallery.sopas_gallery.entity.User;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Utils {


    public static UserDTO mapUserEntityToDto(User user){
        if (user == null) {
            log.warn("User is null when trying to map to DTO");
            return null; 
        }

        log.info("User at the beginning of user mapping {}", user.getId()); 

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        log.info("User after getId {}", user.getId());

        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setEnabled(user.isEnabled());
        if(user.getRoles() != null){
            userDTO.setRoles(mapRoleListEntityToRoleListDTO(user.getRoles()));
        }
        return userDTO;
    }

    public static UserDTO mapUserEntityToDtoPlusImages(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRegistrationDate(user.getRegistrationDate());

        if(!user.getImages().isEmpty()){
            userDTO.setImages(user.getImages().stream()
            .map(Utils::mapImageEntityToDto)
            .collect(Collectors.toList()));
        }
        if(user.getRoles() != null){
            userDTO.setRoles(mapRoleListEntityToRoleListDTO(user.getRoles()));
        }
        return userDTO;
    }

    public static ImageDTO mapImageEntityToDto(Image image){
        if (image == null) {
            log.warn("Image is null when trying to map to DTO");
            return null; // O manejar de alguna otra forma el caso de imagen nula
        }

      ImageDTO imageDTO = new ImageDTO();
      
      imageDTO.setId(image.getId());
      imageDTO.setPhotoDir(image.getPhotoDir());
      imageDTO.setUploadDate(image.getUploadDate());
      imageDTO.setUser(mapUserEntityToDto(image.getUser()));

     imageDTO.setTags(image.getTags().stream()
    .map(Utils::mapTagEntityToDto)
    .collect(Collectors.toList()));
    
    imageDTO.setComments(image.getComments().stream()
        .map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreatedAt(comment.getCreatedAt());
            // No mapear la imagen completa
            commentDTO.setUser(mapUserEntityToLightDto(comment.getUser()));
            return commentDTO;
        })
        .collect(Collectors.toList()));
      
    imageDTO.setLikeDislikes(image.getLikeDislikes().stream()
    .map(Utils::mapLikeDislikeEntityToDTO)
    .collect(Collectors.toList()));
    
      return imageDTO;
    }

    public static TagDTO mapTagEntityToDto(Tag tag){
    TagDTO tagDTO = new TagDTO();
    tagDTO.setId(tag.getId());
    tagDTO.setName(tag.getName());
    return tagDTO;    
    }

    public static RoleDTO mapRoleEntityToDTO(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    public static CommentDTO mapCommentEntityToDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setImage(mapImageEntityToLightDto(comment.getImage())); // Usar Light DTO
        commentDTO.setUser(mapUserEntityToLightDto(comment.getUser()));
        commentDTO.setCreatedAt(comment.getCreatedAt());

        return commentDTO;
    }

    public static CommentDTO mapCommentEntityWithNoUserToDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setImage(mapImageEntityToLightDto(comment.getImage())); // Usar Light DTO
        commentDTO.setCreatedAt(comment.getCreatedAt());

        return commentDTO;
    }

    public static LikeDislikeDTO mapLikeDislikeEntityToDTO(LikeDislike likeDislike){
        log.info("Mapping beginning: {}", likeDislike.getUser().getId());

        LikeDislikeDTO likeDislikeDTO = new LikeDislikeDTO();
        likeDislikeDTO.setId(likeDislike.getId());
        likeDislikeDTO.setLiked(likeDislike.getLiked());

        log.info("Mapping after: {}", likeDislike.getUser().getId());

        if(likeDislike.getImage() != null){
            likeDislikeDTO.setImage(Utils.mapImageEntityToDto(likeDislike.getImage()));
        }
        log.info("before mapping user in likedislikedto: {}", likeDislike.getUser().getId());

        if(likeDislike.getUser() != null){
            log.info("Mapping user entity to DTO for user ID: {}", likeDislike.getUser().getId());
            likeDislikeDTO.setUser(Utils.mapUserEntityToDto(likeDislike.getUser()));
        }else{
            log.info("No user associated with LikeDislike ID: {}", likeDislike.getId());
        }
        return likeDislikeDTO;
    }

    public static LightImageDTO mapImageEntityToLightDto(Image image) {
    LightImageDTO imageDTO = new LightImageDTO();
    imageDTO.setId(image.getId());
    imageDTO.setPhotoDir(image.getPhotoDir());
    imageDTO.setUploadDate(image.getUploadDate());
    imageDTO.setUsername(image.getUser().getUsername());
    return imageDTO;
    }

    public static LightUserDTO mapUserEntityToLightDto(User user) {
    LightUserDTO userDTO = new LightUserDTO();
    userDTO.setId(user.getId());
    userDTO.setUsername(user.getUsername());
    userDTO.setEmail(user.getEmail());
    return userDTO;
    }
    
    public static LightCommentDTO mapCommentEntityToLightDto(Comment comment){
        LightCommentDTO dto = new LightCommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUser(mapUserEntityToLightDto(comment.getUser()));
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
    
    public static LightLikeDislikeDTO mapLikeDislikeEntityToLightDto(LikeDislike likeDislike){
        LightLikeDislikeDTO dto = new LightLikeDislikeDTO();
        dto.setImage(mapImageEntityToLightDto(likeDislike.getImage()));
        dto.setLiked(likeDislike.getLiked());
        dto.setUser(mapUserEntityToLightDto(likeDislike.getUser()));
        return dto;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList){
        return userList.stream().map(Utils::mapUserEntityToDto).collect(Collectors.toList());
    }
    public static List<CommentDTO> mapCommentListEntityToCommentListDTO(List<Comment> commentList){
        return commentList.stream().map(Utils::mapCommentEntityToDTO).collect(Collectors.toList());
    }
    public static List<CommentDTO> mapCommentListEntityWithNoUserToCommentListDTO(List<Comment> commentList){
        return commentList.stream().map(Utils::mapCommentEntityWithNoUserToDTO).collect(Collectors.toList());
    }
    public static List<ImageDTO> mapImageListEntityToImageListDTO(List<Image> imageList){
        return imageList.stream().map(Utils::mapImageEntityToDto).collect(Collectors.toList());
    }
    public static List<TagDTO> mapTagListEntityToTagListDTO(List<Tag> tagList){
        return tagList.stream().map(Utils::mapTagEntityToDto).collect(Collectors.toList());
    }
    public static List<RoleDTO> mapRoleListEntityToRoleListDTO(List<Role> roleList){
        return roleList.stream().map(Utils::mapRoleEntityToDTO).collect(Collectors.toList());
    }
    public static List<LikeDislikeDTO> mapLikeDislikeListEntityToLikeDislikeListDTO(List<LikeDislike> likeDislikes){
        return likeDislikes.stream().map(Utils::mapLikeDislikeEntityToDTO).collect(Collectors.toList());
    }
    public static List<LightCommentDTO> mapCommentListEntityToLightCommentListDTO(List<Comment> commentList){
        return commentList.stream().map(Utils::mapCommentEntityToLightDto).collect(Collectors.toList());
    }
}
