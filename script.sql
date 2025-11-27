-- jwt > script.sql

create table member (
    username varchar2(50) primary key,  --아이디
    password varchar2(100) not null,    --암호
    role varchar2(50) not null
);

select * from member;

delete from member;

commit;