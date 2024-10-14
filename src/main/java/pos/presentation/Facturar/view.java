package pos.presentation.Facturar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import pos.logic.*;

public class view implements PropertyChangeListener {
    private JComboBox<String> comboBoxClientes;
    private JComboBox<String> comboBoxCajeros;
    private JTextField search;
    private JButton buttonAgregar;
    private JButton buttonCobrar;
    private JButton buttonBuscar;
    private JButton buttonCantidad;
    private JButton buttonQuitar;
    private JButton buttonDescuento;
    private JButton buttonCancelar;
    private JTable list;
    private JPanel panel;
    private JLabel articulosLabel;
    private JLabel articulo;
    private JLabel subtotalLabel;
    private JLabel subtotal;
    private JLabel descuentoLabel;
    private JLabel totalLabel;
    private JLabel descuento;
    private JLabel total;

    public JPanel getPanel() {
        return panel;
    }

    public view() {

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                actualizarComboBoxes();
                comboBoxCajeros.revalidate();
                comboBoxClientes.revalidate();
                comboBoxCajeros.repaint();
                comboBoxClientes.repaint();
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    int row = list.getSelectedRow();
                    controller.edit(row);
                }
            }
        });

        comboBoxClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cliente cli = (Cliente) comboBoxClientes.getSelectedItem();
                double discount = 0;
                discount = cli.getDescuento();
                for (Linea linea : model.getLineas()) {
                    linea.setDescuento((discount / 100) * linea.getProducto().getPrecioUnitario());
                }
                mostrarValoresFactura();                
                panel.revalidate();
                panel.repaint();
            }
        });


        buttonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBoxCajeros.getSelectedIndex() != -1 && comboBoxClientes.getSelectedIndex() != -1) {
                    if (controller != null) {
                        try {
                            Producto filter = new Producto();
                            filter.setCodigo(search.getText());
                            Producto pro = controller.BuscarProducto(filter);
                            Cliente selectedCliente = (Cliente) comboBoxClientes.getSelectedItem();
                            if (pro != null) {
                                controller.AgregarLinea(pro, selectedCliente);
                            } else {
                                JOptionPane.showMessageDialog(panel, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) { JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE); }
                    } else { JOptionPane.showMessageDialog(panel, "Controlador no está inicializado", "Error", JOptionPane.ERROR_MESSAGE); }
                } else { JOptionPane.showMessageDialog(panel, "Cliente y cajero no seleccionado", "Error", JOptionPane.ERROR_MESSAGE); }
            }
        });

        buttonCobrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!model.getLineas().isEmpty()) {
                    FacturarCobrar subventana = new FacturarCobrar(controller);
                    subventana.setModel(model);
                    subventana.setVisible(true);
                    subventana.setModal(true);
                    subventana.pack();
                    if (subventana.getPagoExitoso()) {
                        try {
                            Cliente selectedCliente = (Cliente) comboBoxClientes.getSelectedItem();
                            Cajero selectedCajero = (Cajero) comboBoxCajeros.getSelectedItem();
                            String nombreCliente = selectedCliente.getNombre();
                            String nombreCajero = selectedCajero.getNombre();
                            controller.crearFactura(nombreCliente,nombreCajero);
                            model.setLineas(new ArrayList<>());
                            model.setFilter(new Producto());
                            model.setCurrent(new Linea());
                            JOptionPane.showMessageDialog(panel, "Pago realizado con éxito. La factura ha sido guardada y las líneas limpiadas.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Ocurrió un error al procesar la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else{JOptionPane.showMessageDialog(panel, "El carrito esta vacío", "Error", JOptionPane.ERROR_MESSAGE);}
            }
        });

        buttonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Service service = Service.instance();
                FacturarBuscar buscar = new FacturarBuscar(controller);
                buscar.setVisible(true);
            }
        });

        buttonCantidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model != null && !model.getCurrent().getProducto().getCodigo().isEmpty()) {
                    FacturarCantidad subventanaCant = new FacturarCantidad();
                    subventanaCant.setModel(model);
                    subventanaCant.setController(controller);
                    subventanaCant.setVisible(true);
                    subventanaCant.setModal(true);
                    subventanaCant.pack();
                    model.setCurrent(new Linea());
                }
                else {JOptionPane.showMessageDialog(panel, "Producto no seleccionado", "Error", JOptionPane.ERROR_MESSAGE);}
            }
        });

        buttonQuitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    try {
                        if(!model.getCurrent().getProducto().getCodigo().isEmpty()) {
                            controller.BorrarLinea(model.getCurrent());
                            controller.iniciarLineas();
                            model.setCurrent(new Linea());
                            JOptionPane.showMessageDialog(panel, "Producto Eliminado", "", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {JOptionPane.showMessageDialog(panel, "No ha seleccionado un producto", "Error", JOptionPane.ERROR_MESSAGE);}
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Controlador no está inicializado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonDescuento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model != null && !model.getCurrent().getProducto().getCodigo().isEmpty()) {
                    FacturarDescuento facDes = new FacturarDescuento();
                    facDes.setModel(model);
                    facDes.setController(controller);
                    facDes.setVisible(true);
                    facDes.setModal(true);
                    facDes.pack();
                    model.setCurrent(new Linea());
                }
                else {JOptionPane.showMessageDialog(panel, "Producto no seleccionado", "Error", JOptionPane.ERROR_MESSAGE);}
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!model.getLineas().isEmpty()) {
                    if (controller != null) {
                        try {
                            controller.cancelar();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Ocurrió un error al procesar la cancelación de la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, "Controlador no está inicializado", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }else{JOptionPane.showMessageDialog(panel, "El carrito esta vacío", "Error", JOptionPane.ERROR_MESSAGE);}
            }
        });
    }

    Model model;
    Controller controller;

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void actualizarComboBoxes() {
        comboBoxClientes.setModel(new DefaultComboBoxModel(model.getClientes().toArray()));
        comboBoxCajeros.setModel(new DefaultComboBoxModel(model.getCajeros().toArray()));
    }

    private void mostrarValoresFactura() {
        pos.presentation.Facturar.TableModel tableModel = (pos.presentation.Facturar.TableModel) list.getModel();
        int rowCount = tableModel.getRowCount();

        int cantidadArticulos = 0;
        double subtotalFactura = 0;
        double descuentoFactura = 0;
        double totalFactura = 0;

        for (int i = 0; i < rowCount; i++) {
            int cantidad = (int) tableModel.getValueAt(i, pos.presentation.Facturar.TableModel.CANTIDAD);
            double precio = (double) tableModel.getValueAt(i, pos.presentation.Facturar.TableModel.PRECIO);
            double descuento = (double) tableModel.getValueAt(i, pos.presentation.Facturar.TableModel.DESCUENTO);

            cantidadArticulos += cantidad;
            subtotalFactura += precio;
            descuentoFactura += descuento;
        }

        totalFactura = subtotalFactura - descuentoFactura;

        articulo.setText(String.valueOf(cantidadArticulos));
        subtotal.setText(String.format("%.2f", subtotalFactura));
        descuento.setText(String.format("%.2f", descuentoFactura));
        total.setText(String.format("%.2f", totalFactura));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case pos.presentation.Facturar.Model.LISTLINEAS:
                int[] cols = {TableModel.CODIGO, TableModel.ARTICULO, TableModel.CATEGORIA, TableModel.CANTIDAD, TableModel.PRECIO, TableModel.DESCUENTO, TableModel.NETO, TableModel.IMPORTE};
                list.setModel(new TableModel(cols, model.getLineas()));
                list.setRowHeight(30);
                mostrarValoresFactura();
                break;
            case Model.LISTCLIENTES:
            case Model.LISTCAJEROS:
                actualizarComboBoxes();
                break;
            case Model.FILTER:
                search.setText(model.getFilter().getCodigo());
                break;
        }
        this.panel.revalidate();
    }
}
