const { parentPort, workerData } = require('worker_threads');
import { consultaBaseDatos } from "../consultaBaseDatos";

const entregablesCanton = (
    idCanton: number,
    nombreCanton: string
) => {
    // Se llama al procedimiento almacenado que lista los entregables de un canton
    consultaBaseDatos(`EntregablesCanton ${idCanton}`).then(entregables => {
        let resultado = `----- ${nombreCanton} -----\n`;
        entregables.forEach(entregable => {
            resultado += (`
  Descripción: ${entregable.descripcion}\n
  Fecha de cumplimiento: ${entregable.fecha_cumplimiento}\n
  Valor de kpi: ${entregable.valor_kpi}\n
  Unidad de kpi: ${entregable.unidad_kpi}\n
            `);
        });
        resultado += "\n---------------";
        console.log(resultado);
        process.exit(0);
    });
}

parentPort.postMessage(entregablesCanton(
    workerData.idCanton,
    workerData.nombreCanton
));