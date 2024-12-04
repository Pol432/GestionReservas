import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainGUI {
    private static List<Equipo> equipos = new ArrayList<>();
    private static List<CabeceraReserva> reservas = new ArrayList<>();

    public static void main(String[] args) {
        // Configuración inicial de equipos
        equipos.add(new Equipo("Osciloscopio", "Disponible", new Date()));
        equipos.add(new Equipo("Fuente de voltaje", "Requiere mantenimiento", new Date()));
        equipos.add(new Equipo("Generador de onda", "Requiere mantenimiento", new Date()));

        // Configuración GUI
        JFrame win = new JFrame();
        win.setTitle("Gestión de Equipos");
        win.setSize(800, 600);
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
        DefaultTableModel modeloReservas = new DefaultTableModel(
                new String[]{"Número", "Equipo", "Fecha", "Duración (días)", "Eliminar"}, 0
        );
        JTable tablaReservas = new JTable(modeloReservas);

        // Llenar la tabla con reservas existentes
        actualizarTablaReservas(modeloReservas, estudiante);

        JScrollPane scrollReservas = new JScrollPane(tablaReservas);
        panelEstudiante.add(scrollReservas, BorderLayout.NORTH);

        // Panel de Nueva Reserva
        JPanel nuevaReservaPanel = new JPanel(new GridLayout(4, 2));
        JComboBox<String> equipoComboBox = new JComboBox<>(equipos.stream()
                .filter(equipo -> !equipo.isPrestado())
                .map(Equipo::getNombre)
                .toArray(String[]::new)
        );
        JTextField duracionField = new JTextField();
        JComboBox<String> laboratorioComboBox = new JComboBox<>(new String[]{"Electrónica", "Automatización", "Robótica"});
        JTextField horarioField = new JTextField();
        JTextField ocupantesField = new JTextField();
        JButton reservarButton = new JButton("Realizar Prestamo");

        nuevaReservaPanel.setLayout(new GridLayout(0, 4));
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
        nuevaReservaPanel.add(new JLabel(""));  // Espacio vacío
        nuevaReservaPanel.add(reservarButton);

        panelEstudiante.add(nuevaReservaPanel, BorderLayout.CENTER);

        // Acción del botón de reserva
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
                // Crear DetalleReserva
                DetalleReserva detalleReserva = new DetalleReserva(horario, laboratorioSeleccionado, ocupantes);

                // Crear CabeceraReserva
                CabeceraReserva nuevaReserva = new CabeceraReserva("UDLA", estudiante.getCarrera(), estudiante.getNombre(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                nuevaReserva.setDetalleReserva(detalleReserva);
                nuevaReserva.setUsuario(estudiante);
                nuevaReserva.setEquipo(equipo);

                reservas.add(nuevaReserva);
                equipo.setPrestado(true);

                // Actualizar la tabla con la nueva reserva
                actualizarTablaReservas(modeloReservas, estudiante);

                JOptionPane.showMessageDialog(win, "Equipo prestado con éxito");
            } else {
                JOptionPane.showMessageDialog(win, "El equipo seleccionado no está disponible");
            }
        });

        // Botón de Eliminar Reserva
        JButton eliminarReservaButton = new JButton("Devolver Equipo");

        eliminarReservaButton.addActionListener(e -> {
            int filaSeleccionada = tablaReservas.getSelectedRow();
            if (filaSeleccionada != -1) {
                int numeroReserva = (int) modeloReservas.getValueAt(filaSeleccionada, 0);
                CabeceraReserva reservaAEliminar = reservas.stream()
                        .filter(reserva -> reserva.getNumeroReserva() == numeroReserva)
                        .findFirst()
                        .orElse(null);

                if (reservaAEliminar != null) {
                    // Devolver el equipo a disponibilidad
                    reservaAEliminar.getEquipo().setPrestado(false);
                    // Eliminar la reserva de la lista
                    reservas.remove(reservaAEliminar);
                    // Actualizar la tabla
                    actualizarTablaReservas(modeloReservas, estudiante);
                    JOptionPane.showMessageDialog(win, "Equipo devuelto con éxito");
                }
            } else {
                JOptionPane.showMessageDialog(win, "Selecciona el equipo que van a devolver eliminar");
            }
        });

        // Agregar el botón de eliminar debajo de la tabla
        JPanel eliminarReservaPanel = new JPanel();
        eliminarReservaPanel.add(eliminarReservaButton);
        panelEstudiante.add(eliminarReservaPanel, BorderLayout.SOUTH);

        win.add(panelEstudiante);
        win.revalidate();
        win.repaint();
    }



    // Método para actualizar la tabla con las reservas de un estudiante específico
    private static void actualizarTablaReservas(DefaultTableModel modeloReservas, Estudiante estudiante) {
        modeloReservas.setRowCount(0); // Limpiar la tabla
        reservas.stream()
                .filter(r -> r.getUsuario().equals(estudiante))
                .forEach(r -> modeloReservas.addRow(new Object[]{
                        r.getNumeroReserva(),
                        r.getEquipo().getNombre(),
                        r.getFecha(),
                        r.getDetalleReserva().getNumeroOcupantes()
                }));
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