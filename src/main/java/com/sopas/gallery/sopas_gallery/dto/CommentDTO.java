package com.sopas.gallery.sopas_gallery.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {

    private Long id;
    private String content;
    private UserDTO user;
    private ImageDTO image;
    private LocalDateTime createdAt;

}
