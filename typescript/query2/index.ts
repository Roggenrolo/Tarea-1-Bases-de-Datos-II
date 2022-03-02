const { StaticPool } = require('node-worker-threads-pool');
import { iniciarTemporizador, finalizarHilo } from "../temporizador";

const crearHilos = () => {
    iniciarTemporizador();
    // Se crea un pool especificando el archivo dónde se ejecutará el hilo
    const pool = new StaticPool({
        size: 1,
        task: "./dist/query2/worker"
    });
    for (let i = 0; i < 10; i++) {
        // Se inicializa el hilo
        pool.exec().then(finalizarHilo);
    }
}

crearHilos();