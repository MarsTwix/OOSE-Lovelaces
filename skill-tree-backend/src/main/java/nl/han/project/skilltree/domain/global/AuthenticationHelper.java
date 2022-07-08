package nl.han.project.skilltree.domain.global;

public class AuthenticationHelper {
    private AuthenticationHelper() {
    }

    public static String getBearerFromHeader(String header) {
        return header.split(" ")[1];
    }
}
