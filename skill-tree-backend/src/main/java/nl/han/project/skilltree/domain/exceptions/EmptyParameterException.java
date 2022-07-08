package nl.han.project.skilltree.domain.exceptions;

public class EmptyParameterException extends RuntimeException {
    public EmptyParameterException(String message) {
        super(message);
    }
}
