package devgaf.ngrok.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Messages {

    private final MessageSource messageSource;

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json; charset=UTF-8";
    public static final String ERROR_INTERNAL = "error.internal";
    public static final String ERROR_SSL = "error.ssl";
    public static final String ERROR_NO_CONTENT = "error.noContent";
    public static final String ERROR_IO = "error.IoError";
    public static final String ERROR_HEADER = "Error";

    public Messages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
