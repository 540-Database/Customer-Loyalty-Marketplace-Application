import java.sql.*;
import java.util.Scanner;

public class CustomerLogin {

    public static void verifyLogin(Connection connection){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Please enter customer's user name and password, split with comma:");
            System.out.println("To return to the root menu, please enter 0.");
            String nameAndPassword = scanner.next();
            if (nameAndPassword.equals("0"))
                return;
            ResultSet resultSet = null;
            try{
                String[] nameAndPasswordSplit = nameAndPassword.split(",");
                String name = nameAndPasswordSplit[0], password = nameAndPasswordSplit[1];
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(String.format("SELECT * FROM CUSTOMER WHERE NAME='%s' AND PASSWORD='%s'", name, password));
                if (!resultSet.next())
                    System.out.println("Wrong customer username or password entered! Please try again.");
                else{
                    System.out.println("Login success! Welcome~");
                    break;
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void run(Connection connection) throws SQLException {
        verifyLogin(connection);
        Scanner scanner = new Scanner(System.in);

        int choice = 0;
        do {
            System.out.println("---------- Customer Main Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("0. Exit\n1. Enroll in a Loyalty Program\n2. Reward Activities\n3. View Wallet\n" +
                    "4. Redeem Points\n5. Log out");

            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
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
}
