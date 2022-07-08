package nl.han.project.skilltree.domain.providers;

import nl.han.project.skilltree.domain.exceptions.UnauthorizedException;
import nl.han.project.skilltree.domain.global.AuthenticationHelper;
import nl.han.project.skilltree.domain.providers.namebindings.Secured;
import nl.han.project.skilltree.domain.user.service.UserService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Inject
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String authorizationHeader = containerRequestContext.getHeaderString("Authorization");

        if (authorizationHeader == null) {
            throw new UnauthorizedException("Authorization header must be provided");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization header must start with Bearer and must be provided with a token");
        }

        String token = AuthenticationHelper.getBearerFromHeader(authorizationHeader);

        boolean isValid = userService.validateToken(token);

        if (!isValid) {
            throw new UnauthorizedException("Invalid token");
        }
    }
}
