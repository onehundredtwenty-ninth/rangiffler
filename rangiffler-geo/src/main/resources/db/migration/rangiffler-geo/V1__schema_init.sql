create extension if not exists "uuid-ossp";

create table if not exists "country"
(
    id                      UUID unique        not null default uuid_generate_v1(),
    code                    varchar(50)        not null,
    "name"                  varchar(255)       not null,
    "flag"                  bytea              not null,
    primary key (id)
);