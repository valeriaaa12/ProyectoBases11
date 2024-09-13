package proyectobases11;

import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;
        
public class ConnectionORCL {
    private String password;
    private Connection conn;
    public ConnectionORCL(String pass)  throws ClassNotFoundException, SQLException{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.password = pass;
        this.conn = DriverManager.getConnection("jdbc:oracle:thin:@dbempresagrupo1.chjz8vr5lh2z.us-east-1.rds.amazonaws.com:1521:EMPRESA", "admin", password);
    }
    
    public void conexion(String pass) throws ClassNotFoundException, SQLException{
        if (conn != null && !conn.isClosed()) {
            System.out.println("Connected to the database.");
        } else {
            System.out.println("Not connected to the database.");
}
    }
    
    public void insertIntoTabla(int a, String b, String c) throws SQLException{
        // 2. Prepare the PL/SQL function call
            CallableStatement callableStmt = null;
            String functionCall = "{ ? = call insert_into_tiendas(?, ?, ?) }";

            // 3. Create a CallableStatement for executing the function
            callableStmt = conn.prepareCall(functionCall);

            // 4. Register the return type (VARCHAR2 in this case)
            callableStmt.registerOutParameter(1, Types.VARCHAR);
            // 5. Set input parameters (p_idT, p_nombre, p_horario)
            callableStmt.setInt(2, a);                // p_idT (integer)
            callableStmt.setString(3, b);     // p_nombre (string)
            callableStmt.setString(4, c); // p_horario (string)

            // 6. Execute the function
            callableStmt.execute();

            // 7. Retrieve the returned message from the function
            String result = callableStmt.getString(1);
            System.out.println("Function Result: " + result);
        
    }
    
}
