import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

        int choice;
        do {
            System.out.println("\n---------- Customer Main Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("0. Exit\n1. Enroll in a Loyalty Program\n2. Reward Activities\n3. View Wallet\n" +
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

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            resultSet.next();
            double points = resultSet.getDouble(1);
            points = points * amount / 100;
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


}
