package com.sopas.gallery.sopas_gallery.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LightImageDTO {
    private Long id;
    private String photoDir;
    private LocalDateTime uploadDate;
    private String username; 
}
