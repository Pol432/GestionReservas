public class Administrador extends Usuario {

    public Administrador(String cedula, String nombre, String correo, String direccion, String clave, String telefono) {
        super(cedula, nombre, correo, direccion, clave, telefono);
    }

    public boolean realizarMantenimiento(Equipo equipo) {
        if ("Dañado".equals(equipo.getEstado())) {
            equipo.setEstado("Arreglado");
            return true;
        }
        return false;
    }
}
