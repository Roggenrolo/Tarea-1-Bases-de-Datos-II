const sql = require('mssql')

const dbSettings = {
    user: "prueba",
    password: "prueba",
    server: "localhost",
    database: "Aseni",
    options: {
        trustServerCertificate: true
    }
};

const consultaBaseDatos = async (consulta : string) => {
    const connection = await sql.connect(dbSettings);
    const result = await connection.request().query(consulta);
    return result.recordset;
}

export {
    consultaBaseDatos
};