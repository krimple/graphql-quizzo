update quizzo.role
set role_name = 'ROLE_ADMIN'
where role_name = 'admin';

update quizzo.role
set role_name = 'ROLE_MODERATOR'
where role_name = 'moderator';

update quizzo.role
set role_name = 'ROLE_PLAYER'
where role_name = 'player';
