package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.mapper.Mapper;
import nl.han.project.skilltree.domain.user.data.User;
import nl.han.project.skilltree.domain.user.presentation.dto.UserRequest;
import nl.han.project.skilltree.domain.user.presentation.dto.UserResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapperImpl implements Mapper<UserRequest, User, UserResponse> {
    @Override
    public User mapRequestToEntity(UserRequest req, Object... args) {
         User entity = new User();
        entity.setEmail(req.getEmail());
        entity.setPassword(req.getPassword());
         return entity;
    }

    @Override
    public User mapResultSetToEntity(ResultSet rs) throws SQLException {
        User entity = new User();
        entity.setId(rs.getInt("Id"));
        entity.setFirstname(rs.getString("Firstname"));
        entity.setLastname(rs.getString("Lastname"));
        entity.setEmail(rs.getString("Email"));
        entity.setPassword(rs.getString("Password"));
        entity.setRoleId(rs.getInt("RoleId"));
        entity.setToken(rs.getString("Token"));
        entity.setCreatedOn(rs.getTimestamp("CreatedOn"));
        return entity;
    }

    @Override
    public UserResponse mapEntityToResponse(User entity, Object... args) {
        UserResponse response = new UserResponse();
        response.setId(entity.getId());
        response.setFirstname(entity.getFirstname());
        response.setLastname(entity.getLastname());
        response.setEmail(entity.getEmail());
        response.setRoleId(entity.getRoleId());
        response.setToken(entity.getToken());
        return response;
    }
}
