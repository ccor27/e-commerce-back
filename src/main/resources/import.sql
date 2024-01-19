INSERT INTO customer (cellphone,email,last_name,name,enable,is_deleted,receive_notifications,pwd,username) values ('+34903672134','test@gmail.com','testLast','test',true,false,true,'$2a$10$RQc.XIhtqSv8wpic8XNpfu3S8bRh85KtUXKMGs1SEf5CFy20CL32W','test');
INSERT INTO customer (cellphone,email,last_name,name,enable,is_deleted,receive_notifications,pwd,username) values ('123456789','test2@gmail.com','test2Last','test2',true,false,true,'$2a$10$7A9UJxtuPmpsKAAR5PfohOWzPFgdvOYfy8rH2C2Ww7ww8.N.HtiQy','test2');
INSERT INTO customer (cellphone,email,last_name,name,enable,is_deleted,receive_notifications,pwd,username) values ('345222389','test3@gmail.com','osorio','cristian',true,false,true,'$2a$10$7A9UJxtuPmpsKAAR5PfohOWzPFgdvOYfy8rH2C2Ww7ww8.N.HtiQy','rocky');
INSERT INTO customer_roles (customer_id,role) values (1,'CUSTOMER');
INSERT INTO customer_roles (customer_id,role) values (1,'ADMIN');
INSERT INTO customer_roles (customer_id,role) values (2,'ADMIN');
INSERT INTO customer_roles (customer_id,role) values (3,'CUSTOMER');
INSERT INTO CUSTOMER_CHANNEL_NOTIFICATION (channel,customer_id) values('EMAIL',1);
INSERT INTO CUSTOMER_CHANNEL_NOTIFICATION (channel,customer_id) values('SMS',1);
INSERT INTO CUSTOMER_CHANNEL_NOTIFICATION (channel,customer_id) values('EMAIL',2);
INSERT INTO CUSTOMER_CHANNEL_NOTIFICATION (channel,customer_id) values('EMAIL',3);
--address
INSERT INTO address (customer_id,street,country,postal_code) values(1,'spain','Spain','28903');
INSERT INTO address (customer_id,street,country,postal_code) values(2,'valencia','Spain','87390');
--credit cards
INSERT INTO credit_card (customer_id,number,type_card,month_exp,year_exp,cvv,holder_name) values (1,'123456789','VISA','12','2030',766,'test');
INSERT INTO credit_card (customer_id,number,type_card,month_exp,year_exp,cvv,holder_name) values (1,'135723653','MASTER_CARD','12','2030',122,'test');
INSERT INTO credit_card (customer_id,number,type_card,month_exp,year_exp,cvv,holder_name) values (2,'864324632','AMERICAN_EXPRESS','12','2030',030,'test2');
--history
INSERT INTO history (modification_date, customer_full_name) values ('2023-08-09','test testLast');
INSERT INTO history (modification_date, customer_full_name) values ('2023-08-09','test2 test2Last');
INSERT INTO history (modification_date, customer_full_name) values ('2023-08-09','cristian osorio');
UPDATE customer SET history_id=1 WHERE id=1;
UPDATE customer SET history_id=2 WHERE id=2;
UPDATE customer SET history_id=3 WHERE id=3;
--productStock
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Kris', 77, 890, '70-935-0702',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Alissa', 25, 896, '22-685-1621',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Cynthie', 48, 563, '04-714-3217',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Rhetta', 10, 559, '20-143-5181', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Bendick', 11, 72, '12-786-0228', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Darby', 68, 141, '97-002-2471',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Isabel', 49, 55, '88-485-4769', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Marijo', 60, 552, '65-445-4335',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Lonnard', 66, 905, '43-754-3444',true );
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Rancell', 26, 329, '53-914-3899', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Sydney', 79, 830, '11-823-2227', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Belita', 9, 517, '70-630-4569', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Hatty', 11, 515, '81-142-2818', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Michele', 25, 676, '52-434-9596', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Raymond', 41, 727, '74-218-9040', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Kyle', 21, 996, '61-324-0377', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Celine', 73, 433, '47-827-0705', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Yancy', 24, 879, '55-326-8926', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Rafaelia', 78, 393, '25-973-8065', true);
insert into product_stock (name, amount, price_per_unit, bar_code, enable_product) values ('Margalo', 58, 167, '94-245-3319', true);
--products sold
insert into product_sold (bar_code,name,amount,price) values ('94-245-3319','Margalo',10,1670);
insert into product_sold (bar_code,name,amount,price) values ('25-973-8065','Rafaelia',10,3930);
insert into product_sold (bar_code,name,amount,price) values ('47-827-0705','Celine',1,433);
insert into product_sold (bar_code,name,amount,price) values ('25-973-8065','Rafaelia',5,835);
--payment
insert into payment (status_payment,create_at,credit_card_id,is_deleted,total_price) values ('PAID','2023-08-09',1,false,1670);
insert into payment (status_payment,create_at,credit_card_id,is_deleted,total_price) values ('PAID','2023-08-09',1,false,433);
--sale
insert into sale (concept,is_deleted,create_at,payment_id) values('buy test1',false,'2023-08-09',1);
insert into sale (concept,is_deleted,create_at,payment_id) values('buy test2',false,'2023-08-09',2);

insert into history_sale (history_id,sales_id) values (1,1);
insert into history_sale (history_id,sales_id) values (1,2);
--product sold and sale
insert into sale_products (products_sold_id,sale_id) values(1,1);
--insert into sale_products (products_sold_id,sale_id) values(2,1);
insert into sale_products (products_sold_id,sale_id) values(3,2);
--insert into sale_products (products_sold_id,sale_id) values(4,2);


