import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class AccesoBaseDatos{
    public List<List<String>> consultaBaseDatos(String consulta){
        String server =  "LAPTOP-28NT836M";
        String instance = "localhost";
        String port = "1433";
        String database = "Aseni";
        String user = "prueba";
        String password = "prueba";
        List<List<String>> tabla = new ArrayList<>();
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://"+server+"\\"+instance+":"+port+";databaseName="+database+";user="+user+";password="+password+";");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(consulta);
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int numCols = rsmd.getColumnCount();
                List<String> fila = new ArrayList<>(numCols);
                for(int i = 1; i <= numCols; i++) {
                    fila.add(rs.getString(i));
                }
                tabla.add(fila);
            }
        }catch(SQLException e){
            System.out.println("Error SQL: "+e.getMessage());
        }
        return tabla;
    }
}