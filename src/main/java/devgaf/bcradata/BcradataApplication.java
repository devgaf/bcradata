package devgaf.bcradata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "devgaf.bcradata.repositories")
public class BcradataApplication {
    public static void main(String[] args) {
        SpringApplication.run(BcradataApplication.class, args);
    }
}
