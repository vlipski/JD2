package my.pv;


import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.*;

public class helloMysqlTestt {

    @Test
    public void testConnection() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hello_mysql", "root", "Skatina964");
            PreparedStatement ps = connection.prepareStatement("select * from department");
            ResultSet rs = ps.executeQuery();
            assertNotNull(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
