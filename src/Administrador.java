import java.util.List;

public class Administrador extends Usuario {

    public Administrador(String cedula, String nombre, String correo, String direccion, String clave, String telefono) {
        super(cedula, nombre, correo, direccion, clave, telefono);
    }

    public boolean realizarMantenimiento(Equipo equipo) {
        return true;
    }

    public boolean generarReporteInventario() {
        return true;
    }
/*
    public List<Equipo> calibracionesPendientes() {
        return ;
    }*/
}
