-- Initial schema for Retirement Manager
-- This migration creates the core tables for person and account management

CREATE TABLE person (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    filing_status VARCHAR(50) NOT NULL,
    state VARCHAR(2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE income (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    person_id UUID NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    income_type VARCHAR(50) NOT NULL,
    source_name VARCHAR(200),
    amount DECIMAL(12, 2) NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    person_id UUID NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    account_type VARCHAR(50) NOT NULL,
    account_name VARCHAR(200),
    balance DECIMAL(14, 2) NOT NULL DEFAULT 0,
    contribution_ytd DECIMAL(12, 2) NOT NULL DEFAULT 0,
    employer_name VARCHAR(200),
    employer_match_percent DECIMAL(5, 2),
    employer_match_limit_percent DECIMAL(5, 2),
    vesting_percent DECIMAL(5, 2) DEFAULT 100,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_income_person_id ON income(person_id);
CREATE INDEX idx_account_person_id ON account(person_id);
CREATE INDEX idx_account_type ON account(account_type);
