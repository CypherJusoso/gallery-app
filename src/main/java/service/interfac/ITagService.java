package service.interfac;

import dto.Response;
import entity.Tag;

public interface ITagService {

    Response addTag(Tag tag);

    Response getAllTags();

    Response getTagById(Long tagId);

    Response getTagByName(String name);
}
