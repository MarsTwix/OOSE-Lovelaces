package nl.han.project.skilltree.domain.impl.mapper;

import nl.han.project.skilltree.domain.user.data.User;
import nl.han.project.skilltree.domain.user.presentation.dto.UserRequest;
import nl.han.project.skilltree.domain.user.presentation.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserMapperImplTest {

    private UserMapperImpl sut;

    @BeforeEach
    public void setup() {
        sut = new UserMapperImpl();
    }

    @Test
    void mapRequestToEntity() {
        UserRequest req = new UserRequest();
        req.setEmail("email");
        req.setPassword("password");

        User entity = new User();
        entity.setEmail("email");
        entity.setPassword("password");

        User result = sut.mapRequestToEntity(req);

        assertEquals(entity.getEmail(), result.getEmail());
        assertEquals(entity.getPassword(), result.getPassword());
    }

    @Test
    void testMapResultSetToEntity() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("Id")).thenReturn(1);
        when(rs.getString("Firstname")).thenReturn("firstname");
        when(rs.getString("Lastname")).thenReturn("lastname");
        when(rs.getString("Email")).thenReturn("email");
        when(rs.getString("Password")).thenReturn("password");
        when(rs.getInt("RoleId")).thenReturn(2);
        when(rs.getString("Token")).thenReturn("token");
        when(rs.getTimestamp("CreatedOn")).thenReturn(null);

        User entity = new User();
        entity.setId(1);
        entity.setFirstname("firstname");
        entity.setLastname("lastname");
        entity.setEmail("email");
        entity.setPassword("password");
        entity.setRoleId(2);
        entity.setToken("token");
        entity.setCreatedOn(null);

        User result = sut.mapResultSetToEntity(rs);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getFirstname(), result.getFirstname());
        assertEquals(entity.getLastname(), result.getLastname());
        assertEquals(entity.getEmail(), result.getEmail());
        assertEquals(entity.getPassword(), result.getPassword());
        assertEquals(entity.getRoleId(), result.getRoleId());
        assertEquals(entity.getToken(), result.getToken());
        assertEquals(entity.getCreatedOn(), result.getCreatedOn());
    }

    @Test
    void testMapEntityToResponse() {
        User entity = new User();
        entity.setId(1);
        entity.setFirstname("firstname");
        entity.setLastname("lastname");
        entity.setEmail("email");
        entity.setPassword("password");
        entity.setRoleId(2);
        entity.setToken("token");

        UserResponse result = sut.mapEntityToResponse(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getFirstname(), result.getFirstname());
        assertEquals(entity.getLastname(), result.getLastname());
        assertEquals(entity.getEmail(), result.getEmail());
        assertEquals(entity.getRoleId(), result.getRoleId());
        assertEquals(entity.getToken(), result.getToken());
    }
}
