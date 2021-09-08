drop table if exists countries cascade;
create table countries (
        country_code char(2) primary key,
        country_name text unique
    );
	
insert into countries (country_code, country_name) values
	('us', 'United States'),
	('mx', 'Mexico'),
	('au', 'Australia'),
	('gb', 'United Kingdom'),
	('de', 'Germany'),
	('ll', 'Loompland');
	
INSERT INTO countries VALUES
	--('uk', 'United Kingdom'),
	('it', 'Italy');

SELECT * FROM countries;

drop table if exists cities cascade;
create table cities (
    name text not null,
    postal_code varchar(9) check (postal_code <> ''),
    country_code char(2) references countries,
    primary key (country_code, postal_code)
);

insert into cities values ('Toronto', 'M4C1B5', 'ca');

insert into cities values ('Portland', '87200', 'us');

SELECT * FROM cities;

UPDATE cities SET postal_code = '97206' WHERE name = 'Portland' and postal_code = '87200';

SELECT * FROM cities;

SELECT cities.name, cities.postal_code, countries.country_name
FROM cities
INNER JOIN countries ON countries.country_code = cities.country_code;