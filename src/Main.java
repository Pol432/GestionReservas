import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static List<Equipo> equipos = new ArrayList<>();
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<CabeceraReserva> reservas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarDatosPredeterminados();

        System.out.println("Sistema de Gestión de Equipos");

        Usuario usuarioActual = iniciarSesion();
        if (usuarioActual == null) {
            System.out.println("Inicio de sesión fallido.");
            return;
        }

        if (usuarioActual instanceof Estudiante) {
            menuEstudiante((Estudiante) usuarioActual);
        } else if (usuarioActual instanceof Administrador) {
            menuAdministrador((Administrador) usuarioActual);
        }
    }

    private static void inicializarDatosPredeterminados() {
        // Crear equipos
        equipos.add(new Equipo("Osciloscopio", "Disponible", new Date()));
        equipos.add(new Equipo("Fuente de voltaje", "Dañado", new Date()));
        equipos.add(new Equipo("Generador de onda", "Disponible", new Date()));
        equipos.add(new EquipoMedicion("E0102", "Multimetro", "Dañado", false, new Date(), new Date(), 5));

        // Crear usuarios
        Estudiante estudiante1 = new Estudiante("1234567890", "Paul Rosero",
                "pmrosero@gmail.com", "Quito", "1234", "0987654321", "Electrónica", 3);
        Administrador administrador1 = new Administrador("0987654321", "David Iza",
                "david.iza@udla.ec", "Ambato", "5678", "1234567890");

        usuarios.add(estudiante1);
        usuarios.add(administrador1);

        // Crear algunas reservas de ejemplo
        CabeceraReserva reserva1 = new CabeceraReserva("UDLA", "Electrónica",
                estudiante1.getNombre(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reserva1.setEquipo(equipos.getFirst());
        equipos.getFirst().setPrestado(true);
        reservas.add(reserva1);
    }

    private static Usuario iniciarSesion() {
        System.out.print("Ingrese su correo electrónico: ");
        String correo = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String clave = scanner.nextLine();

        return usuarios.stream()
                .filter(u -> u.getCorreo().equals(correo) && u.getClave().equals(clave))
                .findFirst()
                .orElse(null);
    }

    private static void menuEstudiante(Estudiante estudiante) {
        System.out.println("\n--- Menú Estudiante ---");

        // Mostrar reservas previas
        System.out.println("Reservas anteriores:");
        reservas.stream()
                .filter(r -> r.getNombreSolicitante().equals(estudiante.getNombre()))
                .forEach(System.out::println);

        // Realizar nueva reserva
        System.out.println("\nRealizar nueva reserva:");
        equipos.stream()
                .filter(e -> !e.isPrestado())
                .forEach(e -> System.out.println(e.getNombre()));

        System.out.print("Seleccione un equipo: ");
        String equipoNombre = scanner.nextLine();

        Equipo equipoSeleccionado = equipos.stream()
                .filter(e -> e.getNombre().equals(equipoNombre) && !e.isPrestado())
                .findFirst()
                .orElse(null);

        if (equipoSeleccionado != null) {
            CabeceraReserva nuevaReserva = new CabeceraReserva(
                    "UDLA",
                    estudiante.getCarrera(),
                    estudiante.getNombre(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
            nuevaReserva.setEquipo(equipoSeleccionado);
            reservas.add(nuevaReserva);
            equipoSeleccionado.setPrestado(true);
            System.out.println("Reserva realizada con éxito.");
            System.out.println("Lista de reservas: ");
            reservas.stream()
                    .filter(r -> r.getNombreSolicitante().equals(estudiante.getNombre()))
                    .forEach(System.out::println);
        } else {
            System.out.println("Equipo no disponible.");
        }
    }

    private static void menuAdministrador(Administrador administrador) {
        System.out.println("\n--- Menú Administrador ---");

        // Mostrar equipos que requieren mantenimiento
        System.out.println("Equipos que requieren mantenimiento:");
        equipos.stream()
                .filter(e -> Objects.equals(e.getEstado(), "Dañado"))
                .forEach(e -> System.out.println(e.getNombre()));

        System.out.print("Seleccione un equipo para mantenimiento: ");
        String equipoNombre = scanner.nextLine();

        Equipo equipoSeleccionado = equipos.stream()
                .filter(e -> e.getNombre().equals(equipoNombre))
                .findFirst()
                .orElse(null);

        if (equipoSeleccionado != null && "Dañado".equals(equipoSeleccionado.getEstado())) {
            equipoSeleccionado.setEstado("Arreglado");
            System.out.println("Mantenimiento realizado con éxito.");
        } else {
            System.out.println("Equipo no válido para mantenimiento.");
        }
    }
}