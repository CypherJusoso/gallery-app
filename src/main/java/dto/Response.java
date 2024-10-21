package dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;
    private String token;
    private String role;
    private String expirationTime;
    private UserDTO user;
    private TagDTO tag;
    private LikeDislikeDTO likeDislike;
    private ImageDTO imageDTO;
    private CommentDTO commentDTO;
    private List<UserDTO> usersList;
    private List<TagDTO> tagsList;
    private List<LikeDislikeDTO> likeDislikeList;

}