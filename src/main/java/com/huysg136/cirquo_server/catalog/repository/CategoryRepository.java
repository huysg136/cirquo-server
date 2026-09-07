package com.huysg136.cirquo_server.catalog.repository;

import com.huysg136.cirquo_server.catalog.entity.Category;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsBySlug(String slug);

    @EntityGraph(attributePaths = "parent")
    List<Category> findAllByStatusOrderByNameAsc(CatalogStatus status);

    @EntityGraph(attributePaths = "parent")
    Optional<Category> findBySlugAndStatus(String slug, CatalogStatus status);

    boolean existsBySlugAndIdNot(String slug, UUID id);

    @Query("""
        SELECT c
        FROM Category c
        WHERE (:status IS NULL OR c.status = :status)
          AND (
              :keyword IS NULL
              OR LOWER(c.name) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
              OR LOWER(c.slug) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
          )
        ORDER BY c.name ASC
        """)
    @EntityGraph(attributePaths = "parent")
    List<Category> findForAdmin(
            @Param("status") CatalogStatus status,
            @Param("keyword") String keyword
    );
}
