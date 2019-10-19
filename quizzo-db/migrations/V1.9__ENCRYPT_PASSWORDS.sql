alter table quizzo.user
drop column password;

alter table quizzo.user
add column password varchar(80) null;

-- admin123
update quizzo.user
set password = '{bcrypt}$2a$10$gaBUzLTzR3fOUqys7DfL7OalgDyC0gaTkxRTpM6Bxbhkrddrf04VK'
where user_name = 'krimple';

-- Administrator
update quizzo.user
set password = '{bcrypt}$2a$10$lxlo94eDiHmenAHGmwjsEuM5QrG0OA/oNULgIkAQZQnXiFakGx1im'
where user_name = 'admin';

