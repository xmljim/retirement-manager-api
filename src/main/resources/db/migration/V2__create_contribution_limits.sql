-- V2: Contribution Limits Reference Tables
-- Creates reference data tables for IRS contribution limits and phase-out ranges
-- per Phase 1 design spec (docs/design/PHASE1_CONTRIBUTIONS.md)

-- =============================================================================
-- CONTRIBUTION_LIMITS TABLE
-- =============================================================================
-- Stores IRS contribution limits by year and account type
-- limit_type distinguishes between base limits, catch-up provisions, and totals
CREATE TABLE contribution_limits (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    year INTEGER NOT NULL,
    account_type VARCHAR(50) NOT NULL
        CONSTRAINT chk_limit_account_type CHECK (
            account_type IN (
                'TRADITIONAL_401K', 'ROTH_401K', 'TRADITIONAL_IRA', 'ROTH_IRA',
                'SEP_IRA', 'SIMPLE_IRA', 'HSA_SELF', 'HSA_FAMILY', '403B', '457B'
            )
        ),
    limit_type VARCHAR(50) NOT NULL
        CONSTRAINT chk_limit_type CHECK (
            limit_type IN ('BASE', 'CATCHUP_50', 'CATCHUP_55', 'CATCHUP_60_63', 'EMPLOYER_TOTAL', 'COMPENSATION_LIMIT')
        ),
    amount DECIMAL(12, 2) NOT NULL
        CONSTRAINT chk_amount_positive CHECK (amount >= 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Ensure unique combination of year, account_type, and limit_type
    CONSTRAINT uq_contribution_limits UNIQUE (year, account_type, limit_type)
);

-- Index for efficient lookups by year
CREATE INDEX idx_contribution_limits_year ON contribution_limits(year);
CREATE INDEX idx_contribution_limits_account_type ON contribution_limits(account_type);

-- =============================================================================
-- PHASE_OUT_RANGES TABLE
-- =============================================================================
-- Stores MAGI phase-out ranges for IRA contributions and deductions
-- Used to calculate reduced contribution limits based on income
CREATE TABLE phase_out_ranges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    year INTEGER NOT NULL,
    filing_status VARCHAR(50) NOT NULL
        CONSTRAINT chk_phase_out_filing_status CHECK (
            filing_status IN ('SINGLE', 'MARRIED_FILING_JOINTLY', 'MARRIED_FILING_SEPARATELY', 'HEAD_OF_HOUSEHOLD')
        ),
    account_type VARCHAR(50) NOT NULL
        CONSTRAINT chk_phase_out_account_type CHECK (
            account_type IN ('ROTH_IRA', 'TRADITIONAL_IRA', 'TRADITIONAL_IRA_SPOUSE_COVERED')
        ),
    magi_start DECIMAL(12, 2) NOT NULL
        CONSTRAINT chk_magi_start_positive CHECK (magi_start >= 0),
    magi_end DECIMAL(12, 2) NOT NULL
        CONSTRAINT chk_magi_end_positive CHECK (magi_end >= 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Ensure magi_end is greater than or equal to magi_start
    CONSTRAINT chk_magi_range_valid CHECK (magi_end >= magi_start),
    -- Ensure unique combination of year, filing_status, and account_type
    CONSTRAINT uq_phase_out_ranges UNIQUE (year, filing_status, account_type)
);

-- Index for efficient lookups by year
CREATE INDEX idx_phase_out_ranges_year ON phase_out_ranges(year);
CREATE INDEX idx_phase_out_ranges_filing_status ON phase_out_ranges(filing_status);

-- =============================================================================
-- HIGH_EARNER_THRESHOLDS TABLE
-- =============================================================================
-- Stores SECURE 2.0 high earner threshold for mandatory Roth catch-up rule
-- Employees earning above this threshold must make catch-up contributions as Roth
CREATE TABLE high_earner_thresholds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    year INTEGER NOT NULL,
    amount DECIMAL(12, 2) NOT NULL
        CONSTRAINT chk_threshold_positive CHECK (amount >= 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Ensure unique year
    CONSTRAINT uq_high_earner_thresholds_year UNIQUE (year)
);

-- Index for efficient lookups by year
CREATE INDEX idx_high_earner_thresholds_year ON high_earner_thresholds(year);

-- =============================================================================
-- 2025 SEED DATA: CONTRIBUTION LIMITS
-- =============================================================================

-- 401(k) Limits (also applies to 403(b) and 457(b))
INSERT INTO contribution_limits (year, account_type, limit_type, amount) VALUES
    (2025, 'TRADITIONAL_401K', 'BASE', 23500.00),
    (2025, 'TRADITIONAL_401K', 'CATCHUP_50', 7500.00),
    (2025, 'TRADITIONAL_401K', 'CATCHUP_60_63', 11250.00),
    (2025, 'TRADITIONAL_401K', 'EMPLOYER_TOTAL', 70000.00),
    (2025, 'TRADITIONAL_401K', 'COMPENSATION_LIMIT', 350000.00),
    (2025, 'ROTH_401K', 'BASE', 23500.00),
    (2025, 'ROTH_401K', 'CATCHUP_50', 7500.00),
    (2025, 'ROTH_401K', 'CATCHUP_60_63', 11250.00),
    (2025, 'ROTH_401K', 'EMPLOYER_TOTAL', 70000.00),
    (2025, 'ROTH_401K', 'COMPENSATION_LIMIT', 350000.00);

-- 403(b) Limits (same as 401k)
INSERT INTO contribution_limits (year, account_type, limit_type, amount) VALUES
    (2025, '403B', 'BASE', 23500.00),
    (2025, '403B', 'CATCHUP_50', 7500.00),
    (2025, '403B', 'CATCHUP_60_63', 11250.00),
    (2025, '403B', 'EMPLOYER_TOTAL', 70000.00),
    (2025, '403B', 'COMPENSATION_LIMIT', 350000.00);

-- 457(b) Limits (same as 401k)
INSERT INTO contribution_limits (year, account_type, limit_type, amount) VALUES
    (2025, '457B', 'BASE', 23500.00),
    (2025, '457B', 'CATCHUP_50', 7500.00),
    (2025, '457B', 'CATCHUP_60_63', 11250.00),
    (2025, '457B', 'EMPLOYER_TOTAL', 70000.00),
    (2025, '457B', 'COMPENSATION_LIMIT', 350000.00);

-- IRA Limits
INSERT INTO contribution_limits (year, account_type, limit_type, amount) VALUES
    (2025, 'TRADITIONAL_IRA', 'BASE', 7000.00),
    (2025, 'TRADITIONAL_IRA', 'CATCHUP_50', 1000.00),
    (2025, 'ROTH_IRA', 'BASE', 7000.00),
    (2025, 'ROTH_IRA', 'CATCHUP_50', 1000.00);

-- SEP IRA Limits (25% of compensation up to max)
INSERT INTO contribution_limits (year, account_type, limit_type, amount) VALUES
    (2025, 'SEP_IRA', 'BASE', 70000.00),
    (2025, 'SEP_IRA', 'COMPENSATION_LIMIT', 350000.00);

-- SIMPLE IRA Limits
INSERT INTO contribution_limits (year, account_type, limit_type, amount) VALUES
    (2025, 'SIMPLE_IRA', 'BASE', 16500.00),
    (2025, 'SIMPLE_IRA', 'CATCHUP_50', 3500.00),
    (2025, 'SIMPLE_IRA', 'CATCHUP_60_63', 5250.00);

-- HSA Limits
INSERT INTO contribution_limits (year, account_type, limit_type, amount) VALUES
    (2025, 'HSA_SELF', 'BASE', 4300.00),
    (2025, 'HSA_SELF', 'CATCHUP_55', 1000.00),
    (2025, 'HSA_FAMILY', 'BASE', 8550.00),
    (2025, 'HSA_FAMILY', 'CATCHUP_55', 1000.00);

-- =============================================================================
-- 2025 SEED DATA: PHASE-OUT RANGES
-- =============================================================================

-- Roth IRA Phase-out Ranges
INSERT INTO phase_out_ranges (year, filing_status, account_type, magi_start, magi_end) VALUES
    (2025, 'SINGLE', 'ROTH_IRA', 150000.00, 165000.00),
    (2025, 'HEAD_OF_HOUSEHOLD', 'ROTH_IRA', 150000.00, 165000.00),
    (2025, 'MARRIED_FILING_JOINTLY', 'ROTH_IRA', 236000.00, 246000.00),
    (2025, 'MARRIED_FILING_SEPARATELY', 'ROTH_IRA', 0.00, 10000.00);

-- Traditional IRA Deduction Phase-out (when covered by workplace plan)
INSERT INTO phase_out_ranges (year, filing_status, account_type, magi_start, magi_end) VALUES
    (2025, 'SINGLE', 'TRADITIONAL_IRA', 79000.00, 89000.00),
    (2025, 'HEAD_OF_HOUSEHOLD', 'TRADITIONAL_IRA', 79000.00, 89000.00),
    (2025, 'MARRIED_FILING_JOINTLY', 'TRADITIONAL_IRA', 126000.00, 146000.00),
    (2025, 'MARRIED_FILING_SEPARATELY', 'TRADITIONAL_IRA', 0.00, 10000.00);

-- Traditional IRA Deduction Phase-out (when spouse is covered by workplace plan)
INSERT INTO phase_out_ranges (year, filing_status, account_type, magi_start, magi_end) VALUES
    (2025, 'MARRIED_FILING_JOINTLY', 'TRADITIONAL_IRA_SPOUSE_COVERED', 236000.00, 246000.00);

-- =============================================================================
-- 2025 SEED DATA: HIGH EARNER THRESHOLD
-- =============================================================================

-- SECURE 2.0 High Earner Threshold for mandatory Roth catch-up
-- Effective 2026, employees earning > $145,000 in W-2 wages must make catch-up as Roth
INSERT INTO high_earner_thresholds (year, amount) VALUES
    (2025, 145000.00),
    (2026, 145000.00);
