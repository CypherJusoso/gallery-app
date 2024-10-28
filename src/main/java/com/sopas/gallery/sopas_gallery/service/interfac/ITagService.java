package com.sopas.gallery.sopas_gallery.service.interfac;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.Tag;

public interface ITagService {

    Response addTag(Tag tag);

    Response getAllTags();

    Response getTagById(Long tagId);

    Response getTagByName(String name);
}
