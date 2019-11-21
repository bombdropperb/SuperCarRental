create table vehicle (
	vid integer not null,
	vlicense varchar(7) not null PRIMARY KEY,
	status varchar(12) not null,
	vtname char not null references vehicleType,
	location not null
);

create table vehicleType (
	vtname varchar(12) not null PRIMARY KEY,
	wrate integer not null,
	drate integer not null,
	hrate integer not null,
	wirate integer not null,
	dirate integer not null,
	hirate integer not null,
	krate integer not null
);

create table customer (
	cellphone integer not null,
	name char not null,
	address char not null,
	dlicense char not null PRIMARY KEY
);

create table return (
	rid integer not null references rentals PRIMARY KEY,
	date char not null,
	time char not null,
	odometer char not null,
	fulltank char not null,
	value integer not null
);

create table reservation (
	confNo integer not null PRIMARY KEY,
	vtname char not null references vehicleType,
	dlicense char not null references customer,
	fromDate char not null,
	fromTime char not null,
	toDate char not null,
	toTime char not null
);

create table rentals (
	rid integer not null PRIMARY KEY,
	vid integer not null references vehicles,
	dlicense char not null references customer,
	fromDate char not null,
	fromTime char not null,
	toDate char not null,
	toTime char not null,
	odometer integer not null,
	confNo integer not null references reservation
);

