import java.sql.*;
import java.util.Scanner;

public class BrandLogin {
    static String BrandID;
    boolean isValidated = false;
    static String ProgramID;

    public static void verifyLogin(Connection connection){
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (true){
            System.out.println("Please enter brand's ID and password, split with comma:");
            System.out.println("To return to the root menu, please enter 0.");
            String IDAndPassword = scanner.next();
            if (IDAndPassword.equals("0"))
                return;
            ResultSet resultSet = null;
            try{
                String[] IDAndPasswordSplit = IDAndPassword.split(",");
                BrandID = IDAndPasswordSplit[0].trim();
                String password = IDAndPasswordSplit[1].trim();
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(String.format("SELECT * FROM BRAND WHERE BrandID='%s' AND PASSWORD='%s'", BrandID, password));
                if (!resultSet.next())
                    System.out.println("Wrong brand's ID or password entered! Please try again.");
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
        scanner.useDelimiter("\n");

        int choice = 0;
        do {
            System.out.println("---------- Brand Main Page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Add Loyalty Program\n2. Add RE Rules\n3. Update RE Rules\n4. Add RR Rules\n" +
                    "5. Update RR Rules\n6. Validate Loyalty Program\n7. Log out");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addLoyaltyProgram(connection);
                    break;
                case 2:
//                    addRERules(connection);
                    break;
                case 3:
//                    updateRERules(connection);
                    break;
                case 4:
//                    addRRRules(connection);
                    break;
                case 5:
//                    updateRRRules(connection);
                    break;
                case 6:
//                    validateProgram(connection);
                    break;
                case 7:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 7);
    }

    public static void addLoyaltyProgram(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        int choice = 0;
        do {
            System.out.println("---------- Brand: LoyaltyProgram ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Add Regular Program\n2. Add Tier Program\n3. Go Back");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addRegularProgram(connection);
                    break;
                case 2:
                    addTierProgram(connection);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 3);
    }

    public static void addRegularProgram(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        int choice = 0;

        while (true) {
            System.out.println("Please enter your Loyalty Program ID & Name, split with comma:");
            System.out.println("To return to the Brand: LoyaltyProgram page, please enter 0.");
            String IDAndName = scanner.next();
            if (IDAndName.equals("0"))
                return;
            ResultSet resultSet = null;
            try {
                String[] nameAndPasswordSplit = IDAndName.split(",");
                ProgramID = nameAndPasswordSplit[0].trim();
                String name = nameAndPasswordSplit[1].trim();
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(String.format("INSERT INTO REGULARLOYALTYPROGRAM VALUES ('%s', '%s', '%s', 0)", ProgramID, name, BrandID));
                System.out.println("Create new Regular Program Successfully!");
                break;
            } catch (SQLException e) {
                System.out.println("The Program ID is already exists.");
//                e.printStackTrace();
            }
        }


        do {
            System.out.println("---------- Brand: Regular page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Activity Types\n2. Reward Types\n3. Go Back");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    activityTypes(connection);
                    break;
                case 2:
                    rewardTypes(connection);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 3);
    }

    public static void activityTypes(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        ResultSet resultSet = null;
        int choice = 0;
        do {
            System.out.println("---------- Brand: Activity Types ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Purchase\n2. Leave a review\n3. Refer a friend\n4. Go Back");
            try{
                choice = scanner.nextInt();
                Statement statement = connection.createStatement();
                switch (choice) {
                    case 1:
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_ACTIVITY VALUES ('%s', 'A01')", ProgramID));
                        System.out.println("The activity purchase is added to your program " + ProgramID);
                        break;
                    case 2:
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_ACTIVITY VALUES ('%s', 'A02')", ProgramID));
                        System.out.println("The activity leave a review is added to your program " + ProgramID);
                        break;
                    case 3:
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_ACTIVITY VALUES ('%s', 'A03')", ProgramID));
                        System.out.println("The activity refer a friend is added to your program " + ProgramID);
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invaild option entered. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("The activity you chose is already in your program, you can choose another one.");
            }
        } while (choice != 4);
    }

    public static void rewardTypes(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        ResultSet resultSet = null;
        int choice = 0;
        int quantity = 0;
        do {
            System.out.println("---------- Brand: Activity Types ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1. Gift Card\n2. Free Product\n3. Go Back");
            try{
                choice = scanner.nextInt();
                Statement statement = connection.createStatement();
                switch (choice) {
                    case 1:
                        System.out.println("Please enter the quantity of the reward Gift Card");
                        quantity = scanner.nextInt();
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_REWARD VALUES ('%s', 'R01', '%s')", ProgramID, quantity));
                        System.out.println("The reward Gift Card is added to your program " + ProgramID);
                        break;
                    case 2:
                        System.out.println("Please enter the quantity of the reward Free Product");
                        quantity = scanner.nextInt();
                        resultSet = statement.executeQuery(String.format("INSERT INTO LOYALTY_PROGRAM_HAS_REWARD VALUES ('%s', 'R02')", ProgramID));
                        System.out.println("The reward Free Product is added to your program " + ProgramID);
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Invaild option entered. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("The reward you chose is already in your program, you can choose another one.");
            }
        } while (choice != 4);
    }

    public static void addTierProgram(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        int choice = 0;
        do {
            System.out.println("---------- Brand: Tier page ----------");
            System.out.println("Please enter your option: ");
            System.out.println("1.Tiers Set up\n2. Activity Types\n3. Reward Types\n4. Go Back");

            choice = scanner.nextInt();
            switch (choice) {
                case 1:
//                    tiersSetUp(connection);
                    break;
                case 2:
//                    activityTypes(connection);
                    break;
                case 3:
//                    rewardTypes(connection);
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invaild option entered. Please try again.");
                    break;
            }
        } while (choice != 0);
    }
}
