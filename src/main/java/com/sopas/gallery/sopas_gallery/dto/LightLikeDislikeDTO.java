package com.sopas.gallery.sopas_gallery.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LightLikeDislikeDTO {

    private LightImageDTO image;
    private LightUserDTO user;
    private Boolean liked;
}
