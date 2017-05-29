package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    /* Atributos **************************************************************/
    private Connection conn;

    /* Constructores **********************************************************/
    public ConexionBD() {
        conn = null;
    }

    /* Métodos getters & setters **********************************************/
    public Connection getConn() {
        return conn;
    }
    

    /* Métodos ****************************************************************/
    private void crearTablas() throws Exception {
            Statement st = null;
            String sql;
            try {
                st = conn.createStatement();
                sql = "CREATE TABLE IF NOT EXISTS Cursos ("
                        + "id INTEGER NOT NULL,"
                        + "titulo VARCHAR(50) NOT NULL,"
                        + "horas DECIMAL(6,2),"
                        + "FechaI DATE NULL,"
                        + "FechaF DATE NULL,"
                        + "CONSTRAINT pk_Cursos PRIMARY KEY (id))";
                st.executeUpdate(sql);
                System.out.println("Tabla de Cursos creada correctamente!");                
//                sql = "CREATE TABLE IF NOT EXISTS Alumnos ("
//                        + "idCurso INTEGER NOT NULL,"
//                        + "DNI VARCHAR(9) NOT NULL,"
//                        + "Nombre VARCHAR(30),"
//                        + "MayorDeEdad BOOLEAN,"
//                        + "CONSTRAINT pk_PAlumnos PRIMARY KEY (idCurso,DNI),"
//                        + "CONSTRAINT fk_Alumnos FOREIGN KEY(idCurso) REFERENCES Cursos(id) ON DELETE CASCADE)";
                sql = "CREATE TABLE IF NOT EXISTS Alumnos ("
                        + "idCurso INTEGER NOT NULL,"
                        + "DNI VARCHAR(9) NOT NULL,"
                        + "Nombre VARCHAR(30),"
                        + "MayorDeEdad BOOLEAN,"
                        + "CONSTRAINT pk_PAlumnos PRIMARY KEY (idCurso,DNI),"
                        + "CONSTRAINT fk_Alumnos FOREIGN KEY(idCurso) REFERENCES Cursos(id))";
                st.executeUpdate(sql);
                System.out.println("Tabla de Alumnos creada correctamente!");
            } catch (SQLException e) {
                System.out.println("Error! crearTablas()");
            } finally {
                try {
                    if (st!=null) st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    }

    public void abrirConexion() throws Exception {
        String db = "t11p02";
        String url = "jdbc:mysql://localhost:3306/" + db + "?useSSL=false";
        String user = "alumno";
        String pass = "alumno";
        try {
            conn = DriverManager.getConnection(url, user, pass);
            crearTablas();
        } catch (SQLException e) {
            throw new Exception("Error abrirConexion()!!", e);
        }
    }

    public void cerrarConexion() throws Exception {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new Exception("Error cerrarConexion()!!", e);
        }
    }

}
