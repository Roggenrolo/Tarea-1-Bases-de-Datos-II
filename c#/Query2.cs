using System;
using System.Collections.Generic;
using System.Threading;

namespace queries{
    class Query2{
        static void hiloEntregablesCantones(Object obj){
            // Se ejecuta el inline query
            List<List<string>> entregables = new AccesoBaseDatos().consultaBaseDatos("select count(e.id) as 'entregables', c.nombre from Entregable e, ( select c.* from (select count(p.id) as 'cantidad_partidos', c.id from Entregable e, Canton c, Partido p, Accion a, PlanGobierno g where e.id_canton = c.id and e.id_accion = a.id and a.id_plan_gobierno = g.id and g.id_partido = p.id group by c.id) pc, Canton c where c.id = pc.id and pc.cantidad_partidos <= (select (convert(int,((select count(id) from Partido)/4)))) ) c where e.id_canton = c.id group by c.nombre");
            string resultado = "---------------\n";
            for (int i = 0; i < entregables.Count; i++){
                resultado += "  " + entregables[i][1] + ": " + entregables[i][0] + "\n";
            }
            resultado += "---------------\n";
            Console.WriteLine(resultado);
            new Temporizador().finalizarHilo();
        }

        public void ejecutar(){
            new Temporizador().IniciarTemporizador();
            for(int i=0; i<10; i++){
                // Se inicializan los hilos del thread pool
                ThreadPool.QueueUserWorkItem(hiloEntregablesCantones);
            }
        }
    }
}
