-- Ownership rollout for restaurants that existed before owner_id was introduced.
--
-- Policy for legacy rows (owner_id IS NULL):
--   * No owner is guessed or backfilled from request data. This repository has
--     no authoritative restaurant -> auth-user mapping, so inventing a value
--     would grant access to the wrong merchant.
--   * A legacy restaurant with owner_id IS NULL is treated as unassigned and is
--     NOT merchant-accessible: RestaurantAccessServiceImpl#checkMembership
--     rejects every merchant request with 404 until an owner is assigned.
--   * Assigning an owner is an explicit, audited operator step that must use an
--     authoritative mapping (auth-service / merchant onboarding records).
--
-- Rollout order:
--   1. Apply this script (adds owner_id + index; existing rows stay NULL).
--   2. Assign verified owners from the authoritative mapping (template below).
--   3. Verify the remaining NULL rows are intentionally unassigned (query below).
--   4. Only then deploy/enable merchant access that relies on checkMembership.
-- Deny-by-default for NULL owners means steps 1-2 cannot expose legacy data.

BEGIN;

ALTER TABLE restaurant
    ADD COLUMN IF NOT EXISTS owner_id UUID;

CREATE INDEX IF NOT EXISTS idx_restaurant_owner_id ON restaurant (owner_id);

COMMENT ON COLUMN restaurant.owner_id IS
    'Owner auth-service user id. NULL = legacy/unassigned: not merchant-accessible until an operator assigns a verified owner from the authoritative mapping. Never guess or backfill from request data.';

-- Verification (run after assignment, before enabling merchant access):
--   SELECT id, name, email FROM restaurant WHERE owner_id IS NULL;
--
-- Assignment template (run separately, only with an authoritative mapping):
--   UPDATE restaurant r
--      SET owner_id = m.owner_id
--     FROM legacy_restaurant_owner_map m
--    WHERE r.id = m.restaurant_id
--      AND r.owner_id IS NULL
--      AND m.verified = TRUE;

COMMIT;
