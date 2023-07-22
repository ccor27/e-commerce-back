INSERT INTO customer (id,cellphone,email,last_name,name,enable_user,pwd,username) values (1,'123456789','test@gmail.com','testLast','test',true,'test123','test');
INSERT INTO customer (id,cellphone,email,last_name,name,enable_user,pwd,username) values (2,'123456789','test2@gmail.com','test2Last','test2',true,'test2123','test2');
INSERT INTO customer_roles (customer_id,role) values (1,'CUSTOMER');
INSERT INTO customer_roles (customer_id,role) values (2,'ADMIN');
