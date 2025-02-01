package org.example;

import org.example.model.User;
import org.example.repo.UserRepository;
import org.example.repo.UserRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        User user1 = new User(1, "test");
        User user2 = new User(2, "asdas");

        userRepository.saveUser(user1);
        System.out.println(userRepository.findAllUsers());

        userRepository.saveUser(user2);
        System.out.println(userRepository.findUserById(1));
        System.out.println(userRepository.findAllUsers());

        userRepository.deleteUser(2);
        System.out.println(userRepository.findAllUsers());
        System.out.println(userRepository.findUserById(2));

        user1.setName("newName");
        userRepository.updateUser(user1);
        System.out.println(userRepository.findAllUsers());
        System.out.println("Hello world!");
    }
}