-- V1: Core Tables for Retirement Manager
-- Creates persons and marriages tables per Phase 1 design spec

-- =============================================================================
-- PERSONS TABLE
-- =============================================================================
-- Core entity representing an individual user of the retirement planning system
CREATE TABLE persons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    filing_status VARCHAR(50) NOT NULL
        CONSTRAINT chk_filing_status CHECK (
            filing_status IN ('SINGLE', 'MARRIED_FILING_JOINTLY', 'MARRIED_FILING_SEPARATELY', 'HEAD_OF_HOUSEHOLD')
        ),
    state_of_residence CHAR(2)
        CONSTRAINT chk_state_code CHECK (
            state_of_residence IS NULL OR state_of_residence ~ '^[A-Z]{2}$'
        ),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index for common query patterns
CREATE INDEX idx_persons_last_name ON persons(last_name);
CREATE INDEX idx_persons_filing_status ON persons(filing_status);

-- =============================================================================
-- MARRIAGES TABLE
-- =============================================================================
-- Tracks marriage history for Social Security spousal benefit calculations
-- Note: SS divorced spouse benefits require 10+ year marriages
CREATE TABLE marriages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    person1_id UUID NOT NULL REFERENCES persons(id) ON DELETE CASCADE,
    person2_id UUID NOT NULL REFERENCES persons(id) ON DELETE CASCADE,
    marriage_date DATE NOT NULL,
    divorce_date DATE,
    status VARCHAR(20) NOT NULL
        CONSTRAINT chk_marriage_status CHECK (
            status IN ('MARRIED', 'DIVORCED', 'WIDOWED')
        ),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Ensure divorce_date is after marriage_date when present
    CONSTRAINT chk_divorce_after_marriage CHECK (
        divorce_date IS NULL OR divorce_date > marriage_date
    ),
    -- Ensure person1 and person2 are different people
    CONSTRAINT chk_different_persons CHECK (person1_id <> person2_id)
);

-- Indexes for efficient lookups
CREATE INDEX idx_marriages_person1_id ON marriages(person1_id);
CREATE INDEX idx_marriages_person2_id ON marriages(person2_id);
CREATE INDEX idx_marriages_status ON marriages(status);
CREATE INDEX idx_marriages_marriage_date ON marriages(marriage_date);
