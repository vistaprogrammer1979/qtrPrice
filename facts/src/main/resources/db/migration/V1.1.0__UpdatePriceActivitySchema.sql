
Alter table  price.price_activity
    Add Column  If not exists patient_share double precision,
    Add Column  If not exists  hike double precision,
    Add Column  If not exists  hike_amount double precision;



