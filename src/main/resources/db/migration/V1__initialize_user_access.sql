-- =========================
-- Roles
-- =========================
CREATE TABLE roles (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,

    CONSTRAINT chk_role_name
        CHECK (name IN ('ADMIN', 'STAFF', 'CUSTOMER'))
);

-- =========================
-- Permissions
-- =========================
CREATE TABLE permissions (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- =========================
-- Role - Permission
-- =========================
CREATE TABLE role_permissions (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,

    PRIMARY KEY (role_id, permission_id),

    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY (role_id) REFERENCES roles(id),

    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

-- Query permission → roles
CREATE INDEX idx_role_permissions_permission_id
    ON role_permissions(permission_id);

-- =========================
-- Users
-- =========================
CREATE TABLE users (
    id UUID PRIMARY KEY,

    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),

    status VARCHAR(20) NOT NULL,
    role_id UUID NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT chk_user_status
        CHECK (status IN ('ACTIVE', 'BLOCKED')),

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Query users theo role
CREATE INDEX idx_users_role_id
    ON users(role_id);

-- =========================
-- User addresses
-- =========================
CREATE TABLE user_addresses (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    recipient_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    province VARCHAR(100) NOT NULL,
    ward VARCHAR(100) NOT NULL,
    address_line VARCHAR(255) NOT NULL,

    is_default BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_user_addresses_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Lấy danh sách address của một user
CREATE INDEX idx_user_addresses_user_id
    ON user_addresses(user_id);

-- Mỗi user tối đa một address mặc định
CREATE UNIQUE INDEX uq_user_addresses_default_per_user
    ON user_addresses(user_id)
    WHERE is_default = TRUE;

-- =========================
-- Seed system roles
-- =========================
INSERT INTO roles (id, name)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'ADMIN'),
    ('00000000-0000-0000-0000-000000000002', 'STAFF'),
    ('00000000-0000-0000-0000-000000000003', 'CUSTOMER');