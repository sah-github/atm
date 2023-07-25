import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class User {
    int accno;
    String pwd;
    float balance;
    String bname;
    String branch;

    User() {
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        // Test t = new Test();
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
                        if (rs.getString(2).equals(this.pwd)) {
                            System.out.println("LOGIN SUCCESSFUL");
                            PreparedStatement ps4 = c.prepareStatement("select * from accdetails where accno=(?)");
                            PreparedStatement trig = c.prepareStatement(
                                    "insert into history(amt,type,sentTo,receivedFrom) values(?,?,?,?)");
                            ps4.setInt(1, this.accno);
                            ResultSet ad = ps4.executeQuery();
                            ad.next();
                            this.balance = ad.getFloat(2);
                            this.bname = ad.getString(3);
                            this.branch = ad.getString(4);
                            System.out.println(
                                    "\n1.Check Balance"+"   "+"2.Credit"+"   "+"3.Debit"+"   "+"4.Check details"+"   "+"5.Tansfer"+"   "+"6.Transaction History"+"   "+"7.Close Account"+"   "+"8.Exit");
                            Transaction t = new Transaction();
                            int choice = 0;
                            boolean flag = true;
                            while (flag != false) {
                                System.out.println("Enter choice:");
                                choice = sc.nextInt();
                                switch (choice) {
                                    case (1):
                                        // float tot_amt=t.totalAmt(this);
                                        System.out.println("Current Balance=" + this.balance);
                                        break;
                                    case (2):
                                        t.credit(this);
                                        trig.setFloat(1, t.amt);
                                        trig.setString(2, "credited to");
                                        trig.setInt(3, this.accno);
                                        trig.setInt(4, this.accno);
                                        trig.executeUpdate();
                                        break;
                                    case (3):
                                        t.withdraw(this);
                                        trig.setFloat(1, t.amt);
                                        trig.setString(2, "debited from");
                                        trig.setInt(3, this.accno);
                                        trig.setInt(4, this.accno);
                                        trig.executeUpdate();
                                        break;

                                    case (4):
                                        AccDetails ac = new AccDetails();
                                        ac.printDetails(this);
                                        break;
                                    case (5):
                                        System.out.println("Enter account number: ");
                                        int sendTo = sc.nextInt();

                                        try {

                                            Statement st = c.createStatement();
                                            ResultSet rs1 = st.executeQuery(
                                                    "select balance from accdetails where accno = " + sendTo);
                                            rs1.next();
                                            float[] arr = new float[2];
                                            arr = t.transfer(this, rs1.getInt(1));
                                            st.executeUpdate("update accdetails set balance = " + arr[1]
                                                    + "where accno = " + sendTo);
                                            st.executeUpdate("update accdetails set balance = " + arr[0]
                                                    + "where accno = " + this.accno);

                                        } catch (Exception e) {
                                            System.out.println("Invalid account number");
                                        }
                                        trig.setFloat(1, t.amt);
                                        trig.setString(2, "transfered from");
                                        trig.setInt(3, sendTo);
                                        trig.setInt(4, this.accno);
                                        trig.executeUpdate();
                                        break;
                                    case (6):
                                        History hist = new History();
                                        hist.Transaction(this);
                                        break;
                                    case(7):
                                       CloseAcc cAcc=new CloseAcc();
                                       cAcc.deleteacc(this);
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
                    Pass p=new Pass();
                    do{
                    System.out.println("Enter password:");
                    this.pwd = sc.next();
                    
                    }
                    while(!p.isValidPassword(this));
                        
                    
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
}