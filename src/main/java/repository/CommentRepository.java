package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Comment;
import entity.Image;
import entity.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByImage(Image image);

    List<Comment> findByUser(User user);
}
