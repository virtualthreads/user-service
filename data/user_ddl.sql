
------
-- USERS
------
CREATE DATABASE ecom_users;

CREATE TABLE users (
                       user_id CHAR(36) NOT NULL,

                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100),

                       email VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(20),

                       password_hash VARCHAR(255) NOT NULL,

                       gender ENUM('MALE', 'FEMALE', 'OTHER'),

                       date_of_birth DATE,

                       email_verified BOOLEAN NOT NULL DEFAULT FALSE,
                       phone_verified BOOLEAN NOT NULL DEFAULT FALSE,

                       status ENUM(
        'ACTIVE',
        'INACTIVE',
        'LOCKED',
        'SUSPENDED',
        'DELETED'
    ) NOT NULL DEFAULT 'ACTIVE',

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

                       PRIMARY KEY (user_id),

                       UNIQUE (email)
);

------
-- ROLES
------

CREATE TABLE roles (

                       role_id CHAR(36) NOT NULL,

                       role_name VARCHAR(50) NOT NULL,

                       description VARCHAR(255),

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

                       PRIMARY KEY (role_id),

                       UNIQUE (role_name)

);

------
-- USER ROLES
------

CREATE TABLE user_roles (

                            user_role_id CHAR(36) NOT NULL,

                            user_id CHAR(36) NOT NULL,

                            role_id CHAR(36) NOT NULL,

                            assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            PRIMARY KEY (user_role_id),

                            FOREIGN KEY (user_id)
                                REFERENCES users(user_id),

                            FOREIGN KEY (role_id)
                                REFERENCES roles(role_id)

);

------
-- ADDRESSES
------


CREATE TABLE addresses (

                           address_id CHAR(36) NOT NULL,

                           user_id CHAR(36) NOT NULL,

                           address_type ENUM(
        'HOME',
        'WORK',
        'OTHER'
    ) NOT NULL,

                           recipient_name VARCHAR(150) NOT NULL,

                           phone_number VARCHAR(20),

                           address_line_1 VARCHAR(255) NOT NULL,

                           address_line_2 VARCHAR(255),

                           landmark VARCHAR(255),

                           city VARCHAR(100) NOT NULL,

                           state VARCHAR(100) NOT NULL,

                           country VARCHAR(100) NOT NULL,

                           postal_code VARCHAR(20) NOT NULL,

                           latitude DECIMAL(10,8),

                           longitude DECIMAL(11,8),

                           is_default BOOLEAN NOT NULL DEFAULT FALSE,

                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP,

                           PRIMARY KEY (address_id),

                           FOREIGN KEY (user_id)
                               REFERENCES users(user_id)

);