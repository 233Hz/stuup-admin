insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('爱国主义教育', 'rec_1', null, 1, 6, 1, 2, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '爱国爱校'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('主题教育活动', 'rec_2', '班会课，升旗仪式，主题教育活动', 1, 6, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '爱国爱校'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参加时政学习', 'rec_3', '时政大赛', 1, 6, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '时政学习'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('时政比赛', 'rec_4', null, 1, 6, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '时政学习'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参加党团学习', 'rec_5', null, 1, 5, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '时政学习'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参加安全教育', 'rec_6', null, 1, 5, 1, 2, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '安全法制'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('法制教育', 'rec_7', null, 1, 5, 1, 2, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '安全法制'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参加国防民防教育', 'rec_8', null, 1, 7, 1, 2, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '安全法制'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('优秀学员', 'rec_9', null, 1, 7, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '军事训练'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('合格学员', 'rec_10', null, 1, 7, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '军事训练'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参加社团', 'rec_11', null, 1, 5, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '思想品德'),
        (select t.id from t_growth t where t.name = '社团活动'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('纪律处分', 'rec_12', null, 1, 5, 2, 2, 2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '遵纪自律'),
        (select t.id from t_growth t where t.name = '遵纪守法'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('迟到早退一次', 'rec_13', null, 1, 3, 1, 2, 0.2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '遵纪自律'),
        (select t.id from t_growth t where t.name = '日常考勤'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('上课缺勤一次', 'rec_14', '无故缺勤', 1, 3, 2, 2, 0.2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '遵纪自律'),
        (select t.id from t_growth t where t.name = '日常考勤'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('不准时出操', 'rec_15', '无辜缺勤，不认真做', 1, 3, 3, 2, 0.2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '遵纪自律'),
        (select t.id from t_growth t where t.name = '日常考勤'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('未按规定着装校服校裤', 'rec_16', null, 1, 3, 1, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪表规范'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('佩戴胸卡', 'rec_17', null, 1, 3, 1, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪表规范'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('不染发', 'rec_18', null, 1, 3, 0.5, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪容整洁'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('不烫发', 'rec_19', null, 1, 3, 0.5, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪容整洁'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('不披散长发', 'rec_20', null, 1, 3, 0.5, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪容整洁'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('不化妆不美甲不佩戴首饰', 'rec_21', null, 1, 3, 0.5, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '仪容整洁'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('礼貌用语', 'rec_22', null, 1, 5, 0.5, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '文明礼仪'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('文明就餐', 'rec_23', null, 1, 5, 0.5, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '文明礼仪'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('个人卫生', 'rec_24', null, 1, 5, 0.5, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '卫生环境'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('书桌整洁', 'rec_25', null, 1, 5, 0.5, 2, 0.5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '卫生环境'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('前十名班级', 'rec_26', null, 1, 4, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '六项评比（流动红旗）'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('评为先进', 'rec_27', null, 1, 5, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '文明修养'),
        (select t.id from t_growth t where t.name = '文明寝室'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('国家奖学金', 'rec_28', null, 1, 6, 5, 1, 5, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '个人荣誉（奖学金、三好学生、优秀学生（团）干部、药校好学子'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('市级', 'rec_29', null, 1, 6, 4, 1, 4, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '个人荣誉（奖学金、三好学生、优秀学生（团）干部、药校好学子'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('区级（行业）', 'rec_30', null, 1, 6, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '个人荣誉（奖学金、三好学生、优秀学生（团）干部、药校好学子'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('校级', 'rec_31', null, 1, 5, 1, 1, 1, 1,
        (select t.id from t_growth t where t.name = '道德与公民素养'),
        (select t.id from t_growth t where t.name = '个人荣誉（奖学金、三好学生、优秀学生（团）干部、药校好学子'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('平均≥90分', 'rec_32', null, 1, 5, 6, 1, 6, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '学科成绩'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('平均≥80分', 'rec_33', null, 1, 5, 5, 1, 5, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '学科成绩'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('平均≥70分', 'rec_34', null, 1, 5, 4, 1, 4, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '学科成绩'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('平均≥60分', 'rec_35', null, 1, 5, 3, 1, 3, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '学科成绩'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('每个证书', 'rec_36', null, 1, 1, 8888, 1, 3, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业资格证书'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('国家级', 'rec_37', null, 1, 1, 8888, 1, 4, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业素养'),
        (select t.id from t_growth t where t.name = '各类各级比赛'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('市级', 'rec_38', null, 1, 1, 8888, 1, 3, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业素养'),
        (select t.id from t_growth t where t.name = '各类各级比赛'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('区级', 'rec_39', null, 1, 1, 8888, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业素养'),
        (select t.id from t_growth t where t.name = '各类各级比赛'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('校级', 'rec_40', null, 1, 1, 8888, 1, 1, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '职业素养'),
        (select t.id from t_growth t where t.name = '各类各级比赛'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('国家级', 'rec_41', null, 1, 1, 8888, 1, 4, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = '比赛获奖（每项）'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('市级', 'rec_42', null, 1, 1, 8888, 1, 3, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = '比赛获奖（每项）'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('区级', 'rec_43', null, 1, 1, 8888, 1, 2, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = '比赛获奖（每项）'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('校级', 'rec_44', null, 1, 1, 8888, 1, 1, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = '比赛获奖（每项）'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('发明专利', 'rec_45', null, 1, 1, 8888, 1, 4, 1,
        (select t.id from t_growth t where t.name = '技能与学习素养'),
        (select t.id from t_growth t where t.name = '双创比赛'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('体质达标', 'rec_46', null, 1, 5, 8888, 1, 3, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = null),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参加体育课外活动', 'rec_47', '1.	由学校统一组织的体育活动
2.	校队等训练活动', 1, 5, 2, 1, 1, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('国家级', 'rec_48', null, 1, 1, 8888, 1, 3, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = '体育比赛'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('市级', 'rec_49', null, 1, 1, 8888, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = '体育比赛'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('区级', 'rec_50', null, 1, 1, 8888, 1, 1, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = '体育比赛'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('校级', 'rec_51', null, 1, 1, 8888, 1, 0.5, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '身体素养'),
        (select t.id from t_growth t where t.name = '体育比赛'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参加心理剧或心理月展示活动', 'rec_52', null, 1, 1, 8888, 1, 1, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '心理素养'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参与心理中心活动', 'rec_53', null, 1, 1, 8888, 1, 1, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '心理素养'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('场馆观摩', 'rec_54', null, 1, 1, 8888, 1, 1, 1,
        (select t.id from t_growth t where t.name = '审美与艺术修养'),
        (select t.id from t_growth t where t.name = '艺术活动'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('国家级比赛', 'rec_55', null, 1, 1, 8888, 1, 4, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '才艺展示'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('市级比赛', 'rec_56', null, 1, 1, 8888, 1, 3, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '才艺展示'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('区级比赛', 'rec_57', null, 1, 1, 8888, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '才艺展示'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('校级比赛', 'rec_58', null, 1, 1, 8888, 1, 1, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '才艺展示'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('学校及以上组织', 'rec_59', null, 1, 1, 8888, 1, 2, 1,
        (select t.id from t_growth t where t.name = '运动与身心健康'),
        (select t.id from t_growth t where t.name = '艺术社团'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参加志愿服务每次', 'rec_60', null, 1, 1, 8888, 1, 2, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '志愿者服务'),
        (select t.id from t_growth t where t.name = '职业体验日'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('参加交通岗值勤', 'rec_61', null, 1, 1, 8888, 1, 0.5, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '志愿者服务'),
        (select t.id from t_growth t where t.name = '交通文明岗'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('修满学分', 'rec_62', null, 1, 5, 3, 1, 3, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '志愿者服务'),
        (select t.id from t_growth t where t.name = '志愿者活动'));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('日常家务劳动', 'rec_63', null, 1, 5, 1, 1, 0.5, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '劳动实践'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('值日值周', 'rec_64', null, 1, 5, 1, 1, 0.5, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '劳动实践'),
        (select t.id from t_growth t where t.name = null));
insert into t_growth_item (name, code, description, fill_period, score_period, score_upper_limit, calculate_type, score,
                           create_user, first_level_id, second_level_id, three_level_id)
values ('修满学分', 'rec_65', null, 1, 5, 2, 1, 2, 1,
        (select t.id from t_growth t where t.name = '劳动与职业素养'),
        (select t.id from t_growth t where t.name = '劳动实践'),
        (select t.id from t_growth t where t.name = '生产劳动实践'));
