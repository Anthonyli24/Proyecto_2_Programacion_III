package pos.presentation.Facturar;

import javax.swing.table.DefaultTableModel;
import pos.logic.Producto;
import pos.logic.Service;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class FacturarBuscar extends JDialog {
    private JTextField textField1;
    private JButton buscarButton;
    private JTable table1;
    private JPanel panel;
    private JButton atrasButton;
    private Service service;
    private Controller controlller;

    public FacturarBuscar(Controller controller) {
        setContentPane(panel);
        setModal(true);
        pack();
        this.controlller = controller;
        RellenarTabla();

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductos();
            }
        });

        atrasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RellenarTabla();
            }
        });
    }

    public Controller getController(){
        return controlller;
    }

    public void RellenarTabla(){
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Código", "Descripción", "Precio", "Existencias"}, 0);
        table1.setModel(model);
        for (Producto producto : getController().ListaPrincipalProductos()) {
            model.addRow(new Object[]{
                    producto.getCodigo(),
                    producto.getDescripcion(),
                    producto.getPrecioUnitario(),
                    producto.getExistencias()
            });
        }
    }

    private void buscarProductos() {
        String descripcion = textField1.getText();
        Producto filtro = new Producto();

        if (descripcion != null && !descripcion.isEmpty()) {filtro.setDescripcion(descripcion);}
        else {
            JOptionPane.showMessageDialog(null, "La descripción no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            List<Producto> productos = getController().buscarDescripcion(filtro);
            if (productos.isEmpty()) { throw new Exception("No se encontraron productos con la descripción: " + descripcion); }
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            for (Producto producto : productos) {
                model.addRow(new Object[]{
                        producto.getCodigo(),
                        producto.getDescripcion(),
                        producto.getPrecioUnitario(),
                        producto.getExistencias()
                });
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(null, "Error al buscar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);}
    }
}