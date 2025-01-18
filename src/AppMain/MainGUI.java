package AppMain;

import javax.swing.*;

public class MainGUI {
    private static App app = new App();
    private static JFrame mainFrame;

    public static void main(String[] args) {
        App.inicializar();
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Gestión de Equipos");
            mainFrame.setSize(800, 600);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GUIController controller = new GUIController(mainFrame, app);
            controller.mostrarPanelLogin();

            mainFrame.setVisible(true);
        });
    }
}