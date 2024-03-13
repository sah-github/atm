import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Accounts {
    Connection c;
    PreparedStatement trig;
    float balance ;
    int accno;
    

    Accounts(User u) {
        this.accno = u.accno;
        this.balance = u.balance;
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm", "root", "Sahithi@04");

            trig = c.prepareStatement(
                    "insert into history(amt,type,sentTo,receivedFrom) values(?,?,?,?)");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    Scanner sc = new Scanner(System.in);
    float amt = 0;

    public void withdraw(User u) throws Exception {
        System.out.println("Enter the amount to withdraw:");
        this.amt = sc.nextInt();
        if (u.balance < this.amt) {
            System.out.println("INSUFFICIENT BALANCE");
            return;
        }
        u.balance = u.balance - this.amt;
        trig.setFloat(1, this.amt);
        trig.setString(2, "debited from");
        trig.setInt(3, u.accno);
        trig.setInt(4, u.accno);
        trig.executeUpdate();
        System.out.println("DEBITED");
    }

    public void credit(User u) throws Exception {
        try {
            System.out.println("Enter the amount to credit:");
            this.amt = sc.nextInt();
            u.balance = u.balance + this.amt;
            System.out.println("CREDITED");
            trig.setFloat(1, this.amt);
            trig.setString(2, "credited to");
            trig.setInt(3, u.accno);
            trig.setInt(4, u.accno);
            trig.executeUpdate();
        } catch (InputMismatchException e) {
            System.out.println("Invalid entry");
        }
    }

    public float[] transfer(User u1, float balance) {
        float[] arr = new float[3];
        arr[2] = 0;
        System.out.println("Enter amount: ");
        this.amt = sc.nextInt();
        if (u1.balance < this.amt) {
            System.out.println("Insufficient balance");
            arr[0] = u1.balance;
            arr[1] = balance;
            arr[2] = amt;
            return arr;
        }
        balance = balance + this.amt;
        u1.balance = u1.balance - this.amt;
        arr[0] = u1.balance;
        arr[1] = balance;
        arr[2] = this.amt;
        System.out.println("Transaction successful");
        return arr;
    }

    public void fetchDetails(User u) throws Exception{
        PreparedStatement ps4 = c.prepareStatement("select * from accdetails where accno=(?)");
        ps4.setInt(1, u.accno);
        ResultSet ad = ps4.executeQuery();
        ad.next();
        u.balance = ad.getFloat(2);
        u.bname = ad.getString(3);
        u.branch = ad.getString(4);
    }

    public void updateTransaction(User u) {
        System.out.println("Enter account number: ");
        int sendTo = sc.nextInt();
        float[] arr = new float[2];
        try {
            Statement st = c.createStatement();
            ResultSet rs1 = st.executeQuery(
                    "select balance from accdetails where accno = " + sendTo);
            if (rs1.next()) {
                arr = transfer(u, rs1.getInt(1));
                st.executeUpdate("update accdetails set balance = " + arr[1]
                        + "where accno = " + sendTo);
                st.executeUpdate("update accdetails set balance = " + arr[0]
                        + "where accno = " + u.accno);
            } else {
                System.out.println("Account doesn't exist");
            }
            if (arr[0] > arr[2]) {
                trig.setFloat(1, this.amt);
                trig.setString(2, "transfered from");
                trig.setInt(3, sendTo);
                trig.setInt(4, u.accno);
                trig.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Invalid account number");
        }

    }

    void deleteacc(User u) throws Exception {
        Statement st = c.createStatement();
        st.executeUpdate("delete from accdetails where accno=" + u.accno);
        st.executeUpdate("delete from accounts where accno=" + u.accno);
        System.out.println("Account deleted succesfully");
        st.close();
    }

    public void printDetails(User u) throws Exception {
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery("select * from accdetails where accno=" + u.accno);
        System.out.println(u.accno);
        rs.next();
        System.out.println("Acc no: " + rs.getInt(1) + "\nBalance: " + rs.getFloat(2) + "\nBank Name: "
                + rs.getString(3) + "\nBranch: " + rs.getString(4));
    }

    public void TransactionDetails(User u) {
        try {
            // cection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm",
            // "root", "Sahithi@04");
            PreparedStatement ps = c
                    .prepareStatement("Select * from history where receivedFrom=(?) or sentTo = (?)");
            ps.setInt(1, u.accno);
            ps.setInt(2, u.accno);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (!rs.next()) {
                System.out.println("No transaction history found");
            }
            while (rs != null) {
                if (rs.getFloat(2) != 0) {
                    if ("credited to".equals(rs.getString(3))) {
                        System.out.println(rs.getTimestamp(1) + " " + rs.getFloat(2) + " credited");
                    } else if ("debited from".equals(rs.getString(3))) {
                        System.out.println(rs.getTimestamp(1) + " " + rs.getFloat(2) + " " + "withdrawn");
                    } else {
                        if (rs.getInt(4) == u.accno) {
                            System.out.println(
                                    rs.getTimestamp(1) + " " + rs.getFloat(2) + " credited by " + rs.getInt(5));
                        } else {
                            System.out
                                    .println(rs.getTimestamp(1) + " " + rs.getFloat(2) + " debited to " + rs.getInt(4));
                        }
                    }
                }
                rs.next();
            }
        } catch (Exception e) {
            // System.out.println(e);
        }
    }
}