create table Admin
(
    AdminId  integer,
    Password varchar(45)
);

CREATE TABLE Brand
(
    BrandID  varchar(45) NOT NULL,
    Password varchar(45) NOT NULL,
    Name     VARCHAR(45),
    Address  VARCHAR(255),
    JoinDate DATE,
    PRIMARY KEY (BrandID)
);

CREATE TABLE Customer
(
    CustomerID varchar(45) NOT NULL,
    Password   varchar(45) NOT NULL,
    Name       VARCHAR(45),
    Phone      VARCHAR(45),
    Address    VARCHAR(255),
    WalletID   varchar(45),
    PRIMARY KEY (CustomerID)
);

CREATE TABLE RegularLoyaltyProgram
(
    Loyalty_Program_ID   varchar(45) NOT NULL,
    Loyalty_Program_Name varchar(45) NOT NULL,
    BrandID              varchar(45),
    IsTiered             NUMBER(1),
    -- regular: 0
    -- tiered: 1
    foreign key (BrandID) references Brand,
    PRIMARY KEY (Loyalty_Program_ID)
);


CREATE TABLE TieredProgram
(
    Loyalty_Program_ID varchar(45) NOT NULL,
    LevelNumber        INTEGER     NOT NULL,
    PointsRequired     INTEGER,
    LevelName          VARCHAR(45),
    multiplier         number(10, 1),
    FOREIGN KEY (Loyalty_Program_ID) REFERENCES REGULARLOYALTYPROGRAM,
    PRIMARY KEY (Loyalty_Program_ID, LevelNumber)
);

-- Both activity categories and reward categories have globally unique codes (alphanumeric) and a name.
-- CREATE TABLE Categories
-- (
--     CategoryID INTEGER NOT NULL,
--     Name       VARCHAR(45),
--     PRIMARY KEY (CategoryID)
-- );

-- 我们提供
-- special activity: join
CREATE TABLE Activity
(
    ActivityID   varchar(45) NOT NULL,
    ActivityName varchar(45),
--     FOREIGN KEY (ActivityID) REFERENCES Categories,
    PRIMARY KEY (ActivityID)
);
-- purchase, leave a review, refer a friend.
CREATE TABLE Loyalty_Program_has_Activity
(
    Loyalty_Program_ID varchar(45) NOT NULL,
    ActivityID         varchar(45) NOT NULL,

    FOREIGN KEY (Loyalty_Program_ID) REFERENCES RegularLoyaltyProgram,
    FOREIGN KEY (ActivityID) REFERENCES Activity,
    PRIMARY KEY (Loyalty_Program_ID, ActivityID)
);
-- CREATE TABLE Rules
-- (
--     RuleCode VARCHAR(45) NOT NULL,
--     PRIMARY KEY (RuleCode)
-- );
--从Loyalty_Program_has_Activity里选，建规则
-- 主键怎么建？根据flow直接输入了
CREATE TABLE RERules
(
    RECode             VARCHAR(45) NOT NULL,
    BrandID            varchar(45),
    Loyalty_Program_ID varchar(45) NOT NULL,
    ActivityID         varchar(45) NOT NULL,
    Points             Integer,
    VersionNumber      Integer,
    Status             integer,
    --True: 1
    FOREIGN KEY (ActivityID) REFERENCES Activity,
--     FOREIGN KEY (RECode) REFERENCES Rules,
    foreign key (BrandID) references Brand,
    PRIMARY KEY (RECode, VersionNumber)
);


-- 我们提供
-- 只是类别

-- gift card, free product (new types may also be added later)
CREATE TABLE Reward
(
    RewardID   varchar(45) NOT NULL,
    RewardName varchar(45),
--     FOREIGN KEY (RewardCode) REFERENCES Categories,
    PRIMARY KEY (RewardID)
);

--这里，选类别，加数量
CREATE TABLE Loyalty_Program_has_Reward
(
    Loyalty_Program_ID varchar(45) NOT NULL,
    RewardID           varchar(45) NOT NULL,
    Quantity           integer,
    FOREIGN KEY (Loyalty_Program_ID) REFERENCES RegularLoyaltyProgram,
    FOREIGN KEY (RewardID) REFERENCES Reward,
    PRIMARY KEY (Loyalty_Program_ID, RewardID)
);



CREATE TABLE RRRules
(
    RRCode             VARCHAR(45) NOT NULL,
    Loyalty_Program_ID varchar(45) NOT NULL,
    BrandID            varchar(45) NOT NULL,
    RewardID           varchar(45) NOT NULL,
    Points             Integer,
    Status             NUMBER(1)   NOT NULL,
    --True: 1
    VersionNum         VARCHAR(45),
--     FOREIGN KEY (RRCode) REFERENCES Rules,
    FOREIGN KEY (RewardID) REFERENCES Reward,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (RRCode, VersionNum)
);


-- special activity: join
CREATE TABLE CustomerActivities
(
    CustomerActivityID Integer     not null,
    CustomerID         varchar(45) NOT NULL,
    BrandID            varchar(45),
    ActivityID         varchar(45) NOT NULL,
    PointsEarned       Integer,
    ActivityDate       DATE,

    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (ActivityID) REFERENCES Activity,
    FOREIGN KEY (BrandID) REFERENCES BRAND,
    PRIMARY KEY (CustomerActivityID)
);

--比如ActivityID对应的是purchase，就去purchase这张表里加具体数据
CREATE TABLE PurchaseRecord
(
    CustomerActivityID INTEGER     NOT NULL,
    CustomerID         varchar(45) NOT NULL,
    BrandID            varchar(45) NOT NULL,
    MoneySpent         INTEGER     NOT NULL,
    PointsEarned       Integer     NOT NULL,
    GiftCardUsed       INTEGER     NOT NULL,
    TotalAmount        INTEGER     NOT NULL,
    PurchaseDate       DATE        NOT NULL,
    RECode             VARCHAR(45) NOT NULL,
    VersionNumber      Integer,
    FOREIGN KEY (RECode, VersionNumber) REFERENCES RERules,
    FOREIGN KEY (CustomerActivityID) references CustomerActivities,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (CustomerActivityID)
);
create table ReviewRecord
(
    CustomerActivityID INTEGER     NOT NULL,
    CustomerID         varchar(45) NOT NULL,
    BrandID            varchar(45) NOT NULL,
    Review             varchar(45) NOT NULL,
    ReviewDate         DATE        NOT NULL,
    RECode             VARCHAR(45) NOT NULL,
    VersionNumber      Integer,
    FOREIGN KEY (RECode, VersionNumber) REFERENCES RERules,
    FOREIGN KEY (CustomerActivityID) references CustomerActivities,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (CustomerActivityID)
);
create table ReferRecord
(
    CustomerActivityID INTEGER     NOT NULL,
    CustomerID         varchar(45) NOT NULL,
    BrandID            varchar(45) NOT NULL,
    ReferCustomerID    varchar(45) NOT NULL,
    ReferDate          DATE        NOT NULL,
    RECode             VARCHAR(45) NOT NULL,
    VersionNumber      Integer,
    FOREIGN KEY (RECode, VersionNumber) REFERENCES RERules,
    FOREIGN KEY (CustomerActivityID) references CustomerActivities,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (ReferCustomerID) REFERENCES Customer,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (CustomerActivityID)
);



CREATE TABLE Wallet
(
    WalletID           varchar(45) NOT NULL,
    CustomerID         varchar(45) NOT NULL,
    Loyalty_Program_ID varchar(45) NOT NULL,
    BrandID            varchar(45) NOT NULL,
    Points             INTEGER     NOT NULL,
    TotalPoints        INTEGER     NOT NULL,
    LevelNumber        Integer,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (Loyalty_Program_ID) REFERENCES RegularLoyaltyProgram,
--     FOREIGN KEY (LevelNumber) references TieredProgram,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (WalletID, BrandID)
);
--记录每个客户兑换过的reward，消费时会用上
create table WalletRewards
(
    CustomerID varchar(45) not null,
    BrandID    varchar(45) NOT NULL,
    RewardID   varchar(45) NOT NULL,
    Quantity   integer,
    primary key (CustomerID, BrandID, RewardID)
);

CREATE TABLE RedeemRecord
(
    CustomerID    varchar(45) NOT NULL,
    BrandID       varchar(45) NOT NULL,
    PointRedeemed INTEGER     NOT NULL,
    RRCode        VARCHAR(45),
    VersionNum    VARCHAR(45),
    RedeemDate    DATE        NOT NULL,
    RewardID      varchar(45) NOT NULL,
    Quantity      integer,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    FOREIGN KEY (RRCode, VersionNum) REFERENCES RRRules,
    PRIMARY KEY (CustomerID, RedeemDate, BrandID)
);
