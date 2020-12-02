
package logica; 

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;  
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class conexion {
    
    private final String base = "accesorios";
    private final String user = "root";
    private final String password = "";
    private final String url = "jdbc:mysql://127.0.0.1:3306/" + base;
    private Connection con = null;
    
    public Connection getConexion(){
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(this.url, this.user, this.password);
            
        } catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error al Conectarse a la base de datos");
            System.err.println(e);
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error al Conectarse a la base de datos");
            Logger.getLogger(conexion.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
      return con;  
    }

}
