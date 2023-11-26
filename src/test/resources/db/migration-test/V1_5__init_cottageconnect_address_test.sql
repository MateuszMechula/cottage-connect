CREATE TABLE _ADDRESS
(
    ADDRESS_ID  SERIAL      NOT NULL,
    STREET      VARCHAR(64) NOT NULL,
    POSTAL_CODE VARCHAR(32) NOT NULL,
    CITY        VARCHAR(32) NOT NULL,
    VOIVODESHIP VARCHAR(64) NOT NULL,
    COUNTRY     VARCHAR(32) NOT NULL,
    PRIMARY KEY (ADDRESS_ID)
);