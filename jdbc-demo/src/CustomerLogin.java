import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerLogin {
    static String customerId = null;

    public static void verifyLogin(Connection connection) {
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
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        String sql = String.format("SELECT LOYALTY_PROGRAM_ID FROM WALLET WHERE CUSTOMERID = '%s'", customerId);

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            System.out.println("LOYALTY_PROGRAM_ID");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please select loyalty programs");
        String loyaltyProgramId = scanner.next();
        String sql2 = String.format("SELECT DISTINCT ACTIVITYID FROM RERULES WHERE LOYALTY_PROGRAM_ID = '%s'", loyaltyProgramId);
        List<String> activityIds = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql2);
            while (resultSet.next()) {
                activityIds.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // todo: if error, stay in this page


        List<String> activityNames = new ArrayList<>();
        for (String activityId : activityIds) {
            String sql3 = String.format("SELECT ACTIVITYNAME FROM ACTIVITY WHERE ACTIVITYID = '%s'", activityId);
            try {
                ResultSet resultSet = connection.createStatement().executeQuery(sql3);
                while (resultSet.next()) {
                    activityNames.add(resultSet.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        do {
            System.out.println("Please select activities");
            for (int i = 0; i < activityNames.size(); i++) {
                System.out.println(i + ". " + activityNames.get(i));
            }
            System.out.println(activityNames.size() + ". Back");
            int activityId = scanner.nextInt();
            if (activityId == activityNames.size()) {
                break;
            }
            String ActivityId = scanner.next().trim();
            switch (ActivityId){
                case "A01":
                    purchase(connection, loyaltyProgramId);
                    break;
                case "A02":
                    break;
                case "A03":
                    break;

            }

        } while (true);
    }


}
