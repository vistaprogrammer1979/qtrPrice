CREATE SCHEMA IF NOT EXISTS price;
create table if not exists price.account
(
    user_id  serial,
    username varchar(70),
    password varchar(70),
    role     varchar(50)
    );

alter table price.account
    owner to "SA";

insert into price.account (user_id, username, password, role) values (1, 'admin', 'ccf5538dc31d435d6bab145c924041d8', 'SuperAdmin');

create table if not exists price.price_request
(
    created_at          timestamp(6),
    finished_at         timestamp(6),
    process_time        bigint,
    recall              bigint,
    price_request_id    uuid                                     not null
    constraint price_request_pkey
    primary key,
    claim_id            varchar(255),
    error_message       varchar(255),
    facility            varchar(255),
    request_type        varchar(255)
    constraint price_request_request_type_check
    check ((request_type)::text = ANY
(ARRAY [('DHA'::character varying)::text, ('CHI'::character varying)::text, ('DOH'::character varying)::text, ('MDS'::character varying)::text, ('QTR'::character varying)::text, ('KSA'::character varying)::text])),
    status              varchar(255)
    constraint price_request_status_check
    check ((status)::text = ANY
(ARRAY [('IN_PROGRESS'::character varying)::text, ('FINISHED'::character varying)::text, ('FAILED'::character varying)::text])),
    trace_id            varchar(255),
    patient_share       double precision,
    multiple_procedures integer,
    primary_proc        double precision,
    secondary_proc      double precision,
    third_proc          double precision,
    forth_proc          double precision,
    cus_dental_id       bigint,
    spc_id              bigint,
    cus_id              bigint,
    net                 double precision,
    gross               double precision,
    member_id           varchar(255),
    market              varchar default 'UAE'::character varying not null,
    sub_billing_group   varchar,
    visit_id            varchar
    );

alter table price.price_request
    owner to "SA";

create table if not exists price.price_logs
(
    timestamp         timestamp(6),
    preice_request_id uuid not null
    constraint price_logs_pkey
    primary key
    constraint fk75l143m1jjba81gsyus4gqxyr
    references price.price_request,
    engine_hostname   varchar(255),
    error_hostname    varchar(255),
    error_message     text,
    exception         text,
    fact_hostname     varchar(255)
    );

alter table price.price_logs
    owner to "SA";

create table if not exists price.price_activity
(
    price_activity_id      uuid not null
    constraint price_activity_pkey
    primary key,
    price_request_id       uuid
    constraint fkeedoabmhnpmpo1itf8vrra7at
    references price.price_request,
    activity_id            varchar(255),
    clinician              varchar(255),
    code                   varchar(255),
    net                    varchar(255),
    ordering_clinician     varchar(255),
    prior_authorization_id varchar(255),
    quantity               varchar(255),
    start                  varchar(255),
    type                   varchar(255),
    copayment              double precision,
    provider_patient_share double precision,
    deductible             double precision,
    discount_percentage    double precision,
    discount               double precision,
    spc_factor             double precision,
    gross                  double precision,
    list                   double precision,
    list_price_predefined  integer,
    anaesthesia_base_units double precision
    );

alter table price.price_activity
    owner to "SA";

create table if not exists price.price_alert
(
    created_at       timestamp(6),
    id               uuid not null
    constraint price_alert_pkey
    primary key,
    price_request_id uuid
    constraint fkkchlimt1r3cibf9w9f11j256j
    references price.price_request,
    short_message    varchar(500),
    long_message     text,
    object_id        varchar(255),
    object_type      varchar(255),
    rule_id          varchar(255),
    severity         varchar(255),
    price_object_id  uuid
    );

alter table price.price_alert
    owner to "SA";

create table if not exists price.price_body
(
    price_request_id uuid not null
    constraint price_body_pkey
    primary key
    constraint fkobx0d9nw5l2j5j22l4b4f7b8m
    references price.price_request,
    facts            text,
    request          text,
    response         text
);

alter table price.price_body
    owner to "SA";


