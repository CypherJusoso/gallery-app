package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Tag;
import java.util.List;
import entity.Image;



public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    List<Tag> findByImages(List<Image> images);

    List<Tag> findByNameContainingIgnoreCase(String namePart);
}
