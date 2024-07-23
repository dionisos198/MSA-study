package org.example.repository;

import java.util.Optional;
import org.example.entity.CatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<CatalogEntity,Long> {

    Optional<CatalogEntity> findByProductId(String productId);

}
