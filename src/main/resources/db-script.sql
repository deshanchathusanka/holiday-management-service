-------------Database Script for Holiday Management System--------------------

CREATE DATABASE holiday;

---------------Employee table contains all employee data-----------------

CREATE TABLE employee
(
  id serial,
  email character varying(50) NOT NULL,
  active boolean,
  last_name character varying(30),
  first_name character varying(30) NOT NULL,
  password character varying(255),
  role character varying(20),
  CONSTRAINT users_pkey PRIMARY KEY (id)
);

INSERT INTO employee (email,active,last_name,first_name,password,role) values('deshan@email.com',true,'Manager','Project','$2a$10$pNOQHgXFKcfrqzdgsPI2oOBJlXaGiAWQqh8fy2sAV9cOtpzfiyBf.','MANAGER');
INSERT INTO employee (email,active,last_name,first_name,password,role) values('prasannna@email.com',true,'HR','Project','$2a$10$pNOQHgXFKcfrqzdgsPI2oOBJlXaGiAWQqh8fy2sAV9cOtpzfiyBf.','HR');

-------- Leave Details table-----------

CREATE TABLE leave_details
(
	id serial,
	username character varying(50) NOT NULL,
	employee_name character varying(50) NOT NULL,
    from_date timestamp NOT NULL,
    to_date timestamp NOT NULL,
    covering_emp_username character varying(50) NOT NULL,
    resolved_by character varying(50),
	leave_type character varying(50) NOT NULL,
	reason character varying(300) NOT NULL,
	duration integer,
	status character varying(20) NOT NULL,
	active boolean,
	CONSTRAINT leave_pkey PRIMARY KEY (id)
);

