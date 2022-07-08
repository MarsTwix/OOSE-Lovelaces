package nl.han.project.skilltree.domain.impl.service;

import nl.han.project.skilltree.domain.exceptions.EmptyParameterException;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.exceptions.UnauthorizedException;
import nl.han.project.skilltree.domain.user.data.Feedback;
import nl.han.project.skilltree.domain.user.data.User;
import nl.han.project.skilltree.domain.user.data.UserDao;
import nl.han.project.skilltree.domain.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import java.util.List;
import java.util.UUID;

@Default
public class UserServiceImpl implements UserService {

    @Inject
    private UserDao dao;


    @Override
    public boolean validateUser(String email, String password) {
        User user;
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            throw new EmptyParameterException("Email or password is empty");
        }
        user = dao.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        String hashedPassword = DigestUtils.sha256Hex(password);
        return hashedPassword.equals(user.getPassword());
    }

    @Override
    public boolean validateToken(String token) {
        User user;
        user = dao.getUserByToken(token);

        if (user == null) {
            return false;
        }

        return !user.getToken().isEmpty();
    }

    @Override
    public void logout(String token) {
        if (!validateToken(token)) {
            throw new UnauthorizedException("User not found");
        }

        dao.logout(token);
    }

    @Override
    public List<User> getAll() {
        return dao.getAll();
    }

    @Override
    public User getUserByEmail(User user) {
        if (!validateUser(user.getEmail(), user.getPassword())) {
            throw new ForbiddenException("User not found");
        }
        user = dao.getUserByEmail(user.getEmail());

        if (user == null) {
            throw new ForbiddenException("User not found by email");
        }

        String token = this.createToken(user.getId());
        user.setToken(token);

        return user;
    }

    @Override
    public User getUserByToken(String token) {
        User user;
        user = dao.getUserByToken(token);

        if (user == null) {
            throw new UnauthorizedException("User not found by token");
        }
        return user;
    }

    @Override
    public void insertFeedback(int fromUserId, Feedback feedback) {
        if (fromUserId < 1 || feedback.getForUserId() < 1) {
            throw new ServerException("Invalid logged in user id or invalid target user id");
        }
        if (dao.insertFeedback(fromUserId, feedback) < 1) {
            throw new ServerException("Error while giving feedback");
        }
    }

    public String createToken(int userId) {
        String token = UUID.randomUUID().toString();
        if (dao.saveUserToken(userId, token) < 1) {
            throw new ServerException("Error while creating token");
        }
        return token;
    }
}
