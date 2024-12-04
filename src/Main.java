import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Main {
    private static List<Equipo> equipos = new ArrayList<>();
    private static List<CabeceraReserva> reservas = new ArrayList<>();

    public static void main(String[] args) {
        // Configuración inicial de equipos
        equipos.add(new EquipoMicroprocesador("E001", "Laptop Dell", "Disponible", false, new Date(2323223232L), 8, "Intel i5", "Intel Core i5"));
        equipos.add(new EquipoMedicion("E002", "Proyector Epson", "Disponible", false, new Date(2323223232L), new Date(2323223232L), 12));
        equipos.add(new Equipo("E003", "Microscopio", new Date(2323223232L)));

        // Configuración GUI
        JFrame win = new JFrame();
        win.setTitle("Gestión de Equipos");
        win.setSize(1000, 600);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creación de usuarios
        Estudiante estudiante1 = new Estudiante("1234567890", "Paul Rosero", "pmrosero@gmail.com", "Quito", "1234", "0987654321", "Electrónica", 3);
        Administrador administrador1 = new Administrador("0987654321", "David Iza", "david.iza@udla.ec", "Ambato", "5678", "1234567890");

        // Panel de login
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JTextField correoField = new JTextField();
        JPasswordField claveField = new JPasswordField();
        JButton loginButton = new JButton("Iniciar Sesión");

        loginPanel.add(new JLabel("Correo:"));
        loginPanel.add(correoField);
        loginPanel.add(new JLabel("Contraseña:"));
        loginPanel.add(claveField);
        loginPanel.add(loginButton);

        win.add(loginPanel);
        win.setVisible(true);

        loginButton.addActionListener(e -> {
            String correo = correoField.getText();
            String clave = new String(claveField.getPassword());

            Usuario usuarioActual = null;
            if (estudiante1.getCorreo().equals(correo) && estudiante1.getClave().equals(clave)) {
                usuarioActual = estudiante1;
                mostrarPanelEstudiante(win, (Estudiante) usuarioActual);
            } else if (administrador1.getCorreo().equals(correo) && administrador1.getClave().equals(clave)) {
                usuarioActual = administrador1;
                mostrarPanelAdministrador(win, (Administrador) usuarioActual);
            } else {
                JOptionPane.showMessageDialog(win, "Credenciales inválidas");
            }
        });
    }

    private static void mostrarPanelEstudiante(JFrame win, Estudiante estudiante) {
        win.getContentPane().removeAll();
        JPanel panelEstudiante = new JPanel(new BorderLayout());

        // Panel de Reservas Actuales
        JTable tablaReservas = new JTable(new DefaultTableModel(
                new String[]{"Número", "Equipo", "Fecha", "Duración (días)"}, 0
        ));

        reservas.stream()
                .filter(r -> r.getUsuario().equals(estudiante))
                .forEach(r -> {
                    DefaultTableModel model = (DefaultTableModel) tablaReservas.getModel();
                    model.addRow(new Object[]{
                            r.getNumeroReserva(),
                            r.getEquipo().getNombre(),
                            r.getFecha()
                    });
                });
        JScrollPane scrollReservas = new JScrollPane(tablaReservas);
        panelEstudiante.add(scrollReservas, BorderLayout.NORTH);

        // Panel de Nueva Reserva
        JPanel nuevaReservaPanel = new JPanel(new GridLayout(4, 2));
        JComboBox<String> equipoComboBox = new JComboBox<>(
                equipos.stream()
                        .filter(equipo -> !equipo.isPrestado())
                        .map(Equipo::getNombre)
                        .toArray(String[]::new)
        );
        JTextField duracionField = new JTextField();
        JComboBox<String> laboratorioComboBox = new JComboBox<>(new String[]{"Electrónica", "Automatización", "Robótica"});
        JTextField horarioField = new JTextField();
        JTextField ocupantesField = new JTextField();
        JButton reservarButton = new JButton("Realizar Reserva");

        nuevaReservaPanel.add(new JLabel("Equipo:"));
        nuevaReservaPanel.add(equipoComboBox);
        nuevaReservaPanel.add(new JLabel("Duración (días):"));
        nuevaReservaPanel.add(duracionField);
        nuevaReservaPanel.add(new JLabel("Laboratorio:"));
        nuevaReservaPanel.add(laboratorioComboBox);
        nuevaReservaPanel.add(new JLabel("Horario:"));
        nuevaReservaPanel.add(horarioField);
        nuevaReservaPanel.add(new JLabel("Número de Ocupantes:"));
        nuevaReservaPanel.add(ocupantesField);
        nuevaReservaPanel.add(new JLabel(""));
        nuevaReservaPanel.add(reservarButton);

        panelEstudiante.add(nuevaReservaPanel, BorderLayout.CENTER);

        reservarButton.addActionListener(e -> {
            String equipoSeleccionado = (String) equipoComboBox.getSelectedItem();
            int duracion;
            try {
                duracion = Integer.parseInt(duracionField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(win, "Duración inválida");
                return;
            }
            String laboratorioSeleccionado = (String) laboratorioComboBox.getSelectedItem();
            String horario = horarioField.getText();
            int ocupantes;
            try {
                ocupantes = Integer.parseInt(ocupantesField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(win, "Número de ocupantes inválido");
                return;
            }

            Equipo equipo = equipos.stream()
                    .filter(eq -> eq.getNombre().equals(equipoSeleccionado))
                    .findFirst()
                    .orElse(null);

            if (equipo != null && !equipo.isPrestado()) {
                CabeceraReserva nuevaReserva = new CabeceraReserva("UDLA", estudiante.getCarrera(), estudiante.getNombre(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                DetalleReserva detalleReserva = new DetalleReserva(horario, laboratorioSeleccionado, ocupantes);
                reservas.add(nuevaReserva);
                equipo.setPrestado(true);
                JOptionPane.showMessageDialog(win, "Reserva realizada con éxito");
            } else {
                JOptionPane.showMessageDialog(win, "El equipo seleccionado no está disponible");
            }
        });

        win.add(panelEstudiante);
        win.revalidate();
        win.repaint();
    }


    private static void mostrarPanelAdministrador(JFrame win, Administrador administrador) {
        win.getContentPane().removeAll();
        JPanel panelAdministrador = new JPanel(new BorderLayout());

        // Panel de Equipos que Requieren Mantenimiento
        JTable tablaEquipos = new JTable(new DefaultTableModel(
                new String[]{"Equipo", "Requiere Mantenimiento Correctivo", "Requiere Mantenimiento Preventivo"}, 0
        ));
        equipos.forEach(equipo -> {
            DefaultTableModel model = (DefaultTableModel) tablaEquipos.getModel();
            model.addRow(new Object[]{equipo.getNombre(), equipo.requiereMantenimientoCorrectivo(), equipo.requiereMantenimientoPreventivo()});
        });

        JScrollPane scrollEquipos = new JScrollPane(tablaEquipos);
        panelAdministrador.add(scrollEquipos, BorderLayout.NORTH);

        // Panel de Mantenimiento
        JPanel mantenimientoPanel = new JPanel(new GridLayout(2, 2));
        JComboBox<String> equipoMantenimientoComboBox = new JComboBox<>(
                equipos.stream()
                        .filter(equipo -> equipo.requiereMantenimientoCorrectivo() || equipo.requiereMantenimientoPreventivo())
                        .map(Equipo::getNombre)
                        .toArray(String[]::new)
        );
        JButton realizarMantenimientoButton = new JButton("Realizar Mantenimiento");

        mantenimientoPanel.add(new JLabel("Equipo para Mantenimiento:"));
        mantenimientoPanel.add(equipoMantenimientoComboBox);
        mantenimientoPanel.add(new JLabel(""));
        mantenimientoPanel.add(realizarMantenimientoButton);

        panelAdministrador.add(mantenimientoPanel, BorderLayout.CENTER);

        realizarMantenimientoButton.addActionListener(e -> {
            String equipoSeleccionado = (String) equipoMantenimientoComboBox.getSelectedItem();
            Equipo equipo = equipos.stream()
                    .filter(eq -> eq.getNombre().equals(equipoSeleccionado))
                    .findFirst()
                    .orElse(null);

            if (equipo != null) {
                administrador.realizarMantenimiento(equipo);
                JOptionPane.showMessageDialog(win, "Mantenimiento realizado con éxito");
                // Actualizar tabla de equipos
                ((DefaultTableModel)tablaEquipos.getModel()).setRowCount(0);
                equipos.forEach(eq -> {
                    DefaultTableModel model = (DefaultTableModel) tablaEquipos.getModel();
                    model.addRow(new Object[]{eq.getNombre(), eq.requiereMantenimientoCorrectivo(), eq.requiereMantenimientoPreventivo()});
                });
            }
        });

        win.add(panelAdministrador);
        win.revalidate();
        win.repaint();
    }
}