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
