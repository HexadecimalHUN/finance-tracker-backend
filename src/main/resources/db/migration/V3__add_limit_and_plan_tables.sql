-- V3__add_limit_and_plan_tables.sql
-- Create spending_limit table
CREATE TABLE spending_limit (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    amount NUMERIC(38, 2) NOT NULL,
    user_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_spending_limit_user FOREIGN KEY (user_id) REFERENCES app_user (id)
);

-- Create plan table
CREATE TABLE plan (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    amount NUMERIC(38, 2) NOT NULL,
    recurrence_interval_months INT NOT NULL,
    start_date DATE NOT NULL,
    next_transaction_date DATE NOT NULL,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_plan_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_plan_category FOREIGN KEY (category_id) REFERENCES spending_category (id)
);