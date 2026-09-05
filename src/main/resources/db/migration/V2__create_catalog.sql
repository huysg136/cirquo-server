-- =========================
-- Categories
-- =========================
CREATE TABLE categories (
    id UUID PRIMARY KEY,

    parent_id UUID
        REFERENCES categories(id),

    name VARCHAR(100) NOT NULL,
    slug VARCHAR(150) NOT NULL UNIQUE,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT chk_category_status
        CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_categories_parent_id
    ON categories(parent_id);


-- =========================
-- Products
-- =========================
CREATE TABLE products (
    id UUID PRIMARY KEY,

    category_id UUID NOT NULL
        REFERENCES categories(id),

    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,

    short_description TEXT,
    description TEXT,
    specs JSONB,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT chk_product_status
        CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_products_category_id
    ON products(category_id);


-- =========================
-- Product variants
-- =========================
CREATE TABLE product_variants (
    id UUID PRIMARY KEY,

    product_id UUID NOT NULL
        REFERENCES products(id),

    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255),

    price NUMERIC(15, 0) NOT NULL,
    compare_at_price NUMERIC(15, 0),

    attributes JSONB,

    stock_quantity INT NOT NULL DEFAULT 0,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT chk_variant_price
        CHECK (price >= 0),

    CONSTRAINT chk_variant_compare_at_price
        CHECK (compare_at_price IS NULL OR compare_at_price >= 0),

    CONSTRAINT chk_variant_stock_quantity
        CHECK (stock_quantity >= 0),

    CONSTRAINT chk_variant_status
        CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_product_variants_product_id
    ON product_variants(product_id);


-- =========================
-- Product images
-- =========================
CREATE TABLE product_images (
    id UUID PRIMARY KEY,

    product_id UUID NOT NULL
        REFERENCES products(id),

    variant_id UUID
        REFERENCES product_variants(id),

    image_url TEXT NOT NULL,

    sort_order INT NOT NULL DEFAULT 0,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT chk_product_image_sort_order
        CHECK (sort_order >= 0)
);

CREATE INDEX idx_product_images_product_id
    ON product_images(product_id);

CREATE INDEX idx_product_images_variant_id
    ON product_images(variant_id);

-- Mỗi Product chỉ có một ảnh chính chung.
CREATE UNIQUE INDEX uq_product_images_product_primary
    ON product_images(product_id)
    WHERE variant_id IS NULL AND is_primary = TRUE;

-- Mỗi Variant chỉ có một ảnh chính riêng.
CREATE UNIQUE INDEX uq_product_images_variant_primary
    ON product_images(variant_id)
    WHERE variant_id IS NOT NULL AND is_primary = TRUE;