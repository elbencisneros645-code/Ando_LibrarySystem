package Main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.MainForm;

public class MainClass {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                System.out.println("Using default appearance.");
            }

            new MainForm().setVisible(true);
        });
    }
}