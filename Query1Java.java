import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

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

class HiloEntregablesCanton extends Thread{
    int idCanton = 0;
    String nombreCanton = "";

    public HiloEntregablesCanton(int idCanton, String nombreCanton){
        this.idCanton = idCanton;
        this.nombreCanton = nombreCanton;
    }

    public void run(){
        List<List<String>> entregables = new AccesoBaseDatos().consultaBaseDatos("EntregablesCanton " + idCanton);
        String resultado = "----- "+nombreCanton+" -----\n";
        for(int i=0; i<entregables.size(); i++){
            resultado += (
                "  Descripción: "+entregables.get(i).get(0) + "\n" +
                "  Fecha de cumplimiento: "+entregables.get(i).get(1) + "\n" +
                "  Valor de kpi: "+entregables.get(i).get(2) + "\n" +
                "  Unidad de kpi: "+entregables.get(i).get(3) + "\n"
            );
        }
        resultado += "\n---------------";
        System.out.println(resultado);
    }
}



public class Query1Java {
    static int generarNumeroAleatorio(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    static void crearHilos(){
        List<List<String>> cantones = new AccesoBaseDatos().consultaBaseDatos("ObtenerCantones");
        for(int i=0; i<10; i++){
            int posCanton = generarNumeroAleatorio(0, cantones.size());
            HiloEntregablesCanton hilo = new HiloEntregablesCanton(
                Integer.parseInt(cantones.get(posCanton).get(0)),
                cantones.get(posCanton).get(1)
            );
            hilo.start();
            cantones.remove(posCanton);
        }
    }

    public static void main(String[] args) {
        crearHilos();
    }
}