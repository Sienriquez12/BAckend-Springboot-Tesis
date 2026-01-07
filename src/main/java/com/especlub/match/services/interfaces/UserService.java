
package com.especlub.match.services.interfaces;

import com.especlub.match.models.UserInfo;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserInfo createUser(UserInfo user);
    List<UserInfo> getAllUsers();
    Optional<UserInfo> getUserById(Long id);
    UserInfo updateUser(Long id, UserInfo user);
    void deleteUser(Long id);
}

