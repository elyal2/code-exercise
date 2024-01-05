-- Activate Row-Level Security for the Database
ALTER DATABASE wfdb SET row_security = 'on';

-- Create a new user for database connections
DO $$
BEGIN
    IF NOT EXISTS(SELECT * FROM pg_roles WHERE rolname = '${APP_USER}') THEN
        CREATE USER ${APP_USER} WITH LOGIN PASSWORD '${APP_USER_PASSWORD}';
    END IF;
END $$;

-- Create the Employee Table with updates
CREATE TABLE employees (
    id UUID PRIMARY KEY,  -- UUID for the primary key
    tenant_id VARCHAR(1024) NOT NULL,
    name VARCHAR(2048),
    surname VARCHAR(2048),
    status VARCHAR(50) NOT NULL DEFAULT 'REGISTERED' check (status in ('REGISTERED', 'ACTIVE', 'SUSPENDED', 'DELETED')),
    date_registered TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_status_update TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(1024),
    reports_to_id UUID,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(1024),
    last_modified_at TIMESTAMP(6) WITHOUT TIME ZONE,
    last_modified_by VARCHAR(1024),
    FOREIGN KEY (reports_to_id) REFERENCES employees(id)  -- Foreign key constraint for self-referencing
);
GRANT SELECT, INSERT, UPDATE, DELETE ON employees TO ${APP_USER};

-- Enabling Row-Level Security on the Employee Table
ALTER TABLE employees ENABLE ROW LEVEL SECURITY;

-- Create the Revision Info Table for Envers
CREATE TABLE revinfo (
    rev BIGINT PRIMARY KEY,  -- Using BIGINT for revision number, column name match Envers default
    revtstmp BIGINT NOT NULL  -- Column name match Envers default
);
GRANT SELECT, INSERT, UPDATE, DELETE ON revinfo TO ${APP_USER};

-- Create sequence for Hibernate, if not using the default sequence
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1 INCREMENT 1;
GRANT USAGE, SELECT ON SEQUENCE hibernate_sequence TO ${APP_USER};

-- Create Row-Level Security Policy for the Employee Table
CREATE POLICY employee_rls_policy ON employees
USING (tenant_id = current_setting('app.current_tenant')::VARCHAR);

CREATE TABLE jobs (
    id UUID PRIMARY KEY,  -- UUID for the primary key
    tenant_id VARCHAR(1024) NOT NULL,
    name VARCHAR(1024),  -- Job Position name
    description VARCHAR(2048),  -- Job Position description
    category VARCHAR(2048),  -- Professional Category
    group_agreement VARCHAR(1024),  -- Professional Group Agreement
    people_managed SMALLINT,  -- People in this Position, using SMALLINT for the range
    department VARCHAR(1024),  -- Department
    location VARCHAR(1024),  -- Location of the Position
    work_center VARCHAR(1024),  -- Work Center
    work_schedule VARCHAR(1024),  -- Work Schedule
    is_teleworking BOOLEAN,  -- Teleworking (yes/no)
    has_travel_availability BOOLEAN,  -- Availability to Travel (yes/no)
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,  -- Audit field
    created_by VARCHAR(1024),  -- Audit field
    last_modified_at TIMESTAMP(6) WITHOUT TIME ZONE,  -- Audit field
    last_modified_by VARCHAR(1024)  -- Audit field
);

GRANT SELECT, INSERT, UPDATE, DELETE ON jobs TO ${APP_USER};

-- Enabling Row-Level Security on the Jobs Table
ALTER TABLE jobs ENABLE ROW LEVEL SECURITY;

-- Create Row-Level Security Policy for the Jobs Table
CREATE POLICY job_rls_policy ON jobs
USING (tenant_id = current_setting('app.current_tenant')::VARCHAR);

-- Create the Skills Table
CREATE TABLE skills (
    id UUID PRIMARY KEY,  -- UUID for the primary key
    tenant_id VARCHAR(1024) NOT NULL,
    name VARCHAR(1024),  -- Name of the Skill
    priority VARCHAR(50) NOT NULL,  -- Criticality of the Skill
    internal_interaction VARCHAR(50) NOT NULL,  -- Internal Interaction Frequency
    external_interaction VARCHAR(50) NOT NULL,  -- External Interaction Frequency
    education VARCHAR(1024),  -- Education/Qualification
    specialization VARCHAR(1024),  -- Specialization/Postgraduate Studies
    previous_work_experience VARCHAR(1024),  -- Previous Work Experience
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,  -- Audit field
    created_by VARCHAR(1024),  -- Audit field
    last_modified_at TIMESTAMP(6) WITHOUT TIME ZONE,  -- Audit field
    last_modified_by VARCHAR(1024)  -- Audit field
);
GRANT SELECT, INSERT, UPDATE, DELETE ON skills TO ${APP_USER};

-- Create the Language Skills Table (ElementCollection)
CREATE TABLE language_skills (
    skill_id UUID NOT NULL,
    name VARCHAR(1024) NOT NULL,
    level VARCHAR(50) NOT NULL,
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON language_skills TO ${APP_USER};

-- Create the Office Automation Skills Table (ElementCollection)
CREATE TABLE office_automation_skills (
    skill_id UUID NOT NULL,
    name VARCHAR(1024) NOT NULL,
    level VARCHAR(50) NOT NULL,
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON office_automation_skills TO ${APP_USER};

-- Enabling Row-Level Security on the Skills Table
ALTER TABLE skills ENABLE ROW LEVEL SECURITY;

-- Create Row-Level Security Policy for the Skills Table
CREATE POLICY skill_rls_policy ON skills
USING (tenant_id = current_setting('app.current_tenant')::VARCHAR);

