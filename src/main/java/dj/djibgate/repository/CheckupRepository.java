package dj.djibgate.repository;

import dj.djibgate.domain.Checkup;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Checkup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckupRepository extends JpaRepository<Checkup, Long> {
}
