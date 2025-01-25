package devgaf.bcradata.exceptions;

public class SSLConfigurationException extends RuntimeException {
    public SSLConfigurationException() {
        super("SSL Configuration Error");
    }

    public SSLConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSLConfigurationException(String message) {
        super(message);
    }
}
