package Lab9;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTest {

    public static void main(String[] args) {
        Connection conn = null;
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/lab9?user=root&password=zhangzhiheng");
            st = conn.createStatement();
            // rs = st.executeQuery("SELECT * from Student where fname='" + name + "'");
            ps = conn.prepareStatement("select Lab9.grades.ClassName, count(Lab9.grades.ClassName) as 'Number of Students' from lab9.grades GROUP BY ClassName order by `Number of Students`");
            // set first variable in prepared statement
            rs = ps.executeQuery();
            while (rs.next()) {
                String fname = rs.getString("ClassName");
                String lname = rs.getString("Number of Students");
                System.out.printf("ClassName = %s Number of Students = %s\n", fname, lname);
            }
            System.out.println("------------------------------");
            ps = conn.prepareStatement("select g.ClassName, s.Name, g.grade from lab9.grades g, lab9.studentinfo s where s.SID = g.SID ");
            // set first variable in prepared statement
            rs = ps.executeQuery();
            while (rs.next()) {
                String fname = rs.getString("ClassName");
                String lname = rs.getString("Name");
                String grade = rs.getString("grade");
                System.out.printf("ClassName = \"%s\" Name = \"%s\" Grade = %s\n", fname, lname, grade);
            }
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println("sqle: " + sqle.getMessage());
            }
        }
    }
}