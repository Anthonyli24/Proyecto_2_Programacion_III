package pos.presentation.Productos;

import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import pos.logic.Categoria;
import pos.logic.Producto;
import pos.Application;
import pos.logic.Service;

import javax.swing.*;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JButton search;
    private JButton save;
    private JTable list;
    private JButton delete;
    private JTextField codigo;
    private JTextField descripcion;
    private JTextField precio;
    private JLabel codigoLbl;
    private JLabel descripcionLbl;
    private JLabel precioLbl;
    private JButton clear;
    private JLabel unidadLbl;
    private JTextField unidad;
    private JLabel categoriaLbl;
    private JComboBox categoria;
    private JLabel searchProductoLbl;
    private JTextField searchDescripcion;
    private JLabel existenciasLbl;
    private JTextField existencias;
    private JButton report;

    public View() {
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Producto filter = new Producto();
                    filter.setDescripcion(searchDescripcion.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Producto n = take();
                    try {
                        controller.save(n);
                        JOptionPane.showMessageDialog(panel, "Registro Aplicado", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "Producto ya existe" , "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.delete();
                    JOptionPane.showMessageDialog(panel, "Registro Borrado", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "No se pudo eliminar el producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
                searchDescripcion.setText(" ");
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = list.getSelectedRow();
                controller.edit(row);
            }
        });

        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.print();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                model.setList(Service.instance().search(model.getFilter()));
            }
        });
    }

    public JPanel getPanel() { return panel; }

    Controller controller;
    Model model;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        searchDescripcion.setText(model.getFilter().getDescripcion());
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.CODIGO, TableModel.DESCRIPCION, TableModel.UNIDAD, TableModel.PRECIO, TableModel.CATEGORIA, TableModel.EXISTENCIAS, TableModel.IMAGEN};
                list.setModel(new TableModel(cols, model.getList()));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(1).setPreferredWidth(120);
                columnModel.getColumn(4).setPreferredWidth(120);
                columnModel.getColumn(6).setPreferredWidth(50);
                break;
            case Model.CURRENT:
                codigo.setText(model.getCurrent().getCodigo());
                descripcion.setText(model.getCurrent().getDescripcion());
                unidad.setText(model.getCurrent().getUnidadMedida());
                precio.setText(String.format("%.0f", model.getCurrent().getPrecioUnitario()));
                categoria.setSelectedItem(model.getCurrent().getCategoria());
                existencias.setText("" + model.getCurrent().getExistencias());

                codigoLbl.setBorder(null);
                codigoLbl.setToolTipText(null);
                descripcionLbl.setBorder(null);
                descripcionLbl.setToolTipText(null);
                precioLbl.setBorder(null);
                precioLbl.setToolTipText(null);
                unidadLbl.setBorder(null);
                unidadLbl.setToolTipText(null);
                categoriaLbl.setBorder(null);
                categoriaLbl.setToolTipText(null);
                break;
            case Model.CATEGORIAS:
                categoria.setModel(new DefaultComboBoxModel(model.getCategorias().toArray()));
                break;
        }
        if (model.getMode() == Application.MODE_EDIT) {
            codigo.setEnabled(false);
            delete.setEnabled(true);
        } else {
            codigo.setEnabled(true);
            delete.setEnabled(false);
        }

        this.panel.revalidate();
    }

    public Producto take() {
        Producto e = new Producto();
        e.setCodigo(codigo.getText());
        e.setDescripcion(descripcion.getText());
        e.setUnidadMedida(unidad.getText());
        e.setPrecioUnitario(Double.parseDouble(precio.getText()));
        e.setCategoria((Categoria) categoria.getSelectedItem());
        e.setExistencias(Integer.parseInt(existencias.getText()));
        return e;
    }

    private boolean validate() {
        boolean valid = true;

        if (codigo.getText().isEmpty()) {
            valid = false;
            codigoLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Código requerido", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            codigoLbl.setBorder(null);
            codigoLbl.setToolTipText(null);
        }

        if (descripcion.getText().isEmpty()) {
            valid = false;
            descripcionLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Descripción requerida", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            descripcionLbl.setBorder(null);
            descripcionLbl.setToolTipText(null);
        }

        if (unidad.getText().isEmpty()) {
            valid = false;
            unidadLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Unidad requerida", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            unidadLbl.setBorder(null);
            unidadLbl.setToolTipText(null);
        }

        try {
            if( 0 < Double.parseDouble(precio.getText())) {
                precioLbl.setBorder(null);
                precioLbl.setToolTipText(null);
            }
            else {
                valid = false;
                precioLbl.setBorder(Application.BORDER_ERROR);
                JOptionPane.showMessageDialog(panel, "Coloque un valor válido para precio", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            valid = false;
            precioLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Coloque un valor válido para precio", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            if( 0 <= Integer.parseInt(existencias.getText()) ) {
                existenciasLbl.setBorder(null);
                existenciasLbl.setToolTipText(null);
            }
            else{
                valid = false;
                existenciasLbl.setBorder(Application.BORDER_ERROR);
                JOptionPane.showMessageDialog(panel, "Coloque un valor válido para existencias", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            valid = false;
            existenciasLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Coloque un valor válido para existencias", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return valid;
    }
}