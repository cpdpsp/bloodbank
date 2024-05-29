
create table IF NOT EXISTS donor (donor_id varchar(255) not null,
blood_group enum('O+','O-','A+','B+','A-','B-','AB-','AB+') not null,
city varchar(255),
email varchar(255) not null,
first_name varchar(255) not null,
last_name varchar(255),
registration_date datetime(6) not null,
primary key (donor_id));

create table IF NOT EXISTS donation (donation_id varchar(255) not null,
donated_on datetime(6) not null,
units_donated float(23) not null,
donor_id varchar(255),
reusable boolean,
primary key (donation_id),
FOREIGN KEY (donor_id) references donor(donor_id)
on delete cascade);

