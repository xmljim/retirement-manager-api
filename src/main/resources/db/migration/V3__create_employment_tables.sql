-- V3: Employment and Income Tables
-- Creates tables for tracking employment history and income records
-- per Phase 2 design spec (M2: Employment & Income)

-- =============================================================================
-- EMPLOYERS TABLE
-- =============================================================================
-- Represents companies/organizations that employ people
CREATE TABLE employers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    ein VARCHAR(10)
        CONSTRAINT chk_ein_format CHECK (
            ein IS NULL OR ein ~ '^\d{2}-\d{7}$'
        ),
    address_line1 VARCHAR(200),
    address_line2 VARCHAR(200),
    city VARCHAR(100),
    state CHAR(2)
        CONSTRAINT chk_employer_state_code CHECK (
            state IS NULL OR state ~ '^[A-Z]{2}$'
        ),
    zip_code VARCHAR(10)
        CONSTRAINT chk_zip_format CHECK (
            zip_code IS NULL OR zip_code ~ '^\d{5}(-\d{4})?$'
        ),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index for common query patterns
CREATE INDEX idx_employers_name ON employers(name);

-- =============================================================================
-- EMPLOYMENT TABLE
-- =============================================================================
-- Tracks employment records linking persons to employers
-- Used for calculating contribution eligibility and employer matching
CREATE TABLE employment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    person_id UUID NOT NULL REFERENCES persons(id) ON DELETE CASCADE,
    employer_id UUID NOT NULL REFERENCES employers(id) ON DELETE RESTRICT,
    job_title VARCHAR(200),
    start_date DATE NOT NULL,
    end_date DATE,
    employment_type VARCHAR(30) NOT NULL
        CONSTRAINT chk_employment_type CHECK (
            employment_type IN ('FULL_TIME', 'PART_TIME', 'CONTRACT', 'SELF_EMPLOYED')
        ),
    is_retirement_plan_eligible BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Ensure end_date is after start_date when present
    CONSTRAINT chk_employment_dates CHECK (
        end_date IS NULL OR end_date >= start_date
    )
);

-- Indexes for efficient lookups
CREATE INDEX idx_employment_person_id ON employment(person_id);
CREATE INDEX idx_employment_employer_id ON employment(employer_id);
CREATE INDEX idx_employment_start_date ON employment(start_date);
CREATE INDEX idx_employment_end_date ON employment(end_date);

-- =============================================================================
-- INCOME TABLE
-- =============================================================================
-- Tracks annual income records for each employment
-- W2 wages are tracked separately for SECURE 2.0 high earner catch-up rules
CREATE TABLE income (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employment_id UUID NOT NULL REFERENCES employment(id) ON DELETE CASCADE,
    year INTEGER NOT NULL
        CONSTRAINT chk_income_year_range CHECK (
            year >= 1900 AND year <= 2100
        ),
    annual_salary DECIMAL(12, 2) NOT NULL DEFAULT 0
        CONSTRAINT chk_salary_non_negative CHECK (annual_salary >= 0),
    bonus DECIMAL(12, 2) NOT NULL DEFAULT 0
        CONSTRAINT chk_bonus_non_negative CHECK (bonus >= 0),
    other_compensation DECIMAL(12, 2) NOT NULL DEFAULT 0
        CONSTRAINT chk_other_comp_non_negative CHECK (other_compensation >= 0),
    w2_wages DECIMAL(12, 2)
        CONSTRAINT chk_w2_wages_non_negative CHECK (w2_wages IS NULL OR w2_wages >= 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Ensure unique income record per employment per year
    CONSTRAINT uq_income_employment_year UNIQUE (employment_id, year)
);

-- Index for efficient lookups by year
CREATE INDEX idx_income_employment_id ON income(employment_id);
CREATE INDEX idx_income_year ON income(year);
