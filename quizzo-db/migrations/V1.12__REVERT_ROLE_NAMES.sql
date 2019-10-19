update quizzo.role
set role_name = 'ROLE_ADMIN'
where role_name = 'ADMIN';

update quizzo.role
set role_name = 'ROLE_MODERATOR'
where role_name = 'MODERATOR';

update quizzo.role
set role_name = 'ROLE_PLAYER'
where role_name = 'PLAYER';
