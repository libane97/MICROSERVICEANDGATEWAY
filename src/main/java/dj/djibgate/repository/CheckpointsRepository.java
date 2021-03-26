package dj.djibgate.repository;

import dj.djibgate.domain.Checkpoints;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Checkpoints entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckpointsRepository extends JpaRepository<Checkpoints, Long> {
}
