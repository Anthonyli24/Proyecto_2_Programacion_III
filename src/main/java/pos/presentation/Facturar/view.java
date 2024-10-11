package pos.presentation.Facturar;

import pos.logic.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class view implements PropertyChangeListener {
    private JComboBox<Cliente> comboBoxClientes;
    private JComboBox<Cajero> comboBoxCajeros;
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

    public void iniciarComboBoxClientes(List<Cliente> clientes) {
        comboBoxClientes.removeAllItems(); // Limpiar los elementos existentes
        for (Cliente cliente : clientes) {
            comboBoxClientes.addItem(cliente); // Agregar el objeto Cliente
        }
        comboBoxClientes.setSelectedIndex(-1); // Establecer ninguna selección
    }


    public void iniciarComboBoxCajeros(List<Cajero> cajeros){
        comboBoxCajeros.removeAllItems();
        for (Cajero cajero :cajeros) {
            comboBoxCajeros.addItem(cajero);
        }
        comboBoxCajeros.setSelectedIndex(-1);
    }

    public view() {
        buttonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (comboBoxCajeros.getSelectedIndex() != -1 && comboBoxClientes.getSelectedIndex() != -1) {
                    if (controller != null) {
                        try {
                            Producto filter = new Producto();
                            filter.setCodigo(search.getText());
                            Producto pro = controller.BuscarProducto(filter);
                            if(model.getFcurrent().getCliente().getNombre().isEmpty() && model.getFcurrent().getCajero().getNombre().isEmpty()) {
                                //crear la factura
                                int numero = Service.instance().getFacturas().size()+1;
                                LocalDate localDate = LocalDate.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                                String formattedDate = localDate.format(formatter);
                                String fecha = formattedDate;
                                String numCodigo = Integer.toString(numero);
                                String nombreFactura = "FC0"+ numCodigo;


                                model.getFcurrent().setCajero((Cajero) comboBoxCajeros.getSelectedItem());
                                model.getFcurrent().setCliente((Cliente) comboBoxClientes.getSelectedItem());
                                model.getFcurrent().setFecha(fecha);
                                model.getFcurrent().setID(nombreFactura);

                            }

                            if (pro != null) {

                            controller.AgregarLinea(pro,model.getFcurrent());

                            } else {
                                JOptionPane.showMessageDialog(panel, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {JOptionPane.showMessageDialog(panel, "Controlador no está inicializado", "Error", JOptionPane.ERROR_MESSAGE);}
                }else{JOptionPane.showMessageDialog(panel, "Cliente y cajero no seleccionado", "Error", JOptionPane.ERROR_MESSAGE);}
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
                            String itemCliente = (String) comboBoxClientes.getSelectedItem();
                            String itemCajero = (String) comboBoxCajeros.getSelectedItem();
                            Factura factura = controller.crearFactura(itemCliente,itemCajero);
                            Service.instance().create(factura);
                            System.out.println("FACTURAS"+Service.instance().getFacturas());
                            controller.cancelar();
                            comboBoxClientes.setSelectedIndex(-1);
                            comboBoxCajeros.setSelectedIndex(-1);
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
                FacturarBuscar buscar = null;
                try {
                    buscar = new FacturarBuscar(controller);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
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
                            controller.BorrarLinea(model.getCurrent(),model.getFcurrent());
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

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    int row = list.getSelectedRow();
                    controller.edit(row);
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

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                controller.CrearFacturaNueva();
            }
        });

    }

    Model model;
    Controller controller;

    private void actualizarEstadoComboBoxClientes() {
        if (model != null) {
            boolean vacio = model.getFcurrent().getCarrito().isEmpty();
            comboBoxClientes.setEnabled(vacio);
        }
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void mostrarValoresFactura() {
        pos.presentation.Facturar.TableModel tableModel = (pos.presentation.Facturar.TableModel) list.getModel();
        int rowCount = tableModel.getRowCount();

        int cantidadArticulos = 0;
        double subtotalFactura = 0;
        double descuentoFactura = 0;
        double totalFactura = 0;

        for (int i = 0; i < rowCount; i++) {
            int cantidad = ((Number) tableModel.getValueAt(i, pos.presentation.Facturar.TableModel.CANTIDAD)).intValue();
            double precio = ((Number) tableModel.getValueAt(i, pos.presentation.Facturar.TableModel.PRECIO)).doubleValue();
            double descuento = ((Number) tableModel.getValueAt(i, pos.presentation.Facturar.TableModel.DESCUENTO)).doubleValue();
            double neto = ((Number) tableModel.getValueAt(i, pos.presentation.Facturar.TableModel.NETO)).doubleValue();

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


    public Cliente getSelectedCliente() {
        Cliente selectedCliente = (Cliente) comboBoxClientes.getSelectedItem(); // Obtener el objeto Cliente directamente
        return selectedCliente; // Retornar el objeto seleccionado (puede ser null si no hay selección)
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case pos.presentation.Facturar.Model.LISTLINEAS:
                list.setModel(new DefaultTableModel(new Object[]{"Código", "Articulo", "Categoria", "Cantidad","Precio","Descuento","Neto", "Importe"}, 0));
                int[] cols = {pos.presentation.Facturar.TableModel.CODIGO, pos.presentation.Facturar.TableModel.ARTICULO, pos.presentation.Facturar.TableModel.CATEGORIA, pos.presentation.Facturar.TableModel.CANTIDAD, pos.presentation.Facturar.TableModel.PRECIO, pos.presentation.Facturar.TableModel.DESCUENTO, pos.presentation.Facturar.TableModel.NETO, pos.presentation.Facturar.TableModel.IMPORTE};
                list.setModel(new pos.presentation.Facturar.TableModel(cols, model.getFcurrent().getCarrito()));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(6).setPreferredWidth(100);
                mostrarValoresFactura();
                actualizarEstadoComboBoxClientes();
                break;
            case Model.LISTCLIENTES:
                iniciarComboBoxClientes(model.getClientes());
                break;
            case Model.LISTCAJEROS:
                iniciarComboBoxCajeros(model.getCajeros());
                break;
            case Model.FILTER:
                search.setText(model.getFilter().getCodigo());
                break;
        }
        this.panel.revalidate();
    }
}