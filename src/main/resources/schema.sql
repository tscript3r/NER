CREATE TABLE IF NOT EXISTS clients
(
    id
    INT
    NOT
    NULL
    PRIMARY
    KEY,
    externalId
    INT,
    company
    VARCHAR
(
    64
),
    contactName VARCHAR
(
    64
),
    street VARCHAR
(
    64
),
    city VARCHAR
(
    64
),
    state VARCHAR
(
    64
),
    postcode VARCHAR
(
    16
),
    country VARCHAR
(
    32
)
    );

CREATE TABLE IF NOT EXISTS items
(
    id
    INT
    NOT
    NULL
    PRIMARY
    KEY,
    externalId
    INT,
    name
    VARCHAR
(
    64
),
    price INT,
    description VARCHAR
(
    256
)
    );