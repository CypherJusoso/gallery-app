package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entity.Image;
import entity.Tag;
import entity.User;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUser(User user);

    @Query("SELECT DISTINCT i FROM Image i JOIN i.tags t WHERE t IN :tags GROUP BY i.id HAVING COUNT (DISTINCT t) = :tagCount")
    List<Image> findByTags(@Param("tags") List<Tag> tags, @Param("tagCount") long tagCount);

    List<Image> findAllByOrderByUploadDateDesc();
    
}
