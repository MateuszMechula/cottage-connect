create table _user
(
    user_id  serial not null,
    email    varchar(255) unique,
    password varchar(255),
    primary key (user_id)
);