create index idx_rec_caucus_stuid on t_rec_caucus (student_id);
create index idx_rec_caucus_yid on t_rec_caucus (year_id);
create index idx_rec_caucus_gid on t_rec_caucus (grow_id);

create index idx_rec_default_stuid on t_rec_default (student_id);
create index idx_rec_default_yid on t_rec_default (year_id);
create index idx_rec_default_gid on t_rec_default (grow_id);

create index idx_rec_honor_stuid on t_rec_honor (student_id);
create index idx_rec_honor_yid on t_rec_honor (year_id);
create index idx_rec_honor_gid on t_rec_honor (grow_id);

create index idx_rec_labor_time_stuid on t_rec_labor_time (student_id);
create index idx_rec_labor_time_yid on t_rec_labor_time (year_id);
create index idx_rec_labor_time_gid on t_rec_labor_time (grow_id);

create index idx_rec_log_yid on t_rec_log (year_id);
create index idx_rec_log_gid on t_rec_log (grow_id);

create index idx_rec_military_stuid on t_rec_military (student_id);
create index idx_rec_military_yid on t_rec_military (year_id);
create index idx_rec_military_gid on t_rec_military (grow_id);

create index idx_rec_nation_stuid on t_rec_nation (student_id);
create index idx_rec_nation_yid on t_rec_nation (year_id);
create index idx_rec_nation_gid on t_rec_nation (grow_id);

create index idx_rec_score_stuid on t_rec_score (student_id);
create index idx_rec_score_yid on t_rec_score (year_id);
create index idx_rec_score_gid on t_rec_score (grow_id);

create index idx_rec_society_stuid on t_rec_society (student_id);
create index idx_rec_society_yid on t_rec_society (year_id);
create index idx_rec_society_gid on t_rec_society (grow_id);

create index idx_rec_volunteer_stuid on t_rec_volunteer (student_id);
create index idx_rec_volunteer_yid on t_rec_volunteer (year_id);
create index idx_rec_volunteer_gid on t_rec_volunteer (grow_id);

create index idx_growth_pid on t_growth (pid);

create index idx_growth_item_fid on t_growth_item (first_level_id);
create index idx_growth_item_sid on t_growth_item (second_level_id);
create index idx_growth_item_tid on t_growth_item (three_level_id);

create index idx_aud_grow_gid on t_aud_grow (grow_id);
create index idx_aud_log_uid on t_aud_log (user_id);