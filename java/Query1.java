import java.util.List;
import java.lang.Math;

class HiloEntregablesCanton extends Thread{
    int idCanton = 0;
    String nombreCanton = "";

    public HiloEntregablesCanton(int idCanton, String nombreCanton){
        this.idCanton = idCanton;
        this.nombreCanton = nombreCanton;
    }

    public void run(){
        // Se llama al procedimiento almacenado que lista los entregables de un cantón
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
        new Temporizador().finalizarHilo();
    }
}

public class Query1{
    static int generarNumeroAleatorio(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    static void crearHilos(){
        new Temporizador().iniciarTemporizador();
        // Se llama al procedimiento almacenado para obtener los cantones
        List<List<String>> cantones = new AccesoBaseDatos().consultaBaseDatos("ObtenerCantones");
        for(int i=0; i<10; i++){
            int posCanton = generarNumeroAleatorio(0, cantones.size());
            // Se crean los hilos
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