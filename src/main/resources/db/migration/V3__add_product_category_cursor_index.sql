CREATE INDEX idx_products_category_status_created_at_id
    ON products(category_id, status, created_at DESC, id DESC);
