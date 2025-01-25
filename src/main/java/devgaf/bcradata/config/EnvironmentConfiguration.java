package devgaf.bcradata.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import lombok.Getter;

@Configuration
@PropertySource("file:.env")
@Getter
public class EnvironmentConfiguration {
    @Value("${urlBcraPrincipalesVariables}")
    private String urlBcraPrincipalesVariables;

    @Value("${urlBcraBase}")
    private String urlBcraBase;

    @Value("${bcraVersion}")
    private String bcraVersion;

    @Value("${bcraPathBase}")
    private String bcraPathBase;

    @Value("${urlBcraFullRecords}")
    private String urlBcraFullRecords;

    @Value("${urlDolarapi}")
    private String urlDolarapi;
}
