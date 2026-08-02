------ ROLES
use ecom_users;
INSERT INTO roles (
    role_id,
    role_name,
    description
)
VALUES
    (
        '11111111-1111-1111-1111-111111111111',
        'ADMIN',
        'System Administrator'
    ),
    (
        '22222222-2222-2222-2222-222222222222',
        'CUSTOMER',
        'Customer of the application'
    ),
    (
        '33333333-3333-3333-3333-333333333333',
        'SELLER',
        'Product Seller'
    ),
    (
        '44444444-4444-4444-4444-444444444444',
        'SUPPORT',
        'Customer Support Executive'
    );

------ USERS

INSERT INTO users (
    user_id,
    first_name,
    last_name,
    email,
    phone_number,
    password_hash,
    gender,
    date_of_birth,
    email_verified,
    phone_verified,
    status
)
VALUES

    (
        'aaaaaaaa-1111-1111-1111-111111111111',
        'John',
        'Doe',
        'john@example.com',
        '9876543210',
        '$2a$10$abcdefghijklmnopqrstuv',
        'MALE',
        '1995-05-20',
        TRUE,
        TRUE,
        'ACTIVE'
    ),

    (
        'bbbbbbbb-2222-2222-2222-222222222222',
        'Jane',
        'Smith',
        'jane@example.com',
        '9876543211',
        '$2a$10$abcdefghijklmnopqrstuv',
        'FEMALE',
        '1997-03-12',
        TRUE,
        FALSE,
        'ACTIVE'
    ),

    (
        'cccccccc-3333-3333-3333-333333333333',
        'Robert',
        'Johnson',
        'robert@example.com',
        '9876543212',
        '$2a$10$abcdefghijklmnopqrstuv',
        'MALE',
        '1992-11-08',
        FALSE,
        FALSE,
        'LOCKED'
    ),

    (
        'dddddddd-4444-4444-4444-444444444444',
        'Emily',
        'Davis',
        'emily@example.com',
        '9876543213',
        '$2a$10$abcdefghijklmnopqrstuv',
        'FEMALE',
        '1999-01-15',
        TRUE,
        TRUE,
        'SUSPENDED'
    ),

    (
        'eeeeeeee-5555-5555-5555-555555555555',
        'Michael',
        'Brown',
        'michael@example.com',
        NULL,
        '$2a$10$abcdefghijklmnopqrstuv',
        'MALE',
        '1990-08-10',
        TRUE,
        FALSE,
        'ACTIVE'
    );

------ USER ROLES

INSERT INTO user_roles (
    user_role_id,
    user_id,
    role_id
)
VALUES

    (
        '90000000-0000-0000-0000-000000000001',
        'aaaaaaaa-1111-1111-1111-111111111111',
        '22222222-2222-2222-2222-222222222222'
    ),

    (
        '90000000-0000-0000-0000-000000000002',
        'bbbbbbbb-2222-2222-2222-222222222222',
        '22222222-2222-2222-2222-222222222222'
    ),

    (
        '90000000-0000-0000-0000-000000000003',
        'cccccccc-3333-3333-3333-333333333333',
        '33333333-3333-3333-3333-333333333333'
    ),

    (
        '90000000-0000-0000-0000-000000000004',
        'dddddddd-4444-4444-4444-444444444444',
        '44444444-4444-4444-4444-444444444444'
    ),

    (
        '90000000-0000-0000-0000-000000000005',
        'eeeeeeee-5555-5555-5555-555555555555',
        '11111111-1111-1111-1111-111111111111'
    ),

    (
        '90000000-0000-0000-0000-000000000006',
        'eeeeeeee-5555-5555-5555-555555555555',
        '33333333-3333-3333-3333-333333333333'
    );

------ ADDRESSES

INSERT INTO addresses (
    address_id,
    user_id,
    address_type,
    recipient_name,
    phone_number,
    address_line_1,
    address_line_2,
    landmark,
    city,
    state,
    country,
    postal_code,
    latitude,
    longitude,
    is_default
)
VALUES

    (
        'a1000000-0000-0000-0000-000000000001',
        'aaaaaaaa-1111-1111-1111-111111111111',
        'HOME',
        'John Doe',
        '9876543210',
        '12 MG Road',
        'Apartment 302',
        'Near Metro Station',
        'Bengaluru',
        'Karnataka',
        'India',
        '560001',
        12.971599,
        77.594566,
        TRUE
    ),

    (
        'a1000000-0000-0000-0000-000000000002',
        'aaaaaaaa-1111-1111-1111-111111111111',
        'WORK',
        'John Doe',
        '9876543210',
        'Tech Park',
        'Building B',
        'Whitefield',
        'Bengaluru',
        'Karnataka',
        'India',
        '560066',
        12.969800,
        77.750000,
        FALSE
    ),

    (
        'a1000000-0000-0000-0000-000000000003',
        'bbbbbbbb-2222-2222-2222-222222222222',
        'HOME',
        'Jane Smith',
        '9876543211',
        '45 Residency Road',
        NULL,
        'Near Mall',
        'Hyderabad',
        'Telangana',
        'India',
        '500001',
        17.385044,
        78.486671,
        TRUE
    ),

    (
        'a1000000-0000-0000-0000-000000000004',
        'cccccccc-3333-3333-3333-333333333333',
        'HOME',
        'Robert Johnson',
        '9876543212',
        '22 Beach Road',
        NULL,
        'Near Harbour',
        'Chennai',
        'Tamil Nadu',
        'India',
        '600001',
        13.082680,
        80.270718,
        TRUE
    ),

    (
        'a1000000-0000-0000-0000-000000000005',
        'eeeeeeee-5555-5555-5555-555555555555',
        'OTHER',
        'Michael Brown',
        NULL,
        '77 Business Avenue',
        'Suite 901',
        'City Center',
        'Mumbai',
        'Maharashtra',
        'India',
        '400001',
        19.076090,
        72.877426,
        TRUE
    );

