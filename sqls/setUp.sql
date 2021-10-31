insert into ADMIN(adminid, password)
VALUES (123, 123456);

insert into BRAND(brandid, password, name, address, joindate)
VALUES (1, 123456, 'brand01', 'EB', sysdate);

insert into BRAND(brandid, password, name, address, joindate)
VALUES (2, 123456, 'brand02', 'EB', sysdate);

insert into REGULARLOYALTYPROGRAM(loyalty_program_id, brandid, IsTiered)
VALUES (1, 1, 0);

insert into REGULARLOYALTYPROGRAM(loyalty_program_id, brandid, IsTiered)
VALUES (2, 2, 1);

insert into TIEREDPROGRAM (LOYALTY_PROGRAM_ID, LEVELNUMBER, POINTSREQUIRED, LEVELNAME)
values (2, 0, 0, 'silver');
insert into TIEREDPROGRAM (LOYALTY_PROGRAM_ID, LEVELNUMBER, POINTSREQUIRED, LEVELNAME)
values (2, 1, 100, 'gold');
insert into TIEREDPROGRAM (LOYALTY_PROGRAM_ID, LEVELNUMBER, POINTSREQUIRED, LEVELNAME)
values (2, 2, 1000, 'platinum');

insert into CUSTOMER(customerid, password, name, phone, address)
values (1, 123456, 'customer01', '9191234567', 'POI');

insert into CUSTOMER(customerid, password, name, phone, address)
values (2, 123456, 'customer02', '9197654321', 'qwe');

insert into ACTIVITY(activityid, activityname)
VALUES (1, 'Join');
insert into ACTIVITY(activityid, activityname)
VALUES (2, 'Purchase');
insert into ACTIVITY(activityid, activityname)
VALUES (3, 'Leave a Review');
insert into ACTIVITY(activityid, activityname)
VALUES (4, 'Refer a friend');

insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
VALUES (1, 1);
insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
VALUES (1, 2);
insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
VALUES (1, 3);
insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
VALUES (1, 4);

insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
VALUES (2, 1);
insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
VALUES (2, 2);
insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
VALUES (2, 3);
insert into LOYALTY_PROGRAM_HAS_ACTIVITY(loyalty_program_id, activityid)
VALUES (2, 4);

insert into REWARD(rewardid, rewardname)
VALUES (1, 'Gift Card');
insert into REWARD(rewardid, rewardname)
VALUES (2, 'Free Product');

insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES (1, 1, 10);
insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES (1, 2, 5);

insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES (2, 1, 100);
insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES (2, 2, 50);

insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('recode01', 1, 1, 2, 100, 1, 1);
insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('recode02', 1, 1, 3, 50, 1, 1);
insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('recode03', 1, 1, 4, 70, 1, 1);


--join
insert into WALLET(customerid, loyalty_program_id, brandid, points, totalpoints)
VALUES (1, 1, 1, 0, 0);

--purchase
--points:100 recode:recode01 versionnumber:1

insert into CUSTOMERACTIVITIES(customeractivityid, customerid, BRANDID, activityid, pointsearned, activitydate)
VALUES (2, 1, 1, 2, 100, sysdate);
insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (2, 1, 1, 100, 100, 0, 100, sysdate, 'recode01', 1);

--review
insert into CUSTOMERACTIVITIES(customeractivityid, customerid, BRANDID, activityid, pointsearned, activitydate)
VALUES (3, 1, 1, 3, 50, sysdate);
insert into REVIEWRECORD(customeractivityid, customerid, brandid, review, reviewdate, recode, versionnumber)
VALUES (3, 1, 1, 'review content', sysdate, 'recode02', 1);

--refer
insert into CUSTOMERACTIVITIES(customeractivityid, customerid, BRANDID, activityid, pointsearned, activitydate)
VALUES (4, 1, 1, 4, 70, sysdate);
insert into REFERRECORD(customeractivityid, customerid, brandid, refercustomerid, referdate, recode, versionnumber)
VALUES (4, 1, 1, 2, sysdate, 'recode03', 1);



insert into RRRULES(rrcode, loyalty_program_id, brandid, rewardid, points, status, versionnum)
VALUES ('rrcode01', 1, 1, 1, 100, 1, 1);
insert into RRRULES(rrcode, loyalty_program_id, brandid, rewardid, points, status, versionnum)
VALUES ('rrcode02', 1, 1, 2, 50, 1, 1);

insert into REDEEMRECORD(customerid, brandid, pointredeemed, rrcode, versionnum, redeemdate, rewardid, quantity)
VALUES (1, 1, 100, 'rrcode01', 1, sysdate, 1, 1);
insert into WALLETREWARDS(CUSTOMERID, BRANDID, rewardid, quantity)
VALUES (1, 1, 1, 1);

update WALLET
set POINTS=120,
    TOTALPOINTS=120
where CUSTOMERID = 1;