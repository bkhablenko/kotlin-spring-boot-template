DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'uuidv7') THEN
        RETURN;
    END IF;
    CREATE DOMAIN "uuidv7" AS uuid CONSTRAINT "uuidv7_version_check" CHECK (uuid_extract_version(VALUE) IS NOT DISTINCT FROM 7);
END;
$$ LANGUAGE plpgsql;

CREATE TABLE IF NOT EXISTS "user"
(
    "id" uuidv7 PRIMARY KEY DEFAULT uuidv7(),

    "username" text NOT NULL UNIQUE,

    "created_at" timestamptz NOT NULL DEFAULT now(),
    "updated_at" timestamptz NOT NULL DEFAULT now()
);
