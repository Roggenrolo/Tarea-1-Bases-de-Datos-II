using System;
using System.Threading;

namespace queries {
    class Query3{
        void hilo(Object obj){
            object[] array = obj as object[];
            int numHilo = Convert.ToInt32(array[0]);
            // Se crea la llave de la cache, y al finalizar se imprime el dato ahí guardado
            string llaveCache = "llave-" + numHilo;
            new Consultas(llaveCache).consultaPartidos();
            Console.WriteLine(new Cache().getInfoCache(llaveCache));
            new Temporizador().finalizarHilo();
        }

        public void ejecutar() {
            new Temporizador().IniciarTemporizador();
            for (int i = 0; i < 10; i++){
                // Se inicializan los hilos del thread pool
                ThreadPool.QueueUserWorkItem(hilo, new object[] { i });
            }
        }
    }
}