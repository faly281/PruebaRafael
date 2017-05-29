package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Curso {

    /* Atributos **************************************************************/
    private int id;
    private String titulo;
    private double horas;
    private LocalDate fechaI;
    private LocalDate fechaF;

    /* Constructores **********************************************************/
    public Curso() {
        id = 0;
        titulo = "";
        horas = 0.0;
        fechaI = null;
        fechaF = null;
    }

    /* Métodos getters & setters **********************************************/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getHoras() {
        return horas;
    }

    public void setHoras(double horas) {
        this.horas = horas;
    }

    public String getFechaI() {
        if (fechaI!=null)
            return fechaI.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        return "";
    }

    public void setFechaI(String fechaI) {
        if (fechaI.equals("0")) {
            this.fechaI = null;
        } else {
            this.fechaI = LocalDate.of(Integer.parseInt(fechaI.substring(6,10)),
                                       Integer.parseInt(fechaI.substring(3,5)),
                                       Integer.parseInt(fechaI.substring(0,2)));
        }
    }

    public String getFechaF() {
        if (fechaF!=null)
            return fechaF.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        return "";
    }

    public void setFechaF(String fechaF) {
        if (fechaF.equals("0")) {
            this.fechaF = null;
        } else {
            this.fechaF = LocalDate.of(Integer.parseInt(fechaF.substring(6,10)),
                                       Integer.parseInt(fechaF.substring(3,5)),
                                       Integer.parseInt(fechaF.substring(0,2)));
        }
    }   

    /* Métodos ****************************************************************/
    public boolean existeCurso(ConexionBD bd) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql;
        try {
            sql = "SELECT * FROM Cursos WHERE id = ?";
            pst = bd.getConn().prepareStatement(sql);
            pst.setInt(1,id);
            rs = pst.executeQuery();
            if (rs.next())
                return true;
        } catch (SQLException e) {
            throw new Exception ("Error! existeCurso()",e);
        } finally {
            try {
                if (rs!=null) rs.close();
                if (pst!=null) pst.close();
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public void altaCurso(ConexionBD bd) throws Exception {
        PreparedStatement pst = null;
        String sql;
        if (existeCurso(bd)) {
            throw new Exception("El curso ya existe!");
        }
        try {
            sql = "INSERT INTO Cursos VALUES(?,?,?,?,?)";
            pst = bd.getConn().prepareStatement(sql);
            pst.setInt(1,id);
            pst.setString(2,titulo);
            pst.setDouble(3,horas);
            pst.setObject(4,fechaI);
            pst.setObject(5,fechaF);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new Exception ("Error! AltaCurso()",e);
        } finally {
            try {
                if (pst!=null) pst.close();
            } catch (SQLException e) {
            }
        }
        
    }

//    public void bajaCurso(ConexionBD bd) throws Exception {
//        PreparedStatement pst = null;
//        String sql;
//        if (!existeCurso(bd)) {
//            throw new Exception("El curso no existe!");
//        }
//        try {
//            sql = "DELETE FROM Cursos WHERE id = ?";
//            pst = bd.getConn().prepareStatement(sql);
//            pst.setInt(1,id);
//            sql  = "DELETE FROM Alumnos WHERE idCurso = ?";
//            pst.setInt(1,id);
//            pst.executeUpdate();
//        } catch (SQLException e) {
//            throw new Exception ("Error! BajaCurso()",e);
//        } finally {
//            try {
//                if (pst!=null) pst.close();
//            } catch (SQLException e) {
//            }
//        }
//    }
    
    public void bajaCurso(ConexionBD bd) throws Exception {
        PreparedStatement pst = null;
        String sql;
        if (!existeCurso(bd)) {
            throw new Exception("El curso no existe!");
        }
        try {
            bd.getConn().setAutoCommit(false);
            sql  = "DELETE FROM Alumnos WHERE idCurso = ?";
            pst = bd.getConn().prepareStatement(sql);
            pst.setInt(1,id);
            pst.executeUpdate();
            sql = "DELETE FROM Cursos WHERE id = ?";
            pst = bd.getConn().prepareStatement(sql);
            pst.setInt(1,id);
            pst.executeUpdate();            
            bd.getConn().commit();
        } catch (SQLException e) {
            bd.getConn().rollback();
            throw new Exception ("Error! BajaCurso()",e);
        } finally {
            bd.getConn().setAutoCommit(true);
            try {
                if (pst!=null) pst.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void listadoCursos(ConexionBD bd, List<Curso> t) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;        
        DateFormat fec = new SimpleDateFormat("dd/MM/uuuu");
        String sql;
        try {
            sql = "SELECT * FROM Cursos ORDER BY id";
            pst = bd.getConn().prepareStatement(sql);
            rs = pst.executeQuery();
            Curso c;
            while (rs.next()) {
                c = new Curso();
                c.setId(rs.getInt(1));
                c.setTitulo(rs.getString(2));
                c.setHoras(rs.getDouble(3));
                c.setFechaI(fec.format(rs.getDate(4)));
                c.setFechaF(fec.format(rs.getDate(5)));
                t.add(c);
            }
        } catch (SQLException e) {
            throw new Exception("Error! ListadoCurso()", e);
        } finally {
            try {                
                if (rs!=null) rs.close();
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
            }
        }
    }

}
