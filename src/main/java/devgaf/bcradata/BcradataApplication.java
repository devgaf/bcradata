package devgaf.bcradata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal de la aplicación.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "devgaf.bcradata.repositories")
public class BcradataApplication {
    /**
     * Metodo principal.
     *
     * @param args argumentos de la linea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(BcradataApplication.class, args);
    }
}
