package proyectobases11;

import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import oracle.jdbc.OracleType;

public class ConnectionORCL {

    private String password;
    private Connection conn;

    public ConnectionORCL(String pass) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.password = pass;
        this.conn = DriverManager.getConnection("jdbc:oracle:thin:@dbempresagrupo1.chjz8vr5lh2z.us-east-1.rds.amazonaws.com:1521:EMPRESA", "admin", password);
    }

    public void conexion(String pass) throws ClassNotFoundException, SQLException {
        if (conn != null && !conn.isClosed()) {
            System.out.println("Connected to the database.");
        } else {
            System.out.println("Not connected to the database.");
        }
    }

    public void insertIntoTabla_Tienda(int a, String b, String c) throws SQLException {
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

    public void insertIntoTabla_Cliente(int id, String namae, String mail, String password, String usern) throws SQLException {
        CallableStatement callableSTM = null;
        String callfuncion = "{ ? = call insert_into_clientes(?, ?, ?, ?, ?) }";
        callableSTM = conn.prepareCall(callfuncion);
        callableSTM.registerOutParameter(1, Types.VARCHAR);
        callableSTM.setInt(2, id);
        callableSTM.setString(3, namae);
        callableSTM.setString(4, mail);
        callableSTM.setString(5, password);
        callableSTM.setString(6, usern);
        callableSTM.execute();
        String result = callableSTM.getString(1);
        JOptionPane.showMessageDialog(null, result);
    }

    public void modify() {

    }

    public String[] search(String functionOracle, String searchUsername) throws SQLException {

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        callableStatement = conn.prepareCall("{ ? = call " + functionOracle + "(?) }");
        callableStatement.registerOutParameter(1, Types.REF_CURSOR);
        callableStatement.setString(2, searchUsername);
        callableStatement.execute();
        resultSet = (ResultSet) callableStatement.getObject(1);
        String[] result = new String[5];
        if (resultSet.next()) {
            
            result[0] = resultSet.getString("username");
            result[1]= resultSet.getString("pass");
            return result;
        } else {
            result[0] = resultSet.getString("message"); 
            
            return result;
        }

    }

    public void setUsernameInContext(String username) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("BEGIN my_package.set_username('" + username + "'); END;");
        }
    }

    public void fJTable1(JTable tabla, String functionOracle) throws SQLException {
        ((DefaultTableModel) tabla.getModel()).setRowCount(0);
        CallableStatement callableStatement = conn.prepareCall("{ ? = call " + functionOracle + "() }");
        callableStatement.registerOutParameter(1, Types.REF_CURSOR);
        callableStatement.execute();

        // Retrieve the result set
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Create column names array
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Set up the table model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tabla.setModel(model);

        // Populate the table model with rows from the result set
        while (resultSet.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = resultSet.getObject(i);
            }
            model.addRow(row);
        }

    }

    public void deletefromtable(String functionOracle, int searchID) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall("{ ? = call " + functionOracle + "(?) }");
        callableStatement.registerOutParameter(1, Types.VARCHAR);
        callableStatement.setInt(2, searchID);
        callableStatement.execute();
        String result = callableStatement.getString(1);
        JOptionPane.showMessageDialog(null, result);
    }

}
