insert into "user" (id, firstname, last_name, username)
values
('e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, 'bee', 'bee', 'bee'),
('7ff6da70-08be-4a53-b1b3-bdad4da19f7b'::uuid, 'Tarra', 'Jones', 'milton.collins'),
('ad01846a-7ca0-4d22-a1f7-b3e35b801f59'::uuid, 'Ethan', 'Leffler', 'arnette.steuber'),
('ac318ea5-7b12-4d59-a57e-bd42c4df0f7d'::uuid, 'Ike', 'Kshlerin', 'aurelio.nikolaus'),
('8c1b5e20-97fe-4a1a-a2bf-c8b4430f5c0b'::uuid, 'Audra', 'Batz', 'cedrick.yundt'),
('2ceb376d-171c-4007-af63-9e5776c95dbf'::uuid, 'Marlana', 'Feeney', 'antone.legros'),
('377b04c6-da52-4ee0-947d-92847cbc83c6'::uuid, 'Gavin', 'Considine', 'contessa.mcglynn'),
('087b7f26-99fa-426e-9b10-0b7418b68e44'::uuid, 'Gerri', 'Hane', 'lupita.reilly'),
('26323813-6e91-4a94-b35f-ccca609502be'::uuid, 'Eufemia', 'Purdy', 'lonny.gorczany'),
('b8ba5611-9315-4be6-9595-00898bbf2d70'::uuid, 'Chester', 'Kub', 'francis.jacobson'),
('f7e7b177-2a7e-4286-8d7c-457f02f97408'::uuid, 'Gene', 'Fay', 'zachery.bernier'),
('ddf80463-1258-442b-abe8-f9f7782bed8f'::uuid, 'Davina', 'Mann', 'aaron.flatley'),
('2551534b-20bf-419f-970d-98cb1e40f36f'::uuid, 'Yuko', 'Witting', 'cheri.blick'),
('9d0afe07-24c1-47d7-804a-e00fc956816d'::uuid, 'Marlys', 'Quitzon', 'florencio.vonrueden'),
('7b3b9b03-4cc1-480a-a00a-6f1e74650f24'::uuid, 'Marisha', 'Ritchie', 'sam.wisoky'),
('79874258-a854-4214-b738-12972da424ec'::uuid, 'Vicki', 'Steuber', 'carmen.konopelski'),
('3d7626b7-5df6-4414-b2d1-ebd8ee6b39f6'::uuid, 'Stanton', 'Franecki', 'shelley.roberts'),
('42c2a83d-fb58-4111-9834-57410f6172dd'::uuid, 'My', 'King', 'emerson.robel'),
('13b4f926-ac31-40d0-8cce-86ea88337d1f'::uuid, 'Sandy', 'Kulas', 'jeremy.schulist'),
('c9d7b698-b143-4e8c-a0ff-723530fe62df'::uuid, 'Lorenzo', 'Block', 'king.kirlin'),
('da0d7bb2-be8a-4f92-8704-0753d21e16f9'::uuid, 'Marcos', 'Predovic', 'analisa.satterfield');

insert into "friendship" (requester_id, addressee_id, created_date, status)
values
('ad01846a-7ca0-4d22-a1f7-b3e35b801f59'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '2023-12-16 15:49:15.174', 'ACCEPTED'),
('ac318ea5-7b12-4d59-a57e-bd42c4df0f7d'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '2022-07-22 05:03:15.732', 'ACCEPTED'),
('377b04c6-da52-4ee0-947d-92847cbc83c6'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '2024-02-03 00:50:39.379', 'ACCEPTED'),
('ddf80463-1258-442b-abe8-f9f7782bed8f'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '2023-02-08 04:41:19.365', 'ACCEPTED'),
('2551534b-20bf-419f-970d-98cb1e40f36f'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '2023-05-07 07:50:46.702', 'ACCEPTED'),
('da0d7bb2-be8a-4f92-8704-0753d21e16f9'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '2022-08-30 06:58:23.344', 'PENDING'),
('c9d7b698-b143-4e8c-a0ff-723530fe62df'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '2023-05-29 18:01:34.796', 'PENDING'),
('e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '13b4f926-ac31-40d0-8cce-86ea88337d1f'::uuid, '2022-03-31 09:20:14.614', 'PENDING'),
('e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '42c2a83d-fb58-4111-9834-57410f6172dd'::uuid, '2022-05-25 08:17:45.398', 'PENDING');