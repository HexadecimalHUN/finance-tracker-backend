package org.acme.demo.repository;

import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.SpendingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpendingCategoryRepository extends JpaRepository<SpendingCategory, Long> {

    List<SpendingCategory> findByUser(AppUser user);

    SpendingCategory findByNameAndUser(String name, AppUser user);
}
