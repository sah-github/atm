import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    int accno;
    String pwd;
    float balance;
    String bname;
    String branch;

    //User() {}
    
    public void start() {
        Accounts t = new Accounts(this);
        Scanner sc = new Scanner(System.in);
        try {
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm", "root", "Sahithi@04");
            System.out.println("1.Login \t 2.sign up");
            int n = sc.nextInt();
            switch (n) {
                case 1:
                    System.out.println("Enter Account No");
                    PreparedStatement ps1 = c.prepareStatement("select * from accounts where accno=(?)");
                    this.accno = sc.nextInt();
                    ps1.setInt(1, this.accno);
                    ResultSet rs = ps1.executeQuery();
                    if (rs.next()) {
                        System.out.println("Enter password:");
                        this.pwd = sc.next();
                        t.fetchDetails(this);
                        if (rs.getString(2).equals(this.pwd)) {
                            System.out.println("LOGIN SUCCESSFUL");
                            System.out.println(
                                    "\n1.Check Balance" + "   " + "2.Credit" + "   " + "3.Debit" + "   "
                                            + "4.Check details" + "   " + "5.Transfer" + "   " + "6.Transaction History"
                                            + "   " + "7.Close Account" + "   " + "8.Exit");
                            int choice = 0;
                            boolean flag = true;
                            while (flag != false) {
                                System.out.println("Enter choice:");
                                choice = sc.nextInt();
                                switch (choice) {
                                    case (1):
                                        System.out.println("Current Balance=" + this.balance);
                                        break;
                                    case (2):
                                        t.credit(this);
                                        break;
                                    case (3):
                                        t.withdraw(this);
                                        break;
                                    case (4):
                                        t.printDetails(this);
                                        break;
                                    case (5):
                                        t.updateTransaction(this);
                                        break;
                                    case (6):
                                        t.TransactionDetails(this);
                                        break;
                                    case (7):
                                        t.deleteacc(this);
                                        break;
                                    case (8):
                                        flag = false;
                                        break;
                                    default:
                                        System.out.println("Invalid choice");
                                }
                                PreparedStatement ps3 = c
                                        .prepareStatement("update accdetails set balance=? where accno=" + this.accno);
                                ps3.setFloat(1, this.balance);
                                ps3.executeUpdate();
                            }

                        } else {
                            System.out.println("Invalid password");
                        }
                    } else {
                        System.out.println("Invalid credentials");
                    }
                    ps1.close();
                    break;
                case 2:
                    PreparedStatement ps2 = c.prepareStatement("insert into accdetails values(?,?,?,?)");
                    Statement st = c.createStatement();
                    System.out.println("Enter Account number");
                    this.accno = sc.nextInt();
                    System.out.println("Enter BankName");
                    this.bname = sc.next();
                    System.out.println("Enter Branch :");
                    this.branch = sc.next();
                    do {
                        System.out.println("Enter password:");
                        this.pwd = sc.next();

                    } while (!isValidPassword(this));

                    String insertQuery = "INSERT INTO accounts VALUES (" + this.accno + ", '" + this.pwd + "')";
                    st.executeUpdate(insertQuery);
                    ps2.setInt(1, this.accno);
                    ps2.setFloat(2, this.balance);
                    ps2.setString(3, this.bname);
                    ps2.setString(4, this.branch);
                    int x = ps2.executeUpdate();
                    if (x == 1) {
                        System.out.println("Account created successfully");
                    }
                    ps2.close();

                    break;
            }
            c.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        sc.close();
    }

    public static boolean isValidPassword(User u) {
        String password = u.pwd;
        if (password.length() < 8) {
            System.out.println("Less than 8 characters");
            return false;
        }
        if (!containsUppercaseLetter(password)) {
            System.out.println("Doesn't contain UpperCase Letter");
            return false;
        }
        if (!containsLowercaseLetter(password)) {
            System.out.println("Doesn't contain LowerCase Letter");
            return false;
        }
        if (!containsDigit(password)) {
            System.out.println("Does't contain a Number");
            return false;
        }
        if (!containsSpecialCharacter(password)) {
            System.out.println("Doesn't contain Special Character");
            return false;
        }

        return true;
    }

    private static boolean containsUppercaseLetter(String password) {
        return password.matches(".*[A-Z].*");
    }

    private static boolean containsLowercaseLetter(String password) {
        return password.matches(".*[a-z].*");
    }

    private static boolean containsDigit(String password) {
        return password.matches(".*\\d.*");
    }

    private static boolean containsSpecialCharacter(String password) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }
}