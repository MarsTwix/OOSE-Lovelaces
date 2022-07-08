package nl.han.project.skilltree.domain.providers;

import nl.han.project.skilltree.domain.exceptions.ServerException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerExceptionMapper implements ExceptionMapper<ServerException> {
    @Override
    public Response toResponse(ServerException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
