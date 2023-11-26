CREATE TABLE _RESERVATION
(
    RESERVATION_ID SERIAL                   NOT NULL,
    DAY_IN         TIMESTAMP WITH TIME ZONE NOT NULL,
    DAY_OUT        TIMESTAMP WITH TIME ZONE NOT NULL,
    STATUS         BOOLEAN                  NOT NULL,
    CUSTOMER_ID    BIGINT                   NOT NULL,
    COTTAGE_ID     BIGINT                   NOT NULL,
    PRIMARY KEY (RESERVATION_ID),
    CONSTRAINT FK_RESERVATION_CUSTOMER
        FOREIGN KEY (CUSTOMER_ID)
            REFERENCES _CUSTOMER (CUSTOMER_ID),
    CONSTRAINT FK_RESERVATION_COTTAGE
        FOREIGN KEY (COTTAGE_ID)
            REFERENCES _COTTAGE (COTTAGE_ID)
);