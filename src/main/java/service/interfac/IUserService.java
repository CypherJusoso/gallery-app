package service.interfac;

import dto.LoginRequest;
import dto.Response;
import entity.User;

public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
