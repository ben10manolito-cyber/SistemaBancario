package vista;

import dao.ClienteDAO;
import modelo.Cliente;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FrmInicio extends JFrame {

    public FrmInicio() {
        setTitle("Sistema Bancario - Inicio");
        setSize(360, 140);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));
        JButton btnAdmin = new JButton("Módulo Administración");
        JButton btnCajero = new JButton("Módulo Cajero");

        // Evento temporal para verificar lectura de la BD al pulsar Administración
        btnAdmin.addActionListener(e -> {
            ClienteDAO dao = new ClienteDAO();
            List<Cliente> clientes = dao.listarClientes();
            JOptionPane.showMessageDialog(this, 
                "Acceso a Administración.\nClientes registrados en la BD remota: " + clientes.size());
        });

        btnCajero.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Acceso a Módulo Cajero.");
        });

        panel.add(btnAdmin);
        panel.add(btnCajero);
        add(panel);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new FrmInicio().setVisible(true);
        });
    }
}