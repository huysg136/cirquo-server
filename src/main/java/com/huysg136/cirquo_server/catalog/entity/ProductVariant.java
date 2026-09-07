package com.huysg136.cirquo_server.catalog.entity;

import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import com.huysg136.cirquo_server.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductVariant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(length = 100, nullable = false, unique = true)
    private String sku;

    private String name;

    @Column(nullable = false, precision = 15)
    private BigDecimal price;

    @Column(name = "compare_at_price", precision = 15)
    private BigDecimal compareAtPrice;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> attributes;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CatalogStatus status = CatalogStatus.ACTIVE;

}
