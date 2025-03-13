package devgaf.ngrok.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import devgaf.ngrok.models.DolarEntity;

/**
 * Repositorio de la entidad DolarEntity.
 * 
 * @version 1.0
 */
@Repository
public interface DolarRepository extends JpaRepository<DolarEntity, Long> {
    /**
     * Encuentra un DolarEntity por la fecha de última actualización.
     *
     * @param lastUpdated la fecha de última actualización
     * @return el DolarEntity encontrado
     */
    DolarEntity findByLastUpdated(LocalDate lastUpdated);
}