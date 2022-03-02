import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Query3 {
    static void ejecutar() {
        new Temporizador().iniciarTemporizador();
        // Se instancia el pool, especificando los 10 hilos que ejecutará
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++){
            // Se ejecutan los hilos
            executor.execute(new Hilo(i));
        }
        // Se pone a disposición la finaliación de los hilos
        executor.shutdown();
    }

    public static void main(String[] args){
        ejecutar();
    } 
}

class Hilo implements Runnable{
    int numHilo;

    public Hilo(int numHilo){
        this.numHilo = numHilo;
    }
    public void run(){
        // Crea una llave única para el acceso a cache
        String llaveCache = "llave-" + numHilo;
        new Consultas(llaveCache).consultaPartidos();
        System.out.println(new Cache().getInfoCache(llaveCache));
        new Temporizador().finalizarHilo();
    }
}

// No pude utilizar ningún ORM para java, así que simulé uno
class Modelo{
    List<List<String>> getObjetos(String nombreTabla){
        return new AccesoBaseDatos().consultaBaseDatos("select * from " + nombreTabla);
    }
}

class Partido extends Modelo{
    String nombreTabla = "Partido";

    int id;
    String biografia_personal;
    String foto;
    String bandera;
    String nombre;

    Partido(){}
    Partido(
        int id,
        String biografia_personal,
        String foto,
        String bandera,
        String nombre
    ){
        this.id = id;
        this.biografia_personal = biografia_personal;
        this.foto = foto;
        this.bandera = bandera;
        this.nombre = nombre;
    }

    List<Partido> getObjetos(){
        List<Partido> partidos = new ArrayList<>();
        List<List<String>> objetos = super.getObjetos(nombreTabla);
        for(List<String> objeto: objetos){
            partidos.add(new Partido(
                Integer.parseInt(objeto.get(0)),
                objeto.get(1),
                objeto.get(2),
                objeto.get(3),
                objeto.get(4)
            ));
        }
        return partidos;
    }
}

class PlanGobierno extends Modelo{
    String nombreTabla = "PlanGobierno";

    int id;
    int id_partido;

    PlanGobierno(){}
    PlanGobierno(
        int id,
        int id_partido
    ){
        this.id = id;
        this.id_partido = id_partido;
    }

    List<PlanGobierno> getObjetos(){
        List<PlanGobierno> planesGobierno = new ArrayList<>();
        List<List<String>> objetos = super.getObjetos(nombreTabla);
        for(List<String> objeto: objetos){
            planesGobierno.add(new PlanGobierno(
                Integer.parseInt(objeto.get(0)),
                Integer.parseInt(objeto.get(1))
            ));
        }
        return planesGobierno;
    }
}

class Accion extends Modelo{
    String nombreTabla = "Accion";

    int id;
    String descripcion;
    int id_plan_gobierno;

    Accion(){}
    Accion(
        int id,
        String descripcion,
        int id_plan_gobierno
    ){
        this.id = id;
        this.descripcion = descripcion;
        this.id_plan_gobierno = id_plan_gobierno;
    }

    List<Accion> getObjetos(){
        List<Accion> acciones = new ArrayList<>();
        List<List<String>> objetos = super.getObjetos(nombreTabla);
        for(List<String> objeto: objetos){
            acciones.add(new Accion(
                Integer.parseInt(objeto.get(0)),
                objeto.get(1),
                Integer.parseInt(objeto.get(2))
            ));
        }
        return acciones;
    }
}

class Canton extends Modelo{
    String nombreTabla = "Canton";

    int id;
    String nombre;

    Canton(){}
    Canton(
        int id,
        String nombre
    ){
        this.id = id;
        this.nombre = nombre;
    }

    List<Canton> getObjetos(){
        List<Canton> cantones = new ArrayList<>();
        List<List<String>> objetos = super.getObjetos(nombreTabla);
        for(List<String> objeto: objetos){
            cantones.add(new Canton(
                Integer.parseInt(objeto.get(0)),
                objeto.get(1)
            ));
        }
        return cantones;
    }
}

class Entregable extends Modelo{
    String nombreTabla = "Entregable";

    int id;
    String fecha_cumplimiento;
    int valor_kpi;
    String unidad_kpi;
    int id_accion;
    int id_canton;

    Entregable(){}
    Entregable(
        int id,
        String fecha_cumplimiento,
        int valor_kpi,
        String unidad_kpi,
        int id_accion,
        int id_canton
    ){
        this.id = id;
        this.fecha_cumplimiento = fecha_cumplimiento;
        this.valor_kpi = valor_kpi;
        this.unidad_kpi = unidad_kpi;
        this.id_accion = id_accion;
        this.id_canton = id_canton;
    }

    List<Entregable> getObjetos(){
        List<Entregable> entregables = new ArrayList<>();
        List<List<String>> objetos = super.getObjetos(nombreTabla);
        for(List<String> objeto: objetos){
            entregables.add(new Entregable(
                Integer.parseInt(objeto.get(0)),
                objeto.get(1),
                Integer.parseInt(objeto.get(2)),
                objeto.get(3),
                Integer.parseInt(objeto.get(4)),
                Integer.parseInt(objeto.get(5))
            ));
        }
        return entregables;
    }
}

// 'Cache' global que irá guardando los datos de los hilos ('simulada', porque no pude instalar una)
class Cache{
    public static HashMap<String, String> cache = new HashMap<String, String>();

    public String getInfoCache(String llave){
        return cache.get(llave);
    }

    public void agregarInfoCache(String llave, String info){
        String infoPrevia = getInfoCache(llave);
        cache.put(llave, (infoPrevia!=null ? infoPrevia : "") + info);
    }
}

class Consultas{
    String llaveCache;

    public Consultas(String llaveCache){
        this.llaveCache = llaveCache;
    }

    // Se va guardando la info en la posición de la cache
    void addInfo(String info){
        new Cache().agregarInfoCache(llaveCache, info);
    }

    // Se consultan los mínimos y máximos de entregables de cantón
    void minMaxEntregablesCanton(int idAccion){
        List<Canton> cantones = new Canton().getObjetos();
        List<Entregable> entregables = new Entregable().getObjetos();
        int minCuentaCanton = 0;
        String minNombreCanton = "";
        int maxCuentaCanton = 0;
        String maxNombreCanton = "";
        List<Integer> idsCantones = new ArrayList<>();
        List<String> nombresCantones = new ArrayList<>();
        for(Canton canton: cantones){
            idsCantones.add(canton.id);
            nombresCantones.add(canton.nombre);
        }
        for (int i = 0; i < idsCantones.size(); i++){
            int cuentaCanton = 0;
            for(Entregable entregable: entregables){
                if (
                    entregable.id_accion == idAccion &&
                    entregable.id_canton == idsCantones.get(i)
                ){
                    cuentaCanton++;
                }
            }
            if (minCuentaCanton == 0 || cuentaCanton < minCuentaCanton){
                minCuentaCanton = cuentaCanton;
                minNombreCanton = nombresCantones.get(i);
            }
            if (maxCuentaCanton == 0 || cuentaCanton > maxCuentaCanton){
                maxCuentaCanton = cuentaCanton;
                maxNombreCanton = nombresCantones.get(i);
            }
        }
        addInfo("     Mínimo: " + minNombreCanton + " con " + minCuentaCanton + " entregable(s)\n");
        addInfo("     Máximo: " + maxNombreCanton + " con " + maxCuentaCanton + " entregable(s)\n");
    }

    void consultaAcciones(int idPlanGobierno){
        List<Accion> acciones = new Accion().getObjetos();
        for(Accion accion: acciones){
            if (accion.id_plan_gobierno == idPlanGobierno){
                addInfo("    " + accion.descripcion + "\n");
                minMaxEntregablesCanton(accion.id);
            }
        }
    }

    void consultaPlanesGobierno(int idPartido){
        List<PlanGobierno> planesGobierno = new PlanGobierno().getObjetos();
        for(PlanGobierno plan: planesGobierno){
            if (plan.id_partido == idPartido){
                addInfo("  " + plan.id + ":\n");
                addInfo("   Acciones:\n");
                consultaAcciones(plan.id);
            }
        }
    }

    public void consultaPartidos(){
        var partidos = new Partido().getObjetos();
        for(Partido partido: partidos){
            addInfo(
                "----- " + partido.nombre + " -----\n" +
                " Planes de gobierno:\n"
            );
            consultaPlanesGobierno(partido.id);
            addInfo("----------\n\n");
        }
        addInfo("**********\n");
    }
}