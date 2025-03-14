package devgaf.bcradata.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import lombok.Getter;

/**
 * se configuraron las variables de entorno en un archivo .env
 * se inyectan las variables de entorno en el archivo EnvironmentConfiguration
 * se utiliza @Getter de lombok para generar los getters de las variables de
 * entorno
 * 
 * Configuración del entorno de la aplicación.
 */
@Configuration
@PropertySource("file:.env")
@Getter
public class EnvironmentConfiguration {

    /*
     * Se inyecta la variable de entorno urlBcraPrincipalesVariables
     * que contiene la url de la api de bcra para obtener las principales variables
     */
    @Value("${urlBcraPrincipalesVariables}")
    private String urlBcraPrincipalesVariables;

    /*
     * Se inyecta la variable de entorno urlBcraBase
     * que contiene la url base de la api de bcra
     */
    @Value("${urlBcraBase}")
    private String urlBcraBase;

    /*
     * Se inyecta la variable de entorno bcraVersion
     * que contiene la version de la api de bcra
     */
    @Value("${bcraVersion}")
    private String bcraVersion;

    /*
     * Se inyecta la variable de entorno bcraPathBase
     * que contiene el path base de la api de bcra
     */
    @Value("${bcraPathBase}")
    private String bcraPathBase;

    /*
     * Se inyecta la variable de entorno urlBcraFullRecords
     * que contiene la url de la api de bcra para obtener todos los registros
     * del ICL
     */
    @Value("${urlBcraFullRecords}")
    private String urlBcraFullRecords;

    /*
     * Se inyecta la variable de entorno urlDolarapi
     * que contiene la url de la api de dolar
     */
    @Value("${urlDolarapi}")
    private String urlDolarapi;
}
