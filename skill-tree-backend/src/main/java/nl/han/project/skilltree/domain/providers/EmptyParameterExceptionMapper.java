package nl.han.project.skilltree.domain.providers;

import nl.han.project.skilltree.domain.exceptions.EmptyParameterException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EmptyParameterExceptionMapper implements ExceptionMapper<EmptyParameterException> {

    @Override
    public Response toResponse(EmptyParameterException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
