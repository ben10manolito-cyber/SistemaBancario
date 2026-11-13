package vista;

import dao.CajeroDAO;
import dao.ClienteDAO;
import modelo.Cajero;
import modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmAdmin extends JFrame {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private CajeroDAO cajeroDAO = new CajeroDAO();

    private JTable tblClientes;
    private DefaultTableModel modelClientes;
    private JTable tblCajeros;
    private DefaultTableModel modelCajeros;

    public FrmAdmin() {
        setTitle("Módulo de Administración - Banco");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña 1: Gestión de Clientes
        tabbedPane.addTab("Clientes", crearPanelClientes());

        // Pestaña 2: Gestión de Cajeros
        tabbedPane.addTab("Cajeros", crearPanelCajeros());

        // Pestaña 3: Reportes / Estadísticas
        tabbedPane.addTab("Estadísticas", crearPanelEstadisticas());

        add(tabbedPane);
        cargarClientes();
        cargarCajeros();
    }

  
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout());

        
        modelClientes = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Teléfono", "Ciudad", "N° Cuenta", "Saldo", "Estado"}, 0);
        tblClientes = new JTable(modelClientes);
        panel.add(new JScrollPane(tblClientes), BorderLayout.CENTER);

        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtBuscar = new JTextField(12);
        JButton btnBuscarNombre = new JButton("Buscar Nombre");
        JButton btnBuscarId = new JButton("Buscar ID");
        JButton btnRefrescar = new JButton("Ver Todos");

        topPanel.add(new JLabel("Buscar:"));
        topPanel.add(txtBuscar);
        topPanel.add(btnBuscarNombre);
        topPanel.add(btnBuscarId);
        topPanel.add(btnRefrescar);

        panel.add(topPanel, BorderLayout.NORTH);

      
        JPanel botPanel = new JPanel();
        JButton btnAgregar = new JButton("Agregar Cliente");
        JButton btnEditar = new JButton("Editar Saldo/Datos");
        JButton btnEliminar = new JButton("Eliminar");

        botPanel.add(btnAgregar);
        botPanel.add(btnEditar);
        botPanel.add(btnEliminar);
        panel.add(botPanel, BorderLayout.SOUTH);

      
        btnRefrescar.addActionListener(e -> cargarClientes());

        btnBuscarNombre.addActionListener(e -> {
            String q = txtBuscar.getText().trim();
            if (!q.isEmpty()) {
                List<Cliente> res = clienteDAO.buscarPorNombre(q);
                actualizarTablaClientes(res);
            }
        });

        btnBuscarId.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtBuscar.getText().trim());
                Cliente c = clienteDAO.buscarPorId(id);
                modelClientes.setRowCount(0);
                if (c != null) {
                    modelClientes.addRow(new Object[]{c.getId(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getCiudad(), c.getNcuenta(), c.getSaldo(), c.getEstado() == 0 ? "Disponible" : "Ocupado"});
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un ID numérico válido.");
            }
        });

        btnAgregar.addActionListener(e -> agregarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());

        return panel;
    }

    
    private JPanel crearPanelCajeros() {
        JPanel panel = new JPanel(new BorderLayout());

        modelCajeros = new DefaultTableModel(new String[]{"ID Cajero", "$10k", "$20k", "$50k", "$100k", "Saldo Total", "Estado"}, 0);
        tblCajeros = new JTable(modelCajeros);
        panel.add(new JScrollPane(tblCajeros), BorderLayout.CENTER);

        JPanel botPanel = new JPanel();
        JButton btnAgregarCajero = new JButton("Agregar Cajero");
        JButton btnModificarBilletes = new JButton("Cargar/Quitar Billetes");
        JButton btnEliminarCajero = new JButton("Eliminar Cajero");
        JButton btnRefrescarC = new JButton("Actualizar Tabla");

        botPanel.add(btnAgregarCajero);
        botPanel.add(btnModificarBilletes);
        botPanel.add(btnEliminarCajero);
        botPanel.add(btnRefrescarC);

        panel.add(botPanel, BorderLayout.SOUTH);

        btnRefrescarC.addActionListener(e -> cargarCajeros());
        btnAgregarCajero.addActionListener(e -> agregarCajero());
        btnModificarBilletes.addActionListener(e -> modificarBilletes());
        btnEliminarCajero.addActionListener(e -> eliminarCajero());

        return panel;
    }

   
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTotalClientes = new JLabel("Total de clientes: -", SwingConstants.CENTER);
        JLabel lblPromedioSaldo = new JLabel("Saldo promedio: -", SwingConstants.CENTER);
        JButton btnCalcular = new JButton("Calcular Estadísticas en Tiempo Real");

        lblTotalClientes.setFont(new Font("Arial", Font.BOLD, 16));
        lblPromedioSaldo.setFont(new Font("Arial", Font.BOLD, 16));

        btnCalcular.addActionListener(e -> {
            List<Cliente> lista = clienteDAO.listarClientes();
            int total = lista.size();
            double suma = 0;
            for (Cliente c : lista) {
                suma += c.getSaldo();
            }
            double promedio = total > 0 ? (suma / total) : 0;

            lblTotalClientes.setText("Total de clientes: " + total);
            lblPromedioSaldo.setText(String.format("Saldo promedio: $%.2f COP", promedio));
        });

        panel.add(lblTotalClientes);
        panel.add(lblPromedioSaldo);
        panel.add(btnCalcular);

        return panel;
    }

   
    private void cargarClientes() {
        actualizarTablaClientes(clienteDAO.listarClientes());
    }

    private void actualizarTablaClientes(List<Cliente> lista) {
        modelClientes.setRowCount(0);
        for (Cliente c : lista) {
            modelClientes.addRow(new Object[]{
                c.getId(), c.getNombre(), c.getApellido(), c.getTelefono(),
                c.getCiudad(), c.getNcuenta(), c.getSaldo(),
                c.getEstado() == 0 ? "Disponible" : "Ocupado"
            });
        }
    }

    private void cargarCajeros() {
        modelCajeros.setRowCount(0);
        List<Cajero> lista = cajeroDAO.listarCajeros();
        for (Cajero c : lista) {
            modelCajeros.addRow(new Object[]{
                c.getId(), c.getNdiez(), c.getNveinte(), c.getNcincuenta(),
                c.getNcien(), c.getSaldoTotal(), c.getEstado() == 0 ? "Disponible" : "Ocupado"
            });
        }
    }

    private void agregarCliente() {
        JTextField nom = new JTextField();
        JTextField ape = new JTextField();
        JTextField tel = new JTextField();
        JTextField ciu = new JTextField();
        JTextField cta = new JTextField();
        JTextField sal = new JTextField("0");

        Object[] fields = {
            "Nombre:", nom, "Apellido:", ape, "Teléfono:", tel,
            "Ciudad:", ciu, "N° Cuenta:", cta, "Saldo Inicial:", sal
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Nuevo Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Cliente c = new Cliente(0, nom.getText(), ape.getText(), tel.getText(), ciu.getText(),
                        Integer.parseInt(cta.getText()), Double.parseDouble(sal.getText()), 0);
                if (clienteDAO.agregar(c)) {
                    JOptionPane.showMessageDialog(this, "Cliente guardado en BD.");
                    cargarClientes();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados.");
            }
        }
    }

    private void editarCliente() {
        int row = tblClientes.getSelectedRow();
        if (row >= 0) {
            int id = (int) modelClientes.getValueAt(row, 0);
            Cliente c = clienteDAO.buscarPorId(id);
            if (c != null) {
                JTextField nom = new JTextField(c.getNombre());
                JTextField ape = new JTextField(c.getApellido());
                JTextField tel = new JTextField(c.getTelefono());
                JTextField ciu = new JTextField(c.getCiudad());
                JTextField cta = new JTextField(String.valueOf(c.getNcuenta()));
                JTextField sal = new JTextField(String.valueOf(c.getSaldo()));

                Object[] fields = {
                    "Nombre:", nom, "Apellido:", ape, "Teléfono:", tel,
                    "Ciudad:", ciu, "N° Cuenta:", cta, "Saldo:", sal
                };

                int option = JOptionPane.showConfirmDialog(this, fields, "Editar Cliente ID " + id, JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        c.setNombre(nom.getText());
                        c.setApellido(ape.getText());
                        c.setTelefono(tel.getText());
                        c.setCiudad(ciu.getText());
                        c.setNcuenta(Integer.parseInt(cta.getText()));
                        c.setSaldo(Double.parseDouble(sal.getText()));

                        if (clienteDAO.editar(c)) {
                            JOptionPane.showMessageDialog(this, "Cliente actualizado en BD.");
                            cargarClientes();
                        } else {
                            JOptionPane.showMessageDialog(this, "No se pudo actualizar el cliente.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error en los datos ingresados.");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla.");
        }
    }

    private void eliminarCliente() {
        int row = tblClientes.getSelectedRow();
        if (row >= 0) {
            int id = (int) modelClientes.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar cliente ID " + id + "?") == JOptionPane.OK_OPTION) {
                clienteDAO.eliminar(id);
                cargarClientes();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla.");
        }
    }

    private void agregarCajero() {
        Cajero c = new Cajero(0, 10, 10, 10, 10, 0);
        if (cajeroDAO.agregar(c)) {
            JOptionPane.showMessageDialog(this, "Cajero registrado.");
            cargarCajeros();
        }
    }

    private void modificarBilletes() {
        int row = tblCajeros.getSelectedRow();
        if (row >= 0) {
            int id = (int) modelCajeros.getValueAt(row, 0);
            JTextField d10 = new JTextField(modelCajeros.getValueAt(row, 1).toString());
            JTextField d20 = new JTextField(modelCajeros.getValueAt(row, 2).toString());
            JTextField d50 = new JTextField(modelCajeros.getValueAt(row, 3).toString());
            JTextField d100 = new JTextField(modelCajeros.getValueAt(row, 4).toString());

            Object[] fields = {"$10,000:", d10, "$20,000:", d20, "$50,000:", d50, "$100,000:", d100};
            if (JOptionPane.showConfirmDialog(this, fields, "Modificar Billetes - Cajero " + id, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    cajeroDAO.actualizarBilletes(id, Integer.parseInt(d10.getText()), Integer.parseInt(d20.getText()),
                            Integer.parseInt(d50.getText()), Integer.parseInt(d100.getText()));
                    cargarCajeros();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Cantidades no válidas.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cajero de la tabla.");
        }
    }

    private void eliminarCajero() {
        int row = tblCajeros.getSelectedRow();
        if (row >= 0) {
            int id = (int) modelCajeros.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar cajero ID " + id + "?") == JOptionPane.OK_OPTION) {
                cajeroDAO.eliminar(id);
                cargarCajeros();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cajero de la tabla.");
        }
    }
}