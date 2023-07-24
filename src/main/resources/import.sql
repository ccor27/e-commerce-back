INSERT INTO customer (cellphone,email,last_name,name,enable_user,pwd,username) values ('123456789','test@gmail.com','testLast','test',true,'$2a$10$RQc.XIhtqSv8wpic8XNpfu3S8bRh85KtUXKMGs1SEf5CFy20CL32W','test');
INSERT INTO customer (cellphone,email,last_name,name,enable_user,pwd,username) values ('123456789','test2@gmail.com','test2Last','test2',true,'$2a$10$7A9UJxtuPmpsKAAR5PfohOWzPFgdvOYfy8rH2C2Ww7ww8.N.HtiQy','test2');
INSERT INTO customer_roles (customer_id,role) values (1,'CUSTOMER');
--INSERT INTO customer_roles (customer_id,role) values (1,'ADMIN');
INSERT INTO customer_roles (customer_id,role) values (2,'ADMIN');
--address
INSERT INTO address (customer_id,street,country,postal_code) values(1,'spain','Spain','28903');
INSERT INTO address (customer_id,street,country,postal_code) values(2,'valencia','Spain','87390');
--credit cards
INSERT INTO credit_card (customer_id,number,type_card) values (1,'123456789','VISA');
INSERT INTO credit_card (customer_id,number,type_card) values (1,'135723653','MASTER_CARD');
INSERT INTO credit_card (customer_id,number,type_card) values (2,'864324632','AMERICAN_EXPRESS');
--history
INSERT INTO history DEFAULT VALUES;
INSERT INTO history DEFAULT VALUES;
UPDATE customer SET history_id=1 WHERE id=1;
UPDATE customer SET history_id=2 WHERE id=2;
--productStock
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('TV',20,150000.0,'7-1242-3525-9',true);
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Laptop',15,200000.0,'6-9274-3525-9',true);
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Keyboard',5,90000.0,'6-1233-3344-9',true);
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Mouse',20,15000.0,'6-1111-1247-9',true);
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Headphones',9,95000.0,'6-9973-2361-9',true);
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Desktop',30,350000.0,'8-66423-1572-9',true);
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Chair',300,150000.0,'8-0096-0030-9',true);
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Webcam',80,100000.0,'6-9980-1234-9',true);
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Laptop charger',25,90000.0,'6-3342-9989-9',true);
INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Computer screen',30,130000.0,'6-1772-7199-9',true);

