package dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDislikeDTO {
private Long id;

    private ImageDTO image;
    private UserDTO user;
    private Boolean isLike;

}
