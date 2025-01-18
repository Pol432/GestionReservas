package AppMain;

import ReservasManagement.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AdministradorPanel extends JPanel {
    private GUIController guiController;
    private App app;
    private Administrador administrador;

    private JTable tablaEquipos;
    private JComboBox<String> equipoComboBox;

    public AdministradorPanel(GUIController guiController, App app) {
        this.guiController = guiController;
        this.app = app;
        initializePanel();
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
        actualizarTablaEquipos();
        actualizarComboBoxEquipos();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());

        // Panel de tabla de equipos
        tablaEquipos = new JTable(new DefaultTableModel(
                new String[]{"Equipo", "Mantenimiento Correctivo", "Mantenimiento Preventivo"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        add(scrollPane, BorderLayout.NORTH);

        // Panel de mantenimiento
        JPanel mantenimientoPanel = new JPanel(new GridLayout(6, 1));
        equipoComboBox = new JComboBox<>();
        JButton btnPreventivo = new JButton("Mantenimiento Preventivo");
        JButton btnCorrectivo = new JButton("Mantenimiento Correctivo");
        JButton btnHistorial = new JButton("Ver Historial");

        mantenimientoPanel.add(new JLabel("Equipo para Mantenimiento:"));
        mantenimientoPanel.add(equipoComboBox);
        mantenimientoPanel.add(btnPreventivo);
        mantenimientoPanel.add(btnCorrectivo);
        mantenimientoPanel.add(btnHistorial);
        add(mantenimientoPanel, BorderLayout.CENTER);

        // Panel de salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> guiController.mostrarPanelLogin());
        JPanel salirPanel = new JPanel();
        salirPanel.add(btnSalir);
        add(salirPanel, BorderLayout.SOUTH);

        // Acciones de botones
        btnPreventivo.addActionListener(e -> realizarMantenimiento("Preventivo"));
        btnCorrectivo.addActionListener(e -> realizarMantenimiento("Correctivo"));
        btnHistorial.addActionListener(e -> mostrarHistorial());
    }

    private void actualizarTablaEquipos() {
        DefaultTableModel model = (DefaultTableModel) tablaEquipos.getModel();
        model.setRowCount(0); // Limpiar tabla

        List<Equipo> equipos = app.getEquipos();
        for (Equipo equipo : equipos) {
            model.addRow(new Object[]{
                    equipo.getNombre(),
                    equipo.requiereMantenimientoCorrectivo(),
                    equipo.requiereMantenimientoPreventivo()
            });
        }
    }

    private void realizarMantenimiento(String tipoMantenimiento) {
        String equipoSeleccionado = (String) equipoComboBox.getSelectedItem();
        if (equipoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String responsable = JOptionPane.showInputDialog(this, "Ingrese el nombre del responsable:");
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        int resultado = JOptionPane.showConfirmDialog(this, dateChooser, "Seleccione la fecha",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION && dateChooser.getDate() != null) {
            LocalDate fechaMantenimiento = dateChooser.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            Equipo equipo = app.buscarEquipoPorNombre(equipoSeleccionado);

            try {
                administrador.realizarMantenimiento(
                        equipo,
                        tipoMantenimiento,
                        fechaMantenimiento.toString(),
                        responsable,
                        app.getHistorialMantenimientos()
                );

                JOptionPane.showMessageDialog(this,
                        "Mantenimiento " + tipoMantenimiento + " realizado exitosamente.");
                actualizarTablaEquipos();
                actualizarComboBoxEquipos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarComboBoxEquipos() {
        equipoComboBox.removeAllItems();
        List<Equipo> equiposParaMantenimiento = administrador.obtenerEquiposParaMantenimiento(App.getEquipos());
        equiposParaMantenimiento.forEach(eq -> equipoComboBox.addItem(eq.getNombre()));
    }

    private void mostrarHistorial() {
        JDialog historialDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Historial de Mantenimientos", true);
        historialDialog.setSize(600, 400);

        String[] columnas = {"Equipo", "Tipo", "Fecha", "Responsable"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);

        for (RegistroMantenimiento registro : App.getHistorialMantenimientos()) {
            model.addRow(new Object[]{registro.getEquipo(), registro.getTipoMantenimiento(), registro.getFecha(), registro.getResponsable()});
        }

        JTable tablaHistorial = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        historialDialog.add(scrollPane);

        historialDialog.setLocationRelativeTo(this);
        historialDialog.setVisible(true);
    }
}
