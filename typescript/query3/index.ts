const { StaticPool } = require('node-worker-threads-pool');
const NodeCache = require( "node-cache" );
import { iniciarTemporizador, finalizarHilo } from "../temporizador";

const cache = new NodeCache({ stdTTL: 100, checkperiod: 120 });

const imprimirInfo = info => {
    console.log("**********");
    info.forEach(partido => {
        console.log(`----- ${partido.nombre} -----`);
        console.log(" Planes de gobierno:");
        partido.planesGobierno.forEach(plan => {
            console.log(`  ${plan.id}:`);
            console.log("    Acciones:");
            plan.acciones.forEach(accion => {
                console.log(`     ${accion.descripcion}:`);
                console.log(`      Mínimo: ${accion.minmax.min.nombre} con ${accion.minmax.min.cantidad_entregables} entregable(s)`);
                console.log(`      Máximo: ${accion.minmax.max.nombre} con ${accion.minmax.max.cantidad_entregables} entregable(s)`);
            });
        });
        console.log("----------");
    });
    console.log("**********");
}

const crearHilos = () => {
    iniciarTemporizador();
    // Se crea el pool especificando el archivo dónde ejecutará el hilo
    const pool = new StaticPool({
        size: 1,
        task: "./dist/query3/worker"
    });
    for (let i = 0; i < 10; i++) {
        const llaveCache = `query3-${i}`;
        // Se ejecutan los hilos
        pool.exec().then(resultado => {
            cache.set(llaveCache, resultado, 10000);
            imprimirInfo(cache.get(llaveCache));
            finalizarHilo();
        });
    }
}

crearHilos();