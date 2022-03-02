const { parentPort } = require('worker_threads');
import { consultaPartidos } from "./queries";

parentPort.on('message', async () => {
    parentPort.postMessage(await consultaPartidos());
});