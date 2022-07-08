package nl.han.project.skilltree.domain.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<RequestT, EntityT, ResponseT> {

    EntityT mapRequestToEntity(RequestT req, Object... args);
    EntityT mapResultSetToEntity(ResultSet rs) throws SQLException;
    ResponseT mapEntityToResponse(EntityT entity, Object... args);
}
