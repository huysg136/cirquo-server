package com.huysg136.cirquo_server.catalog.repository;

import com.huysg136.cirquo_server.catalog.entity.Product;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, UUID productId);

    @EntityGraph(attributePaths = "category")
    Optional<Product> findBySlugAndStatusAndCategoryStatus(
            String slug,
            CatalogStatus status,
            CatalogStatus categoryStatus
    );

    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryIdAndStatusOrderByCreatedAtDescIdDesc(
            UUID categoryId,
            CatalogStatus status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "category")
    @Query("""
        SELECT p
        FROM Product p
        WHERE p.category.id = :categoryId
          AND p.status = :status
          AND (
              p.createdAt < :cursorCreatedAt
              OR (
                  p.createdAt = :cursorCreatedAt
                  AND p.id < :cursorId
              )
          )
        ORDER BY p.createdAt DESC, p.id DESC
        """)
    List<Product> findByCategoryAndStatusAfterCursor(
            @Param("categoryId") UUID categoryId,
            @Param("status") CatalogStatus status,
            @Param("cursorCreatedAt") OffsetDateTime cursorCreatedAt,
            @Param("cursorId") UUID cursorId,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "category")
    @Query("""
        SELECT p
        FROM Product p
        WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
          AND (:status IS NULL OR p.status = :status)
          AND (
              :keyword IS NULL
              OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
              OR LOWER(p.slug) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
          )
        """)
    Page<Product> findForAdmin(
            @Param("categoryId") UUID categoryId,
            @Param("status") CatalogStatus status,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
