package com.sopas.gallery.sopas_gallery.service.interfac;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.User;

public interface IUserService {

    Response register(User user);

    Response getAllUsers();

    Response getImagesByUserId(Long userId);

    Response deleteUser(Long userId);

    Response getUserById(Long userId);

    Response getMyInfo(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    

}
