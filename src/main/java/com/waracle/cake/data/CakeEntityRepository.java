package com.waracle.cake.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CakeEntityRepository extends JpaRepository<CakeEntity, Integer> {
}
