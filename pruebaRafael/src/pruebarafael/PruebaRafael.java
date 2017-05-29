package pruebarafael;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import modelo.Alumno;
import modelo.ConexionBD;
import modelo.Curso;

public class PruebaRafael {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int op;
        ConexionBD bd = new ConexionBD();

        /* ABRIR CONEXIÓN BD **************************************************/
            try {
                System.out.println("Abriendo conexion...");
                bd.abrirConexion();
                System.out.println("OK!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        /* ********************************************************************/

        do {
            System.out.println("");
            System.out.println("Seleccionar una opción:");
            System.out.println("-----------------------");
            System.out.println();
            System.out.println("1.- Alta de Curso.");
            System.out.println("2.- Baja de Curso.");
            System.out.println("3.- Alta de Alumno.");
            System.out.println("4.- Baja de Alumno.");
            System.out.println("5.- Listado de Cursos y Alumnos.");
            System.out.println("0.- Salir.");
            System.out.println();
            System.out.print("Opción? ");
            op = sc.nextInt();
            System.out.println("");

            switch (op) {
                case 1: // Alta de Curso
                {
                    Curso c = new Curso();
                    System.out.print("Introduce el id del curso: ");
                    c.setId(sc.nextInt());
                    System.out.print("Introduce el título del curso: ");
                    c.setTitulo(sc.next());
                    System.out.print("Introduce las horas del curso: ");
                    c.setHoras(sc.nextDouble());
                    System.out.print("Introduce la fecha inicial (dd/MM/YYYY) (0:Null): ");
                    c.setFechaI(sc.next());
                    System.out.print("Introduce la fecha final (dd/MM/YYYY) (0:Null): ");
                    c.setFechaF(sc.next());
                    System.out.println("");
                    try {
                        c.altaCurso(bd);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 2: // Baja de Curso
                {
                    Curso c = new Curso();
                    System.out.print("Introduce el id del curso: ");
                    c.setId(sc.nextInt());
                    try {
                        c.bajaCurso(bd);
                        System.out.println("Curso dado de baja correctamente!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 3: // Alta de Alumno
                {
                    Curso c = new Curso();       
                    try {
                        do {
                            System.out.print("Introduce el Id del curso al que pertenece el alumno: ");
                            c.setId(sc.nextInt());         
                            if (!c.existeCurso(bd)) {
                                System.out.println("El curso no existe!");                             
                            }
                        } while (!c.existeCurso(bd));
                    } catch (Exception e) {
                    }
                    Alumno a = new Alumno();
                    a.setIdCurso(c.getId());
                    System.out.print("Introduce el dni del alumno: ");
                    a.setDni(sc.next());
                    System.out.print("Introduce el nombre: ");
                    a.setNombre(sc.next());
                    System.out.print("El alumno es mayor de edad?(0:Si): ");
                    int temporal = sc.nextInt();
                    if (temporal==0) {
                        a.setMayorEdad(true);
                    }
                    try {
                        a.altaAlumno(bd);
                        System.out.println("Alumno dado de alta!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 4: // Baja de Alumnos
                {
                    Alumno a = new Alumno();
                    System.out.print("Introduce el DNI del alumno que deseas eliminar: ");
                    a.setDni(sc.next());
                    try {
                        a.bajaAlumno(bd);
                        System.out.println("Alumno eliminado!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 5: // Listado de Cursos y Alumnos
                {
                    System.out.println("LISTADO DE CURSOS");
                    System.out.println("-----------------");
                    List<Curso> tCursos = new ArrayList<>();
                    List<Alumno> tAlumnos = new ArrayList<>();
                    try {
                        Curso.listadoCursos(bd,tCursos);
                        Alumno.listadoAlumnos(bd, tAlumnos);
                        for (Curso curso : tCursos) {
                            System.out.printf("ID: %d TITULO: %s HORAS: %.2f FECHAINICIAL: %s FECHAFINAL: %s\n",
                                    curso.getId(),
                                    curso.getTitulo(),
                                    curso.getHoras(),
                                    curso.getFechaI(),
                                    curso.getFechaF());
                            for (Alumno alumno : tAlumnos) {
                                if (alumno.getIdCurso() == curso.getId()) {
                                    System.out.printf("    IDCURSO: %d DNI: %s NOMBRE: %s MayorDeEdad: %s\n",
                                                        alumno.getIdCurso(),alumno.getDni(),
                                                        alumno.getNombre(),alumno.isMayorEdad());
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 0: // Salir
                {
                    /* CERRAR CONEXIÓN BD *************************************/
                        try {
                            System.out.println("Cerrando conexion...");
                            bd.cerrarConexion();
                            System.out.println("Bye!");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    /* ********************************************************/
                    break;
                }
                default: {
                    System.out.println("Opción incorrecta!!");
                    System.out.println("");
                }
            }
        } while (op != 0);
        System.out.println("");

    }
}

