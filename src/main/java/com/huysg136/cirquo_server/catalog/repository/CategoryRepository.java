package com.huysg136.cirquo_server.catalog.repository;

import com.huysg136.cirquo_server.catalog.entity.Category;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsBySlug(String slug);

    List<Category> findAllByStatusOrderByNameAsc(CatalogStatus status);

    boolean existsBySlugAndIdNot(String slug, UUID id);
}
