-- SYS 계정이 XEPDB1 플러그인 데이터베이스로 전환하도록 함
ALTER SESSION SET CONTAINER=XEPDB1;

-- springboot 유저 생성
CREATE USER springboot IDENTIFIED BY java1234;

-- ORA-01045 오류 해결: 'CREATE SESSION' 권한을 *명시적*으로 부여
GRANT CREATE SESSION TO springboot;

-- 테이블 생성, 시퀀스 생성, 리소스 사용 권한 부여
GRANT RESOURCE TO springboot;
GRANT CREATE TABLE TO springboot;
GRANT CREATE VIEW TO springboot;
GRANT CREATE SEQUENCE TO springboot;

-- 테이블스페이스 용량 할당
ALTER USER springboot DEFAULT TABLESPACE USERS QUOTA UNLIMITED ON USERS;

-- springboot 유저 소유로 테이블 생성
create table springboot.tblTeam (
    seq number primary key,
    name varchar2(100) not null unique,
    description varchar2(300) not null
);

create sequence springboot.seqTeam;

insert into springboot.tblTeam values (springboot.seqTeam.nextVal, '개발', '시스템 개발을 담당합니다.');
insert into springboot.tblTeam values (springboot.seqTeam.nextVal, 'DB', '데이터베이스를 담당합니다.');
insert into springboot.tblTeam values (springboot.seqTeam.nextVal, '운영', '구축된 시스템의 유지보수를 담당합니다.');
insert into springboot.tblTeam values (springboot.seqTeam.nextVal, '영업', '진행중인 프로젝트를 관리합니다.');
insert into springboot.tblTeam values (springboot.seqTeam.nextVal, '보안', '시스템의 보안을 담당합니다.');

commit;

