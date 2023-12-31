insert into t_growth (pid, name, description, sort, create_user)
values (0, '道德与公民素养', null, 1, 1),
       (0, '技能与学习素养', null, 2, 2),
       (0, '运动与身心健康', null, 3, 3),
       (0, '审美与艺术修养', null, 4, 4),
       (0, '劳动与职业素养', null, 5, 5),
       ((select t.id from t_growth t where t.name = '道德与公民素养'), '思想品德', null, 1, 1),
       ((select t.id from t_growth t where t.name = '道德与公民素养'), '遵纪自律', null, 2, 2),
       ((select t.id from t_growth t where t.name = '道德与公民素养'), '文明修养', null, 3, 3),
       ((select t.id from t_growth t where t.name = '道德与公民素养'),
        '个人荣誉（奖学金、三好学生、优秀学生（团）干部、药校好学子',
        null, 4, 4),
       ((select t.id from t_growth t where t.name = '技能与学习素养'), '学科成绩', null, 1, 1),
       ((select t.id from t_growth t where t.name = '技能与学习素养'), '职业资格证书', null, 2, 2),
       ((select t.id from t_growth t where t.name = '技能与学习素养'), '职业素养', null, 3, 3),
       ((select t.id from t_growth t where t.name = '技能与学习素养'), '双创比赛', null, 4, 4),
       ((select t.id from t_growth t where t.name = '运动与身心健康'), '身体素养', null, 5, 1),
       ((select t.id from t_growth t where t.name = '运动与身心健康'), '心理素养', null, 6, 2),
       ((select t.id from t_growth t where t.name = '审美与艺术修养'), '艺术活动', null, 1, 1),
       ((select t.id from t_growth t where t.name = '审美与艺术修养'), '才艺展示', null, 2, 2),
       ((select t.id from t_growth t where t.name = '审美与艺术修养'), '艺术社团', null, 3, 3),
       ((select t.id from t_growth t where t.name = '劳动与职业素养'), '志愿者服务', null, 1, 1),
       ((select t.id from t_growth t where t.name = '劳动与职业素养'), '劳动实践', null, 2, 2),
       ((select t.id from t_growth t where t.name = '思想品德'), '爱国爱校', null, 1, 1),
       ((select t.id from t_growth t where t.name = '思想品德'), '时政学习', null, 2, 2),
       ((select t.id from t_growth t where t.name = '思想品德'), '安全法制', null, 3, 3),
       ((select t.id from t_growth t where t.name = '思想品德'), '军事训练', null, 4, 4),
       ((select t.id from t_growth t where t.name = '思想品德'), '社团活动', null, 5, 5),
       ((select t.id from t_growth t where t.name = '遵纪自律'), '遵纪守法', null, 1, 1),
       ((select t.id from t_growth t where t.name = '遵纪自律'), '日常考勤', null, 2, 2),
       ((select t.id from t_growth t where t.name = '文明修养'), '仪表规范', null, 1, 1),
       ((select t.id from t_growth t where t.name = '文明修养'), '仪容整洁', null, 2, 2),
       ((select t.id from t_growth t where t.name = '文明修养'), '文明礼仪', null, 3, 3),
       ((select t.id from t_growth t where t.name = '文明修养'), '卫生环境', null, 4, 4),
       ((select t.id from t_growth t where t.name = '文明修养'), '六项评比（流动红旗）', null, 5, 5),
       ((select t.id from t_growth t where t.name = '文明修养'), '文明寝室', null, 6, 6),
       ((select t.id from t_growth t where t.name = '职业素养'), '各类各级比赛', null, 1, 1),
       ((select t.id from t_growth t where t.name = '双创比赛'), '比赛获奖（每项）', null, 1, 1),
       ((select t.id from t_growth t where t.name = '身体素养'), '体育比赛', null, 1, 1),
       ((select t.id from t_growth t where t.name = '志愿者服务'), '职业体验日', null, 1, 1),
       ((select t.id from t_growth t where t.name = '志愿者服务'), '交通文明岗', null, 2, 2),
       ((select t.id from t_growth t where t.name = '志愿者服务'), '志愿者活动', null, 3, 3),
       ((select t.id from t_growth t where t.name = '劳动实践'), '生产劳动实践', null, 1, 3);

# 成长项初始化模板
insert into t_growth_item (name, code, description, score_period, score_upper_limit, collect_limit, calculate_type,
                           score, create_user, gatherer, type, first_level_id, second_level_id, three_level_id)
values ('"&A2&"', '"&B2&"', '"&C2&"', "&D2&", "&E2&", "&F2&", "&G2&", "&H2&", 1, "&M2&", "&N2&",
        (select t.id from t_growth t where t.name = '"&J2&"'), (select t.id from t_growth t where t.name = '"&K2&"'),
        (select t.id from t_growth t where t.name = '"&L2&"'));