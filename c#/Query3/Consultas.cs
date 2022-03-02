using System.Collections.Generic;

namespace queries{
    class Consultas{
        string llaveCache;

        public Consultas(string llaveCache){
            this.llaveCache = llaveCache;
        }

        void addInfo(string info){
            new Cache().agregarInfoACache(llaveCache, info);
        }

        // Se busca el máximo y mínimo de entregables de cantón
        void minMaxEntregablesCanton(int idAccion){
            using (var bd = new ConexionBD()){
                var cantones = bd.Canton;
                var entregables = bd.Entregable;
                int minCuentaCanton = 0;
                string minNombreCanton = "";
                int maxCuentaCanton = 0;
                string maxNombreCanton = "";
                List<int> idsCantones = new List<int>();
                List<string> nombresCantones = new List<string>();
                foreach (var canton in cantones){
                    idsCantones.Add(canton.id);
                    nombresCantones.Add(canton.nombre);
                }
                for (int i = 0; i < idsCantones.Count; i++){
                    int cuentaCanton = 0;
                    foreach (var entregable in entregables){
                        if (
                            entregable.id_accion == idAccion &&
                            entregable.id_canton == idsCantones[i]
                        ){
                            cuentaCanton++;
                        }
                    }
                    if (minCuentaCanton == 0 || cuentaCanton < minCuentaCanton){
                        minCuentaCanton = cuentaCanton;
                        minNombreCanton = nombresCantones[i];
                    }
                    if (maxCuentaCanton == 0 || cuentaCanton > maxCuentaCanton){
                        maxCuentaCanton = cuentaCanton;
                        maxNombreCanton = nombresCantones[i];
                    }
                }
                addInfo("     Mínimo: " + minNombreCanton + " con " + minCuentaCanton + " entregable(s)\n");
                addInfo("     Máximo: " + maxNombreCanton + " con " + maxCuentaCanton + " entregable(s)\n");
            }
        }

        void consultaAcciones(int idPlanGobierno){
            using (var bd = new ConexionBD()){
                var acciones = bd.Accion;
                foreach (var accion in acciones){
                    if (accion.id_plan_gobierno == idPlanGobierno){
                        addInfo("    " + accion.descripcion + "\n");
                        minMaxEntregablesCanton(accion.id);
                    }
                }
            }
        }

        void consultaPlanesGobierno(int idPartido){
            using (var bd = new ConexionBD()){
                var planesGobierno = bd.PlanGobierno;
                foreach (var plan in planesGobierno){
                    if (plan.id_partido == idPartido){
                        addInfo("  " + plan.id + ":\n");
                        addInfo("   Acciones:\n");
                        consultaAcciones(plan.id);
                    }
                }
            }
        }

        public void consultaPartidos(){
            using (var bd = new ConexionBD()){
                var partidos = bd.Partido;
                foreach (var partido in partidos){
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
    }
}