package org.example.repo;

import org.example.model.User;

import java.util.List;

public interface UserRepository {
    void saveUser(User user);
    User findUserById(int id);
    List<User> findAllUsers();
    void deleteUser(int id);
    void updateUser(User user);
}
