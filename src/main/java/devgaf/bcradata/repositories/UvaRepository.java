package devgaf.bcradata.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import devgaf.bcradata.models.UvaEntity;

/**
 * Repositorio de la entidad UvaEntity.
 * 
 * @version 1.0
 */
@Repository
public interface UvaRepository extends JpaRepository<UvaEntity, Long> {

     UvaEntity findByDate(LocalDate date);
     
     List<UvaEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);
}