import java.sql.*;
import java.util.Scanner;

public class BrandLogin {

    public static void verifyLogin(Connection connection){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Please enter brand's name and password, split with comma:");
            System.out.println("To return to the root menu, please enter 0.");
            String nameAndPassword = scanner.next();
            if (nameAndPassword.equals("0"))
                return;
            ResultSet resultSet = null;
            try{
                String[] nameAndPasswordSplit = nameAndPassword.split(",");
                String name = nameAndPasswordSplit[0], password = nameAndPasswordSplit[1];
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(String.format("SELECT * FROM BRAND WHERE NAME='%s' AND PASSWORD='%s'", name, password));
                if (!resultSet.next())
                    System.out.println("Wrong brand's name or password entered! Please try again.");
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
            System.out.println("---------- Brand Main Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("0. Exit\n1. Add Loyalty Program\n2. Add RE Rules\n3. Update RE Rules\n4. Add RR Rules\n" +
                    "5. Update RR Rules\n6. Validate Loyalty Program\n7. Log out");

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
