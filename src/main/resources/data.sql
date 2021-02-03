-- ALTER DATABASE mytestdb CHARACTER SET utf8 COLLATE utf8_general_cs;
REPLACE INTO `roles` VALUES (1,'ADMIN');
REPLACE INTO `roles` VALUES (2,'USER');

-- insert Ukraine regions
REPLACE INTO `ukraine_regions` VALUES (1, 'місто КИЇВ');
REPLACE INTO `ukraine_regions` VALUES (2, 'Вінницька область');
REPLACE INTO `ukraine_regions` VALUES (3, 'Волинська область');
REPLACE INTO `ukraine_regions` VALUES (4, 'Дніпропетровська область');
REPLACE INTO `ukraine_regions` VALUES (5, 'Донецька область');
REPLACE INTO `ukraine_regions` VALUES (6, 'Житомирська область');
REPLACE INTO `ukraine_regions` VALUES (7, 'Закарпатська область');
REPLACE INTO `ukraine_regions` VALUES (8, 'Запорізька область');
REPLACE INTO `ukraine_regions` VALUES (9, 'Івано-Франківська область');
REPLACE INTO `ukraine_regions` VALUES (10, 'Київська область');
REPLACE INTO `ukraine_regions` VALUES (11, 'Кіровоградська область ');
REPLACE INTO `ukraine_regions` VALUES (12, 'Луганська область');
REPLACE INTO `ukraine_regions` VALUES (13, 'Львівська область');
REPLACE INTO `ukraine_regions` VALUES (14, 'Миколаївська область');
REPLACE INTO `ukraine_regions` VALUES (15, 'місто Севастополь');
REPLACE INTO `ukraine_regions` VALUES (16, 'Одеська область');
REPLACE INTO `ukraine_regions` VALUES (17, 'Полтавська область');
REPLACE INTO `ukraine_regions` VALUES (18, 'Рівненська область');
REPLACE INTO `ukraine_regions` VALUES (19, 'Сумська область');
REPLACE INTO `ukraine_regions` VALUES (20, 'Тернопільська область');
REPLACE INTO `ukraine_regions` VALUES (21, 'Харківська область');
REPLACE INTO `ukraine_regions` VALUES (22, 'Херсонська область');
REPLACE INTO `ukraine_regions` VALUES (23, 'Хмельницька область');
REPLACE INTO `ukraine_regions` VALUES (24, 'Черкаська область');
REPLACE INTO `ukraine_regions` VALUES (25, 'Чернівецька область');
REPLACE INTO `ukraine_regions` VALUES (26, 'Чернігівська область');

-- insert ADMIN info
REPLACE INTO `users` (user_id, id_number_tax_code, first_name, last_name, middle_name, birth_date, email,
                      is_active, password, secret_word, user_name)
VALUES (1, 1234567890,  'Андрій', 'Адмінський', '', '1977-04-08 00-00-00', 'admin@enterbank.ua',
        1, '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'admin12345', 'admin');
REPLACE INTO `address` (address_id, address_line1, address_line2, city, region_id)
      VALUES (1, 'PEREYASLIV VAL, 1D','24', 'Kyiv', 1);
REPLACE INTO `user_addresses` (user_id, address_id) VALUES (1, 1);
REPLACE INTO `phone_number` (phone_id, is_primary, phone, user_id) VALUES (1, 1, '502421111', 1);
REPLACE INTO `passport_data` (passport_id, passport_issue_date, passport_issued_by, passport_number,
                              passport_series, user_id)
      VALUES (1, '2015-08-08 00-00-00', 'Оболонським РВ ГУ ДМСУ в м. Києві', 252525, 'СО', 1);
REPLACE INTO `bank_accounts` (bank_account_id, account_amount, bank_account_number, user_id)
      VALUES (1, 53000.21, 26320130000001, 1);
REPLACE INTO `credit_card` (credit_card_id, card_name, card_number, cvc2, expire_date, is_active, issue_date,
                            owner_name, bank_account_id)
      VALUES (1, 'VISA', 1111222233334444, 111, '2021-08-08 00-00-00', 1, '2019-08-08 00-00-00',
              'Andrii Adminskii', 1);

-- insert USER info
REPLACE INTO `users` (user_id, id_number_tax_code, first_name, last_name, middle_name, birth_date, email,
                      is_active, password, secret_word, user_name)
VALUES (2, 2234567890,  'Максим', 'Юзерський', '', '1988-04-25 00-00-00', 'user1@user.ua',
        1, '$2a$10$ByIUiNaRfBKSV6urZoBBxe4UbJ/sS6u1ZaPORHF9AtNWAuVPVz1by', 'user1', 'user1');
REPLACE INTO `address` (address_id, address_line1, address_line2, city, region_id)
     VALUES (2, 'PEREYASLIV VAL, 1D','25', 'Kyiv', 1);
REPLACE INTO `user_addresses` (user_id, address_id) VALUES (2, 2);
REPLACE INTO `phone_number` (phone_id, is_primary, phone, user_id) VALUES (2, 1, '952483838', 2);
REPLACE INTO `passport_data` (passport_id, passport_issue_date, passport_issued_by, passport_number, passport_series,
                              user_id)
    VALUES (2, '2010-11-11 00-00-00', 'Оболонським РВ ГУ ДМСУ в м. Києві', 757575, 'НС', 2);
REPLACE INTO `bank_accounts` (bank_account_id, account_amount, bank_account_number, user_id)
      VALUES (2, 200.11, 26320130000002, 2);
REPLACE INTO `bank_accounts` (bank_account_id, account_amount, bank_account_number, account_type, user_id)
      VALUES (3, 25000.02, 26320130000003, 'Gold', 2);
REPLACE INTO `credit_card` (credit_card_id, card_name, card_number, cvc2, expire_date, is_active, issue_date,
                            owner_name, bank_account_id)
      VALUES (2, 'VISA', 5555222233334444, 222, '2021-06-30 23-59-59', 1, '2019-06-08 00-00-00',
              'Maxim Yuzersky', 2);
REPLACE INTO `credit_card` (credit_card_id, card_name, card_number, cvc2, expire_date, is_active, issue_date,
                            owner_name, bank_account_id)
      VALUES (3, 'VISA', 5555222233337777, 333, '2021-07-31 23-59-59', 1, '2019-07-08 00-00-00',
              'Maxim Yuzersky', 3);
REPLACE INTO `credit_card` (credit_card_id, card_name, card_number, cvc2, expire_date, is_active, issue_date,
                            owner_name, bank_account_id)
     VALUES (4, 'MASTER CARD', 5555222233338888, 444, '2021-08-31 23-59-59', 1, '2019-08-08 00-00-00',
        'Maxim Yuzersky', 3);

-- insert USER info
REPLACE INTO `users` (user_id, id_number_tax_code, first_name, last_name, middle_name, birth_date, email,
                      is_active, password, secret_word, user_name)
VALUES (3, 3234567890,  'Антон', 'Юзерський', '', '1988-03-25 00-00-00', 'user2@user.ua',
        1, '$2a$10$ByIUiNaRfBKSV6urZoBBxe4UbJ/sS6u1ZaPORHF9AtNWAuVPVz1by', 'user2', 'user2');
REPLACE INTO `address` (address_id, address_line1, address_line2, city, region_id)
    VALUES (32, 'BOHDANA KHMELNYTSKOHO, 5', '25', 'Kyiv', 1);
REPLACE INTO `user_addresses` (user_id, address_id) VALUES (3, 3);
REPLACE INTO `phone_number` (phone_id, is_primary, phone, user_id) VALUES (3, 1, '682483838', 3);
REPLACE INTO `passport_data` (passport_id, passport_issue_date, passport_issued_by, passport_number,
                              passport_series, user_id)
    VALUES (3, '2015-11-18 00-00-00', 'Оболонським РВ ГУ ДМСУ в м. Києві', 247575, 'СО', 3);
REPLACE INTO `bank_accounts` (bank_account_id, account_amount, bank_account_number, user_id)
    VALUES (4, 22200.11, 26320130000004, 3);
REPLACE INTO `credit_card` (credit_card_id, card_name, card_number, cvc2, expire_date, is_active, issue_date,
                            owner_name, bank_account_id)
    VALUES (5, 'VISA', 1212222233334444, 111, '2022-06-30 23-59-59', 1, '2020-06-08 00-00-00',
        'Anton Yuzersky', 4);
REPLACE INTO `credit_card` (credit_card_id, card_name, card_number, cvc2, expire_date, is_active, issue_date,
                            owner_name, bank_account_id)
    VALUES (6, 'VISA', 1212222233337777, 111, '2023-07-31 23-59-59', 0, '2020-07-08 00-00-00',
        'Anton Yuzersky', 4);

-- users' roles
REPLACE INTO `user_roles` (user_id, role_id) VALUES (1, 1);
REPLACE INTO `user_roles` (user_id, role_id) VALUES (2, 2);
REPLACE INTO `user_roles` (user_id, role_id) VALUES (3, 2);

