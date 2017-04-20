package org.jhipster.health.repository;

import org.jhipster.health.domain.Bloodpressure;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bloodpressure entity.
 */
@SuppressWarnings("unused")
public interface BloodpressureRepository extends JpaRepository<Bloodpressure,Long> {

    @Query("select bloodpressure from Bloodpressure bloodpressure where bloodpressure.user.login = ?#{principal.username}")
    List<Bloodpressure> findByUserIsCurrentUser();

}
