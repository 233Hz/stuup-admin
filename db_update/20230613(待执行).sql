ALTER TABLE t_growth_item
    MODIFY COLUMN score DECIMAL(5, 2);
ALTER TABLE t_growth_item
    MODIFY COLUMN score_upper_limit DECIMAL(5, 2);
ALTER TABLE t_stu_score
    MODIFY COLUMN score DECIMAL(5, 2);
ALTER TABLE t_rec_score
    MODIFY COLUMN score DECIMAL(5, 2);
ALTER TABLE t_ranking_year
    MODIFY COLUMN score DECIMAL(5, 2);
ALTER TABLE t_ranking_month
    MODIFY COLUMN score DECIMAL(5, 2);

-- 删除用户
delete
from t_user
where user_name = '杨慧萍';

-- 插入学生用户
insert into t_user(user_name, sex, mobile, user_type, id_card, login_name, password, state)
select `name`,
       sex,
       phone,
       1                      as user_type,
       id_card,
       student_no,
       MD5(RIGHT(id_card, 6)) as `password`,
       1                      as state
from t_student;

update t_menu
set name = '综评记录'
where name = '成长填报记录';
update t_menu
set name = '成长积分查询',
    pid  = 0
where name = '成长积分记录';

alter table t_rec_military
    modify grow_id bigint(20) null;

-- 成长项目添加字段
alter table t_growth_item
    add column gatherer int(1) not null comment '采集者类型（1.指定负责人 2.学生）';

-- 设置默认值
alter table t_aud_grow
    alter column state set default 0;

-- 成长项目审核添加字段
alter table t_aud_grow
    add column applicant bigint(20) not null comment '申请人';

alter table t_aud_grow
    modify column file_ids varchar(64) null;

alter table t_aud_grow
    alter column state set default 1;


-- 成长项目日志添加字段
alter table t_aud_log
    add column aud_id bigint(20) not null comment '审核记录id';
alter table t_aud_log
    add column grow_id bigint(20) not null comment '成长项目id';
alter table t_aud_log
    add column reason varchar(100) null comment '原因';

-- 测试学生账号
-- 2210001
-- 773034209a0ef80339697319760581c1d19b64f787323319
-- 测试老师账号
-- 6018
-- d9f99082fd85b3511911b486d8794189f660504c5cb0bb85


-- 新增加t_file表


