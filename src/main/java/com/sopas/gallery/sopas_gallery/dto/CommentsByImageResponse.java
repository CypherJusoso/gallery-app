package com.sopas.gallery.sopas_gallery.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentsByImageResponse {
    private LightImageDTO image; 
    private List<LightCommentDTO> comments;
}
