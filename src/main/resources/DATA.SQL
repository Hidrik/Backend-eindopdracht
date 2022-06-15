INSERT INTO project
(project_code, description, progress_percentage, deadline, budget)
VALUES
('PROJECT001', 'test', 10, '2022-10-01', 1000),
('PROJECT002', 'test', 20, '2022-10-02', 2000),
('PROJECT003', 'test', 30, '2022-10-03', 3000),
('PROJECT004', 'test', 40, '2022-10-04', 4000),
('PROJECT005', 'test', 50, '2022-10-05', 5000),
('PROJECT006', 'test', 60, '2022-10-06', 6000);

INSERT INTO users
(username, email, enabled, password)
VALUES
('landlust', 'landlust@hotmail.com', true, '$2a$10$5asci6b1D9/YFJP964cYq./MObMGYmffjOIYQDEd77WD.598gJPeq');

INSERT INTO authorities
(username, authority)
VALUES
('landlust', 'ROLE_ADMIN'),
('landlust', 'ROLE_USER');
