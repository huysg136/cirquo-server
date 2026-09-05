package com.huysg136.cirquo_server.catalog.repository;

import com.huysg136.cirquo_server.catalog.entity.Product;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import org.springframework.data.domain.Pageable;
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

    Optional<Product> findBySlugAndStatus(
            String slug,
            CatalogStatus status
    );

    List<Product> findByStatusOrderByCreatedAtDescIdDesc(
            CatalogStatus status,
            Pageable pageable
    );

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.status = :status
          AND (
              p.createdAt < :cursorCreatedAt
              OR (
                  p.createdAt = :cursorCreatedAt
                  AND p.id < :cursorId
              )
          )
        ORDER BY p.createdAt DESC, p.id DESC
        """)
    List<Product> findByStatusAfterCursor(
            @Param("status") CatalogStatus status,
            @Param("cursorCreatedAt") OffsetDateTime cursorCreatedAt,
            @Param("cursorId") UUID cursorId,
            Pageable pageable
    );
}
