create extension if not exists "uuid-ossp";

create table if not exists "photo"
(
    id                      UUID unique not null default uuid_generate_v1(),
    user_id                 UUID        not null,
    country_id              UUID        not null,
    "description"           varchar(255),
    photo                   bytea,
    created_date            timestamp   not null,
    primary key (id)
);

create table if not exists "like"
(
    id                      UUID unique not null default uuid_generate_v1(),
    user_id                 UUID        not null,
    created_date            timestamp   not null,
    primary key (id)
);

create table if not exists "photo_like"
(
    photo_id                 UUID        not null,
    like_id                  UUID        not null,
    primary key (photo_id, like_id),
    constraint ph_like_photo_id foreign key (photo_id) references "photo" (id),
    constraint lk_like_photo_id foreign key (like_id) references "like" (id)
);

create table if not exists "statistic"
(
    id                      UUID unique not null default uuid_generate_v1(),
    user_id                 UUID        not null,
    country_id              UUID        not null,
    count                   int         not null,
    primary key (id)
);

alter table "photo"
    owner to postgres;

alter table "like"
    owner to postgres;

alter table "photo_like"
    owner to postgres;

alter table "statistic"
    owner to postgres;