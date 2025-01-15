package AppMain;

import java.util.ArrayList;
import java.util.List;

import ReservasManagement.*;

public class App {
    private static List<Equipo> equipos = new ArrayList<>();
    private static List<DetalleReserva> reservas = new ArrayList<>();
    private static List<Estudiante> estudiantes = new ArrayList<>();
    private static List<Administrador> administradores = new ArrayList<>();
    private static List<RegistroMantenimiento> historialMantenimientos = new ArrayList<>();

    public static List<Equipo> getEquipos() {
        return equipos;
    }

    public static List<DetalleReserva> getReservas() {
        return reservas;
    }

    public static List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public static List<Administrador> getAdministradores() {
        return administradores;
    }

    public static List<RegistroMantenimiento> getHistorialMantenimientos() {
        return historialMantenimientos;
    }


    public void agregarUsuario(String cedula, String nombre, String correo, String clave, String telefono, String ciudad, String tipoUsuario) throws Exception
    {

        if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty() || telefono.isEmpty() || ciudad.isEmpty()) {
            throw new Exception("Llene todos los campos.");
        }

        if (!Usuario.validarcedula(cedula)) {
            throw new Exception("Cédula inválida. Debe ingresar 10 caracteres numéricos.");
        }

        if (!Usuario.validarCorreo(correo)) {
            throw new Exception("Correo ingresado es inválido");
        }

        // Validar que la cédula y el correo no existan ya
        boolean cedulaExiste = estudiantes.stream().anyMatch(eu -> eu.getCedula().equals(cedula)) ||
                administradores.stream().anyMatch(ad -> ad.getCedula().equals(cedula));

        boolean correoExiste = estudiantes.stream().anyMatch(eu -> eu.getCorreo().equals(correo)) ||
                administradores.stream().anyMatch(ad -> ad.getCorreo().equals(correo));

        if (cedulaExiste) {
            throw new Exception("Cédula ingresada ya existe");
        }

        if (correoExiste) {
            throw new Exception("Correo ingresado ya existe");
        }

        assert tipoUsuario != null;
        if (tipoUsuario.equals("ReservasManagement.Estudiante")) {
            Estudiante nuevoEstudiante = new Estudiante(cedula, nombre, correo, ciudad, clave, telefono, "Carrera", 1);
            estudiantes.add(nuevoEstudiante);
        } else {
            Administrador nuevoAdministrador = new Administrador(cedula, nombre, correo, ciudad, clave, telefono);
            administradores.add(nuevoAdministrador);
        }
    }


    public Usuario ingresarUsuario(String correo, String clave) throws Exception
    {
        // Validar credenciales en estudiantes
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getCorreo().equals(correo) && estudiante.getClave().equals(clave)) {
                return estudiante;
            }
        }
        // Validar credenciales en administradores
        for (Administrador administrador : administradores) {
            if (administrador.getCorreo().equals(correo) && administrador.getClave().equals(clave)) {
                return administrador;
            }
        }

        throw new Exception("Credecianles inválidas");
    }

}
