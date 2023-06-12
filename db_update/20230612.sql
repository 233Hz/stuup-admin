insert into t_menu (pid, name, code, path, flag)
values ((select t.oid from t_menu t where name = '成长排名'), '系部排名', 'RankingFaculty', '/faculty', 2);
insert into t_menu (pid, name, code, path, flag)
values ((select t.oid from t_menu t where name = '成长排名'), '年度排名', 'RankingYear', '/year', 2);
insert into t_menu (pid, name, code, path, flag)
values ((select t.oid from t_menu t where name = '成长排名'), '进步榜', 'RankingProgress', '/progress', 2);