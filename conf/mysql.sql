create database ecm_stat;

use ecm_stat;

-- DDL
create table login_log (
  user_name varchar(20),
  user_real_name varchar(50), 
  year int,
  month int,
  day int,
  hour tinyint,
  login_date datetime   
);

-- month report
select count(*) as login_count, year, month
from login_log
where '2011-01-01 00:00:00' <= login_date and login_date < '2012-10-01 00:00:00'
group by year, month
order by year, month ASC;

-- day day
select count(*) as login_count, year, month, day
from login_log
group by year, month, day
order by year, month, day ASC;

-- Reload sample data
delete from login_log;

insert into login_log values('a', 'real_a', 2011, 9, 1, 2, '2011-09-01 02:08:01');
insert into login_log values('a', 'real_a', 2011, 9, 1, 2, '2011-09-01 02:08:02');
insert into login_log values('a', 'real_a', 2011, 9, 1, 3, '2011-09-01 03:08:40');
insert into login_log values('a', 'real_a', 2011, 9, 1, 3, '2011-09-01 03:08:20');
insert into login_log values('a', 'real_a', 2011, 9, 2, 2, '2011-09-02 02:09:02');

insert into login_log values('a', 'real_a', 2011, 10, 1, 2, '2011-10-01 02:08:01');
insert into login_log values('a', 'real_a', 2011, 10, 1, 2, '2011-10-01 02:08:02');
insert into login_log values('a', 'real_a', 2011, 10, 1, 3, '2011-10-01 03:08:40');
insert into login_log values('a', 'real_a', 2011, 10, 1, 3, '2011-10-01 03:08:20');
insert into login_log values('a', 'real_a', 2011, 10, 2, 2, '2011-10-02 02:10:02');
insert into login_log values('a', 'real_a', 2011, 10, 2, 2, '2011-10-02 02:10:07');

insert into login_log values('a', 'real_a', 2011, 11, 1, 2, '2011-11-01 02:08:01');
insert into login_log values('a', 'real_a', 2011, 11, 1, 2, '2011-11-01 02:08:02');
insert into login_log values('a', 'real_a', 2011, 11, 1, 3, '2011-11-01 03:08:40');
insert into login_log values('a', 'real_a', 2011, 11, 1, 3, '2011-11-01 03:08:20');
insert into login_log values('a', 'real_a', 2011, 11, 2, 2, '2011-11-02 02:11:02');
insert into login_log values('a', 'real_a', 2011, 11, 2, 2, '2011-11-02 02:11:07');

insert into login_log values('a', 'real_a', 2012, 9, 1, 2, '2012-09-01 02:08:01');
insert into login_log values('a', 'real_a', 2012, 9, 1, 2, '2012-09-01 02:08:02');
insert into login_log values('a', 'real_a', 2012, 9, 1, 3, '2012-09-01 03:08:40');
insert into login_log values('a', 'real_a', 2012, 9, 1, 3, '2012-09-01 03:08:20');
insert into login_log values('a', 'real_a', 2012, 9, 2, 2, '2012-09-02 02:09:02');
insert into login_log values('a', 'real_a', 2012, 9, 2, 2, '2012-09-02 02:09:07');

insert into login_log values('a', 'real_a', 2012, 10, 1, 2, '2012-10-01 02:08:01');
insert into login_log values('a', 'real_a', 2012, 10, 1, 2, '2012-10-01 02:08:02');
insert into login_log values('a', 'real_a', 2012, 10, 1, 3, '2012-10-01 03:08:40');
insert into login_log values('a', 'real_a', 2012, 10, 1, 3, '2012-10-01 03:08:20');
insert into login_log values('a', 'real_a', 2012, 10, 2, 2, '2012-10-02 02:10:02');
insert into login_log values('a', 'real_a', 2012, 10, 2, 2, '2012-10-02 02:10:07');

insert into login_log values('a', 'real_a', 2012, 11, 1, 2, '2012-11-01 02:08:01');
insert into login_log values('a', 'real_a', 2012, 11, 1, 2, '2012-11-01 02:08:02');
insert into login_log values('a', 'real_a', 2012, 11, 1, 3, '2012-11-01 03:08:40');
insert into login_log values('a', 'real_a', 2012, 11, 1, 3, '2012-11-01 03:08:20');
insert into login_log values('a', 'real_a', 2012, 11, 2, 2, '2012-11-02 02:11:02');
insert into login_log values('a', 'real_a', 2012, 11, 2, 2, '2012-11-02 02:11:07');

