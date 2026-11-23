-- Apply to an existing restaurant table to add ownership without changing existing rows.
BEGIN;

ALTER TABLE restaurant
    ADD COLUMN IF NOT EXISTS owner_id UUID;

CREATE INDEX IF NOT EXISTS idx_restaurant_owner_id ON restaurant (owner_id);

COMMIT;
