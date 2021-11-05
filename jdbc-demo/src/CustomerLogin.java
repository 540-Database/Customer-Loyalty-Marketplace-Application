import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CustomerLogin {
    static String customerId;
    static String loyaltyProgramId;
    static String activityId;
    static String brandId;

    public static void verifyLogin(Connection connection) {
        customerId = null;
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (true) {
            System.out.println("Please enter customer's user id and password, split with comma:");
            System.out.println("To return to the root menu, please enter 0.");
            String idAndPassword = scanner.next();
            if (idAndPassword.equals("0"))
                return;
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
    }

    public static void run(Connection connection) throws SQLException {
        verifyLogin(connection);
        Scanner scanner = new Scanner(System.in);

        int choice = 0;
        do {
            System.out.println("\n---------- Customer Main Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("0. Exit\n1. Enroll in a Loyalty Program\n2. Reward Activities\n3. View Wallet\n" +
                    "4. Redeem Points\n5. Log out\n");

            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    rewardActivities(connection);
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 0);
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
                //review(connection);
                break;
            case "A03":
                // refer(connection);
                break;
        }
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
