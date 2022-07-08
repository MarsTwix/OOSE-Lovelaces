package nl.han.project.skilltree.domain.user.data;

import java.util.List;

public interface UserDao {

    User getUserByEmail(String email);

    User getUserByToken(String token);

    int saveUserToken(int userId, String token);

    void logout(String token);

    List<User> getAll();

    int insertFeedback(int fromUserId, Feedback feedback);
}
