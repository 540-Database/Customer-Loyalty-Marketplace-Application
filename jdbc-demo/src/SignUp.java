import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class SignUp {

    public static void run(Connection connection) throws SQLException {
        //verifyLogin(connection);
        Scanner scanner = new Scanner(System.in);

        int choice = 0;
        do {
            System.out.println("---------- Signup Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("0. Exit\n1. Sign up as customer\n2. Sign up as brand\n");

            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    break;
                case 1:
                    registerAsCustomer(connection);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 0);
    }

    public static void registerAsCustomer(Connection connection){
        System.out.println("Please enter your customerID, password, name, phone, address, walletID. (split by ',')");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] sp = input.split(",");
        System.out.println(sp.length);
        if (sp.length != 6) {
            System.out.println("Input format error!!!");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sp.length; i++) {
            if("NULL".equals(sp[i])){
                sb.append(sp[i].trim());
            }else{
                sb.append(String.format("'%s'", sp[i].trim()));
            }
            if (i != sp.length - 1) {
                sb.append(",");
            }
        }

        String sql = String.format("INSERT INTO %s VALUES (%s)","CUSTOMER",sb);
        System.out.println(sql);
        try {
            connection.createStatement().executeUpdate(sql);
            System.out.println("Congrats! You sign up as customer successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // System.out.println(sql);
    }


}
