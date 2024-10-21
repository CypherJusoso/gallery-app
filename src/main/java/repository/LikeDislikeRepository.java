package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Image;
import entity.LikeDislike;
import entity.User;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {

    List<LikeDislike> findByImage(Image image);

    List<LikeDislike> findByUser(User user);

    long countByImageAndIsLikeTrue(Image image);
    
    long countByImageAndIsLikeFalse(Image image);

}
