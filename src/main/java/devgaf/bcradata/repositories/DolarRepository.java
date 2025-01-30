package devgaf.bcradata.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import devgaf.bcradata.models.DolarEntity;

@Repository
public interface DolarRepository extends JpaRepository<DolarEntity, Long> {
}