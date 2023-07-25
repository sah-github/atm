import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CloseAcc {
    
    void deleteacc(User u) throws Exception{
        Connection c1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm","root","Sahithi@04");
        Statement st = c1.createStatement();
        st.executeUpdate("delete from accdetails where accno="+u.accno);               
        st.executeUpdate("delete from accounts where accno="+u.accno);
        System.out.println("Account deleted succesfully");               
        st.close();
        c1.close();              

    
    }
}
