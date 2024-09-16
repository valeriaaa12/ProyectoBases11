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

        
        callableStmt.registerOutParameter(1, Types.VARCHAR);
        
        callableStmt.setInt(2, a);                
        callableStmt.setString(3, b);     
        callableStmt.setString(4, c); 

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
    
    public void insertIntoTabla_Producto(int upc, int numero, String Nombre,int tam,String embalaje,String marca, double precio) throws SQLException{
        CallableStatement callableSTM = null;
        String callfuncion = "{ ? = call insert_into_productos(?, ?, ?, ?, ?, ?, ?) }";
        callableSTM = conn.prepareCall(callfuncion);
        callableSTM.registerOutParameter(1, Types.VARCHAR);
        callableSTM.setInt(2, upc);
        callableSTM.setInt(3, numero);
        callableSTM.setString(4, Nombre);
        callableSTM.setInt(5, tam);
        callableSTM.setString(6, embalaje);
        callableSTM.setString(7, marca);
        callableSTM.setDouble(8, precio);
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

        
        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

       
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tabla.setModel(model);

       
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
