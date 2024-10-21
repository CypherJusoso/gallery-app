package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {

    private Long id;
    private String name;
    private List<ImageDTO> images = new ArrayList<>();
    private LocalDateTime createdAt;
    
}
