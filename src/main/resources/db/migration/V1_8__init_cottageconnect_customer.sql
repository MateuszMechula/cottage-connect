CREATE TABLE _CUSTOMER
(
    CUSTOMER_ID SERIAL      NOT NULL,
    FIRSTNAME   VARCHAR(64) NOT NULL,
    LASTNAME    VARCHAR(64) NOT NULL,
    PHONE       VARCHAR(32) NOT NULL,
    USER_ID     BIGINT      NOT NULL,
    PRIMARY KEY (CUSTOMER_ID),
    CONSTRAINT FK_CUSTOMER_USER
        FOREIGN KEY (USER_ID)
            REFERENCES _USER (USER_ID)
);