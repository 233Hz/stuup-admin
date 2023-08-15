insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('爱国主义教育', 'CZ_001', null, 6, 1, null, 2, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '爱国爱校'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('主题教育活动', 'CZ_002', '班会课，升旗仪式，主题教育活动', 6, 1, null, 2, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '爱国爱校'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参加时政学习', 'CZ_003', '时政大赛', 6, 1, null, 1, 1, 1, 2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '时政学习'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('时政比赛', 'CZ_004', null, 6, 1, null, 1, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '时政学习'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参加党团学习', 'CZ_005', null, 5, 2, null, 1, 2, 1, 1, 2,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '时政学习'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参加安全教育', 'CZ_006', null, 5, 1, null, 2, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '安全法制'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('法制教育', 'CZ_007', null, 5, 1, null, 2, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '安全法制'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参加国防民防教育', 'CZ_008', null, 1, null, 1, 2, 1, 1, 1, 2,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '安全法制'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('优秀学员', 'CZ_009', null, 1, null, 1, 1, 2, 1, 1, 2,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '军事训练'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('合格学员', 'CZ_010', null, 1, null, 1, 1, 1, 1, 1, 2,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '军事训练'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参加社团', 'CZ_011', null, 5, 2, null, 1, 2, 1, 1, 2,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '社团活动'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('纪律处分', 'CZ_012', null, 5, 2, null, 2, 2, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '遵纪自律'),
        (select t.id from t_growth t where t.name = '遵纪守法'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('迟到早退一次', 'CZ_013', null, 3, 1, null, 2, 0.2, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '遵纪自律'),
        (select t.id from t_growth t where t.name = '日常考勤'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('上课缺勤一次', 'CZ_014', '无故缺勤', 3, 1, null, 2, 0.2, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '遵纪自律'),
        (select t.id from t_growth t where t.name = '日常考勤'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('不准时出操', 'CZ_015', '无辜缺勤，不认真做', 3, 1, null, 2, 0.2, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '遵纪自律'),
        (select t.id from t_growth t where t.name = '日常考勤'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('未按规定着装校服校裤', 'CZ_016', null, 3, 1, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪表规范'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('佩戴胸卡', 'CZ_017', null, 3, 1, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪表规范'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('不染发', 'CZ_018', null, 3, 0.5, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪容整洁'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('不烫发', 'CZ_019', null, 3, 0.5, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪容整洁'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('不披散长发', 'CZ_020', null, 3, 0.5, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪容整洁'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('不化妆不美甲不佩戴首饰', 'CZ_021', null, 3, 0.5, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪容整洁'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('礼貌用语', 'CZ_022', null, 5, 0.5, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '文明礼仪'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('文明就餐', 'CZ_023', null, 5, 0.5, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '文明礼仪'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('个人卫生', 'CZ_024', null, 5, 0.5, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '卫生环境'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('书桌整洁', 'CZ_025', null, 5, 0.5, null, 2, 0.5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '卫生环境'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('前十名班级', 'CZ_026', null, 4, 2, null, 1, 2, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '六项评比（流动红旗）'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('评为先进', 'CZ_027', null, 5, 1, null, 1, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '文明寝室'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('国家奖学金', 'CZ_028', null, 6, 5, null, 1, 5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '个人荣誉（奖学金、三好学生、优秀学生（团）干部、药校好学子'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('市级', 'CZ_029', null, 6, 4, null, 1, 4, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '个人荣誉（奖学金、三好学生、优秀学生（团）干部、药校好学子'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('区级（行业）', 'CZ_030', null, 6, 2, null, 1, 2, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '个人荣誉（奖学金、三好学生、优秀学生（团）干部、药校好学子'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('校级', 'CZ_031', null, 5, 1, null, 1, 1, 1, 2, 2,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '个人荣誉（奖学金、三好学生、优秀学生（团）干部、药校好学子'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('平均≥90分', 'CZ_032', null, 5, 6, null, 1, 6, 1, 1, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '学科成绩'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('平均≥80分', 'CZ_033', null, 5, 5, null, 1, 5, 1, 1, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '学科成绩'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('平均≥70分', 'CZ_034', null, 5, 4, null, 1, 4, 1, 1, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '学科成绩'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('平均≥60分', 'CZ_035', null, 5, 3, null, 1, 3, 1, 1, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '学科成绩'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('每个证书', 'CZ_036', null, 1, null, null, 1, 3, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业资格证书'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('国家级', 'CZ_037', null, 1, null, null, 1, 4, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业素养'),
        (select t.id from t_growth t where t.name = '各类各级比赛'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('市级', 'CZ_038', null, 1, null, null, 1, 3, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业素养'),
        (select t.id from t_growth t where t.name = '各类各级比赛'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('区级', 'CZ_039', null, 1, null, null, 1, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业素养'),
        (select t.id from t_growth t where t.name = '各类各级比赛'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('校级', 'CZ_040', null, 1, null, null, 1, 1, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业素养'),
        (select t.id from t_growth t where t.name = '各类各级比赛'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('国家级', 'CZ_041', null, 1, null, null, 1, 4, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = '比赛获奖（每项）'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('市级', 'CZ_042', null, 1, null, null, 1, 3, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = '比赛获奖（每项）'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('区级', 'CZ_043', null, 1, null, null, 1, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = '比赛获奖（每项）'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('校级', 'CZ_044', null, 1, null, null, 1, 1, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = '比赛获奖（每项）'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('发明专利', 'CZ_045', null, 1, null, null, 1, 4, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('体质达标', 'CZ_046', null, 5, 3, null, 1, 3, 1, 1, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参加体育课外活动', 'CZ_047', '1.	由学校统一组织的体育活动
2.	校队等训练活动', 5, 2, null, 1, 1, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('国家级', 'CZ_048', null, 1, null, null, 1, 3, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = '体育比赛'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('市级', 'CZ_049', null, 1, null, null, 1, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = '体育比赛'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('区级', 'CZ_050', null, 1, null, null, 1, 1, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = '体育比赛'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('校级', 'CZ_051', null, 1, null, null, 1, 0.5, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = '体育比赛'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参加心理剧或心理月展示活动', 'CZ_052', null, 1, null, null, 1, 1, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '心理素养'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参与心理中心活动', 'CZ_053', null, 1, null, null, 1, 1, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '心理素养'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('场馆观摩', 'CZ_054', null, 1, null, null, 1, 1, 1, 2, 1,
        (select t.id from t_growth t where t.name = '审美与艺术修养'),
        (select t.id from t_growth t where t.name = '艺术活动'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('国家级比赛', 'CZ_055', null, 1, null, null, 1, 4, 1, 2, 1,
        (select t.id from t_growth t where t.name = '审美与艺术修养'),
        (select t.id from t_growth t where t.name = '才艺展示'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('市级比赛', 'CZ_056', null, 1, null, null, 1, 3, 1, 2, 1,
        (select t.id from t_growth t where t.name = '审美与艺术修养'),
        (select t.id from t_growth t where t.name = '才艺展示'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('区级比赛', 'CZ_057', null, 1, null, null, 1, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '审美与艺术修养'),
        (select t.id from t_growth t where t.name = '才艺展示'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('校级比赛', 'CZ_058', null, 1, null, null, 1, 1, 1, 2, 1,
        (select t.id from t_growth t where t.name = '审美与艺术修养'),
        (select t.id from t_growth t where t.name = '才艺展示'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('学校及以上组织', 'CZ_059', null, 1, null, null, 1, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '审美与艺术修养'),
        (select t.id from t_growth t where t.name = '艺术社团'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参加志愿服务每次', 'CZ_060', null, 1, null, null, 1, 2, 1, 2, 2,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '志愿者服务'),
        (select t.id from t_growth t where t.name = '职业体验日'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('参加交通岗值勤', 'CZ_061', null, 1, null, null, 1, 0.5, 1, 2, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '志愿者服务'),
        (select t.id from t_growth t where t.name = '交通文明岗'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('修满学分', 'CZ_062', null, 5, 3, null, 1, 3, 1, 1, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '志愿者服务'),
        (select t.id from t_growth t where t.name = '志愿者活动'));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('日常家务劳动', 'CZ_063', null, 5, 1, null, 1, 0.5, 1, 2, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '劳动实践'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('值日值周', 'CZ_064', null, 5, 1, null, 1, 0.5, 1, 2, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '劳动实践'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('修满学分', 'CZ_065', null, 5, 2, null, 1, 2, 1, 1, 2,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '劳动实践'),
        (select t.id from t_growth t where t.name = '生产劳动实践'));
