package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime registrationDate;
    private List<ImageDTO> images = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();;
    private List<LikeDislikeDTO> likeDislikes = new ArrayList<>();;
    private List<RoleDTO> roles;

}
