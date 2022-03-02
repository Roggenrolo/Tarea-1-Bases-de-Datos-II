const { parentPort } = require('worker_threads');
import { consultaBaseDatos } from "../consultaBaseDatos";

const hiloEntregablesCantones = async () => {
    // Se ejecuta el inline query con la cantidad de entregables de un cantón con máximo el 25% de partidos
    const entregables = await consultaBaseDatos("select count(e.id) as 'entregables', c.nombre from Entregable e, ( select c.* from (select count(p.id) as 'cantidad_partidos', c.id from Entregable e, Canton c, Partido p, Accion a, PlanGobierno g where e.id_canton = c.id and e.id_accion = a.id and a.id_plan_gobierno = g.id and g.id_partido = p.id group by c.id) pc, Canton c where c.id = pc.id and pc.cantidad_partidos <= (select (convert(int,((select count(id) from Partido)/4)))) ) c where e.id_canton = c.id group by c.nombre");
    let resultado = "---------------\n";
    for (let i = 0; i < entregables.length; i++) {
        resultado += (
            "  " + entregables[i].nombre + ": " + entregables[i].entregables + "\n"
        );
    }
    resultado += "---------------\n";
    console.log(resultado);
}

parentPort.on('message', async () => {
    await hiloEntregablesCantones();
    parentPort.postMessage(true);
});