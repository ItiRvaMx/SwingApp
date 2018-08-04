CREATE TABLE sysuser (
    id int,
    name character varying not null,
    login character varying not null,
    password character varying not null,
    passwordSalt character varying not null,
    permissions int not null default 1
);
CREATE SEQUENCE sysuserid_seq
    start with 1
    no maxvalue
    no minvalue;
ALTER SEQUENCE sysuserid_seq OWNED BY sysuser.id;
ALTER TABLE sysuser ALTER COLUMN id SET DEFAULT nextval('sysuserid_seq');
ALTER TABLE sysuser ADD CONSTRAINT sysuserpk PRIMARY KEY(id);
ALTER TABLE sysuser ADD CONSTRAINT sysuniquelogin UNIQUE(login);


CREATE TABLE customer (
    id bigint,
    name character varying not null,
    lastname character varying not null,
    vat character varying not null,
    email character varying not null,
    active boolean not null default true
);
CREATE SEQUENCE customerid_seq
    start with 1
    no maxvalue
    no minvalue;
ALTER SEQUENCE customerid_seq OWNED BY customer.id;
ALTER TABLE customer ALTER COLUMN id SET DEFAULT nextval('customerid_seq');
ALTER TABLE customer ADD CONSTRAINT customerpk PRIMARY KEY(id);
ALTER TABLE customer ADD CONSTRAINT uniquecustomer UNIQUE(vat);


CREATE TABLE customeraddress (
    id int,
    name character varying not null,
    customerId int not null,
    street character varying,
    zip character varying not null,
    active boolean not null default true
);
CREATE SEQUENCE customeraddressid_seq
    start with 1
    no maxvalue
    no minvalue;
ALTER SEQUENCE customeraddressid_seq OWNED BY customeraddress.id;
ALTER TABLE customeraddress ALTER COLUMN id SET DEFAULT nextval('customeraddressid_seq');
ALTER TABLE customeraddress ADD CONSTRAINT customeraddresspk PRIMARY KEY(id);
ALTER TABLE customeraddress ADD CONSTRAINT customerfk FOREIGN KEY(customerId) REFERENCES customer(id);