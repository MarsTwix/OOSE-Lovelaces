package nl.han.project.skilltree.domain.user.service;

import nl.han.project.skilltree.domain.user.data.Feedback;
import nl.han.project.skilltree.domain.user.data.User;

import java.util.List;

public interface UserService {
    boolean validateUser(String email, String password);
    boolean validateToken(String token);

    User getUserByEmail(User user);
    User getUserByToken(String token);

    void logout(String token);

    List<User> getAll();

    void insertFeedback(int fromUserId, Feedback feedback);
}
