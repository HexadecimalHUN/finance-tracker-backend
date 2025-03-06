package org.acme.demo.repository;

import org.acme.demo.entity.PredefinedLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredefinedLimitRepository extends JpaRepository<PredefinedLimit, Long> {

}
