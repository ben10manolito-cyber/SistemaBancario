package vista;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FrmInicio extends JFrame {

    public FrmInicio() {
        setTitle("Sistema Bancario - Selección de Módulo");
        setSize(420, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JLabel lblTitulo = new JLabel("Bienvenido al Sistema Bancario", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(lblTitulo);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAdmin = new JButton("Módulo Administración");
        JButton btnCajero = new JButton("Módulo Cajero");

        btnAdmin.setFont(new Font("Arial", Font.PLAIN, 13));
        btnCajero.setFont(new Font("Arial", Font.PLAIN, 13));

        // Evento para abrir el Módulo de Administración
        btnAdmin.addActionListener(e -> {
            new FrmAdmin().setVisible(true);
        });

        // Evento para abrir el Módulo de Cajero Automático
        btnCajero.addActionListener(e -> {
            new FrmCajero().setVisible(true);
        });

        panelBotones.add(btnAdmin);
        panelBotones.add(btnCajero);
        mainPanel.add(panelBotones);

        add(mainPanel);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new FrmInicio().setVisible(true);
        });
    }
}