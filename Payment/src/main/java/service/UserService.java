package service;

import Model.User;
import repository.UserRepository;
import util.SecurityUtil;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public User registerUser(String username, String password, User.Role role) {
        String hash = SecurityUtil.hashPassword(password);
        User user = new User(System.currentTimeMillis(), username, hash, role);
        userRepository.save(user);
        return user;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && SecurityUtil.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        throw new RuntimeException("Invalid login");
    }
}