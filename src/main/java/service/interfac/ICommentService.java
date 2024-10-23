package service.interfac;

import dto.Response;
import entity.Comment;

public interface ICommentService {

    Response addComent(Comment comment);

    Response getCommentsByImageId(Long imageId);

    Response getCommentsByUserId(Long userId);

    Response deleteComment(Long commentId);
}
