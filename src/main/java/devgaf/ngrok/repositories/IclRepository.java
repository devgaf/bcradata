package devgaf.ngrok.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import devgaf.ngrok.models.IclEntity;

/**
 * Repositorio de la entidad IclEntity.
 * 
 * @version 1.0
 */
@Repository
public interface IclRepository extends JpaRepository<IclEntity, Long> {

     IclEntity findByDate(LocalDate date);
}
