import java.sql.*;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class BrandLogin {
    private static String BrandID;
    private static boolean isNewProgram = false;
    private static String ProgramID;

    public static boolean verifyLogin(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (true) {
            System.out.println("Please enter brand's ID and password, split with comma:");
            System.out.println("To return to the root menu, please enter 0.");
            String IDAndPassword = scanner.next();
            if (IDAndPassword.equals("0"))
                return false;
            ResultSet resultSet = null;
            try {
                String[] IDAndPasswordSplit = IDAndPassword.split(",");
                BrandID = IDAndPasswordSplit[0].trim();
                String password = IDAndPasswordSplit[1].trim();
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(String.format("SELECT * FROM BRAND WHERE BrandID='%s' AND PASSWORD='%s'", BrandID, password));
                if (!resultSet.next()) {
                    System.out.println("Wrong brand's ID or password entered! Please try again.");
                    resultSet.close();
                    statement.close();
                }
                else {
                    System.out.println("Login success! Welcome~");
                    resultSet.close();
                    statement.close();
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void run(Connection connection) throws SQLException {
        if (!verifyLogin(connection)) return;
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        int choice = 0;
        do {
            System.out.println("---------- Brand Main Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Add Loyalty Program\n2. Add RE Rules\n3. Update RE Rules\n4. Add RR Rules\n" +
                    "5. Update RR Rules\n6. Validate Loyalty Program\n7. Log out");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addLoyaltyProgram(connection);
                    break;
                case 2:
                    addRERules(connection);
                    break;
                case 3:
                    updateRERules(connection);
                    break;
                case 4:
                    addRRRules(connection);
                    break;
                case 5:
                    updateRRRules(connection);
                    break;
                case 6:
                    // todo: when isNewProgram it's true, check all tables related to the current BrandID.
                    validateProgram(connection);
                    break;
                case 7:
                    // todo: check whether isNewProgram is false, if it's true, ask the user to delete it or continue.
                    choice = logOut(connection);
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 8);
    }

    public static void addLoyaltyProgram(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

////        Detect whether this brand have a Loyalty Program or not.
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("select * from REGULARLOYALTYPROGRAM where BrandID = ('%s')", BrandID));
        if (resultSet.next()) {
            System.out.println("This Brand is already having a Loyalty Program, you can update it.");
            resultSet.close();
            statement.close();
            return;
        }


        int choice = 0;
        do {
            System.out.println("---------- Brand: LoyaltyProgram ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Add Regular Program\n2. Add Tier Program\n3. Go Back");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addRegularProgram(connection);
                    break;
                case 2:
                    addTierProgram(connection);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 3);
    }

    public static void addRegularProgram(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        int choice = 0;

        while (true) {
            System.out.println("Please enter your Loyalty Program ID & Name, split with comma:");
            System.out.println("To return to the Brand: LoyaltyProgram page, please enter 0.");
            String IDAndName = scanner.next();
            if (IDAndName.equals("0"))
                return;
            ResultSet resultSet = null;
            try {
                String[] nameAndPasswordSplit = IDAndName.split(",");
                ProgramID = nameAndPasswordSplit[0].trim();
                String name = nameAndPasswordSplit[1].trim();
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(String.format("INSERT INTO REGULARLOYALTYPROGRAM VALUES ('%s', '%s', '%s', 0)", ProgramID, name, BrandID));
                resultSet.close();
                System.out.println("Create new Regular Program Successfully!");
                isNewProgram = true;
                break;
            } catch (SQLException e) {
                System.out.println("The Program ID is already exists.");
//                e.printStackTrace();
            }
        }


        do {
            System.out.println("---------- Brand: Regular page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Activity Types\n2. Reward Types\n3. Go Back");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    activityTypes(connection);
                    break;
                case 2:
                    rewardTypes(connection);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 3);
    }

    public static void addTierProgram(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        while (true) {
            System.out.println("Please enter your Loyalty Program ID & Name, split with comma:");
            System.out.println("To return to the Brand: LoyaltyProgram page, please enter 0.");
            String IDAndName = scanner.next();
            if (IDAndName.equals("0"))
                return;
            ResultSet resultSet = null;
            try {
                String[] nameAndPasswordSplit = IDAndName.split(",");
                ProgramID = nameAndPasswordSplit[0].trim();
                String name = nameAndPasswordSplit[1].trim();
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(String.format("INSERT INTO REGULARLOYALTYPROGRAM VALUES ('%s', '%s', '%s', 1)", ProgramID, name, BrandID));
                resultSet.close();
                statement.close();
                System.out.println("Create new Tiered Program Successfully!");
                isNewProgram = true;
                break;
            } catch (SQLException e) {
                System.out.println("The Program ID is already exists.");
//                e.printStackTrace();
            }
        }

        int choice = 0;
        do {
            System.out.println("---------- Brand: Tier page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Tiers Set up\n2. Activity Types\n3. Reward Types\n4. Go Back");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    tiersSetUp(connection);
                    break;
                case 2:
                    activityTypes(connection);
                    break;
                case 3:
                    rewardTypes(connection);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 4);
    }

    public static void tiersSetUp(Connection connection) throws SQLException {
        String sql = String.format("select * from TIEREDPROGRAM where Loyalty_Program_ID = '%s'", ProgramID);
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            if (resultSet.next()) {
                System.out.println("You already have set up your tiers");
                resultSet.close();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        int choice = 0;
        do {
            System.out.println("---------- Brand: Tiers Set up page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Set up\n2. Go Back");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    Statement statement = connection.createStatement();
                    System.out.println("Please enter the Number of tiers (max 3)");
                    int numTiers = scanner.nextInt();
                    while (numTiers != 1 && numTiers != 2 && numTiers != 3) {
                        System.out.println("Number of tiers should be in range 1 to 3. Please enter a new one.");
                        numTiers = scanner.nextInt();
                    }

                    int preTierPoints = 0;
                    for (int i = 1; i <= numTiers; ++i) {
                        System.out.println("Please enter the name of Tier " + i);
                        String tierName = scanner.next();
                        System.out.println("Please enter the points required of Tier " + i);
                        int tierPoints = scanner.nextInt();
                        while (tierPoints <= preTierPoints) {
                            System.out.println("The points required you entered should be strictly larger than the previous tier, please enter a new one");
                            tierPoints = scanner.nextInt();
                        }
                        System.out.println("Please enter the multiplier of Tier " + i);
                        double tierMultiplier = scanner.nextDouble();
                        while (tierMultiplier <= 0) {
                            System.out.println("The multiplier should be strictly larger than 0, please enter a new one");
                            tierMultiplier = scanner.nextDouble();
                        }
                        ResultSet resultSet = statement.executeQuery(String.format("INSERT INTO TIEREDPROGRAM VALUES ('%s', '%s', '%s', '%s', '%s')", ProgramID, i, tierPoints, tierName, tierMultiplier));
                        System.out.println("Tier " + i + "set up successfully!");
                        resultSet.close();
                        preTierPoints = tierPoints;
                    }
                    statement.close();
                    return;
                case 2:
                    return;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 2);
    }

    public static void activityTypes(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        ResultSet resultSet = null;
        int choice = 0;
        do {
            System.out.println("---------- Brand: Activity Types Pages ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Purchase\n2. Leave a review\n3. Refer a friend\n4. Go Back");
            try {
                choice = scanner.nextInt();
                Statement statement = connection.createStatement();
                switch (choice) {
                    case 1:
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_ACTIVITY VALUES ('%s', 'A01')", ProgramID));
                        System.out.println("The activity purchase is added to your program " + ProgramID);
                        resultSet.close();
                        statement.close();
                        break;
                    case 2:
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_ACTIVITY VALUES ('%s', 'A02')", ProgramID));
                        System.out.println("The activity leave a review is added to your program " + ProgramID);
                        resultSet.close();
                        statement.close();
                        break;
                    case 3:
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_ACTIVITY VALUES ('%s', 'A03')", ProgramID));
                        System.out.println("The activity refer a friend is added to your program " + ProgramID);
                        resultSet.close();
                        statement.close();
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invaild option entered. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("The activity you chose is already in your program, you can choose another one.");
            }
        } while (choice != 4);
    }

    public static void rewardTypes(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        ResultSet resultSet = null;
        int choice = 0;
        int quantity = 0;
        do {
            System.out.println("---------- Brand: Activity Types Pages ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Gift Card\n2. Free Product\n3. Go Back");
            try {
                choice = scanner.nextInt();
                Statement statement = connection.createStatement();
                switch (choice) {
                    case 1:
                        System.out.println("Please enter the quantity of the reward Gift Card");
                        quantity = scanner.nextInt();
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_REWARD VALUES ('%s', 'R01', '%s')", ProgramID, quantity));
                        System.out.println("The reward Gift Card is added to your program " + ProgramID);
                        resultSet.close();
                        statement.close();
                        break;
                    case 2:
                        System.out.println("Please enter the quantity of the reward Free Product");
                        quantity = scanner.nextInt();
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_REWARD VALUES ('%s', 'R02', '%s')", ProgramID, quantity));
                        System.out.println("The reward Free Product is added to your program " + ProgramID);
                        resultSet.close();
                        statement.close();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invaild option entered. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("The reward you chose is already in your program, you can choose another one.");
            }
        } while (choice != 3);
    }

    public static void addRERules(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

//        Detect whether this Brand have a program.
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("select LOYALTY_PROGRAM_ID from REGULARLOYALTYPROGRAM where BrandID = ('%s')", BrandID));
        if (!resultSet.next()) {
            System.out.println("This Brand has no Loyalty Program, please create one first.");
            resultSet.close();
            statement.close();
            return;
        }

        int choice = 0;
        do {
            System.out.println("---------- Brand: addRERules Types Pages ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. addRERule\n2. Go Back");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
//                      Show what activities current program has
                        ProgramID = resultSet.getString(1);
                        ResultSet availableActivitySet = connection.createStatement().executeQuery(String.format("select ACTIVITYID from LOYALTY_PROGRAM_HAS_ACTIVITY where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                        ResultSet notAvailableActivitySet = connection.createStatement().executeQuery(String.format("select distinct ACTIVITYID from RERULES where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                        String activities = "";
                        Set<String> set1 = new HashSet<>(), set2 = new HashSet<>();
                        while (availableActivitySet.next()) {
//                            System.out.println(availableActivitySet.getString(1));
                            set1.add(availableActivitySet.getString(1));
                        }
                        while (notAvailableActivitySet.next()) set2.add(notAvailableActivitySet.getString(1));
                        Set<String> copySet1 = new HashSet<>(set1);
                        for (String activity : copySet1) {
                            if (!set2.contains(activity)) {
                                activities += activity + " ";
                            } else {
                                set1.remove(activity);
                            }
                        }
                        availableActivitySet.close();
                        notAvailableActivitySet.close();
                        if (set1.size() > 0) {
                            System.out.println("Please select from the below activities:");
                            System.out.println(activities);
                            String chosenAct = scanner.next();
                            while (!set1.contains(chosenAct)) {
                                System.out.println("You should choose from the listing activities, Please try again");
                                chosenAct = scanner.next();
                            }
                            System.out.println("Please enter the RECODE");
                            String RECODE = scanner.next();
                            System.out.println("Please enter the points of this rule");
                            int points = scanner.nextInt();
                            while (points <= 0) {
                                System.out.println("the points of rules should be strictly larger than 0, please enter a new one.");
                                points = scanner.nextInt();
                            }
                            statement.executeQuery(String.format("INSERT INTO RERULES VALUES ('%s', '%s','%s','%s','%s','%s')", RECODE, ProgramID, chosenAct, points, 1, 1));
                            System.out.println("Add RERules Successfully!");
                            resultSet.close();
                            statement.close();
                        }
                        else {
                            System.out.println("There are no avaliable activities to choose.");
                            resultSet.close();
                            statement.close();
                            return;
                        }
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Invaild option entered. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("The RECODE you entered already exists, please enter a new one.");
            }
        } while (choice != 2);
    }

    public static void addRRRules(Connection connection) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

//        Detect whether this Brand have a program.
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("select LOYALTY_PROGRAM_ID from REGULARLOYALTYPROGRAM where BrandID = ('%s')", BrandID));
        if (!resultSet.next()) {
            System.out.println("This Brand has no Loyalty Program, please create one first.");
            resultSet.close();
            statement.close();
            return;
        }

        int choice = 0;
        do {
            System.out.println("---------- Brand: addRRRules Types Pages ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. addRRRule\n2. Go Back");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
//                      Show what activities current program has
                        ProgramID = resultSet.getString(1);
                        ResultSet availableRewardSet = connection.createStatement().executeQuery(String.format("select REWARDID from LOYALTY_PROGRAM_HAS_REWARD where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                        ResultSet notAvailableRewardSet = connection.createStatement().executeQuery(String.format("select distinct REWARDID from RRRULES where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                        String rewards = "";
                        Set<String> set1 = new HashSet<>(), set2 = new HashSet<>();
                        while (availableRewardSet.next()) {
//                            System.out.println(availableActivitySet.getString(1));
                            set1.add(availableRewardSet.getString(1));
                        }
                        while (notAvailableRewardSet.next()) set2.add(notAvailableRewardSet.getString(1));
                        availableRewardSet.close();
                        notAvailableRewardSet.close();
                        Set<String> copySet1 = new HashSet<>(set1);
                        for (String reward : copySet1) {
                            if (!set2.contains(reward)) {
                                rewards += reward + " ";
                            } else {
                                set1.remove(reward);
                            }
                        }
                        if (set1.size() > 0) {
                            System.out.println("Please select from the below rewards:");
                            System.out.println(rewards);
                            String chosenReward = scanner.next();
                            while (!set1.contains(chosenReward)) {
                                System.out.println("You should choose from the listing rewards, Please try again");
                                chosenReward = scanner.next();
                            }
                            System.out.println("Please enter the RRCODE");
                            String RRCODE = scanner.next();
                            System.out.println("Please enter the points of this rule");
                            int points = scanner.nextInt();
                            while (points <= 0) {
                                System.out.println("the points of rules should be strictly larger than 0, please enter a new one.");
                                points = scanner.nextInt();
                            }
                            statement.executeQuery(String.format("INSERT INTO RRRULES VALUES ('%s', '%s','%s','%s','%s','%s')", RRCODE, ProgramID, chosenReward, points, 1, 1));
                            System.out.println("ADD RRRULES successfully!");
                            resultSet.close();
                            statement.close();
                        }
                        else {
                            System.out.println("There are no avaliable rewards to choose.");
                            resultSet.close();
                            statement.close();
                            return;
                        }
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Invaild option entered. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("The RRCODE you entered already exists, please enter a new one.");
            }
        } while (choice != 2);
    }

    public static void updateRRRules (Connection connection) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        ResultSet resultSet = connection.createStatement().executeQuery(String.format("select RRCODE from RRRULES where BrandID = ('%s') and Status = 1", BrandID));

        Set<String> RRRULES = new HashSet<>();
        String Rules = "";
        while (resultSet.next()) {
            RRRULES.add(resultSet.getString(1));
            Rules += resultSet.getString(1) + " ";
        }
        resultSet.close();

        if (RRRULES.size() > 0) {
            while (true) {
                System.out.println("Please select the rule code you want to update from the below list:");
                System.out.println(Rules);
                String RRCODE = scanner.next();
                try {
                    ResultSet oldSet = connection.createStatement().executeQuery(String.format("select * from RRRULES where RRCODE = ('%s') and Status = 1", RRCODE));
                    oldSet.next();
                    ProgramID = oldSet.getString(2);
                    int version = oldSet.getInt(7);
                    String rewardID = oldSet.getString(4);
                    int points;
                    while (true) {
                        System.out.println("Please enter the updated points");
                        try {
                            points = scanner.nextInt();
                            if (points <= 0) {
                                System.out.println("The points you entered should be strictly larger than 0");
                                continue;
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please try again.");
                            scanner.next();
                            e.printStackTrace();
                        }
                    }
                    connection.createStatement().executeQuery(String.format("update RRRULES set Status = 0 where RRCODE = ('%s') and Status = 1", RRCODE));
                    connection.createStatement().executeQuery(String.format("INSERT INTO RRRULES VALUES ('%s', '%s','%s','%s','%s','%s')", RRCODE, ProgramID, rewardID, points, 1, version + 1));
                    oldSet.close();
                    System.out.println("Rules updated successfully!");
                    break;
                } catch (Exception e) {
                    System.out.println("Please enter the RRCODE listed below.");
                }
            }
        }
        else {
            System.out.println("There are no avaliable rules to update, please add it first.");
            return;
        }

    }

    public static void updateRERules (Connection connection) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        ResultSet resultSet = connection.createStatement().executeQuery(String.format("select RECODE from RERULES where BrandID = ('%s') and Status = 1", BrandID));

        Set<String> ReRules = new HashSet<>();
        String Rules = "";
        while (resultSet.next()) {
            ReRules.add(resultSet.getString(1));
            Rules += resultSet.getString(1) + " ";
        }
        resultSet.close();

        if (ReRules.size() > 0) {
            while (true) {
                System.out.println("Please select the rule code you want to update from the below list:");
                System.out.println(Rules);
                String RECODE = scanner.next();
                try {
                    ResultSet oldSet = connection.createStatement().executeQuery(String.format("select * from RERULES where RECODE = ('%s') and Status = 1", RECODE));
                    oldSet.next();
                    ProgramID = oldSet.getString(3);
                    int version = oldSet.getInt(6);
                    String activityID = oldSet.getString(4);
                    int points;
                    while (true) {
                        System.out.println("Please enter the updated points");
                        try {
                            points = scanner.nextInt();
                            if (points <= 0) {
                                System.out.println("The points you entered should be strictly larger than 0");
                                continue;
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please try again.");
                            scanner.next();
                            e.printStackTrace();
                        }
                    }


                    connection.createStatement().executeQuery(String.format("update RERULES set Status = 0 where RECODE = ('%s') and Status = 1", RECODE));
                    connection.createStatement().executeQuery(String.format("INSERT INTO RERULES VALUES ('%s', '%s','%s','%s','%s','%s')", RECODE, ProgramID, activityID, points, version + 1, 1));
                    System.out.println("Rules updated successfully!");
                    oldSet.close();
                    break;
                } catch (Exception e) {
                    System.out.println("Please enter the RECODE listed below.");
                }
            }
        }
        else {
            System.out.println("There are no avaliable rules to update, please add it first.");
            return;
        }
    }

    public static void validateProgram(Connection connection)  throws SQLException{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        int choice = 0;
        do {
            System.out.println("---------- Brand: validateLoyaltyProgram Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Validate\n2. Go Back");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        if (isNewProgram) {
                            ResultSet resultSet = connection.createStatement().executeQuery(String.format("select LOYALTY_PROGRAM_ID, ISTIERED from REGULARLOYALTYPROGRAM where BrandID = ('%s')", BrandID));
                            ProgramID = resultSet.getString(1);
                            ResultSet activityTypes = null, rewardTypes = null, RERules = null, RRRules = null, TierSetup = null;
                            Boolean isTiered = resultSet.getInt(2) == 1;
                            resultSet.close();
                            try {
                                activityTypes = connection.createStatement().executeQuery(String.format("select ACTIVITYID from LOYALTY_PROGRAM_HAS_ACTIVITY where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                                rewardTypes = connection.createStatement().executeQuery(String.format("select REWARDID from LOYALTY_PROGRAM_HAS_REWARD where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                                RERules = connection.createStatement().executeQuery(String.format("select RECODE from RERULES where LOYALTY_PROGRAM_ID = '%s' and Status = 1", ProgramID));
                                RRRules = connection.createStatement().executeQuery(String.format("select RRCODE from RRRULES where LOYALTY_PROGRAM_ID = '%s' and Status = 1", ProgramID));
                                TierSetup = connection.createStatement().executeQuery(String.format("select * from TIEREDPROGRAM where LOYALTY_PROGRAM_ID = '%s'", ProgramID));
                            } catch (SQLException e) {
//                                System.out.println("Some errors occur");
                                e.printStackTrace();
                            }

                            if (!activityTypes.next())
                                System.out.println("There is no available activity types in current program, please add at least one.");
                            else if (!rewardTypes.next())
                                System.out.println("There is no available reward types in current program, please add at least one.");
                            else if (!RERules.next())
                                System.out.println("There is no available RE Rules in current program, please add at least one.");
                            else if (!RRRules.next())
                                System.out.println("There is no available RR Rules in current program, please add at least one.");
                            else if (isTiered && !TierSetup.next())
                                System.out.println("Current program is a Tiered Program and you haven't done the Tier set up, please set it before validate.");
                            else {
                                System.out.println("Validate current Program successfully!");
                                isNewProgram = false;
                                activityTypes.close();
                                rewardTypes.close();
                                RERules.close();
                                RRRules.close();
                                TierSetup.close();
                                return;
                            }

                            if (isNewProgram) {
                                activityTypes.close();
                                rewardTypes.close();
                                RERules.close();
                                RRRules.close();
                                TierSetup.close();
                                return;
                            }
                        }
                        else {
                            System.out.println("There is no Program in current brand, please add one first.");
                            return;
                        }
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Invaild option entered. Please try again.");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (choice != 2);
    }

    public static int logOut(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        if (!isNewProgram) return 8;
        else {
            int choice = 0;
            do {
                System.out.println("There is a program settings unvalidated in your current brand, Please enter your option:");
                System.out.println("1. Log out (all your setting will be lost!!!)\n2. Go Back");
                try {
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            try {
                                ResultSet resultSet = connection.createStatement().executeQuery(String.format("select LOYALTY_PROGRAM_ID from REGULARLOYALTYPROGRAM where BrandID = ('%s')", BrandID));
                                ProgramID = resultSet.getString(1);
                                connection.createStatement().executeQuery(String.format("Delete from REGULARLOYALTYPROGRAM where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                                connection.createStatement().executeQuery(String.format("Delete from TIEREDPROGRAM where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                                connection.createStatement().executeQuery(String.format("Delete from LOYALTY_PROGRAM_HAS_ACTIVITY where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                                connection.createStatement().executeQuery(String.format("Delete from LOYALTY_PROGRAM_HAS_REWARD where LOYALTY_PROGRAM_ID = ('%s')", ProgramID));
                                connection.createStatement().executeQuery(String.format("Delete from RERULES where LOYALTY_PROGRAM_ID = '%s'", ProgramID));
                                connection.createStatement().executeQuery(String.format("Delete from RRRULES where LOYALTY_PROGRAM_ID = '%s'", ProgramID));
                                resultSet.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            return 8;
                        case 2:
                            return 7;
                        default:
                            System.out.println("Invaild option entered. Please try again.");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (choice != 2);
        }
        return 7;
    }
}
