CREATE TABLE menu_category
(
    id            UUID         NOT NULL,
    restaurant_id UUID         NOT NULL,
    name          VARCHAR(255) NOT NULL,
    description   VARCHAR(255),
    CONSTRAINT pk_menu_category PRIMARY KEY (id),
    CONSTRAINT uk_menu_category_restaurant_name UNIQUE (restaurant_id, name),
    CONSTRAINT fk_menu_category_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant (id)
);

CREATE TABLE menu_item
(
    id          UUID             NOT NULL,
    category_id UUID             NOT NULL,
    name        VARCHAR(255)     NOT NULL,
    description VARCHAR(255),
    price       DOUBLE PRECISION,
    image_url   VARCHAR(255),
    available   BOOLEAN,
    tag   VARCHAR(255),

    CONSTRAINT pk_menu_item PRIMARY KEY (id),
    CONSTRAINT fk_menu_item_category FOREIGN KEY (category_id) REFERENCES menu_category (id)
);

ALTER TABLE menu_item
    ADD COLUMN IF NOT EXISTS tag VARCHAR(255);
