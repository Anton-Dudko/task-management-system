create table users
(
    id         bigint not null
        primary key,
    email      varchar(255)
        unique,
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    role       varchar(255)
        constraint users_role_check
            check ((role)::text = ANY ((ARRAY ['USER'::character varying, 'ADMIN'::character varying])::text[]))
    );

alter table users
    owner to "user";

create table tasks
(
    author_id   bigint
        constraint fkhods8r8oyyx7tuj3c91ki2sk1
            references users,
    id          bigint not null
        primary key,
    description varchar(255),
    priority    varchar(255)
        constraint tasks_priority_check
            check ((priority)::text = ANY
        ((ARRAY ['HIGH'::character varying, 'MEDIUM'::character varying, 'LOW'::character varying])::text[])),
    status      varchar(255)
        constraint tasks_status_check
            check ((status)::text = ANY
                   ((ARRAY ['IN_PROGRESS'::character varying, 'WAITING'::character varying, 'COMPLETED'::character varying])::text[])),
    title       varchar(255)
);

alter table tasks
    owner to "user";

create table performer_task
(
    performer_id bigint not null
        constraint fk7unckxb77lf5yi9bo787q5goy
            references users,
    task_id      bigint not null
        constraint fk2f6qbck71ywjwj8ie3pi4orgy
            references tasks,
    primary key (performer_id, task_id)
);

alter table performer_task
    owner to "user";

create table comments
(
    id      bigint not null
        primary key,
    task_id bigint
        constraint fki7pp0331nbiwd2844kg78kfwb
            references tasks,
    user_id bigint
        constraint fk8omq0tc18jd43bu5tjh6jvraq
            references users,
    comment varchar(255)
);

alter table comments
    owner to "user";