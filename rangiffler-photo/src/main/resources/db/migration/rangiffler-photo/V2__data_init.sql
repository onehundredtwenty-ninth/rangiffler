insert into photo (id, user_id, country_id, description, created_date)
values
('451e4a2f-6a47-4c8b-b90e-af2e91651998'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '4cca3bae-f195-11ee-9b32-0242ac110002'::uuid, 'sluntvnhd9m', '2022-03-29 11:58:07.0'),
('99a5c875-e7d5-459f-9618-69947ec27ac3'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '4cca3bae-f195-11ee-9b32-0242ac110002'::uuid, 'hfkqrvxonxk9k', '2023-12-16 15:49:15.174'),
('552f72c2-f782-4156-b174-94152a15dbf2'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '4cc91f80-f195-11ee-9b32-0242ac110002'::uuid, '92b9z7ksndae', '2021-11-23 01:34:53.988'),
('9bcd83b8-5720-44fc-a4bb-7be38fb3ae33'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '4cc91f80-f195-11ee-9b32-0242ac110002'::uuid, 'e27w6vm5hb2vg', '2024-02-24 12:13:53.647'),
('6eb64139-e581-4f4d-9667-03193eb0b4a6'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '4cc899ca-f195-11ee-9b32-0242ac110002'::uuid, '2l7b9ty71x9b6', '2023-10-27 22:51:22.76'),
('451e4a2f-6a47-4c8b-b90e-af2e91651999'::uuid, '377b04c6-da52-4ee0-947d-92847cbc83c6'::uuid, '4cca3bae-f195-11ee-9b32-0242ac110002'::uuid, 'sluntvnhd190m', '2022-03-29 11:58:07.678');

insert into "like" (id, user_id, created_date)
values
('2047e8a2-2c6e-41b9-a60e-d2f78fba80da'::uuid, 'ad01846a-7ca0-4d22-a1f7-b3e35b801f59'::uuid, '2024-03-25 03:56:53.721'),
('ac57dc45-355b-47b0-be5d-70f3c2b6d74a'::uuid, 'da0d7bb2-be8a-4f92-8704-0753d21e16f9'::uuid, '2024-03-25 03:56:53.721'),
('3ac02f44-fae7-4876-9829-3347285d3964'::uuid, 'e9202290-8ade-426b-a3b6-40ef149dbb39'::uuid, '2024-03-25 03:56:53.721');

insert into photo_like (photo_id, like_id)
values
('451e4a2f-6a47-4c8b-b90e-af2e91651998'::uuid, '2047e8a2-2c6e-41b9-a60e-d2f78fba80da'::uuid),
('451e4a2f-6a47-4c8b-b90e-af2e91651998'::uuid, 'ac57dc45-355b-47b0-be5d-70f3c2b6d74a'::uuid),
('451e4a2f-6a47-4c8b-b90e-af2e91651998'::uuid, '3ac02f44-fae7-4876-9829-3347285d3964'::uuid);