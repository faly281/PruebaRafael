package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Alumno {

    /* Atributos **************************************************************/
    private int idCurso;
    private String dni;
    private String nombre;
    private boolean mayorEdad;

    /* Constructores **********************************************************/
    public Alumno() {
        idCurso = 0;
        dni = "";
        nombre = "";
        mayorEdad = false;
    }

    /* Métodos getters & setters **********************************************/
    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isMayorEdad() {
        return mayorEdad;
    }

    public void setMayorEdad(boolean mayorEdad) {
        this.mayorEdad = mayorEdad;
    }

    /* Métodos ****************************************************************/
    public boolean existeAlumno(ConexionBD bd) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql;
        try {
            sql = "SELECT * FROM Alumnos WHERE idCurso = ? AND dni = ?";
            pst = bd.getConn().prepareStatement(sql);
            pst.setInt(1,idCurso);
            pst.setString(2,dni);
            rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new Exception("Error! existeAlumno()",e);
        } finally {
            try {
                if (rs!=null) rs.close();
                if (pst!=null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }        
    
    public void altaAlumno(ConexionBD bd) throws Exception {
        PreparedStatement pst = null;
        String sql;
        if (existeAlumno(bd)) {
            throw new Exception ("El alumno ya existe!");
        }
        try {
            sql = "INSERT INTO Alumnos values(?,?,?,?)";
            pst = bd.getConn().prepareStatement(sql);
            pst.setInt(1,idCurso);
            pst.setString(2,dni);
            pst.setString(3,nombre);
            pst.setBoolean(4,mayorEdad);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error! altaAlumno()",e);
        } finally {
            try {
                if (pst!=null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
    }

    public void bajaAlumno(ConexionBD bd) throws Exception {
        PreparedStatement pst = null;
        String sql;
        if (!existeAlumno(bd)) {
            throw new Exception ("El alumno no existe!");
        }
        try {
            sql = "DELETE FROM Alumnos WHERE DNI = ?";
            pst = bd.getConn().prepareStatement(sql);
            pst.setString(2,this.dni);
            pst.executeUpdate();            
        } catch (SQLException e) {
            throw new Exception ("Error! bajaAlumno()",e);
        } finally {
            try {
                if (pst!=null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void listadoAlumnos(ConexionBD bd, List<Alumno> t) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql;
        try {
            sql = "SELECT * FROM Alumnos ORDER BY idCurso";
            pst = bd.getConn().prepareStatement(sql);
            rs = pst.executeQuery();
            Alumno a;
            while (rs.next()) {
                a = new Alumno();
                a.setIdCurso(rs.getInt(1));
                a.setDni(rs.getString(2));
                a.setNombre(rs.getString(3));
                a.setMayorEdad(rs.getBoolean(4));
                t.add(a);
            }
        } catch (SQLException e) {
            throw new Exception("Error! ListadoAlumnos()",e);
        } finally {
            try {
                
                if (rs!=null) rs.close();
                if (pst!=null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
    