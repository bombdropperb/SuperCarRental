drop table return;
drop table rentals;
drop table reservation;
drop table customer;
drop table vehicles;
drop table vehicleType;

create table vehicleType (
	vtname char(20) not null PRIMARY KEY,
	wrate integer not null,
	drate integer not null,
	hrate integer not null,
	wirate integer not null,
	dirate integer not null,
	hirate integer not null,
	krate integer not null
);

create table vehicles (
	vid integer not null,
	vlicense char(20) not null PRIMARY KEY,
	odometer char(20) not null,
	status char(20) not null,
	vtname char(20) not null references vehicleType,
	location char(20) not null
);

create table customer (
	cellphone integer not null,
	name varchar(20) not null,
	address varchar(20) not null,
	dlicense char(20) not null PRIMARY KEY
);

create table reservation (
	confNo char(20) not null PRIMARY KEY,
	vtname char(20) not null references vehicleType,
	dlicense char(20)not null references customer,
	fromDate DATE not null,
	fromTime integer CHECK (fromTime >= 0 AND fromTime <= 2400),
	toDate DATE not null,
	toTime integer CHECK (toTime >= 0 AND toTime <= 2400)
);

create table rentals (
	rid integer not null PRIMARY KEY,
	vlicense char(20) not null references vehicles,
	dlicense char(20) not null references customer,
	fromDate DATE not null,
	fromTime integer CHECK (fromTime >= 0 AND fromTime <= 2400),
	toDate DATE not null,
	toTime integer CHECK (toTime >= 0 AND toTime <= 2400),
	odometer integer not null,
	confNo char(20) references reservation
);

create table return (
	rid integer not null references rentals PRIMARY KEY,
	dateR DATE not null,
	timeR integer CHECK (timeR >= 0 AND timeR<= 2400),
	odometer char(20) not null,
	fulltank char(20) not null,
	valueR integer not null
);

