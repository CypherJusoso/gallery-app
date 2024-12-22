package com.sopas.gallery.sopas_gallery.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LightCommentDTO {
    private Long id;
    private String content;
    private LightUserDTO user; 
    private LocalDateTime createdAt;
}
