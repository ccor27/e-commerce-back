INSERT INTO customer (cellphone,email,last_name,name,enable,pwd,username) values ('123456789','test@gmail.com','testLast','test',true,'$2a$10$RQc.XIhtqSv8wpic8XNpfu3S8bRh85KtUXKMGs1SEf5CFy20CL32W','test');
INSERT INTO customer (cellphone,email,last_name,name,enable,pwd,username) values ('123456789','test2@gmail.com','test2Last','test2',true,'$2a$10$7A9UJxtuPmpsKAAR5PfohOWzPFgdvOYfy8rH2C2Ww7ww8.N.HtiQy','test2');
INSERT INTO customer_roles (customer_id,role) values (1,'CUSTOMER');
INSERT INTO customer_roles (customer_id,role) values (1,'ADMIN');
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
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('TV',20,150000.0,'7-1242-3525-9',true);
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Laptop',15,200000.0,'6-9274-3525-9',true);
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Keyboard',5,90000.0,'6-1233-3344-9',true);
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Mouse',20,15000.0,'6-1111-1247-9',true);
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Headphones',9,95000.0,'6-9973-2361-9',true);
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Desktop',30,350000.0,'8-66423-1572-9',true);
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Chair',300,150000.0,'8-0096-0030-9',true);
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Webcam',80,100000.0,'6-9980-1234-9',true);
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Laptop charger',25,90000.0,'6-3342-9989-9',true);
--INSERT INTO product_stock (name,amount,price_per_unit,bar_code,enable_product) values('Computer screen',30,130000.0,'6-1772-7199-9',true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Kris', 77, 890.18, '70-935-0702',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Alissa', 25, 896.17, '22-685-1621',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Cynthie', 48, 563.41, '04-714-3217',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Rhetta', 10, 559.18, '20-143-5181', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Bendick', 11, 72.36, '12-786-0228', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Darby', 68, 141.39, '97-002-2471',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Isabel', 49, 55.60, '88-485-4769', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Marijo', 60, 552.17, '65-445-4335',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Lonnard', 66, 905.89, '43-754-3444',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Rancell', 26, 329.88, '53-914-3899', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Sydney', 79, 830.60, '11-823-2227', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Belita', 9, 517.69, '70-630-4569', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Hatty', 11, 515.08, '81-142-2818', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Michele', 25, 676.92, '52-434-9596', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Raymond', 41, 727.00, '74-218-9040', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Kyle', 21, 996.05, '61-324-0377', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Celine', 73, 433.91, '47-827-0705', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Yancy', 24, 879.23, '55-326-8926', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Rafaelia', 78, 393.40, '25-973-8065', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Margalo', 58, 167.79, '94-245-3319', true);
