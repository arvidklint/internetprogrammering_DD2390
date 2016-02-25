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

insert into residence values ('Stockholm','Hyresrätt','Observatoriegatan 3',165,5,0,12483);

insert into residence values ('Stockholm','Bostadsrätt','Norrbackagatan 44',28,1,2800000,1200);

insert into residence values ('Kiruna','Villa','Gruvgatan 4',300,8,300000,200);

insert into residence values ('Kiruna','Villa','Schaktsvängen 23',49,3,500000,1500);

insert into residence values ('Kiruna','Bostadsrätt','Dåndimpsfjället 89',10,1,20000,0);

insert into residence values ('Malmö','Bostadsrätt','Rosengårdsgatan 157',28,1,2800000,1200);

insert into residence values ('Malmö','Hyresrätt','Norrbackagatan 33',28,1,2800000,1200);

insert into residence values ('Malmö','Radhus','Radhuskröken 4',28,3,7000000,12300);

insert into residence values ('Malmö','Radhus','Radhuskröken 44',28,1,2800000,1200);

insert into residence values ('Norrköping','Bostadsrätt','Kulvägen 8',47,2,3200000,1400);

insert into residence values ('Norrköping','Bostadsrätt','Första Gatan 1',630,6,9500000,1200);

insert into residence values ('Norrköping','Villa','Villavillekullavägen 1000',28,1,666,1200);

insert into residence values ('Norrköping','Bostadsrätt','bogatan 4',28,4,720000,1337);


SELECT * FROM residence