-- V4__add_predefined_limit_table.sql
-- Create predefined_limit table
CREATE TABLE predefined_limit (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    amount NUMERIC(38, 2) NOT NULL,
    description VARCHAR(255) NOT NULL
);

-- Modify spending_limit table to support predefined limits
ALTER TABLE spending_limit
ADD COLUMN is_predefined BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN predefined_id BIGINT,
ADD CONSTRAINT fk_spending_limit_predefined FOREIGN KEY (predefined_id) REFERENCES predefined_limit (id);

INSERT INTO predefined_limit (amount, description) VALUES
(500.00, 'Basic Monthly Limit'),
(1000.00, 'Standard Monthly Limit'),
(1500.00, 'Premium Monthly Limit'),
(2000.00, 'High-Spender Monthly Limit');