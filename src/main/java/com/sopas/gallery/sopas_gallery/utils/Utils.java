package com.sopas.gallery.sopas_gallery.utils;


import java.util.stream.Collectors;

import com.sopas.gallery.sopas_gallery.dto.CommentDTO;
import com.sopas.gallery.sopas_gallery.dto.ImageDTO;
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

import java.util.List;

public class Utils {


    public static UserDTO mapUserEntityToDto(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
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
      ImageDTO imageDTO = new ImageDTO();
      
      imageDTO.setId(image.getId());
      imageDTO.setPhotoDir(image.getPhotoDir());
      imageDTO.setUploadDate(image.getUploadDate());
      imageDTO.setUser(mapUserEntityToDto(image.getUser()));

     imageDTO.setTags(image.getTags().stream()
    .map(Utils::mapTagEntityToDto)
    .collect(Collectors.toList()));
    
    imageDTO.setComments(image.getComments().stream()
    .map(Utils::mapCommentEntityToDTO)
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
        commentDTO.setImage(mapImageEntityToDto(comment.getImage()));
        commentDTO.setCreatedAt(comment.getCreatedAt());

        commentDTO.setUser(mapUserEntityToDto(comment.getUser()));
        return commentDTO;
    }

    public static LikeDislikeDTO mapLikeDislikeEntityToDTO(LikeDislike likeDislike){
        LikeDislikeDTO likeDislikeDTO = new LikeDislikeDTO();
        likeDislikeDTO.setId(likeDislike.getId());
        likeDislikeDTO.setIsLike(likeDislike.getIsLike());

        if(likeDislike.getImage() != null){
            likeDislikeDTO.setImage(Utils.mapImageEntityToDto(likeDislike.getImage()));
        }
        if(likeDislike.getUser() != null){
            likeDislikeDTO.setUser(Utils.mapUserEntityToDto(likeDislike.getUser()));
        }
        return likeDislikeDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList){
        return userList.stream().map(Utils::mapUserEntityToDto).collect(Collectors.toList());
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
}
