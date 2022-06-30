INSERT INTO project(project_code, costs, description, progress_percentage, deadline, budget)
VALUES
('PROJECT001', 100, 'test', 10, '2022-10-01', 1000),
('PROJECT002', 100, 'test', 20, '2022-10-02', 2000),
('PROJECT003', 100, 'test', 30, '2022-10-03', 3000),
('PROJECT004', 100, 'test', 40, '2022-10-04', 4000),
('PROJECT005', 100, 'test', 50, '2022-10-05', 5000),
('PROJECT006', 100, 'test', 60, '2022-10-06', 6000);

INSERT INTO component(description, manufacturer, price, article_number, order_link, stock)
VALUES
('Dit is component met id 1', 'NOVI', 1000, 'ABC-DEF-GHI-JKL', 'https://www.bestel.nl/', 0);

INSERT INTO account
(employee_function, first_name, last_name, postal_code, street_name, house_number, city)
values
('THE_BOSS' , 'Hidrik' , 'Landlust', '1234 AB', 'LaanStraatWeg', 69, 'Monkeytown');

INSERT INTO users
(username, email, enabled, password, account_id)
VALUES
('landlust', 'landlust@hotmail.com', true, '$2a$10$5asci6b1D9/YFJP964cYq./MObMGYmffjOIYQDEd77WD.598gJPeq', 1);

INSERT INTO authorities
(username, authority)
VALUES
('landlust', 'ROLE_ADMIN'),
('landlust', 'ROLE_USER');

INSERT INTO project_members
(project_id, account_id)
values
(1, 1);