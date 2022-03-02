using System;
using System.Diagnostics;

namespace queries{

    class Temporizador{
        public static Stopwatch watch;
        public static int hilosFinalizados;

        public void IniciarTemporizador(){
            watch = Stopwatch.StartNew();
        }

        public void finalizarHilo(){
            hilosFinalizados++;
            if (hilosFinalizados == 10){
                finalizarTemporizador();
            }

        }

        public void finalizarTemporizador(){
            watch.Stop();
            long elapsedMs = watch.ElapsedMilliseconds;
            Console.WriteLine("Tomó " + elapsedMs + " milisegundos");
        }
    }
}
