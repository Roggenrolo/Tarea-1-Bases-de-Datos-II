using System.Collections.Generic;
using System.Data.SqlClient;

namespace queries{
    class AccesoBaseDatos{
        public List<List<string>> consultaBaseDatos(string consulta){
            string instance = "localhost";
            string database = "Aseni";
            string user = "prueba";
            string password = "prueba";
            SqlConnection conexion = new SqlConnection("Data Source = " + instance + "; Initial Catalog = " + database + "; User ID = " + user + "; Password=" + password);
            conexion.Open();
            SqlCommand comando = new SqlCommand(consulta, conexion);
            SqlDataReader lector = comando.ExecuteReader();
            List<List<string>> resultado = new List<List<string>>();
            while (lector.Read()){
                List<string> fila = new List<string>();
                for (int i = 0; i < lector.FieldCount; i++){
                    fila.Add(lector.GetValue(i).ToString());
                }
                resultado.Add(fila);
            }
            conexion.Close();
            return resultado;
        }
    }
}