CREATE TABLE restaurant
(
    id                UUID                           NOT NULL,
    owner_id           UUID,
    created_at        TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    updated_at        TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    name              VARCHAR(255)                   NOT NULL,
    description       VARCHAR(1000),
    email             VARCHAR(255)                   NOT NULL,
    phone_number      VARCHAR(255)                   NOT NULL,
    status            VARCHAR(255)                   NOT NULL,
    latitude          DOUBLE PRECISION               NOT NULL,
    longitude         DOUBLE PRECISION               NOT NULL,
    delivery_radius_m INTEGER                        NOT NULL,
    rating            DOUBLE PRECISION               NOT NULL,
    rating_count      INTEGER                        NOT NULL,
    street_address    VARCHAR(255)                   NOT NULL,
    city              VARCHAR(255)                   NOT NULL,
    state             VARCHAR(255),
    country           VARCHAR(255)                   NOT NULL,
    CONSTRAINT pk_restaurant PRIMARY KEY (id),
    CONSTRAINT uk_restaurant_email UNIQUE (email),
    CONSTRAINT uk_restaurant_phone_number UNIQUE (phone_number)
);

CREATE INDEX idx_restaurant_owner_id ON restaurant (owner_id);

COMMENT ON COLUMN restaurant.owner_id IS
    'Owner auth-service user id. NULL = legacy/unassigned: not merchant-accessible until an operator assigns a verified owner from the authoritative mapping. Never guess or backfill from request data.';