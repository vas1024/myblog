-- Таблица с пользователями
create table if not exists posts(
  id bigserial primary key,
  title varchar(256) not null,
  text varchar(256) not null,
  likes integer not null,
  tags varchar(256) not null
  );

insert into posts(title, text, likes, tags) values ('my very firs post', '[INFO] Total time:  5.905 s  [INFO] Finished at: 2025-05-01T17:23:01+03:00', 3, '#bull#shit#');
insert into posts(title, text, likes, tags) values ('i will write here every day', 'благими намерениями устлана дорога в ад', 2, '#bull#');
insert into posts(title, text, likes, tags) values ('its so hard to write here', 'да пошло оно всё лесом, надоело', 100, '');
