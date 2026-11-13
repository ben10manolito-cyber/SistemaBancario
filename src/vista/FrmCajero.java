package vista;

import dao.CajeroDAO;
import dao.ClienteDAO;
import modelo.Cajero;
import modelo.Cliente;

import javax.swing.*;
import java.awt.*;

public class FrmCajero extends JFrame {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private CajeroDAO cajeroDAO = new CajeroDAO();

    private Cliente clienteActual = null;
    private Cajero cajeroActual = null;

    private JTextField txtPantalla;
    private JLabel lblNombreCliente;
    private JLabel lblEstadoSesion;

    public FrmCajero() {
        setTitle("Cajero Automático");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

       
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                finalizarSesion();
            }
        });

       
        JPanel topPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblEstadoSesion = new JLabel("Ingrese ID Cajero y N° Cuenta para iniciar", SwingConstants.CENTER);
        lblNombreCliente = new JLabel("Cliente: ---", SwingConstants.CENTER);
        lblNombreCliente.setFont(new Font("Arial", Font.BOLD, 14));

        txtPantalla = new JTextField();
        txtPantalla.setFont(new Font("Monospaced", Font.BOLD, 18));
        txtPantalla.setHorizontalAlignment(JTextField.RIGHT);
        txtPantalla.setEditable(false);

        topPanel.add(lblEstadoSesion);
        topPanel.add(lblNombreCliente);
        topPanel.add(txtPantalla);
        add(topPanel, BorderLayout.NORTH);


        JPanel keypadPanel = new JPanel(new GridLayout(5, 3, 5, 5));
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] botones = {
            "7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            "0", "Backspace", "Limpiar"
        };

        for (String b : botones) {
            JButton btn = new JButton(b);
            btn.addActionListener(e -> procesarTeclado(b));
            keypadPanel.add(btn);
        }

        JButton btnIngresar = new JButton("Ingresar / Validar");
        JButton btnRetirar = new JButton("Retirar");
        JButton btnCancelar = new JButton("Cancelar / Salir");

        btnIngresar.addActionListener(e -> iniciarSesion());
        btnRetirar.addActionListener(e -> ejecutarRetiro());
        btnCancelar.addActionListener(e -> finalizarSesion());

        keypadPanel.add(btnIngresar);
        keypadPanel.add(btnRetirar);
        keypadPanel.add(btnCancelar);

        add(keypadPanel, BorderLayout.CENTER);
    }

    private void procesarTeclado(String comando) {
        if (comando.equals("Limpiar")) {
            txtPantalla.setText("");
        } else if (comando.equals("Backspace")) {
            String t = txtPantalla.getText();
            if (!t.isEmpty()) {
                txtPantalla.setText(t.substring(0, t.length() - 1));
            }
        } else {
            txtPantalla.setText(txtPantalla.getText() + comando);
        }
    }

    private void iniciarSesion() {
        if (clienteActual != null) {
            JOptionPane.showMessageDialog(this, "Ya hay una sesión activa.");
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Ingrese datos en formato: ID_CAJERO,NUMERO_CUENTA\nEjemplo: 1,12345");
        if (input == null || !input.contains(",")) {
            JOptionPane.showMessageDialog(this, "Formato incorrecto.");
            return;
        }

        try {
            String[] partes = input.split(",");
            int idCajero = Integer.parseInt(partes[0].trim());
            int ncuenta = Integer.parseInt(partes[1].trim());

            Cajero cj = cajeroDAO.buscarPorId(idCajero);
            Cliente cl = clienteDAO.buscarPorNCuenta(ncuenta);

            if (cj == null || cl == null) {
                JOptionPane.showMessageDialog(this, "Cajero o Cuenta inexistente.");
                return;
            }

            if (cj.getEstado() == 1 || cl.getEstado() == 1) {
                JOptionPane.showMessageDialog(this, "El cajero o el cliente se encuentran ocupados actualmente.");
                return;
            }

           
            cajeroDAO.cambiarEstado(cj.getId(), 1);
            clienteDAO.cambiarEstado(cl.getId(), 1);

            this.cajeroActual = cj;
            this.clienteActual = cl;

            lblNombreCliente.setText("Cliente: " + cl.getNombre() + " " + cl.getApellido());
            lblEstadoSesion.setText("Sesión Ocupada (ID Cajero: " + cj.getId() + ")");
            txtPantalla.setText("");
            JOptionPane.showMessageDialog(this, "Bienvenido " + cl.getNombre() + ". Puede ingresar el monto a retirar.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en los datos ingresados.");
        }
    }

    private void ejecutarRetiro() {
        if (clienteActual == null || cajeroActual == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión primero.");
            return;
        }

        String textoMonto = txtPantalla.getText().trim();
        if (textoMonto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto en la pantalla.");
            return;
        }

        try {
            int monto = Integer.parseInt(textoMonto);

         
            if (monto < 10000 || monto > 1000000 || monto % 10000 != 0) {
                JOptionPane.showMessageDialog(this, "Monto no válido. Debe ser entre $10.000 y $1.000.000, en múltiplos de 10.000.");
                txtPantalla.setText("");
                return;
            }

       
            if (clienteActual.getSaldo() < monto) {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente en su cuenta.");
                return;
            }

           
            if (cajeroActual.getSaldoTotal() < monto) {
                JOptionPane.showMessageDialog(this, "El cajero no tiene saldo suficiente.");
                return;
            }

           
            int[] desglose = calcularBilletes(monto, cajeroActual);
            if (desglose == null) {
                JOptionPane.showMessageDialog(this, "El cajero no cuenta con la combinación necesaria de billetes.");
                return;
            }

           
            int b10 = desglose[0], b20 = desglose[1], b50 = desglose[2], b100 = desglose[3];

            
            cajeroActual.setNdiez(cajeroActual.getNdiez() - b10);
            cajeroActual.setNveinte(cajeroActual.getNveinte() - b20);
            cajeroActual.setNcincuenta(cajeroActual.getNcincuenta() - b50);
            cajeroActual.setNcien(cajeroActual.getNcien() - b100);

            cajeroDAO.actualizarBilletes(cajeroActual.getId(), cajeroActual.getNdiez(), cajeroActual.getNveinte(),
                    cajeroActual.getNcincuenta(), cajeroActual.getNcien());

            
            double nuevoSaldo = clienteActual.getSaldo() - monto;
            clienteActual.setSaldo(nuevoSaldo);
            clienteDAO.actualizarSaldo(clienteActual.getId(), nuevoSaldo);

         
            String resumen = String.format("Retiro Exitoso!\n\nDesglose de billetes entregados:\n- Billetes $100.000: %d\n- Billetes $50.000: %d\n- Billetes $20.000: %d\n- Billetes $10.000: %d\n\nSaldo restante en cuenta: $%.2f COP",
                    b100, b50, b20, b10, nuevoSaldo);

            JOptionPane.showMessageDialog(this, resumen);
            txtPantalla.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto numérico no válido.");
        }
    }

    private int[] calcularBilletes(int monto, Cajero cj) {
        int b100 = 0, b50 = 0, b20 = 0, b10 = 0;
        int restante = monto;

       
        if (monto == 50000) {
            b20 = 2; b10 = 1;
            restante = 0;
        } else if (monto == 100000) {
            b50 = 1; b20 = 2; b10 = 1;
            restante = 0;
        } else if (monto > 100000) {
            b50 = 1; b20 = 2; b10 = 1;
            restante = monto - 100000;
        }

      
        if (b10 > cj.getNdiez() || b20 > cj.getNveinte() || b50 > cj.getNcincuenta()) {
            return null;
        }

       
        int c100 = Math.min(restante / 100000, cj.getNcien() - b100);
        b100 += c100;
        restante -= c100 * 100000;

        int c50 = Math.min(restante / 50000, cj.getNcincuenta() - b50);
        b50 += c50;
        restante -= c50 * 50000;

        int c20 = Math.min(restante / 20000, cj.getNveinte() - b20);
        b20 += c20;
        restante -= c20 * 20000;

        int c10 = Math.min(restante / 10000, cj.getNdiez() - b10);
        b10 += c10;
        restante -= c10 * 10000;

        if (restante > 0) return null; 

        return new int[]{b10, b20, b50, b100};
    }

    private void finalizarSesion() {
        if (clienteActual != null && cajeroActual != null) {
            clienteDAO.cambiarEstado(clienteActual.getId(), 0);
            cajeroDAO.cambiarEstado(cajeroActual.getId(), 0);
            JOptionPane.showMessageDialog(this, "Sesión finalizada. Usuario y Cajero retornaron a estado Disponible.");
            clienteActual = null;
            cajeroActual = null;
        }
        lblNombreCliente.setText("Cliente: ---");
        lblEstadoSesion.setText("Ingrese ID Cajero y N° Cuenta para iniciar");
        txtPantalla.setText("");
    }
}
