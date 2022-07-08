package nl.han.project.skilltree.domain.impl.data;

import nl.han.project.skilltree.datasource.util.DatabaseConnection;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.impl.mapper.UserMapperImpl;
import nl.han.project.skilltree.domain.user.data.Feedback;
import nl.han.project.skilltree.domain.user.data.User;
import nl.han.project.skilltree.domain.user.data.UserDao;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Default
public class UserDaoImpl implements UserDao {

    @Inject
    private UserMapperImpl mapper;

    @Override
    public User getUserByEmail(String email) {
        User user = null;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
        ) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapper.mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServerException("Error while getting user by email");
        }

        return user;
    }

    @Override
    public User getUserByToken(String token) {
        User user = null;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE token = ?");
        ) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapper.mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            throw new ServerException("Error while getting user by token");
        }


        return user;
    }

    @Override
    public void logout(String token) {
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET token = null WHERE token = ?")
        ) {
            stmt.setString(1, token);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException("Error while logging out");
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
             ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                users.add(mapper.mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new ServerException("Error while retrieving all users");
        }

        return users;
    }

    public int saveUserToken(int id, String token) {
        int affectedRows;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET token = ? WHERE Id = ?")
        ) {
            stmt.setString(1, token);
            stmt.setInt(2, id);
            affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException("Error while saving user token");
        }
        return affectedRows;
    }

    @Override
    public int insertFeedback(int fromUserId, Feedback feedback) {
        int affectedRows;
        try (Connection conn = DatabaseConnection.buildConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO feedbacks (SkillId, ForUserId, FromUserId, Feedback) VALUES (?, ?, ?, ?)")
        ) {
            stmt.setInt(1, feedback.getSkillId());
            stmt.setInt(2, feedback.getForUserId());
            stmt.setInt(3, fromUserId);
            stmt.setString(4, feedback.getDescription());
            affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException("Error while inserting feedback");
        }

        return affectedRows;
    }
}
