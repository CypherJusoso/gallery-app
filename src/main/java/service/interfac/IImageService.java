package service.interfac;

import java.util.List;

import dto.Response;
import entity.Image;
import entity.Tag;

public interface IImageService {

    Response uploadImage(Image image);

    Response getImagesByUserId(Long userId);

    Response getImagesByTags(List<Tag> tags);

    Response getImageById(Long imageId);

    Response deleteImage(Long imageId);

    Response getAllImages();

    //Title, Tags
    Response updateImage(Long imageId, String title, String tags);

}
