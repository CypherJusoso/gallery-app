package com.sopas.gallery.sopas_gallery.service.interfac;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.Comment;

public interface ICommentService {

    Response addComent(Comment comment);

    Response getCommentsByImageId(Long imageId);

    Response getCommentsByUserId(Long userId);

    Response deleteComment(Long commentId);

    Response updateComment(Long commentId, String newContent);
}
