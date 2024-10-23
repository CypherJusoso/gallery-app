package service.interfac;

import dto.Response;
import entity.LikeDislike;

public interface ILikeDislikeService {

    Response addLikeDislike(LikeDislike likeDislike);

    Response getLikesByImageId(Long imageId);

    Response getDislikesByImageId(Long imageId);

    Response deleteLikeDislike(Long likeDislikeId);
}
