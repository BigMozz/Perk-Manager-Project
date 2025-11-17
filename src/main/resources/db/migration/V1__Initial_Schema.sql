-- V1__Initial_Schema.sql
-- SQL Server migration script

-- Create app_user table
CREATE TABLE app_user (
                          uid INT IDENTITY(1,1) PRIMARY KEY,
                          name NVARCHAR(255),
                          email NVARCHAR(255) UNIQUE NOT NULL,
                          password NVARCHAR(255) NOT NULL
);

-- Create membership table
CREATE TABLE membership (
                            mid INT IDENTITY(1,1) PRIMARY KEY,
                            uid INT,
                            type NVARCHAR(255),
                            number INT,
                            CONSTRAINT FK_membership_user FOREIGN KEY (uid) REFERENCES app_user(uid) ON DELETE CASCADE
);

-- Create perk table
CREATE TABLE perk (
                      pid INT IDENTITY(1,1) PRIMARY KEY,
                      title NVARCHAR(255),
                      discount NVARCHAR(255),
                      product NVARCHAR(255),
                      expiry_date DATE,
                      mid INT,
                      CONSTRAINT FK_perk_membership FOREIGN KEY (mid) REFERENCES membership(mid) ON DELETE CASCADE
);

-- Create rating table
CREATE TABLE rating (
                        rid INT IDENTITY(1,1) PRIMARY KEY,
                        uid INT,
                        pid INT,
                        upvote INT DEFAULT 0,
                        downvote INT DEFAULT 0,
                        CONSTRAINT FK_rating_user FOREIGN KEY (uid) REFERENCES app_user(uid) ON DELETE CASCADE,
                        CONSTRAINT FK_rating_perk FOREIGN KEY (pid) REFERENCES perk(pid) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_user_email ON app_user(email);
CREATE INDEX idx_membership_uid ON membership(uid);
CREATE INDEX idx_perk_mid ON perk(mid);
CREATE INDEX idx_rating_uid ON rating(uid);
CREATE INDEX idx_rating_pid ON rating(pid);