import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class showQueries {
    public static void run(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("Select Query: \n");
            System.out.println("0. Exit\n" +
                    "1. List all customers that are not part of Brand02’s program.\n" +
                    "2. List customers that have joined a loyalty program but have not participated in any activity in that program (list the customerid and the loyalty program id).\n" +
                    "3. List the rewards that are part of Brand01 loyalty program.\n" +
                    "4. List all the loyalty programs that include “refer a friend” as an activity in at least one of their reward rules.\n" +
                    "5. For Brand01, list for each activity type in their loyalty program, the number instances that have occurred.\n" +
                    "6. List customers of Brand01 that have redeemed at least twice.\n" +
                    "7. All brands where total number of points redeemed overall is less than 500 points\n" +
                    "8. For Customer C0003, and Brand02, number of activities they have done in the period of 08/1/2021 and 9/30/2021.\n");
            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    break;
                case 1:
                    query(connection, 1);
                    break;
                case 2:
                    query(connection, 2);
                    break;
                case 3:
                    query(connection, 3);
                    break;
                case 4:
                    query(connection, 4);
                    break;
                case 5:
                    query(connection, 5);
                    break;
                case 6:
                    query(connection, 6);
                    break;
                case 7:
                    query(connection, 7);
                    break;
                case 8:
                    query(connection, 8);
                    break;
                default:
                    System.out.println("Input error, please try again.\n");
            }
        } while (choice != 0);
    }

    public static void query(Connection connection, int i) {

        String q1 = "select CUSTOMERID\n" +
                "from WALLET\n" +
                "where CUSTOMERID not in\n" +
                "      (select CUSTOMERID\n" +
                "       from WALLET\n" +
                "       where BRANDID = 'Brand02')";

        String q2 = "select CUSTOMERID, LOYALTY_PROGRAM_ID\n" +
                "from WALLET\n" +
                "where TOTALPOINTS = 0";

        String q3 = "select REWARDNAME\n" +
                "from REWARD\n" +
                "where REWARDID in\n" +
                "      (select REWARDID\n" +
                "       from LOYALTY_PROGRAM_HAS_REWARD\n" +
                "       where LOYALTY_PROGRAM_ID =\n" +
                "             (select LOYALTY_PROGRAM_ID\n" +
                "              from REGULARLOYALTYPROGRAM\n" +
                "              where BRANDID = 'Brand01')\n" +
                "      )";

        String q4 = "select LOYALTY_PROGRAM_ID\n" +
                "from RERULES\n" +
                "where ACTIVITYID =\n" +
                "      (select ACTIVITYID\n" +
                "       from ACTIVITY\n" +
                "       where ACTIVITYNAME = 'Refer a friend')";

        String q5 = "select A.ACTIVITYNAME, count(*)\n" +
                "from CUSTOMERACTIVITIES C,\n" +
                "     ACTIVITY A\n" +
                "where C.BrandID = 'Brand01'\n" +
                "  and C.ACTIVITYID = A.ACTIVITYID\n" +
                "  and C.ACTIVITYID in (select ACTIVITYID\n" +
                "                       from LOYALTY_PROGRAM_HAS_ACTIVITY\n" +
                "                       where LOYALTY_PROGRAM_ID = (select LOYALTY_PROGRAM_ID\n" +
                "                                                   from REGULARLOYALTYPROGRAM\n" +
                "                                                   where BRANDID = 'Brand01'))\n" +
                "group by A.ACTIVITYID, A.ACTIVITYNAME";

        String q6 = "select CUSTOMER.CUSTOMERID, CUSTOMER.NAME\n" +
                "from REDEEMRECORD,\n" +
                "     CUSTOMER\n" +
                "where BRANDID = 'Brand01'\n" +
                "  and CUSTOMER.CUSTOMERID = REDEEMRECORD.CUSTOMERID\n" +
                "group by CUSTOMER.CUSTOMERID, CUSTOMER.NAME\n" +
                "having count(*) > 1";

        String q7 = "select BRANDID\n" +
                "from REDEEMRECORD\n" +
                "group by BRANDID\n" +
                "having sum(POINTREDEEMED) < 500";

        String q8 = "select count(*)\n" +
                "from CUSTOMERACTIVITIES\n" +
                "where CUSTOMERID = 'C0003'\n" +
                "  and BRANDID = 'Brand02'\n" +
                "  and ACTIVITYDATE >= to_date('08/01/2021', 'mm/dd/yyyy')\n" +
                "  and ACTIVITYDATE <= to_date('09/30/2021', 'mm/dd/yyyy')";

        switch (i) {
            case 1:
                queryRun(connection, q1);
                break;
            case 2:
                queryRun(connection, q2);
                break;
            case 3:
                queryRun(connection, q3);
                break;
            case 4:
                queryRun(connection, q4);
                break;
            case 5:
                queryRun(connection, q5);
                break;
            case 6:
                queryRun(connection, q6);
                break;
            case 7:
                queryRun(connection, q7);
                break;
            case 8:
                queryRun(connection, q8);
                break;
        }
    }

    public static void queryRun(Connection connection, String sql) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println("");
            }
            System.out.println("");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
