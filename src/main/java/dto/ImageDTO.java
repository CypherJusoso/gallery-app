package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {
private Long id;

    private String photoDir;

    private LocalDateTime uploadDate = LocalDateTime.now();

    private UserDTO user;

  
    private List<TagDTO> tags = new ArrayList<>();

    private List<CommentDTO> comments = new ArrayList<>();

    private List<LikeDislikeDTO> likeDislikes = new ArrayList<>();
}
