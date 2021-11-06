import java.sql.*;
import java.util.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CustomerLogin {
    static String customerId;
    static String loyaltyProgramId;
    static String activityId;
    static String brandId;

    public static boolean verifyLogin(Connection connection) {
        customerId = null;
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (true) {
            System.out.println("Please enter customer's user id and password, split with comma:");
            System.out.println("To return to the root menu, please enter 0.");
            String idAndPassword = scanner.next();
            if (idAndPassword.equals("0"))
                return false;
            ResultSet resultSet = null;
            try {
                String[] idAndPasswordSplit = idAndPassword.split(",");
                String id = idAndPasswordSplit[0].trim(), password = idAndPasswordSplit[1].trim();
                customerId = id;
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(String.format("SELECT * FROM CUSTOMER WHERE CUSTOMERID='%s' AND PASSWORD='%s'", id, password));
                if (!resultSet.next())
                    System.out.println("Wrong customer id or password entered! Please try again.");
                else {
                    System.out.println("\nLogin success! Welcome~");
                    break;
                }
            } catch (SQLException e) {
                customerId = null;
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void run(Connection connection) throws SQLException {
        if (!verifyLogin(connection)){
            return;
        }
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("\n---------- Customer Main Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Enroll in a Loyalty Program\n2. Reward Activities\n3. View Wallet\n" +
                    "4. Redeem Points\n5. Log out\n");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    enrollLoyaltyPrograms(connection);
                    break;
                case 2:
                    rewardActivities(connection);
                    break;
                case 3:
                    break;
                case 4:
                    redeemMenu(connection);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 5);
    }

    public static void enrollLoyaltyPrograms(Connection connection) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        List<String> loyaltyProgramIds = new ArrayList<>();

        // Checking all loyalty programs
        try{
            Statement statement = connection.createStatement();
            ResultSet loyaltyPrograms = statement.executeQuery(String.format("SELECT * FROM REGULARLOYALTYPROGRAM"));

            if (!loyaltyPrograms.next()){
                System.out.println("There are no available loyalty programs currently.");
                System.out.println("Redirecting to the customer login page...");
                return;
            } else {
                System.out.println("---------- Displaying All Validate Loyalty Programs ----------");
                System.out.println(String.format("%12s %15s %10s ", "Program id", "Program name", "Brand id"));
                String loyaltyProgramId = loyaltyPrograms.getString(1);
                String loyaltyProgramName = loyaltyPrograms.getString(2);
                String loyaltyProgramBrandId = loyaltyPrograms.getString(3);
                System.out.println(String.format("%12s %15s %10s", loyaltyProgramId, loyaltyProgramName, loyaltyProgramBrandId));
                loyaltyProgramIds.add(loyaltyProgramId);
                while (loyaltyPrograms.next()){
                    loyaltyProgramId = loyaltyPrograms.getString(1);
                    loyaltyProgramName = loyaltyPrograms.getString(2);
                    loyaltyProgramBrandId = loyaltyPrograms.getString(3);
                    System.out.println(String.format("%12s %15s %10s", loyaltyProgramId, loyaltyProgramName, loyaltyProgramBrandId));
                    loyaltyProgramIds.add(loyaltyProgramId);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Please enter the program id of the program you want to enroll in.");
        System.out.println("To return to the customer login menu, please enter 0.");
        while (true) {
            String loyaltyProgramId = scanner.next().trim();
            if (loyaltyProgramId.equals("0"))
                return;

            if (!loyaltyProgramIds.contains(loyaltyProgramId)){
                System.out.println("Wrong program id entered! Please try again.");
                continue;
            }
            try {
                Statement statement = connection.createStatement();
                ResultSet loyaltyProgramsHasEnrolled = statement.executeQuery(String.format("SELECT LOYALTY_PROGRAM_ID FROM WALLET " +
                        "WHERE CUSTOMERID = '%s'", customerId));

                while (loyaltyProgramsHasEnrolled.next()){
                    if (loyaltyProgramId.equals(loyaltyProgramsHasEnrolled.getString(1))){
                        System.out.println("You have enrolled this program! Redirecting to the customer login menu.");
                        return;
                    }
                }

                String walletId = 'W' + customerId.substring(1);
                ResultSet resultBrandId = statement.executeQuery(String.format("SELECT BRANDID FROM REGULARLOYALTYPROGRAM WHERE LOYALTY_PROGRAM_ID = '%s'", loyaltyProgramId));
                resultBrandId.next();
                String brandId = resultBrandId.getString(1);
                statement.executeQuery(String.format("INSERT INTO WALLET (walletid, customerid, loyalty_program_id, brandid, points, totalpoints, levelnumber) " +
                        "VALUES ('%s', '%s', '%s', '%s', 0, 0, 0)", walletId, customerId, loyaltyProgramId, brandId));
                int customerActivityId = getMaxIDFromCustomerActivities(connection);
                String now = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                statement.executeQuery(String.format("INSERT INTO CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, customerid, brandid, activityid, pointsearned, activitydate) " +
                        "VALUES (%d, '%s', '%s', 'A00', 0, to_date('%s', 'mm/dd/yyyy'))", customerActivityId, customerId, brandId, now));
            } catch (SQLException e){
                e.printStackTrace();
            }

            System.out.println("Enroll loyalty program success! Redirecting to customer login menu.");
            return;
        }
    }

    public static void rewardActivities(Connection connection) throws SQLException {
        loyaltyProgramId = null;
        activityId = null;
        brandId = null;

        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        String sql = String.format("SELECT LOYALTY_PROGRAM_ID FROM WALLET WHERE CUSTOMERID = '%s'", customerId);
        List<String> LOYALTY_PROGRAM_IDs = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            System.out.println("LOYALTY_PROGRAM_ID");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
                LOYALTY_PROGRAM_IDs.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        do {
            System.out.println("Please enter the exact Loyalty Program ID from above choices or input 0 to exit");
            loyaltyProgramId = scanner.next();
            if (loyaltyProgramId.equals("0")) {
                return;
            }
        } while (!LOYALTY_PROGRAM_IDs.contains(loyaltyProgramId));


        String sqlForBrandId = String.format("SELECT BRANDID FROM REGULARLOYALTYPROGRAM WHERE LOYALTY_PROGRAM_ID = '%s'", loyaltyProgramId);
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sqlForBrandId);
            resultSet.next();
            brandId = resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        String sql2 = String.format("SELECT DISTINCT A.ACTIVITYID, A.ACTIVITYNAME " +
                "FROM RERULES R, ACTIVITY A WHERE R.ACTIVITYID = A.ACTIVITYID " +
                "and R.LOYALTY_PROGRAM_ID = '%s'", loyaltyProgramId);
        List<String> activityIds = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql2);
            while (resultSet.next()) {
                activityIds.add(resultSet.getString(1));
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        activityId = null;
        do {
            System.out.println("Please enter the exact Activity ID from above choices or input 0 to exit");
            activityId = scanner.next();
            if (activityId.equals("0")) {
                return;
            }
        } while (!activityIds.contains(activityId));


        switch (activityId) {
            case "A01":
                purchase(connection);
                updateTier(connection);
                updateTier(connection);
                break;
            case "A02":
                review(connection);
                updateTier(connection);
                updateTier(connection);
                break;
            case "A03":
                refer(connection);
                updateTier(connection);
                updateTier(connection);
                break;
        }
    }

    public static void redeemMenu(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        int choice;
        do {
            System.out.println("---------- Customer Redeem Points Menu ----------");
            System.out.println("Please enter your option:");
            System.out.println("1. Rewards Selection\n2. Go back");

            choice = scanner.nextInt();
            switch (choice){
                case 1:
                    int result = redeemActivities(connection);
                    if (result == -1) return;
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 2);
    }

    public static int redeemActivities(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        // Checking all loyalty programs the customer enrolled in
        try{
            Statement statement = connection.createStatement();
            ResultSet loyaltyProgramHasEnrolled = statement.executeQuery(String.format("SELECT LOYALTY_PROGRAM_ID, BRANDID, POINTS FROM WALLET " +
                    "WHERE CUSTOMERID = '%s'", customerId));

            if (!loyaltyProgramHasEnrolled.isBeforeFirst()){
                System.out.println("You haven't enrolled in any loyalty program yet!");
                System.out.println("Redirecting to the customer login menu.");
                return -1;
            }

            System.out.println("---------- Displaying all the loyalty programs you've enrolled in ----------");
            System.out.println(String.format("%12s %15s %12s", "Program id", "Brand id", "Your points"));
            Map<String, String> loyaltyProgramIdsEnrolledToPoints = new HashMap<>();
            while (loyaltyProgramHasEnrolled.next()){
                String loyaltyProgramId = loyaltyProgramHasEnrolled.getString(1);
                String loyaltyProgramBrandId = loyaltyProgramHasEnrolled.getString(2);
                String loyaltyProgramPoints = loyaltyProgramHasEnrolled.getString(3);
                loyaltyProgramIdsEnrolledToPoints.put(loyaltyProgramId, loyaltyProgramPoints);
                System.out.println(String.format("%12s %15s %12s", loyaltyProgramId, loyaltyProgramBrandId, loyaltyProgramPoints));
            }

            System.out.println("Please enter the loyalty program id you want to redeem:");
            String loyaltyProgramIdSelected = scanner.next();
            while (true){
                if (!loyaltyProgramIdsEnrolledToPoints.containsKey(loyaltyProgramIdSelected)){
                    System.out.println("Wrong loyalty program id entered. Please try again:");
                    loyaltyProgramIdSelected = scanner.next();
                } else {
                    break;
                }
            }

            statement = connection.createStatement();
            ResultSet rewardSections = statement.executeQuery(String.format(
                    "SELECT rd.REWARDNAME, rrr.POINTS FROM RRRULES rrr, REWARD rd WHERE rrr.STATUS = 1 AND rrr.LOYALTY_PROGRAM_ID = '%s' AND rrr.REWARDID = rd.REWARDID", loyaltyProgramIdSelected));
            if (!rewardSections.isBeforeFirst()){
                System.out.println("Sorry, this program do not have any reward options yet.");
                System.out.println("Redirecting to the loyalty program selection page.");
            }
            Map<String, String> rewardNameToPoints = new HashMap<>();
            while (rewardSections.next()){
                rewardNameToPoints.put(rewardSections.getString(1), rewardSections.getString(2));
            }
            System.out.println("---------- Displaying all available rewards from the program ----------");
            System.out.println(String.format("You have %s points currently.", loyaltyProgramIdsEnrolledToPoints.get(loyaltyProgramIdSelected)));
            System.out.println(String.format("%12s %15s", "Reward Name", "Points needed"));
            for (Map.Entry<String, String> entry : rewardNameToPoints.entrySet()){
                System.out.println(String.format("%12s %15s", entry.getKey(), entry.getValue()));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    public static void purchase(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        double amount;
        while (true) {
            System.out.println("Please enter the amount of purchase");
            try {
                amount = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }

        String sql = String.format("SELECT R.POINTS FROM RERULES R, ACTIVITY A WHERE R.ACTIVITYID = A.ACTIVITYID " +
                "and R.LOYALTY_PROGRAM_ID = '%s' and A.ACTIVITYID = '%s' and STATUS = 1", loyaltyProgramId, activityId);

        String RECode = getRECode(connection);
        int version = getREVersion(connection);
        double multiplier = getMultiplier(connection);
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            resultSet.next();
            double points = resultSet.getDouble(1);
            points = multiplier * points * amount / 100;
            System.out.println("You have earned " + points + " points");
            String sql2 = String.format("UPDATE WALLET" +
                    " SET POINTS = POINTS + %f," +
                    " TOTALPOINTS = TOTALPOINTS + %f" +
                    " WHERE CUSTOMERID = '%s'" +
                    " and LOYALTY_PROGRAM_ID = '%s'", points, points, customerId, loyaltyProgramId);
            connection.createStatement().executeUpdate(sql2);

            int newId = getMaxIDFromCustomerActivities(connection);
            String now = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            String sql3 = String.format("insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)" +
                    " values (%d, '%s', '%s', '%s', %f, to_date('%s', 'mm/dd/yyyy'))", newId, customerId, brandId, activityId, points, now);
            connection.createStatement().executeUpdate(sql3);

            String sql4 = String.format("insert into PURCHASERECORD(customeractivityid, customerid, brandid, moneyspent, pointsearned, giftcardused, totalamount, purchasedate, recode, versionnumber)" +
                    " VALUES (%d, '%s', '%s',  %f, %f, %d, %f, to_date('%s', 'mm/dd/yyyy'), '%s', %d)", newId, customerId, brandId, amount, points, 0, amount, now, RECode, version);
            connection.createStatement().executeUpdate(sql4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void review(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        String review = "";
        while (true) {
            System.out.println("Please enter the review");
            try {
                review = scanner.next();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        String sql = String.format("SELECT R.POINTS FROM RERULES R, ACTIVITY A WHERE R.ACTIVITYID = A.ACTIVITYID " +
                "and R.LOYALTY_PROGRAM_ID = '%s' and A.ACTIVITYID = '%s' and STATUS = 1", loyaltyProgramId, activityId);

        String RECode = getRECode(connection);
        int version = getREVersion(connection);
        double multiplier = getMultiplier(connection);

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            resultSet.next();
            double points = resultSet.getDouble(1);
            points = multiplier * points;
            System.out.println("You have earned " + points + " points");
            String sql2 = String.format("UPDATE WALLET" +
                    " SET POINTS = POINTS + %f," +
                    " TOTALPOINTS = TOTALPOINTS + %f" +
                    " WHERE CUSTOMERID = '%s'" +
                    " and LOYALTY_PROGRAM_ID = '%s'", points, points, customerId, loyaltyProgramId);
            connection.createStatement().executeUpdate(sql2);

            int newId = getMaxIDFromCustomerActivities(connection);
            String now = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            String sql3 = String.format("insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)" +
                    " values (%d, '%s', '%s', '%s', %f, to_date('%s', 'mm/dd/yyyy'))", newId, customerId, brandId, activityId, points, now);
            connection.createStatement().executeUpdate(sql3);

            String sql4 = String.format("insert into REVIEWRECORD(customeractivityid, customerid, brandid, review, reviewdate, recode, versionnumber)" +
                    " VALUES (%d, '%s', '%s',  '%s', to_date('%s', 'mm/dd/yyyy'), '%s', %d)", newId, customerId, brandId, review, now, RECode, version);
            connection.createStatement().executeUpdate(sql4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void refer(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        String referId = "";
        while (true) {
            System.out.println("Please enter the customer id you want to refer");
            try {
                referId = scanner.next();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        String sql = String.format("SELECT R.POINTS FROM RERULES R, ACTIVITY A WHERE R.ACTIVITYID = A.ACTIVITYID " +
                "and R.LOYALTY_PROGRAM_ID = '%s' and A.ACTIVITYID = '%s' and STATUS = 1", loyaltyProgramId, activityId);

        String RECode = getRECode(connection);
        int version = getREVersion(connection);
        double multiplier = getMultiplier(connection);

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            resultSet.next();
            double points = resultSet.getDouble(1);
            points = multiplier * points;
            System.out.println("You have earned " + points + " points");
            String sql2 = String.format("UPDATE WALLET" +
                    " SET POINTS = POINTS + %f," +
                    " TOTALPOINTS = TOTALPOINTS + %f" +
                    " WHERE CUSTOMERID = '%s'" +
                    " and LOYALTY_PROGRAM_ID = '%s'", points, points, customerId, loyaltyProgramId);
            connection.createStatement().executeUpdate(sql2);

            int newId = getMaxIDFromCustomerActivities(connection);
            String now = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            String sql3 = String.format("insert into CUSTOMERACTIVITIES (CUSTOMERACTIVITYID, CUSTOMERID, BRANDID, ACTIVITYID, POINTSEARNED, ACTIVITYDATE)" +
                    " values (%d, '%s', '%s', '%s', %f, to_date('%s', 'mm/dd/yyyy'))", newId, customerId, brandId, activityId, points, now);
            connection.createStatement().executeUpdate(sql3);

//            String sql4 = String.format("insert into REVIEWRECORD(customeractivityid, customerid, brandid, review, reviewdate, recode, versionnumber)" +
//                    " VALUES (%d, '%s', '%s',  '%s', to_date('%s', 'mm/dd/yyyy'), '%s', %d)", newId, customerId, brandId, review, now, RECode, version);
//            connection.createStatement().executeUpdate(sql4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getMaxIDFromCustomerActivities(Connection connection) {
        String sql = "SELECT MAX(CUSTOMERACTIVITYID) FROM CUSTOMERACTIVITIES";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static String getRECode(Connection connection) {
        String sql = String.format("SELECT R.RECODE FROM RERULES R, ACTIVITY A WHERE R.ACTIVITYID = A.ACTIVITYID " +
                "and R.LOYALTY_PROGRAM_ID = '%s' and A.ACTIVITYID = '%s' and STATUS = 1", loyaltyProgramId, activityId);
        String RECode = "";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            resultSet.next();
            RECode = resultSet.getString(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return RECode;
    }

    public static int getREVersion(Connection connection) {
        String sql = String.format("SELECT R.VERSIONNUMBER FROM RERULES R, ACTIVITY A WHERE R.ACTIVITYID = A.ACTIVITYID " +
                "and R.LOYALTY_PROGRAM_ID = '%s' and A.ACTIVITYID = '%s' and STATUS = 1", loyaltyProgramId, activityId);
        int version = 0;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            resultSet.next();
            version = resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static double getMultiplier(Connection connection) {
        String sql = String.format("select ISTIERED " +
                "from REGULARLOYALTYPROGRAM " +
                "where LOYALTY_PROGRAM_ID = '%s'", loyaltyProgramId);
        int ISTIERED = 0;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            resultSet.next();
            ISTIERED = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (ISTIERED == 0) {
            return 1;
        }

        String sql2 = String.format("select LEVELNUMBER from WALLET where CUSTOMERID = '%s' and LOYALTY_PROGRAM_ID = '%s'", customerId, loyaltyProgramId);
        int levelNumber = 1;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql2);
            resultSet.next();
            levelNumber = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        double multiplier = 1;
        String sql3 = String.format("select MULTIPLIER from TIEREDPROGRAM where LOYALTY_PROGRAM_ID = '%s' and LEVELNUMBER = %d", loyaltyProgramId, levelNumber);
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql3);
            resultSet.next();
            multiplier = resultSet.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return multiplier;
    }

    public static void updateTier(Connection connection) {
        String sql1 = String.format("select LEVELNUMBER from WALLET where CUSTOMERID = '%s' and LOYALTY_PROGRAM_ID = '%s'", customerId, loyaltyProgramId);
        int levelNumber = 1;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql1);
            resultSet.next();
            levelNumber = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        String sql2 = String.format("select max(LEVELNUMBER) from TIEREDPROGRAM where LOYALTY_PROGRAM_ID = '%s'", loyaltyProgramId);
        int maxnumber = 3;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql2);
            resultSet.next();
            maxnumber = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (levelNumber == maxnumber) {
            return;
        }

        int nextLevelNumber = levelNumber + 1;
        String sql3 = String.format("select POINTSREQUIRED from TIEREDPROGRAM where LOYALTY_PROGRAM_ID='%s' and LEVELNUMBER = %d", loyaltyProgramId, nextLevelNumber);
        int pointsRequired = 0;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql3);
            resultSet.next();
            pointsRequired = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql4 = String.format("select TOTALPOINTS from WALLET where CUSTOMERID = '%s' and LOYALTY_PROGRAM_ID = '%s'", customerId, loyaltyProgramId);
        int totalPoints = 0;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql4);
            resultSet.next();
            totalPoints = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (totalPoints < pointsRequired) {
            return;
        }

        String sql5 = String.format("update WALLET set LEVELNUMBER = %d where CUSTOMERID = '%s' and LOYALTY_PROGRAM_ID = '%s'", nextLevelNumber, customerId, loyaltyProgramId);
        try {
            connection.createStatement().executeUpdate(sql5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
