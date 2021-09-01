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