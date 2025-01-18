package ReservasManagement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Estudiante extends Usuario {
    private String carrera;
    private int semestre;
    private List<DetalleReserva> reservas;

    public Estudiante(String cedula, String nombre, String correo, String direccion, String clave, String telefono, String carrera, int semestre) {
        super(cedula, nombre, correo, direccion, clave, telefono);
        this.carrera = carrera;
        this.semestre = semestre;
        reservas = new ArrayList<>();
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public List<DetalleReserva> getReservas() {
        return reservas;
    }

    public DetalleReservaEquipo añadirReservaEquipo(LocalDate fecha, Equipo equipo, int duracion, List<DetalleReservaEquipo> equipos) throws Exception {
        // Verificar el límite de reservas de equipos
        if (contarReservasEquipos() >= 5) {
            throw new Exception("No puede realizar más de 5 reservas de equipos");
        }

        // Verificar si el equipo ya está ocupado
        boolean equipoOcupado = equipos.stream()
                .filter(reserva -> reserva.getEquipo() == equipo)
                .anyMatch(reserva -> hayConflictoFechas(fecha, duracion, reserva.getFecha(), reserva.getDuracion()));

        if (equipoOcupado) {
            throw new Exception("Equipo ya ocupado");
        }

        // Crear y añadir la nueva reserva
        DetalleReservaEquipo reserva = new DetalleReservaEquipo(fecha, this, equipo, duracion);
        reservas.add(reserva);
        return reserva;
    }

    public DetalleReservaLaboratorio añadirReservaLaboratorio(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
                                                              String laboratorioReservado, int numeroOcupantes, List<DetalleReservaLaboratorio> laboratorios) throws Exception {
        // Verificar el límite de reservas de laboratorios
        if (contarReservasLaboratorios() >= 3) {
            throw new Exception("No puede realizar más de 3 reservas de laboratorios");
        }

        // Verificar número máximo de ocupantes
        if (numeroOcupantes > 5) {
            throw new Exception("El número de ocupantes no puede superar 5 personas");
        }

        // Verificar si el laboratorio ya está ocupado
        boolean laboratorioOcupado = laboratorios.stream()
                .filter(reserva -> reserva.getLaboratorioReservado().equals(laboratorioReservado))
                .anyMatch(reserva -> hayConflictoFechasYHoras(
                        fecha, horaInicio, horaFin,
                        reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin()
                ));

        if (laboratorioOcupado) {
            throw new Exception("Laboratorio ya ocupado en el horario especificado");
        }

        // Crear y añadir la nueva reserva
        DetalleReservaLaboratorio reserva = new DetalleReservaLaboratorio(
                fecha, this, horaInicio, horaFin, laboratorioReservado, numeroOcupantes);
        reservas.add(reserva);
        return reserva;
    }

    public void eliminarReserva(DetalleReserva reserva) {
        reservas.remove(reserva);
    }

    // Contar las reservas de equipos realizadas por el estudiante
    private long contarReservasEquipos() {
        return reservas.stream()
                .filter(reserva -> reserva instanceof DetalleReservaEquipo)
                .count();
    }

    // Contar las reservas de laboratorios realizadas por el estudiante
    private long contarReservasLaboratorios() {
        return reservas.stream()
                .filter(reserva -> reserva instanceof DetalleReservaLaboratorio)
                .count();
    }

    private boolean hayConflictoFechasYHoras(
            LocalDate fecha1, LocalTime horaInicio1, LocalTime horaFin1,
            LocalDate fecha2, LocalTime horaInicio2, LocalTime horaFin2) {

        // Si las fechas son diferentes, no hay conflicto
        if (!fecha1.isEqual(fecha2)) {
            return false;
        }

        // Verifica si hay solapamiento en las horas
        return (horaInicio1.isBefore(horaFin2) || horaInicio1.equals(horaFin2)) &&
                (horaInicio2.isBefore(horaFin1) || horaInicio2.equals(horaFin1));
    }

    private boolean hayConflictoFechas(LocalDate fecha1, int duracion1, LocalDate fecha2, int duracion2) {
        LocalDate fin1 = fecha1.plusDays(duracion1);
        LocalDate fin2 = fecha2.plusDays(duracion2);

        return (fecha1.isBefore(fin2) || fecha1.isEqual(fin2)) &&
                (fecha2.isBefore(fin1) || fecha2.isEqual(fin1));
    }
}

