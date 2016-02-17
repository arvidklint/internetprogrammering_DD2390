use emiwes; # Byt till eget användarnamn

drop table residence; # Om det finns en tidigare databas

create table residence (
county varchar(64),
objectType varchar(64),
address varchar(64),
area float,
room int,
price float,
rent float
);


insert into residence values ('Stockholm','Bostadsrätt','Polhemsgatan 1',30,1,1000000,1234);

insert into residence values ('Stockholm','Bostadsrätt','Polhemsgatan 2',60,2,2000000,2345);

insert into residence values ('Stockholm','Villa','Storgatan 1',130,5,1000000,3456);

insert into residence values ('Stockholm','Villa','Storgatan 2',160,6,1000000,3456);

insert into residence values ('Uppsala','Bostadsrätt','Gröna gatan 1',30,1,500000,1234);

insert into residence values ('Uppsala','Bostadsrätt','Gröna gatan 2',60,2,1000000,2345);

insert into residence values ('Uppsala','Villa','Kungsängsvägen 1',130,5,1000000,3456);

insert into residence values ('Uppsala','Villa','Kungsängsvägen 2',160,6,1000000,3456);


SELECT * FROM residence