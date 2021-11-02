import java.sql.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AdminLogin {

    public static void verifyLogin(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (true) {
            System.out.println("\nPlease enter admin's user name and password, split with comma:");
            System.out.println("To return to the root menu, please enter 0.");
            String nameAndPassword = scanner.next();
            if (nameAndPassword.equals("0"))
                return;
            ResultSet resultSet = null;
            try {
                String[] nameAndPasswordSplit = nameAndPassword.split(",");
                String name = nameAndPasswordSplit[0].trim(), password = nameAndPasswordSplit[1].trim();
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(String.format("SELECT * FROM ADMIN WHERE ADMINID='%s' AND PASSWORD='%s'", name, password));
                if (!resultSet.next())
                    System.out.println("Wrong admin username or password entered! Please try again.");
                else {
                    System.out.println("\nLogin success! Welcome~");
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void run(Connection connection) throws SQLException {
        verifyLogin(connection);
        Scanner scanner = new Scanner(System.in);

        int choice = 0;
        do {
            System.out.println("\n---------- Admin Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("0. Exit\n1. Add brand\n2. Add customer\n3. Show brand's info\n4. Show customer's info\n" +
                    "5. Add activity type\n6. Add reward type\n7. Log out\n");

            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    break;
                case 1:
                    addBrand(connection);
                    break;
                case 2:
                    addCustomer(connection);
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

    public static void addBrand(Connection connection) throws SQLException {
        System.out.println("Please enter BrandID, password, name, address. (split by ',')");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] sp = input.split(",");
        System.out.println(sp.length);
        if (sp.length != 4) {
            System.out.println("Input format error!!!");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sp.length; i++) {
            if ("NULL".equals(sp[i])) {
                sb.append(sp[i].trim());
            } else {
                sb.append(String.format("'%s'", sp[i].trim()));
            }
            if (i != sp.length - 1) {
                sb.append(",");
            }
        }
        String now = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        sb.append(String.format(",to_date('%s','mm/dd/yyyy')", now));
        String sql = String.format("INSERT INTO %s VALUES (%s)", "BRAND", sb);
        try {
            connection.createStatement().executeUpdate(sql);
            System.out.println("Add brand successfully!\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCustomer(Connection connection) throws SQLException {
        System.out.println("Please enter customerID, password, name, phone, address, walletID. (split by ',')");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] sp = input.split(",");
        if (sp.length != 6) {
            System.out.println("Input format error!!!");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sp.length; i++) {
            if ("NULL".equals(sp[i])) {
                sb.append(sp[i].trim());
            } else {
                sb.append(String.format("'%s'", sp[i].trim()));
            }
            if (i != sp.length - 1) {
                sb.append(",");
            }
        }

        String sql = String.format("INSERT INTO %s VALUES (%s)", "CUSTOMER", sb);
        try {
            connection.createStatement().executeUpdate(sql);
            System.out.println("\nAdd customer successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
