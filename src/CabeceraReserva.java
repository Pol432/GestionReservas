public class CabeceraReserva {
    private String universidad;
    private String carrera;
    private String nombreSolicitante;
    private String fecha;//poner tambine fecha de devolucion
    private int numeroReserva; // Número único generado automáticamente
    private static int contadorReservas = 0; // Contador de reservas global
    private DetalleReserva detalleReserva;
    private Usuario usuario;
    private Equipo equipo;

    // Constructor
    public CabeceraReserva(String universidad, String carrera, String nombreSolicitante, String fecha) {
        this.universidad = universidad;
        this.carrera = carrera;
        this.nombreSolicitante = nombreSolicitante;
        this.fecha = fecha;
        this.numeroReserva = generarNumeroReserva();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    // Método para generar un número de reserva único
    private int generarNumeroReserva() {
        contadorReservas++;
        return contadorReservas;
    }

    // Getters
    public String getUniversidad() {
        return universidad;
    }

    public String getCarrera() {
        return carrera;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public String getFecha() {
        return fecha;
    }

    public int getNumeroReserva() {
        return numeroReserva;
    }

    public void setDetalleReserva(DetalleReserva detalle) {
        this.detalleReserva = detalle;
    }

    public DetalleReserva getDetalleReserva() {
        return detalleReserva;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "Nombre del solicitante='" + nombreSolicitante + '\'' +
                ", carrera='" + carrera + '\'' +
                ", fecha='" + fecha + '\'' +
                ", numeroReserva=" + numeroReserva +
                ", Nombre del Equipo=" + equipo.getNombre() +
                '}';
    }


}