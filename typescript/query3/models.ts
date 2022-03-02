const { Sequelize, DataTypes } = require("sequelize");

// Se especifican los modelos de sequelize junto con el acceso a la base de datos

const sequelize = new Sequelize("Aseni", "prueba", "prueba", {
    dialect: "mssql",
    host: "localhost",
    port: "1433",
    logging: false
});

const id = {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
};

const foreignKey = (tableName: string) => ({
    type: Sequelize.INTEGER,
    references: {
        model: tableName,
        key: 'id',
    }
});

const modelOptions = {
    timestamps: false,
    createdAt: false,
    updatedAt: false,
    freezeTableName: true
};

const Partido = sequelize.define("Partido", {
    id,
    biografia_personal: DataTypes.STRING,
    foto: DataTypes.TEXT,
    bandera: DataTypes.TEXT,
    nombre: DataTypes.STRING
}, modelOptions);

const PlanGobierno = sequelize.define("PlanGobierno", {
    id,
    id_partido: foreignKey("Partido")
}, modelOptions);

const Accion = sequelize.define("Accion", {
    id,
    descripcion: DataTypes.STRING,
    id_plan_gobierno: foreignKey("PlanGobierno")
}, modelOptions);

const Canton = sequelize.define("Canton", {
    id,
    nombre: DataTypes.STRING
}, modelOptions);

const Entregable = sequelize.define("Entregable", {
    id,
    fecha_cumplimiento: DataTypes.DATE,
    valor_kpi: DataTypes.INTEGER,
    unidad_kpi: DataTypes.STRING,
    id_accion: foreignKey("Accion"),
    id_canton: foreignKey("Canton")
}, modelOptions);

Canton.hasMany(Entregable);
Entregable.belongsTo(Canton, {
    foreignKey: 'id_canton'
});

Partido.sync();
PlanGobierno.sync();
Accion.sync();
Canton.sync();
Entregable.sync();

export {
    sequelize,
    Partido,
    PlanGobierno,
    Accion,
    Canton,
    Entregable
}