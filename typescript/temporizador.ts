let inicio = 0;
let hilosFinalizados = 0;

const iniciarTemporizador = () => {
    inicio = performance.now()
}

const finalizarHilo = () => {
    hilosFinalizados++;
    if(hilosFinalizados===10){
        finalizarHilo();
        process.exit(0);
    }
}

const finalizarTemporizador = () => {
    console.log(`Tomó ${performance.now() - inicio} milisegundos`); 
}

export {
    iniciarTemporizador,
    finalizarHilo,
    finalizarTemporizador
}