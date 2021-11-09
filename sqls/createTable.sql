-- BCNF
create table Admin
(
    AdminId  INTEGER     NOT NULL,
    Password VARCHAR(45) NOT NULL,
    PRIMARY KEY (AdminId)
);

-- BCNF
CREATE TABLE Brand
(
    BrandID  VARCHAR(45)  NOT NULL,
    Password VARCHAR(45)  NOT NULL,
    Name     VARCHAR(45)  NOT NULL,
    Address  VARCHAR(255) NOT NULL,
    JoinDate DATE         NOT NULL,
    PRIMARY KEY (BrandID)
);

-- BCNF
CREATE TABLE Customer
(
    CustomerID VARCHAR(45)  NOT NULL,
    Password   VARCHAR(45)  NOT NULL,
    Name       VARCHAR(45)  NOT NULL,
    Phone      VARCHAR(45)  NOT NULL,
    Address    VARCHAR(255) NOT NULL,
    WalletID   VARCHAR(45)  NOT NULL,
    PRIMARY KEY (CustomerID)
);

-- BCNF
CREATE TABLE RegularLoyaltyProgram
(
    Loyalty_Program_ID   VARCHAR(45) NOT NULL,
    Loyalty_Program_Name VARCHAR(45) NOT NULL,
    BrandID              VARCHAR(45) NOT NULL,
    IsTiered             NUMBER(1)   NOT NULL,
    -- regular: 0
    -- tiered: 1
    foreign key (BrandID) references Brand,
    PRIMARY KEY (Loyalty_Program_ID)
);

-- BCNF
CREATE TABLE TieredProgram
(
    Loyalty_Program_ID VARCHAR(45)   NOT NULL,
    LevelNumber        NUMBER(1)     NOT NULL,
    PointsRequired     INTEGER       NOT NULL,
    LevelName          VARCHAR(45)   NOT NULL,
    multiplier         NUMBER(10, 1) NOT NULL,
    FOREIGN KEY (Loyalty_Program_ID) REFERENCES RegularLoyaltyProgram,
    PRIMARY KEY (Loyalty_Program_ID, LevelNumber)
);

-- special activity: join
-- BCNF
CREATE TABLE Activity
(
    ActivityID   VARCHAR(45) NOT NULL,
    ActivityName VARCHAR(45) NOT NULL,
    PRIMARY KEY (ActivityID)
);

-- BCNF
-- purchase, leave a review, refer a friend.
CREATE TABLE Loyalty_Program_has_Activity
(
    Loyalty_Program_ID VARCHAR(45) NOT NULL,
    ActivityID         VARCHAR(45) NOT NULL,
    FOREIGN KEY (Loyalty_Program_ID) REFERENCES RegularLoyaltyProgram,
    FOREIGN KEY (ActivityID) REFERENCES Activity,
    PRIMARY KEY (Loyalty_Program_ID, ActivityID)
);

--从Loyalty_Program_has_Activity里选，建规则
-- 主键怎么建？根据flow直接输入了
-- BCNF
CREATE TABLE RERules
(
    RECode             VARCHAR(45) NOT NULL,
    Loyalty_Program_ID VARCHAR(45) NOT NULL,
    ActivityID         VARCHAR(45) NOT NULL,
    Points             INTEGER     NOT NULL,
    VersionNumber      INTEGER     NOT NULL,
    Status             INTEGER     NOT NULL,
    --True: 1
    FOREIGN KEY (ActivityID) REFERENCES Activity,
    foreign key (Loyalty_Program_ID) references RegularLoyaltyProgram,
    PRIMARY KEY (RECode, VersionNumber)
);

-- BCNF
-- gift card, free product
CREATE TABLE Reward
(
    RewardID   VARCHAR(45) NOT NULL,
    RewardName VARCHAR(45) NOT NULL,
    PRIMARY KEY (RewardID)
);

-- BCNF
CREATE TABLE Loyalty_Program_has_Reward
(
    Loyalty_Program_ID VARCHAR(45) NOT NULL,
    RewardID           VARCHAR(45) NOT NULL,
    Quantity           INTEGER     NOT NULL,
    FOREIGN KEY (Loyalty_Program_ID) REFERENCES RegularLoyaltyProgram,
    FOREIGN KEY (RewardID) REFERENCES Reward,
    PRIMARY KEY (Loyalty_Program_ID, RewardID)
);

-- BCNF
CREATE TABLE RRRules
(
    RRCode             VARCHAR(45) NOT NULL,
    Loyalty_Program_ID VARCHAR(45) NOT NULL,
    RewardID           VARCHAR(45) NOT NULL,
    Points             INTEGER,
    Status             NUMBER(1)   NOT NULL,
    --True: 1
    VersionNum         VARCHAR(45),
    FOREIGN KEY (RewardID) REFERENCES Reward,
    FOREIGN KEY (Loyalty_Program_ID) REFERENCES RegularLoyaltyProgram,
    PRIMARY KEY (RRCode, VersionNum)
);

-- BCNF
-- special activity: join
CREATE TABLE CustomerActivities
(
    CustomerActivityID INTEGER       NOT NULL,
    CustomerID         VARCHAR(45)   NOT NULL,
    BrandID            VARCHAR(45)   NOT NULL,
    ActivityID         VARCHAR(45)   NOT NULL,
    PointsEarned       NUMBER(10, 2) NOT NULL,
    ActivityDate       DATE          NOT NULL,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (ActivityID) REFERENCES Activity,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (CustomerActivityID)
);

-- 2NF
--比如ActivityID对应的是purchase，就去purchase这张表里加具体数据
CREATE TABLE PurchaseRecord
(
    CustomerActivityID INTEGER       NOT NULL,
    CustomerID         VARCHAR(45)   NOT NULL,
    BrandID            VARCHAR(45)   NOT NULL,
    MoneySpent         INTEGER       NOT NULL,
    PointsEarned       NUMBER(10, 2) NOT NULL,
    GiftCardUsed       INTEGER       NOT NULL,
    TotalAmount        INTEGER       NOT NULL,
    PurchaseDate       DATE          NOT NULL,
    RECode             VARCHAR(45)   NOT NULL,
    VersionNumber      INTEGER       NOT NULL,
    FOREIGN KEY (RECode, VersionNumber) REFERENCES RERules,
    FOREIGN KEY (CustomerActivityID) references CustomerActivities,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (CustomerActivityID)
);

-- BCNF
create table ReviewRecord
(
    CustomerActivityID INTEGER     NOT NULL,
    CustomerID         VARCHAR(45) NOT NULL,
    BrandID            VARCHAR(45) NOT NULL,
    Review             VARCHAR(45) NOT NULL,
    ReviewDate         DATE        NOT NULL,
    RECode             VARCHAR(45) NOT NULL,
    VersionNumber      INTEGER     NOT NULL,
    FOREIGN KEY (RECode, VersionNumber) REFERENCES RERules,
    FOREIGN KEY (CustomerActivityID) references CustomerActivities,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (CustomerActivityID)
);

-- BCNF
create table ReferRecord
(
    CustomerActivityID INTEGER     NOT NULL,
    CustomerID         VARCHAR(45) NOT NULL,
    BrandID            VARCHAR(45) NOT NULL,
    ReferCustomerID    VARCHAR(45) NOT NULL,
    ReferDate          DATE        NOT NULL,
    RECode             VARCHAR(45) NOT NULL,
    VersionNumber      INTEGER     NOT NULL,
    FOREIGN KEY (RECode, VersionNumber) REFERENCES RERules,
    FOREIGN KEY (CustomerActivityID) references CustomerActivities,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (ReferCustomerID) REFERENCES Customer,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (CustomerActivityID)
);


-- 3NF
CREATE TABLE Wallet
(
    WalletID           VARCHAR(45)   NOT NULL,
    CustomerID         VARCHAR(45)   NOT NULL,
    Loyalty_Program_ID VARCHAR(45)   NOT NULL,
    BrandID            VARCHAR(45)   NOT NULL,
    Points             NUMBER(10, 2) NOT NULL,
    TotalPoints        NUMBER(10, 2) NOT NULL,
    LevelNumber        INTEGER,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (Loyalty_Program_ID) REFERENCES RegularLoyaltyProgram,
--     FOREIGN KEY (LevelNumber) references TieredProgram,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    PRIMARY KEY (CustomerID, Loyalty_Program_ID)
);

-- BCNF
--记录每个客户兑换过的reward，消费时会用上
create table WalletRewards
(
    CustomerID VARCHAR(45) NOT NULL,
    BrandID    VARCHAR(45) NOT NULL,
    RewardID   VARCHAR(45) NOT NULL,
    Quantity   INTEGER     NOT NULL,
    primary key (CustomerID, BrandID, RewardID)
);

-- 2NF
CREATE TABLE RedeemRecord
(
    id            NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    CustomerID    VARCHAR(45) NOT NULL,
    BrandID       VARCHAR(45) NOT NULL,
    PointRedeemed INTEGER     NOT NULL,
    RRCode        VARCHAR(45) NOT NULL,
    VersionNum    VARCHAR(45) NOT NULL,
    RedeemDate    DATE        NOT NULL,
    RewardID      VARCHAR(45) NOT NULL,
    Quantity      INTEGER     NOT NULL,
    FOREIGN KEY (CustomerID) REFERENCES Customer,
    FOREIGN KEY (BrandID) REFERENCES Brand,
    FOREIGN KEY (RRCode, VersionNum) REFERENCES RRRules,
    PRIMARY KEY (id)
);

-- Both activity categories and reward categories have globally unique codes (alphanumeric) and a name.
-- CREATE TABLE Categories
-- (
--     CategoryID INTEGER NOT NULL,
--     Name       VARCHAR(45),
--     PRIMARY KEY (CategoryID)
-- );

-- CREATE TABLE Rules
-- (
--     RuleCode VARCHAR(45) NOT NULL,
--     PRIMARY KEY (RuleCode)
-- );