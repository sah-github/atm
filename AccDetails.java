import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
public class AccDetails {
    int accno;
    float balance;
    String bankName;
    String branch;
    public void printDetails(User u) {
        try {
            Connection c1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm","root","Sahithi@04");
            Statement st = c1.createStatement();
            ResultSet rs = st.executeQuery("select * from accdetails where accno="+u.accno);
            System.out.println(u.accno);
            rs.next();
            System.out.println("Acc no: "+ rs.getInt(1)+"\nBalance: "+rs.getFloat(2)+"\nBank Name: "+rs.getString(3)+"\nBranch: "+rs.getString(4));
            System.out.println("y");
            c1.close();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
