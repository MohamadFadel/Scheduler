create table "wm_task_type"
(
    "id"           int generated always as identity,
    "type"         varchar(200),
    "api_endpoint" varchar(200),
    primary key ("id")
);

create table "wm_task"
(
    "id"                 int generated always as identity,
    "name"               varchar(200) unique,
    "endpoint_id"        varchar(200),
    "description"        varchar(250),
    "created_on"         timestamp(6),
    "created_by"         varchar(200),
    "last_updated_on"    timestamp(6),
    "last_updated_by"    varchar(200),
    "next_scheduled_run" timestamp(6),
    "state"              varchar(16),
    "configuration"      bytea,
    "emails"             varchar(4000),
    "start_date"         timestamp(6),
    "cron_expression"    varchar(120),
    "task_type_id"       int,
    primary key ("id"),
    constraint "fk_wm_task.task_type_id"
        foreign key ("task_type_id")
            references "wm_task_type" ("id")
);

commit;