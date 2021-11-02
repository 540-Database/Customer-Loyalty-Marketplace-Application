--Part B Queries
-- 1.List all customers that are not part of Brand02’s program
select CUSTOMERID
from WALLET
where CUSTOMERID not in
      (select CUSTOMERID
       from WALLET
       where BRANDID = 'Brand02');

-- 2. List customers that have joined a loyalty program but have not participated in any activity
-- in that program (list the customerid and the loyalty program id).
select CUSTOMERID, LOYALTY_PROGRAM_ID
from WALLET
where TOTALPOINTS = 0;
-- 3. List the rewards that are part of Brand01 loyalty program
select REWARDNAME
from REWARD
where REWARDID in
      (select REWARDID
       from LOYALTY_PROGRAM_HAS_REWARD
       where LOYALTY_PROGRAM_ID =
             (select LOYALTY_PROGRAM_ID
              from REGULARLOYALTYPROGRAM
              where BRANDID = 'Brand01')
      );
-- 4. List all the loyalty programs that include “refer a friend” as an activity in at least one of
-- their reward rules.
select LOYALTY_PROGRAM_ID
from RERULES
where ACTIVITYID =
      (select ACTIVITYID
       from ACTIVITY
       where ACTIVITYNAME = 'Refer a friend');
-- 5. For Brand01, list for each activity type in their loyalty program, the number instances that
-- have occurred.
select A.ACTIVITYNAME, count(*)
from CUSTOMERACTIVITIES C,
     ACTIVITY A
where C.BrandID = 'Brand01'
  and C.ACTIVITYID = A.ACTIVITYID
  and C.ACTIVITYID in (select ACTIVITYID
                       from LOYALTY_PROGRAM_HAS_ACTIVITY
                       where LOYALTY_PROGRAM_ID = (select LOYALTY_PROGRAM_ID
                                                   from REGULARLOYALTYPROGRAM
                                                   where BRANDID = 'Brand01'))
group by A.ACTIVITYID, A.ACTIVITYNAME;

-- 6. List customers of Brand01 that have redeemed at least twice.
select CUSTOMER.CUSTOMERID, CUSTOMER.NAME
from REDEEMRECORD,
     CUSTOMER
where BRANDID = 'Brand01'
  and CUSTOMER.CUSTOMERID = REDEEMRECORD.CUSTOMERID
group by CUSTOMER.CUSTOMERID, CUSTOMER.NAME
having count(*) > 1;
-- 7. All brands where total number of points redeemed overall is less than 500 points
select BRANDID
from REDEEMRECORD
group by BRANDID
having sum(POINTREDEEMED) < 500;
-- 8. For Customer C0003, and Brand02, number of activities they have done in the period of
-- 08/1/2021 and 9/30/2021.
select count(*)
from CUSTOMERACTIVITIES
where CUSTOMERID = 'C0003'
  and BRANDID = 'Brand02'
  and ACTIVITYDATE >= to_date('08/01/2021', 'mm/dd/yyyy')
  and ACTIVITYDATE <= to_date('09/30/2021', 'mm/dd/yyyy');