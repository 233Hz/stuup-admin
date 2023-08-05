-- 增加发布状态字段
alter table t_announcement
    add column state int(1) not null default 1 comment '状态（1.未发布 2.发布）';
-- 修改注释
alter table t_announcement
    modify column type int(1) not null comment '类型（1.系统通知 2.通知公告）';
-- 设置非必填
alter table t_announcement
    modify column scope int(1) null;
alter table t_announcement
    modify column create_user bigint(20) null;

-- 新增 t_semester 表

-- 添加字段
alter table t_rec_score
    add column semester_id bigint(20) not null comment '获取所属学期';

insert into t_config(config_key, config_value, config_note)
values ('last_semester_start_time', '09-01', '上学期开始时间'),
       ('last_semester_end_time', '01-15', '上学期结束时间'),
       ('next_semester_start_time', '02-15', '下学期开始时间'),
       ('next_semester_end_time', '06-30', '下学期结束时间');

insert into t_config(config_key, config_value, config_note)
values ('progress_notify_ranking', '10', '进步提醒名次'),
       ('retrogress_notify_ranking', '10', '退步提醒名次');

-- 新增 t_year_info

insert into t_config(config_key, config_value, config_note)
values ('national_level_competition_award_growth_code',
        'CZ_028,CZ_037,CZ_041,CZ_055',
        '国家级竞赛获奖成长项code'),
       ('city_level_competition_award_growth_code',
        'CZ_029,CZ_038,CZ_042,CZ_056',
        '国家级竞赛获奖成长项code'),
       ('district_level_competition_award_growth_code',
        'CZ_030,CZ_039,CZ_043,CZ_057',
        '国家级竞赛获奖成长项code'),
       ('school_level_competition_award_growth_code',
        'CZ_031,CZ_040,CZ_051,CZ_058',
        '国家级竞赛获奖成长项code'),
       ('scholarship_growth_code',
        'CZ_028,CZ_029,CZ_030,CZ_031',
        '奖学金项目code'),
       ('hold_an_activity_growth_code',
        'CZ_001,CZ_002,CZ_003,CZ_004,CZ_005,CZ_006,CZ_007,CZ_008,CZ_047,CZ_054,CZ_059',
        '举办活动项目code');

alter table t_rec_score
    drop column update_time;

