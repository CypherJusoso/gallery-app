package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Image;
import entity.Tag;
import entity.User;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUser(User user);

    List<Image> findByTags(List<Tag> tags);

    List<Image> findAllByOrderByUploadDateDesc();
    
}
