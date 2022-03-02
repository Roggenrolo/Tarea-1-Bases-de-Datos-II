using System;
using System.Collections.Generic;
using System.Threading;

namespace queries{
    class Query1{
        static List<List<string>> cantones;
        static List<string> seleccionarCanton(){
            Random random = new Random();
            int posCanton = random.Next(0, cantones.Count);
            List<string> canton = cantones[posCanton];
            cantones.RemoveAt(posCanton);
            return canton;
        }

        static void hiloEntregablesCanton(){
            List<string> canton = seleccionarCanton();
            // Se piden los entregables del cantón
            List<List<string>> entregables = new AccesoBaseDatos().consultaBaseDatos("EntregablesCanton " + canton[0]);
            string resultado = "----- " + canton[1] + " -----\n";
            for (int i = 0; i < entregables.Count; i++){
                resultado += (
                    "  Descripción: " + entregables[i][0] + "\n" +
                    "  Fecha de cumplimiento: " + entregables[i][1] + "\n" +
                    "  Valor de kpi: " + entregables[i][2] + "\n" +
                    "  Unidad de kpi: " + entregables[i][3] + "\n"
                );
            }
            resultado += "\n---------------";
            Console.WriteLine(resultado);
            new Temporizador().finalizarHilo();
        }

        public void ejecutar(){
            new Temporizador().IniciarTemporizador();
            // Se piden los cantones por procedimiento almacenado
            cantones = new AccesoBaseDatos().consultaBaseDatos("ObtenerCantones");
            for (int i = 0; i < 10; i++){
                // Se ejecutan los hilos
                Thread hilo = new Thread(hiloEntregablesCanton);
                // Se inicia el hilo
                hilo.Start();
            }
        }
    }
}
