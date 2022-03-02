import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class HiloEntregablesCantones implements Runnable{
    public void run(){
        // Se realiza la consulta que lista la cantidad de entregables que reciben como mucho de 25% de los partidos
        List<List<String>> entregables = new AccesoBaseDatos().consultaBaseDatos("select count(e.id) as 'entregables', c.nombre from Entregable e, ( select c.* from (select count(p.id) as 'cantidad_partidos', c.id from Entregable e, Canton c, Partido p, Accion a, PlanGobierno g where e.id_canton = c.id and e.id_accion = a.id and a.id_plan_gobierno = g.id and g.id_partido = p.id group by c.id) pc, Canton c where c.id = pc.id and pc.cantidad_partidos <= (select (convert(int,((select count(id) from Partido)/4)))) ) c where e.id_canton = c.id group by c.nombre");
        String resultado = "---------------\n";
        for(int i=0; i<entregables.size(); i++){
            resultado += (
                "  " + entregables.get(i).get(1) + ": " + entregables.get(i).get(0) + "\n"
            );
        }
        resultado += "---------------\n";
        System.out.println(resultado);
        new Temporizador().finalizarHilo();
    }
}

public class Query2{
    static void crearHilos(){
        new Temporizador().iniciarTemporizador();
        // Se instancia el pool especificando la cantidad de hilos
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for(int i=0; i<10; i++){
            // Se ejecutan los hilos
            executor.execute(new HiloEntregablesCantones());
        }
        // Se pone a disposición la finaliación de los hilos
        executor.shutdown();
    }

    public static void main(String[] args){
        crearHilos();
    }
}