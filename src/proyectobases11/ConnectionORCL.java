package proyectobases11;

import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;
        
public class ConnectionORCL {
    private String password;
    public ConnectionORCL()  throws ClassNotFoundException{
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }
    
    public void conexion(String pass) throws ClassNotFoundException, SQLException{
        this.password = pass;
        java.sql.Connection con = DriverManager.getConnection("jdbc:oracle:thin:@dbempresagrupo1.chjz8vr5lh2z.us-east-1.rds.amazonaws.com:1521:EMPRESA", "admin", password);
        System.out.println("se conecto aparentemente");
    }
    
}
