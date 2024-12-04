public class DetalleReserva {
    private String horario;
    private String laboratorioReservado; // Puede ser "Electrónica", "Automatización" o "Robótica"
    private int numeroOcupantes;
    private static int reservasElectronica = 0;
    private static int reservasAutomatizacion = 0;
    private static int reservasRobotica = 0;

    // Constructor
    public DetalleReserva(String horario, String laboratorioReservado, int numeroOcupantes) {
        this.horario = horario;
        this.laboratorioReservado = laboratorioReservado;
        this.numeroOcupantes = numeroOcupantes;
        registrarReserva(laboratorioReservado);
    }

    // Método para registrar una reserva en el laboratorio correspondiente
    private void registrarReserva(String laboratorioReservado) {
        switch (laboratorioReservado.toLowerCase()) {
            case "electrónica":
                reservasElectronica++;
                break;
            case "automatización":
                reservasAutomatizacion++;
                break;
            case "robótica":
                reservasRobotica++;
                break;
            default:
                System.out.println("Laboratorio no válido");
                break;
        }
    }

    // Métodos para obtener el número de reservas por laboratorio
    public static int getReservasElectronica() {
        return reservasElectronica;
    }

    public static int getReservasAutomatizacion() {
        return reservasAutomatizacion;
    }

    public static int getReservasRobotica() {
        return reservasRobotica;
    }

    // Getters
    public String getHorario() {
        return horario;
    }

    public String getLaboratorioReservado() {
        return laboratorioReservado;
    }

    public int getNumeroOcupantes() {
        return numeroOcupantes;
    }

    @Override
    public String toString() {
        return "DetalleReserva{" +
                "horario='" + horario + '\'' +
                ", laboratorioReservado='" + laboratorioReservado + '\'' +
                ", numeroOcupantes=" + numeroOcupantes +
                '}';
    }
}