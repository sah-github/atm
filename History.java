import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class History {
    public void Transaction(User u) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm", "root", "Sahithi@04");
            PreparedStatement ps = conn
                    .prepareStatement("Select * from history where receivedFrom=(?) or sentTo = (?)");
            ps.setInt(1, u.accno);
            ps.setInt(2, u.accno);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if(!rs.next()) {
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
