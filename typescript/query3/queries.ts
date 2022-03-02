import {
    sequelize,
    Partido,
    PlanGobierno,
    Accion,
    Canton,
    Entregable
} from "./models";

const minMaxEntregablesCanton = async (idAccion: number) => {
    // Se realiza la consulta de la cantidad de entregables por canton y se saca el máximo y mínimo
    const entregablesCanton = await Entregable.findAll({
        attributes: [
            'Canton.nombre',
            [
                sequelize.fn('COUNT', sequelize.col('Entregable.id')),
                'cantidad_entregables'
            ]
        ],
        include: [{
            model: Canton,
            attributes: [],
            include: []
        }],
        where: {
            id_accion: idAccion
        },
        group: ['Canton.nombre'],
        raw: true
    });
    const minMax = entregablesCanton.reduce((minmax, entregableCanton) => {
        if(!minmax.min || minmax.min.cantidad_entregables > entregableCanton.cantidad_entregables){
            minmax.min = entregableCanton;
        }
        if(!minmax.max || minmax.max.cantidad_entregables < entregableCanton.cantidad_entregables){
            minmax.max = entregableCanton;
        }
        return minmax;
    }, {});
    return minMax;
}

const consultaAcciones = async (idPlanGobierno: number) => {
    const listaAcciones = await Accion.findAll({
        where: {
            id_plan_gobierno: idPlanGobierno
        },
        raw: true
    });
    const accionesMinMax = [];
    for(let accion of listaAcciones){
        accionesMinMax.push({
            ...accion,
            minmax: await minMaxEntregablesCanton(accion.id)
        });
    }
    return accionesMinMax;
}

const consultaPlanesGobierno = async (idPartido: number) => {
    const listaPlanesGobierno = await PlanGobierno.findAll({
        where: {
            id_partido: idPartido
        },
        raw: true
    });
    const planesAcciones = [];
    for(let plan of listaPlanesGobierno){
        planesAcciones.push({
            ...plan,
            acciones: await consultaAcciones(plan.id)
        });
    }
    return planesAcciones;
}

const consultaPartidos = async () => {
    const listaPartidos = await Partido.findAll({raw: true});
    const partidosPlanes = [];
    for(let partido of listaPartidos){
        partidosPlanes.push({
            ...partido,
            planesGobierno: await consultaPlanesGobierno(partido.id)
        });
    }
    return partidosPlanes;
}

export {
    consultaPartidos
}