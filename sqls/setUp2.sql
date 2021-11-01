insert into ADMIN(adminid, password)
VALUES (123, 123456);

insert into REWARD(rewardid, rewardname)
VALUES ('R01', 'Gift Card');
insert into REWARD(rewardid, rewardname)
VALUES ('R02', 'Free Product');

insert into ACTIVITY(activityid, activityname)
VALUES ('A00', 'Join');
insert into ACTIVITY(activityid, activityname)
VALUES ('A01', 'Purchase');
insert into ACTIVITY(activityid, activityname)
VALUES ('A02', 'Leave a Review');
insert into ACTIVITY(activityid, activityname)
VALUES ('A03', 'Refer a friend');


-- add Brand
insert into BRAND(brandid, password, name, address, joindate)
values ('Brand01', 123456, 'Brand X', '503 Rolling Creek Dr Austin, AR', to_date('04/01/2021', 'mm/dd/yyyy'));

insert into BRAND(brandid, password, name, address, joindate)
values ('Brand02', 123456, 'Brand Y', '939 Orange Ave Coronado, CA', to_date('03/25/2021', 'mm/dd/yyyy'));

insert into BRAND(brandid, password, name, address, joindate)
values ('Brand03', 123456, 'Brand Z', '20 Roszel Rd Princeton, NJ', to_date('05/08/2021', 'mm/dd/yyyy'));

-- add loyalty program
insert into REGULARLOYALTYPROGRAM(loyalty_program_id, loyalty_program_name, brandid, istiered)
VALUES ('TLP01', 'SportGoods', 'Brand01', 1);
insert into TIEREDPROGRAM(loyalty_program_id, levelnumber, pointsrequired, levelname, multiplier)
VALUES ('TLP01', 1, 170, 'Bronze', 2);
insert into TIEREDPROGRAM(loyalty_program_id, levelnumber, pointsrequired, levelname, multiplier)
VALUES ('TLP01', 2, 270, 'Silver', 3);
insert into TIEREDPROGRAM(loyalty_program_id, levelnumber, pointsrequired, levelname, multiplier)
VALUES ('TLP01', 3, 450, 'Gold', 4);

insert into REGULARLOYALTYPROGRAM(loyalty_program_id, loyalty_program_name, brandid, istiered)
VALUES ('TLP02', 'MegaCenter', 'Brand02', 1);
insert into TIEREDPROGRAM(loyalty_program_id, levelnumber, pointsrequired, levelname, multiplier)
VALUES ('TLP02', 1, 210, 'Special', 2);
insert into TIEREDPROGRAM(loyalty_program_id, levelnumber, pointsrequired, levelname, multiplier)
VALUES ('TLP02', 2, 500, 'Premium', 3);

insert into REGULARLOYALTYPROGRAM(loyalty_program_id, loyalty_program_name, brandid, istiered)
VALUES ('RLP01', 'TechSups', 'Brand03', 0);

--add loyalty program has activity
insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
values ('TLP01', 'A01');
insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
values ('TLP01', 'A02');


insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
values ('TLP02', 'A01');
insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
values ('TLP02', 'A03');


insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
values ('RLP01', 'A03');

--add loyalty program has reward
insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES ('TLP01', 'R01', 40);
insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES ('TLP01', 'R02', 25);

insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES ('TLP02', 'R01', 30);
insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES ('TLP02', 'R02', 50);

insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES ('RLP01', 'R01', 25);

--add RE rules
insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('B01RE01', 'Brand01', 'TLP01', 'A01', 15, 1, 1);
insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('B01RE02', 'Brand01', 'TLP01', 'A02', 10, 1, 1);

insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('B02RE01', 'Brand02', 'TLP02', 'A01', 40, 1, 1);
insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('B02RE02', 'Brand02', 'TLP02', 'A03', 30, 1, 1);

insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('B03RE01', 'Brand03', 'RLP01', 'A03', 10, 1, 1);

--add RR rules
insert into RRRULES (RRCODE, LOYALTY_PROGRAM_ID, BRANDID, REWARDID, POINTS, STATUS, VERSIONNUM)
values ('B01RR01', 'TLP01', 'Brand01', 'R01', 80, 1, 1);
insert into RRRULES (RRCODE, LOYALTY_PROGRAM_ID, BRANDID, REWARDID, POINTS, STATUS, VERSIONNUM)
values ('B01RR02', 'TLP01', 'Brand01', 'R02', 70, 1, 1);

insert into RRRULES (RRCODE, LOYALTY_PROGRAM_ID, BRANDID, REWARDID, POINTS, STATUS, VERSIONNUM)
values ('B02RR01', 'TLP02', 'Brand02', 'R01', 120, 1, 1);
insert into RRRULES (RRCODE, LOYALTY_PROGRAM_ID, BRANDID, REWARDID, POINTS, STATUS, VERSIONNUM)
values ('B02RR02', 'TLP02', 'Brand02', 'R02', 90, 1, 1);

insert into RRRULES (RRCODE, LOYALTY_PROGRAM_ID, BRANDID, REWARDID, POINTS, STATUS, VERSIONNUM)
values ('B03RR01', 'RLP01', 'Brand03', 'R01', 100, 1, 1);

-- add customer
insert into CUSTOMER(customerid, password, name, phone, address, walletid)
VALUES ('C0001', 123456, 'Peter Parker', '8636368778', '20 Ingram Street, NY', 'W0001');
insert into CUSTOMER(customerid, password, name, phone, address, walletid)
VALUES ('C0002', 123456, 'Steve Rogers', '8972468552', '569 Leaman Place, NY', 'W0002');
insert into CUSTOMER(customerid, password, name, phone, address, walletid)
VALUES ('C0003', 123456, 'Diana Prince', '8547963210', '1700 Broadway St, NY', 'W0003');
insert into CUSTOMER(customerid, password, name, phone, address, walletid)
VALUES ('C0004', 123456, 'Billy Batson', '8974562583', '5015 Broad St, Philadelphia, PA', 'W0004');
insert into CUSTOMER(customerid, password, name, phone, address, walletid)
VALUES ('C0005', 123456, 'Tony Stark', '8731596464', '10880 Malibu Point, CA', 'W0005');

-- Join
-- C0001
insert into WALLET(walletid, customerid, loyalty_program_id, brandid, points, totalpoints, levelnumber)
VALUES ('W0001', 'C0001', 'TLP01', 'Brand01', 0, 0, 0);
insert into WALLET(walletid, customerid, loyalty_program_id, brandid, points, totalpoints, levelnumber)
VALUES ('W0001', 'C0001', 'TLP02', 'Brand02', 0, 0, 0);

insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (1, 'C0001', 'Brand01', 'A00', 0, to_date('6/10/2021', 'mm/dd/yyyy'));
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (2, 'C0001', 'Brand02', 'A00', 0, to_date('8/9/2021', 'mm/dd/yyyy'));

-- Join
-- C0002
insert into WALLET(walletid, customerid, loyalty_program_id, brandid, points, totalpoints, levelnumber)
VALUES ('W0002', 'C0002', 'TLP01', 'Brand01', 0, 0, 0);
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (3, 'C0002', 'Brand01', 'A00', 0, to_date('7/2/2021', 'mm/dd/yyyy'));

-- Join
-- C0003
insert into WALLET(walletid, customerid, loyalty_program_id, brandid, points, totalpoints, levelnumber)
VALUES ('W0003', 'C0003', 'TLP02', 'Brand02', 0, 0, 0);
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (4, 'C0003', 'Brand02', 'A00', 0, to_date('8/1/2021', 'mm/dd/yyyy'));

insert into WALLET(walletid, customerid, loyalty_program_id, brandid, points, totalpoints, levelnumber)
VALUES ('W0003', 'C0003', 'RLP01', 'Brand03', 0, 0, 0);
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (5, 'C0003', 'Brand03', 'A00', 0, to_date('7/30/2021', 'mm/dd/yyyy'));

-- Join
-- C0005
insert into WALLET(walletid, customerid, loyalty_program_id, brandid, points, totalpoints, levelnumber)
VALUES ('W0005', 'C0005', 'TLP01', 'Brand01', 0, 0, 0);
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (6, 'C0005', 'Brand01', 'A00', 0, to_date('8/10/2021', 'mm/dd/yyyy'));

insert into WALLET(walletid, customerid, loyalty_program_id, brandid, points, totalpoints, levelnumber)
VALUES ('W0005', 'C0005', 'TLP02', 'Brand02', 0, 0, 0);
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (7, 'C0005', 'Brand02', 'A00', 0, to_date('10/10/2021', 'mm/dd/yyyy'));

insert into WALLET(walletid, customerid, loyalty_program_id, brandid, points, totalpoints, levelnumber)
VALUES ('W0005', 'C0005', 'RLP01', 'Brand03', 0, 0, 0);
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (8, 'C0005', 'Brand03', 'A00', 0, to_date('9/16/2021', 'mm/dd/yyyy'));

-- C0001
-- purchase at Brand01
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (9, 'C0001', 'Brand01', 'A01', 60, to_date('6/10/2021', 'mm/dd/yyyy'));
insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (9, 'C0001', 'Brand01', 400, 60, 0, 400, to_date('6/10/2021', 'mm/dd/yyyy'), 'B01RE01', 1);

update WALLET
set POINTS=POINTS + 60,
    TOTALPOINTS=TOTALPOINTS + 60
where WALLETID = 'W0001'
  and BRANDID = 'Brand01';

-- C0001
-- Leave a Review at Brand01
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (10, 'C0001', 'Brand01', 'A02', 20, to_date('6/18/2021', 'mm/dd/yyyy'));
insert into REVIEWRECORD(customeractivityid, customerid, brandid, review, reviewdate, recode, versionnumber)
VALUES (10, 'C0001', 'Brand01', 'C0001 Review at Brand01', to_date('6/18/2021', 'mm/dd/yyyy'), 'B01RE02', 1);
update WALLET
set POINTS=POINTS + 20,
    TOTALPOINTS=TOTALPOINTS + 20
where WALLETID = 'W0001'
  and BRANDID = 'Brand01';

-- C0001
-- purchase at Brand02
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (11, 'C0001', 'Brand02', 'A01', 80, to_date('8/9/2021', 'mm/dd/yyyy'));
insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (11, 'C0001', 'Brand02', 200, 80, 0, 200, to_date('8/9/2021', 'mm/dd/yyyy'), 'B02RE01', 1);
update WALLET
set POINTS=POINTS + 80,
    TOTALPOINTS=TOTALPOINTS + 80
where WALLETID = 'W0001'
  and BRANDID = 'Brand02';

insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (12, 'C0001', 'Brand02', 'A01', 40, to_date('8/15/2021', 'mm/dd/yyyy'));
insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (12, 'C0001', 'Brand02', 100, 40, 0, 100, to_date('8/15/2021', 'mm/dd/yyyy'), 'B02RE01', 1);
update WALLET
set POINTS=POINTS + 40,
    TOTALPOINTS=TOTALPOINTS + 40
where WALLETID = 'W0001'
  and BRANDID = 'Brand02';

-- C0001
-- refer at Brand02
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (13, 'C0001', 'Brand02', 'A03', 30, to_date('10/3/2021', 'mm/dd/yyyy'));
insert into CUSTOMERACTIVITIES(CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate)
VALUES (14, 'C0001', 'Brand02', 'A03', 60, to_date('10/18/2021', 'mm/dd/yyyy'));
update WALLET
set POINTS=POINTS + 90,
    TOTALPOINTS=TOTALPOINTS + 90
where WALLETID = 'W0001'
  and BRANDID = 'Brand02';

-- C0002
-- purchase at Brand01
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (15, 'C0002', 'Brand01', 'A01', 30, to_date('7/2/2021', 'mm/dd/yyyy'));
insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (15, 'C0002', 'Brand01', 200, 30, 0, 200, to_date('7/2/2021', 'mm/dd/yyyy'), 'B01RE01', 1);

insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (16, 'C0002', 'Brand01', 'A01', 30, to_date('7/8/2021', 'mm/dd/yyyy'));
insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (16, 'C0002', 'Brand01', 200, 30, 0, 200, to_date('7/8/2021', 'mm/dd/yyyy'), 'B01RE01', 1);

update WALLET
set POINTS=POINTS + 60,
    TOTALPOINTS=TOTALPOINTS + 60
where WALLETID = 'W0002'
  and BRANDID = 'Brand01';

-- C0002
-- review at Brand01
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (17, 'C0002', 'Brand01', 'A02', 10, to_date('8/5/2021', 'mm/dd/yyyy'));
insert into REVIEWRECORD(customeractivityid, customerid, brandid, review, reviewdate, recode, versionnumber)
VALUES (17, 'C0002', 'Brand01', 'C0002 Review at Brand01', to_date('8/5/2021', 'mm/dd/yyyy'), 'B01RE02', 1);
update WALLET
set POINTS=POINTS + 10,
    TOTALPOINTS=TOTALPOINTS + 10
where WALLETID = 'W0002'
  and BRANDID = 'Brand01';

-- C0003
-- refer at Brand03
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (18, 'C0003', 'Brand03', 'A03', 40, to_date('7/30/2021', 'mm/dd/yyyy'));
update WALLET
set POINTS      = POINTS + 40,
    TOTALPOINTS = TOTALPOINTS + 40
where WALLETID = 'W0003'
  and BRANDID = 'Brand03';

-- C0003
-- purchase at Brand02
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (19, 'C0003', 'Brand02', 'A01', 40, to_date('8/1/2021', 'mm/dd/yyyy'));
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (20, 'C0003', 'Brand02', 'A01', 80, to_date('8/10/2021', 'mm/dd/yyyy'));
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (21, 'C0003', 'Brand02', 'A01', 20, to_date('9/2/2021', 'mm/dd/yyyy'));

insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (19, 'C0003', 'Brand02', 100, 40, 0, 100, to_date('8/1/2021', 'mm/dd/yyyy'), 'B02RE01', 1);
insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (20, 'C0003', 'Brand02', 200, 80, 0, 200, to_date('8/10/2021', 'mm/dd/yyyy'), 'B02RE01', 1);
insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (21, 'C0003', 'Brand02', 50, 20, 0, 50, to_date('9/2/2021', 'mm/dd/yyyy'), 'B02RE01', 1);

update WALLET
set POINTS      = POINTS + 140,
    TOTALPOINTS = TOTALPOINTS + 140
where WALLETID = 'W0003'
  and BRANDID = 'Brand02';

-- C0003
-- purchase at Brand02
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (22, 'C0003', 'Brand02', 'A03', 30, to_date('10/1/2021', 'mm/dd/yyyy'));
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (23, 'C0003', 'Brand02', 'A03', 30, to_date('10/16/2021', 'mm/dd/yyyy'));
update WALLET
set POINTS      = POINTS + 60,
    TOTALPOINTS = TOTALPOINTS + 60
where WALLETID = 'W0003'
  and BRANDID = 'Brand02';

-- C0005
-- purchase at Brand01
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (24, 'C0005', 'Brand01', 'A01', 90, to_date('8/10/2021', 'mm/dd/yyyy'));
insert into PURCHASERECORD (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, MONEYSPENT, POINTSEARNED, GIFTCARDUSED,
                            TOTALAMOUNT, PURCHASEDATE, RECODE, VERSIONNUMBER)
values (24, 'C0005', 'Brand01', 600, 90, 0, 600, to_date('8/10/2021', 'mm/dd/yyyy'), 'B01RE01', 1);
update WALLET
set POINTS     = POINTS + 90,
    TOTALPOINTS=TOTALPOINTS + 90
where WALLETID = 'W0005'
  and BRANDID = 'Brand01';

-- C0005
-- refer at Brand03
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (25, 'C0005', 'Brand03', 'A03', 30, to_date('9/16/2021', 'mm/dd/yyyy'));
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (26, 'C0005', 'Brand03', 'A03', 20, to_date('9/30/2021', 'mm/dd/yyyy'));
update WALLET
set POINTS     = POINTS + 50,
    TOTALPOINTS=TOTALPOINTS + 50
where WALLETID = 'W0005'
  and BRANDID = 'Brand03';

-- C0005
-- review at Brand01
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (27, 'C0005', 'Brand01', 'A02', 50, to_date('9/30/2021', 'mm/dd/yyyy'));
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (28, 'C0005', 'Brand01', 'A02', 30, to_date('10/15/2021', 'mm/dd/yyyy'));

insert into REVIEWRECORD (customeractivityid, customerid, brandid, review, reviewdate, recode, versionnumber)
values (27, 'C0005', 'Brand01', 'C0005 Review at Brand01', to_date('9/30/2021', 'mm/dd/yyyy'), 'B01RE02', 1);
insert into REVIEWRECORD (customeractivityid, customerid, brandid, review, reviewdate, recode, versionnumber)
values (28, 'C0005', 'Brand01', 'C0005 Review at Brand01', to_date('10/15/2021', 'mm/dd/yyyy'), 'B01RE02', 1);

update WALLET
set POINTS     = POINTS + 80,
    TOTALPOINTS=TOTALPOINTS + 80
where WALLETID = 'W0005'
  and BRANDID = 'Brand01';

-- C0005
-- purchase at Brand02
insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)
values (29, 'C0005', 'Brand02', 'A01', 160, to_date('10/10/2021', 'mm/dd/yyyy'));
insert into PURCHASERECORD (customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused,
                            totalamount, purchasedate, recode, versionnumber)
values (29, 'C0005', 'Brand02', 400, 160, 0, 400, to_date('10/10/2021', 'mm/dd/yyyy'), 'B02RE01', 1);
update WALLET
set POINTS     = POINTS + 160,
    TOTALPOINTS=TOTALPOINTS + 160
where WALLETID = 'W0005'
  and BRANDID = 'Brand02';


-- C0001
-- redeem at Brand01
insert into REDEEMRECORD (CUSTOMERID, BRANDID, POINTREDEEMED, RRCODE, VERSIONNUM, REDEEMDATE, REWARDID, QUANTITY)
values ('C0001', 'Brand01', 80, 'B01RR01', 1, to_date('7/2/2021', 'mm/dd/yyyy'), 'R01', 1);
insert into WALLETREWARDS (CUSTOMERID, BRANDID, REWARDID, QUANTITY)
values ('C0001', 'Brand01', 'R01', 1);
update WALLET
set POINTS = POINTS - 80
where WALLETID = 'W0001'
  and BRANDID = 'Brand01';

-- C0001
-- redeem at Brand02
insert into REDEEMRECORD (CUSTOMERID, BRANDID, POINTREDEEMED, RRCODE, VERSIONNUM, REDEEMDATE, REWARDID, QUANTITY)
values ('C0001', 'Brand02', 120, 'B02RR01', 1, to_date('8/25/2021', 'mm/dd/yyyy'), 'R01', 1);
insert into WALLETREWARDS (CUSTOMERID, BRANDID, REWARDID, QUANTITY)
values ('C0001', 'Brand02', 'R01', 1);
update WALLET
set POINTS = POINTS - 120
where WALLETID = 'W0001'
  and BRANDID = 'Brand02';

insert into REDEEMRECORD (CUSTOMERID, BRANDID, POINTREDEEMED, RRCODE, VERSIONNUM, REDEEMDATE, REWARDID, QUANTITY)
values ('C0001', 'Brand02', 90, 'B02RR01', 1, to_date('10/20/2021', 'mm/dd/yyyy'), 'R02', 1);
insert into WALLETREWARDS (CUSTOMERID, BRANDID, REWARDID, QUANTITY)
values ('C0001', 'Brand02', 'R02', 1);
update WALLET
set POINTS = POINTS - 90
where WALLETID = 'W0001'
  and BRANDID = 'Brand02';


-- C0002
-- redeem at Brand01
insert into REDEEMRECORD (CUSTOMERID, BRANDID, POINTREDEEMED, RRCODE, VERSIONNUM, REDEEMDATE, REWARDID, QUANTITY)
values ('C0002', 'Brand01', 70, 'B01RR02', 1, to_date('9/1/2021', 'mm/dd/yyyy'), 'R02', 1);
insert into WALLETREWARDS (CUSTOMERID, BRANDID, REWARDID, QUANTITY)
values ('C0002', 'Brand01', 'R02', 1);
update WALLET
set POINTS = POINTS - 70
where WALLETID = 'W0002'
  and BRANDID = 'Brand01';


-- C0003
-- redeem at Brand02
insert into REDEEMRECORD (CUSTOMERID, BRANDID, POINTREDEEMED, RRCODE, VERSIONNUM, REDEEMDATE, REWARDID, QUANTITY)
values ('C0003', 'Brand02', 90, 'B02RR02', 1, to_date('8/26/2021', 'mm/dd/yyyy'), 'R02', 1);
insert into WALLETREWARDS (CUSTOMERID, BRANDID, REWARDID, QUANTITY)
values ('C0003', 'Brand02', 'R02', 1);

insert into REDEEMRECORD (CUSTOMERID, BRANDID, POINTREDEEMED, RRCODE, VERSIONNUM, REDEEMDATE, REWARDID, QUANTITY)
values ('C0003', 'Brand02', 90, 'B02RR02', 1, to_date('10/18/2021', 'mm/dd/yyyy'), 'R02', 1);
update WALLETREWARDS
set QUANTITY=QUANTITY + 1
where CUSTOMERID = 'C0003'
  and BRANDID = 'Brand02'
  and REWARDID = 'R02';

update WALLET
set POINTS = POINTS - 180
where WALLETID = 'W0003'
  and BRANDID = 'Brand02';

-- C0005
-- redeem at Brand02
insert into REDEEMRECORD (CUSTOMERID, BRANDID, POINTREDEEMED, RRCODE, VERSIONNUM, REDEEMDATE, REWARDID, QUANTITY)
values ('C0005', 'Brand02', 120, 'B02RR01', 1, to_date('10/11/2021', 'mm/dd/yyyy'), 'R01', 1);
insert into WALLETREWARDS (CUSTOMERID, BRANDID, REWARDID, QUANTITY)
values ('C0005', 'Brand02', 'R01', 1);
update WALLET
set POINTS = POINTS - 120
where WALLETID = 'W0005'
  and BRANDID = 'Brand02';

-- C0005
-- redeem at Brand01
insert into REDEEMRECORD (CUSTOMERID, BRANDID, POINTREDEEMED, RRCODE, VERSIONNUM, REDEEMDATE, REWARDID, QUANTITY)
values ('C0005', 'Brand01', 80, 'B02RR01', 1, to_date('10/11/2021', 'mm/dd/yyyy'), 'R01', 1);
insert into WALLETREWARDS (CUSTOMERID, BRANDID, REWARDID, QUANTITY)
values ('C0005', 'Brand01', 'R01', 1);

insert into REDEEMRECORD (CUSTOMERID, BRANDID, POINTREDEEMED, RRCODE, VERSIONNUM, REDEEMDATE, REWARDID, QUANTITY)
values ('C0005', 'Brand01', 70, 'B02RR02', 1, to_date('10/17/2021', 'mm/dd/yyyy'), 'R02', 1);
insert into WALLETREWARDS (CUSTOMERID, BRANDID, REWARDID, QUANTITY)
values ('C0005', 'Brand01', 'R02', 1);
update WALLET
set POINTS = POINTS - 150
where WALLETID = 'W0005'
  and BRANDID = 'Brand01';
