package utils;

import dto.CommentDTO;
import dto.ImageDTO;
import dto.LikeDislikeDTO;
import dto.TagDTO;
import dto.UserDTO;
import entity.Comment;
import entity.Image;
import entity.LikeDislike;
import entity.Tag;
import entity.User;
import java.util.stream.Collectors;
import java.util.List;

public class Utils {


    public static UserDTO mapUserEntityToDto(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public static UserDTO mapUserEntityToDtoPlusImages(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setRole(user.getRole());

        if(!user.getImages().isEmpty()){
            userDTO.setImages(user.getImages().stream()
            .map(Utils::mapImageEntityToDto)
            .collect(Collectors.toList()));
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
}
