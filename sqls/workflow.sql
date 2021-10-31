--Login
select ADMINID, PASSWORD
from ADMIN;

select BRANDID, PASSWORD
from BRAND;

select CUSTOMERID, PASSWORD
from CUSTOMER;

--Sign UP
insert into BRAND(brandid, password)
values (1111111, 2222222);
insert into CUSTOMER(CUSTOMERID, password)
values (1111111, 2222222);
insert into BRAND(brandid, password, name, address, joindate)
VALUES
--showQueries

--Add Brand
insert into BRAND(brandid, password, name, address, joindate)
VALUES (111, 2222, 'brand name', 'brand address', sysdate);

--Add Customer
insert into CUSTOMER(customerid, password, name, phone, address)
VALUES (111, 222, 'customer name', 'customer phone', 'customer address');

--Show Brand's Info
select *
from BRAND
where BRANDID = 111;

--Show Customer's Info
select *
from CUSTOMER
where CUSTOMERID = 111;

--Add Activity Type
insert into ACTIVITY(activityid, activityname)
VALUES (111, 'activity name');

--Add reward type
insert into REWARD(rewardid, rewardname)
values (111, 'reward name');


--Add Loyalty Program
--Regular Program
insert into REGULARLOYALTYPROGRAM(loyalty_program_id, brandid, state)
VALUES (111, 111, 0);
--Tier Program
insert into REGULARLOYALTYPROGRAM(loyalty_program_id, brandid, state)
VALUES (111, 111, 1);

--Add Activity Types
insert into LOYALTY_PROGRAM_HAS_ACTIVITY (LOYALTY_PROGRAM_ID, ACTIVITYID)
values (111, 111);

--Add Reward Types
insert into LOYALTY_PROGRAM_HAS_REWARD(loyalty_program_id, rewardid, quantity)
VALUES (111, 1, 100);

--Tier Set Up
insert into TIEREDPROGRAM(loyalty_program_id, levelnumber, pointsrequired, levelname)
VALUES (111, 0, 0, 'silver');
insert into TIEREDPROGRAM(loyalty_program_id, levelnumber, pointsrequired, levelname)
VALUES (111, 1, 100, 'gold');
insert into TIEREDPROGRAM(loyalty_program_id, levelnumber, pointsrequired, levelname)
VALUES (111, 2, 1000, 'platinum');

--Add RERules
insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('recode111111', 1, 1, 2, 100, 1, 1);

--Update RERules
update RERULES
set status=0
where RECODE = 'recode111111'
  and VERSIONNUMBER = 1;

insert into RERULES(recode, brandid, loyalty_program_id, activityid, points, versionnumber, status)
VALUES ('recode111111', 1, 1, 2, 1000, 2, 1);


--Add RRRules
insert into RRRULES(rrcode, loyalty_program_id, brandid, rewardid, points, status, versionnum)
VALUES ('rrcode111111', 1, 1, 1, 1000, 1, 1);

--Update RRRules
update RRRULES
set STATUS=0
where RRCODE = 'rrcode111111'
  and VERSIONNUM = 1;

insert into RRRULES (RRCODE, LOYALTY_PROGRAM_ID, BRANDID, REWARDID, POINTS, STATUS, VERSIONNUM)
values ('rrcode111111', 1, 1, 1, 2000, 1, 2);

--Enroll in Loyalty Program
insert into CUSTOMERACTIVITIES(customeractivityid, customerid, activityid, pointsearned, activitydate)
VALUES (111, 1, 1, 0, sysdate);

insert into WALLET(customerid, loyalty_program_id, brandid, points, totalpoints)
VALUES (1, 1, 1, 0, 0);

--Purchase
insert into CUSTOMERACTIVITIES(customeractivityid, customerid, activityid, pointsearned, activitydate)
VALUES (222, 1, 2, 100, sysdate);

select points,
       RECODE,
       VERSIONNUMBER
from RERULES
where LOYALTY_PROGRAM_ID = 1
  and STATUS = 1
  and ACTIVITYID = 2;

insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount,
                           purchasedate, recode, versionnumber)
VALUES (222, 1, 1, 100, 100, 0, 100, sysdate, 'recode111111', 1);

update WALLET
set POINTS=POINTS + 100,
    TOTALPOINTS=TOTALPOINTS + 100
where CUSTOMERID = 222
  and LOYALTY_PROGRAM_ID = 1;

--Leave a Review
insert into CUSTOMERACTIVITIES(customeractivityid, customerid, activityid, pointsearned, activitydate)
VALUES (222, 1, 3, 50, sysdate);

select points,
       RECODE,
       VERSIONNUMBER
from RERULES
where LOYALTY_PROGRAM_ID = 1
  and STATUS = 1
  and ACTIVITYID = 3;

insert into REVIEWRECORD(customeractivityid, customerid, brandid, review, reviewdate, recode, versionnumber)
VALUES (222, 1, 1, 'review content', sysdate, 'recode2222222', 1);

update WALLET
set POINTS     = points + 50,
    TOTALPOINTS=TOTALPOINTS + 50
where CUSTOMERID = 1
  and LOYALTY_PROGRAM_ID = 1;


--Refer
insert into CUSTOMERACTIVITIES(customeractivityid, customerid, activityid, pointsearned, activitydate)
VALUES (333, 1, 4, 70, sysdate);

select points,
       RECODE,
       VERSIONNUMBER
from RERULES
where LOYALTY_PROGRAM_ID = 1
  and STATUS = 1
  and ACTIVITYID = 4;

insert into REFERRECORD(customeractivityid, customerid, brandid, refercustomerid, referdate, recode, versionnumber)
VALUES (333, 1, 1, 2, sysdate, 'recode333333', 1);

update WALLET
set POINTS     = POINTS + 70,
    TOTALPOINTS=TOTALPOINTS + 70
where CUSTOMERID = 1
  and LOYALTY_PROGRAM_ID = 1;

--View Wallet
select *
from WALLET
where CUSTOMERID = 1;

--Redeem Points
select points, TOTALPOINTS
from WALLET
where CUSTOMERID = 1
  and LOYALTY_PROGRAM_ID = 1;

select REWARDID
from LOYALTY_PROGRAM_HAS_REWARD
where LOYALTY_PROGRAM_ID = 1;

select REWARDNAME
from REWARD
where REWARDID = 111;

select POINTS, RRCODE, VERSIONNUM
from RRRULES
where LOYALTY_PROGRAM_ID = 1
  and STATUS = 1
  and REWARDID = 111;

insert into REDEEMRECORD(customerid, brandid, pointredeemed, rrcode, versionnum, redeemdate, rewardid, quantity)
VALUES (1, 1, 100, 'rrcode01', 1, sysdate, 111, 1);


--这里需要先check钱包里有没有同类的，没有就insert,有就update
insert into WALLETREWARDS(customerid, brandid, rewardid, quantity)
VALUES (1, 1, 1, 1);

update WALLETREWARDS
set QUANTITY = QUANTITY + 1
where CUSTOMERID = 1
  and BRANDID = 1
  and REWARDID = 1;

update WALLET
set POINTS=POINTS - 100,
    TOTALPOINTS=TOTALPOINTS - 100
where LOYALTY_PROGRAM_ID = 1
  and CUSTOMERID = 1;

update LOYALTY_PROGRAM_HAS_REWARD
set QUANTITY = QUANTITY - 1
where LOYALTY_PROGRAM_ID = 1
  and REWARDID = 1;


--Part B Queries
-- 1.List all customers that are not part of Brand02’s program
select CUSTOMERID
from WALLET
where CUSTOMERID !=
      (select CUSTOMERID
       from WALLET
       where BRANDID = 2);

-- 2. List customers that have joined a loyalty program but have not participated in any activity
-- in that program (list the customerid and the loyalty program id).
select CUSTOMERID, LOYALTY_PROGRAM_ID
from WALLET
where TOTALPOINTS = 0;
-- 3. List the rewards that are part of Brand01 loyalty program
select REWARDNAME
from REWARD
where REWARDID =
      (select REWARDID
       from LOYALTY_PROGRAM_HAS_REWARD
       where LOYALTY_PROGRAM_ID =
             (select LOYALTY_PROGRAM_ID
              from REGULARLOYALTYPROGRAM
              where BRANDID = 1)
      );
-- 4. List all the loyalty programs that include “refer a friend” as an activity in at least one of
-- their reward rules.
select distinct(LOYALTY_PROGRAM_ID)
from RERULES
where ACTIVITYID =
      (select ACTIVITYID
       from ACTIVITY
       where ACTIVITYNAME = 'Refer a friend');
-- 5. For Brand01, list for each activity type in their loyalty program, the number instances that
-- have occurred.
select count(*)
from CUSTOMERACTIVITIES
where BrandID = 1
  and ACTIVITYID in (select ACTIVITYID
                     from LOYALTY_PROGRAM_HAS_ACTIVITY
                     where LOYALTY_PROGRAM_ID = (select LOYALTY_PROGRAM_ID
                                                 from REGULARLOYALTYPROGRAM
                                                 where BRANDID = 1))
group by ACTIVITYID;

-- 6. List customers of Brand01 that have redeemed at least twice.
select CUSTOMERID
from REDEEMRECORD
where BRANDID = 1
group by CUSTOMERID
having count(*) > 1;
-- 7. All brands where total number of points redeemed overall is less than 500 points
select BRANDID
from REDEEMRECORD
group by BRANDID
having sum(POINTREDEEMED) < 500;
-- 8. For CustomerA, and Brand02, number of activities they have done in the period of 08/1/2021 and 9/30/2021
select count(*)
from CUSTOMERACTIVITIES
where CUSTOMERID = 1
  and BRANDID = 2
  and ACTIVITYDATE > to_date('08/01/2021', 'mm/dd/yyyy')
  and ACTIVITYDATE < to_date('09/30/2021', 'mm/dd/yyyy');