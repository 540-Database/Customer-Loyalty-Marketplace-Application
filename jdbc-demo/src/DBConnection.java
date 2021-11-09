import java.sql.*;
import java.util.Scanner;

public class DBConnection {
    static final String jdbcURL = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl01";
    static Connection connection = null;

    /**
     * @param user
     * @param password
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void setConnection(String user, String password) throws ClassNotFoundException, SQLException {
        try {

            Class.forName("oracle.jdbc.OracleDriver");

            Statement stmt = null;
            ResultSet rs = null;

            try {
                connection = DriverManager.getConnection(jdbcURL, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            finally {
//                close(rs);
//                close(stmt);
//                close(connection);
//            }
        } catch (Throwable oops) {
            oops.printStackTrace();
        }


    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // connect to the Oracle server
        setConnection("", "");
        Scanner scanner = new Scanner(System.in);
        int choice;
        System.out.println("\nWelcome to the Customer Loyalty Marketplace Application!");
        do {
            System.out.println("\n---------- Main Menu ----------");
            System.out.println("Enter the choice: \n0. Exit\n1. Admin Login\n2. Brand Login\n3. Customer Login\n4. Sign Up\n5. Show Queries\n");
            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    close(connection);
                    System.out.println("Goodbye!");
                    break;
                case 1:
                    AdminLogin.run(connection);
                    break;
                case 2:
                    BrandLogin.run(connection);
                    break;
                case 3:
                    CustomerLogin.run(connection);
                    break;
                case 4:
                    SignUp.run(connection);
                    break;
                case 5:
                    showQueries.run(connection);
                    break;
                default:
                    System.out.println("Input error, please retry.\n");
                    break;
            }
        } while (choice != 0);
    }

    static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Throwable whatever) {
            }
        }
    }

    static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (Throwable whatever) {
            }
        }
    }

    static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Throwable whatever) {
            }
        }
    }
}