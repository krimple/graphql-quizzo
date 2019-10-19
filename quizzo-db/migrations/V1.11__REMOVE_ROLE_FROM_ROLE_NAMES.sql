update quizzo.role
set role_name = 'ADMIN'
where role_name = 'ROLE_ADMIN';

update quizzo.role
set role_name = 'MODERATOR'
where role_name = 'ROLE_MODERATOR';

update quizzo.role
set role_name = 'PLAYER'
where role_name = 'ROLE_PLAYER';
