-- V5__update_predefined_limit_and_spending_limit.sql

-- Drop the existing predefined_limit table (if needed) and recreate it with updated structure
DROP TABLE IF EXISTS predefined_limit CASCADE;

-- Recreate predefined_limit table with updated columns
CREATE TABLE predefined_limit (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    amount NUMERIC(38, 2) NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

-- Modify the spending_limit table to add new columns for predefined limits if they do not exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_name = 'spending_limit' AND column_name = 'is_predefined') THEN
        ALTER TABLE spending_limit
        ADD COLUMN is_predefined BOOLEAN NOT NULL DEFAULT FALSE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_name = 'spending_limit' AND column_name = 'predefined_id') THEN
        ALTER TABLE spending_limit
        ADD COLUMN predefined_id BIGINT,
        ADD CONSTRAINT fk_spending_limit_predefined FOREIGN KEY (predefined_id) REFERENCES predefined_limit (id);
    END IF;
END $$;

-- Insert default predefined limits into the new predefined_limit table
INSERT INTO predefined_limit (amount, title, description) VALUES
(4600.00, '4600 USD / month', 'An average single American household spending each month'),
(7900.00, '7900 USD / month', 'Married American couple with a kid spending in average each month'),
(9200.00, '9200 USD / month', 'Married American couple with two kids spending in average each month');