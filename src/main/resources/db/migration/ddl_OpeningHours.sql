CREATE TABLE opening_hours
(
    id            UUID                      NOT NULL,
    restaurant_id UUID                      NOT NULL,
    day_of_week   INTEGER                   NOT NULL,
    opens_at      time(6) WITHOUT TIME ZONE NOT NULL,
    closes_at     time(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_opening_hours PRIMARY KEY (id)
);

ALTER TABLE opening_hours
    ADD CONSTRAINT FK_OPENING_HOURS_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);