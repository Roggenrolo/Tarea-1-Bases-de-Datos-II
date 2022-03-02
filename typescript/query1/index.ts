import { Worker } from 'worker_threads';
import { consultaBaseDatos } from '../consultaBaseDatos';
import { iniciarTemporizador, finalizarHilo } from '../temporizador';

const generarNumeroAleatorio = (max: number, min: number) => {
	return Math.floor(Math.random() * (max - min) + min);
}

const crearProcesos = async () => {
    iniciarTemporizador();
    // Se llama al procedimiento almacenado para obtener los cantones
    const cantones = await consultaBaseDatos("ObtenerCantones");
    for (let i = 0; i < 10; i++) {
        const posCanton = generarNumeroAleatorio(0, cantones.length);
        // Se inicializan los hilos
        const worker = new Worker('./dist/query1/worker', {
            workerData: {
                idCanton: cantones[posCanton].id,
                nombreCanton: cantones[posCanton].nombre
            }
        });
        worker.on("exit", finalizarHilo);
    }
}

crearProcesos();