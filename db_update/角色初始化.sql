insert into t_role (role_name, create_user)
values ('系统管理员', 1),
       ('班主任', 2),
       ('学生', 2);

insert into t_role_menu (role_id, menu_id)
select 1 as role_id, oid as menu_id
from t_menu;

insert into t_user_role (user_id, role_id)
values (1, 1);
