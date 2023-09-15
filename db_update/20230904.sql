alter table t_growth_item
    modify column first_level_id int null;

alter table t_year
    add year int(4) not null comment '年份';
alter table t_semester
    add year int(4) not null comment '年份';

alter table t_aud_grow
    add column type      int(1)     not null comment '类型（1.本人申请 2.他人导入）',
    add column submitter bigint(20) not null comment '提交人',
    add year_id          bigint(20) not null comment '年份id',
    add semester_id      bigint(20) not null comment '学期id';

alter table t_aud_grow
    add index idx_aud_grow_id (year_id),
    add index idx_aud_grow_semId (semester_id),
    add index idx_aud_grow_type (type),
    add index idx_aud_grow_state (state),
    add index idx_aud_grow_aplId (applicant),
    add index idx_aud_grow_audId (auditor),
    add index idx_aud_grow_subId (submitter);

alter table t_aud_grow
    modify reason varchar(200) null;