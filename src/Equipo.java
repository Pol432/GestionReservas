import java.util.Date;

public class Equipo {
    private String codigo;
    private String nombre;
    private String estado;
    private boolean prestado;
    private Date fechaAdquisicion;

    public Equipo(String nombre, String estado, Date date) {
        this.nombre = nombre;
        this.estado = estado;
        this.prestado = false; // Por defecto, el equipo no está prestado
        this.fechaAdquisicion = date;
    }

    // Métodos getter y setter
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isPrestado() {
        return prestado;
    }

    public void setPrestado(boolean prestado) {
        this.prestado = prestado;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    // Métodos adicionales según el diagrama
    public boolean requiereCalibracion() {
        // Lógica para determinar si el equipo necesita calibración
        return false;
    }

    public int cantidadVecesReservada() {
        // Lógica para retornar la cantidad de veces que ha sido reservado
        return 0;
    }

    public boolean requiereMantenimientoCorrectivo() {
        // Lógica para determinar si requiere mantenimiento correctivo
        return false;
    }

    public boolean requiereMantenimientoPreventivo() {
        // Lógica para determinar si requiere mantenimiento preventivo
        return true; // Ejemplo: se asume que requiere mantenimiento por defecto
    }
}
