public class Temporizador {
    public static int hilosFinalizados = 0;
    public static long inicio;

    public void iniciarTemporizador(){
        inicio = System.currentTimeMillis();
    }

    public void finalizarHilo(){
        hilosFinalizados++;
        if(hilosFinalizados==10){
            finalizarTemporizador();
        }
    }

    public void finalizarTemporizador(){
        long tiempo = System.currentTimeMillis() - inicio;
        System.out.println("Tomó " + tiempo + " milisegundos.");
    }
}
