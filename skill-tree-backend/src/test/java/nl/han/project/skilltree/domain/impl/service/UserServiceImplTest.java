package nl.han.project.skilltree.domain.impl.service;

import nl.han.project.skilltree.domain.exceptions.EmptyParameterException;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.exceptions.UnauthorizedException;
import nl.han.project.skilltree.domain.user.data.Feedback;
import nl.han.project.skilltree.domain.user.data.User;
import nl.han.project.skilltree.domain.user.data.UserDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.ForbiddenException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl sut;

    private User user;
    private static final String password = "password";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john@doe.nl");
        user.setPassword(DigestUtils.sha256Hex(password));
        user.setRoleId(2);
        user.setToken("token");
    }

    @Test
    void testValidateUserEmptyPassword(){
        assertThrows(EmptyParameterException.class, () -> sut.validateUser("email123", ""));
    }

    @Test
    void testValidateUserEmptyEmail() {
        assertThrows(EmptyParameterException.class, () -> sut.validateUser("", "password123"));
    }

    @Test
    void testValidateUserNullPassword(){
        assertThrows(EmptyParameterException.class, () -> sut.validateUser("email123", null));
    }

    @Test
    void testValidateUserNullEmail() {
        assertThrows(EmptyParameterException.class, () -> sut.validateUser(null, "password123"));
    }

    @Test
    void testValidateUserDoesNotExist() {
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(null);
        assertFalse(sut.validateUser(user.getEmail(), password));
    }

    @Test
    void testGetAllUsers() {
       when(userDao.getAll()).thenReturn(List.of(user));
       assertDoesNotThrow(() -> sut.getAll());
    }
    @Test
    void testValidateUserEqualPassword() {
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);

        assertTrue(sut.validateUser(user.getEmail(), password));
    }

    @Test
    void TestValidateUserDifferentPassword() {
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);

        assertFalse(sut.validateUser(user.getEmail(), "wrongPassword"));
    }

    @Test
    void testValidateTokenUserDoesNotExist() {
        when(userDao.getUserByToken(user.getToken())).thenReturn(null);
        assertFalse(sut.validateToken(user.getToken()));
    }

    @Test
    void testValidateTokenUserExists() {
        when(userDao.getUserByToken(user.getToken())).thenReturn(user);
        assertTrue(sut.validateToken(user.getToken()));
    }

    @Test
    void testValidateTokenEmptyToken() {
        user.setToken("");
        when(userDao.getUserByToken(user.getToken())).thenReturn(user);
        assertFalse(sut.validateToken(user.getToken()));
    }

    @Test
    void testLogoutInvalidToken() {
        when(userDao.getUserByToken(user.getToken())).thenReturn(null);
        assertThrows(UnauthorizedException.class, () -> sut.logout(user.getToken()));
    }

    @Test
    void testLogoutSuccess() {
        when(userDao.getUserByToken(user.getToken())).thenReturn(user);
        assertDoesNotThrow(() -> sut.logout(user.getToken()));
    }

    @Test
    void testGetUserByEmailInvalidUser() {
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(null);
        assertThrows(ForbiddenException.class, () -> sut.getUserByEmail(user));
    }

    @Test
    void testGetUserByEmailDoesNotExist() {
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(user).thenReturn(null);
        User inputUser = new User();
        inputUser.setEmail("john@doe.nl");
        inputUser.setPassword(password);
        assertThrows(ForbiddenException.class, () -> sut.getUserByEmail(inputUser));
    }

    @Test
    void testGetUserByEmailSuccess() {
        when(userDao.saveUserToken(anyInt(), anyString())).thenReturn(1);
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);
        User inputUser = new User();
        inputUser.setEmail("john@doe.nl");
        inputUser.setPassword(password);
        assertDoesNotThrow(() -> sut.getUserByEmail(inputUser));
    }

    @Test
    void testGetUserByTokenInvalidToken() {
        when(userDao.getUserByToken(user.getToken())).thenReturn(null);
        assertThrows(UnauthorizedException.class, () -> sut.getUserByToken(user.getToken()));
    }

    @Test
    void testGetUserByTokenSuccess() {
        when(userDao.getUserByToken(user.getToken())).thenReturn(user);
        assertDoesNotThrow(() -> sut.getUserByToken(user.getToken()));
    }

    @Test
    void testInsertFeedbackSuccess() {
        Feedback feedback = new Feedback();
        feedback.setForUserId(2);
        feedback.setSkillId(22);
        feedback.setId(1);
        feedback.setDescription("feedback");

        when(userDao.insertFeedback(user.getId(), feedback)).thenReturn(1);
        assertDoesNotThrow(() -> sut.insertFeedback(user.getId(), feedback));
    }

    @Test
    void testInsertFeedbackNoRowsAffected() {
        Feedback feedback = new Feedback();
        feedback.setId(1);
        feedback.setForUserId(2);
        feedback.setSkillId(22);
        feedback.setDescription("feedback");

        when(userDao.insertFeedback(user.getId(), feedback)).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.insertFeedback(user.getId(), feedback));
    }

    @Test
    void testInsertFeedbackInvalidFromUserId() {
        Feedback feedback = new Feedback();
        feedback.setForUserId(0);
        feedback.setSkillId(22);
        feedback.setId(1);
        feedback.setDescription("feedback");

        assertThrows(ServerException.class, () -> sut.insertFeedback(0, feedback));
    }

    @Test
    void testInsertFeedbackInvalidForUserId() {
        Feedback feedback = new Feedback();
        feedback.setForUserId(0);
        feedback.setSkillId(22);
        feedback.setId(1);
        feedback.setDescription("feedback");

        assertThrows(ServerException.class, () -> sut.insertFeedback(user.getId(), feedback));
    }

    @Test
    void testCreateTokenNoRowsAffected() {
        when(userDao.saveUserToken(anyInt(), anyString())).thenReturn(0);
        assertThrows(ServerException.class, () -> sut.createToken(user.getId()));
    }
}