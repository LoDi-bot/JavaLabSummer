-- создание таблицы с пользователями
create table account
(
    id         serial primary key,
    first_name char(20)                                not null,
    last_name  char(20)                                not null,
    age        integer check (age >= 0 and age <= 120) not null default 1,
);

-- внесение данных в таблицу с пользователями
insert into account(first_name, last_name, age)
values ('Марсель', 'Сидиков', 27);
insert into account(first_name, last_name, age)
values ('Даниил', 'Вдовинов', 21);
insert into account(first_name, last_name, age)
values ('Виктор', 'Евлампьев', 24);
insert into account(first_name, last_name, age)
values ('Максим', 'Поздеев', 22);
insert into account(id, first_name, last_name, age)
values (5, 'Азат', 'Набиев', 20);
alter sequence account_id_seq restart with 6;
insert into account(first_name, last_name, age)
values ('Айрат', 'Мухутдинов', 22);

-- добавление столбца в таблицу
alter table account
    add email char(30) unique;

-- изменение определения колонки в таблице
alter table account
    alter column first_name set default 'FIRST_NAME';

insert into account(last_name)
values ('Иванов');

-- изменение данных в колонке
update account
set age = 28
where id = 2;

-- удаление данных из колонки
delete
from account
where id = 4;

-- создание таблиц с машинами
create table car
(
    id       serial primary key,
    color    char(20),
    model    char(20),
    owner_id integer,
    foreign key (owner_id) references account (id)
);

alter table car
    add number char(30);

insert into car(color, model, number, owner_id)
values ('Черный', 'BMV 3', 'о777аа16', 1);
insert into car(color, model, number, owner_id)
values ('Красный', 'Lada Largus', 'о111аа16', 1);
insert into car(color, model, number, owner_id)
values ('Серый', 'Renault', 'о222аа16', 2);
insert into car(color, model, number, owner_id)
values ('Голубой', 'Bugatti', 'a001aa01', 3);
insert into car(color, model, number, owner_id)
values ('Синяя', 'LADA', 'o111aa116', 2);
insert into car(color, model, number)
values ('Белая', 'Solaris', 'у898aa116');

-- создание таблицы многие-ко-многим
create table car_driver
(
    car_id    integer,
    driver_id integer,
    foreign key (car_id) references car (id),
    foreign key (driver_id) references account (id)
);

insert into car_driver(driver_id, car_id)
values (2, 2);
insert into car_driver(driver_id, car_id)
values (1, 3);
insert into car_driver(driver_id, car_id)
values (5, 4);
insert into car_driver(driver_id, car_id)
values (4, 6);
insert into car_driver(driver_id, car_id)
values (1, 6);
insert into car_driver(driver_id, car_id)
values (5, 6);
insert into car_driver(driver_id, car_id)
values (4, 5);

-- получение всех колонок всех пользователей
select *
from account;

-- получение всех имен пользователей у которых email null
select first_name
from account
where email isnull;

-- получение всех пользователей, которые старше 23-х лет
select first_name
from account
where age > 23;

-- получение всех имен пользователей, старше 23-х лет, но при этом колонка должна называться просто name
select first_name as name
from account
where age > 23;

-- получение имен всех владельцев, у которых есть хотя бы одна машина
select first_name
from account a
where a.id in (select distinct owner_id -- получить уникальные id всех владельцев из таблицы car
               from car
               where owner_id notnull
    );

--- получение имен владельцев машин, у которых более 1-го водителя
select first_name as name
from account a
where a.id in (
    select owner_id -- получить id всех владельцев у машин которых более 1-го водителя
    from car c
    where c.id in (
        select car_id -- вернет id машин у которых более 1-го водителя
        from (
                 select car_id,
                        count(driver_id) as drivers_count -- возвращает id машин и количество их водителей
                 from car_driver
                 group by car_id) cd
        where cd.drivers_count > 1));

-- left joint
select *
from account a
         left join car c on a.id = c.owner_id;

-- right join
select *
from account a
         right join car c on a.id = c.owner_id;

-- inner join
select *
from account a
         inner join car c on a.id = c.owner_id;

-- full join
select *
from account a
         full join car c on a.id = c.owner_id;

-- удаление таблицы
drop table account;
