package dj.djibgate.repository;

import dj.djibgate.domain.Voiture;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Voiture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
}
