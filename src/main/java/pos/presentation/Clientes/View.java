package pos.presentation.Clientes;

import javax.swing.table.TableColumnModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import pos.logic.Cliente;
import pos.Application;
import javax.swing.*;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JTextField searchNombre;
    private JButton search;
    private JButton save;
    private JTable list;
    private JButton delete;
    private JLabel searchNombreLbl;
    private JButton report;
    private JTextField id;
    private JTextField nombre;
    private JTextField email;
    private JLabel idLbl;
    private JLabel nombreLbl;
    private JLabel emailLbl;
    private JButton clear;
    private JLabel telefonoLbl;
    private JTextField telefono;
    private JLabel descuentoLbl;
    private JTextField descuento;

    public JPanel getPanel() {
        return panel;
    }

    public View() {
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Cliente filter = new Cliente();
                    filter.setNombre(searchNombre.getText());
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
                    Cliente n = take();
                    try {
                        controller.save(n);
                        JOptionPane.showMessageDialog(panel, "Registro Aplicado", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "Cliente ya existe" , "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = list.getSelectedRow();
                controller.edit(row);
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.delete();
                    JOptionPane.showMessageDialog(panel, "Registro Borrado", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "No se pudo eliminar el cliente" , "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
                searchNombre.setText(" ");
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
    }

    private boolean validate() {
        boolean valid = true;
        if (id.getText().isEmpty()) {
            valid = false;
            idLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "ID requerido", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            idLbl.setBorder(null);
            idLbl.setToolTipText(null);
        }

        if (nombre.getText().isEmpty()) {
            valid = false;
            nombreLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Nombre requerido", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            nombreLbl.setBorder(null);
            nombreLbl.setToolTipText(null);
        }

        if (telefono.getText().isEmpty()) {
            valid = false;
            telefonoLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Telefono requerido", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            telefonoLbl.setBorder(null);
            telefonoLbl.setToolTipText(null);
        }

        if (email.getText().isEmpty()) {
            valid = false;
            emailLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Unidad requerida", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            emailLbl.setBorder(null);
            emailLbl.setToolTipText(null);
        }

        try {
            if(100 >= Double.parseDouble(descuento.getText())) {
                descuentoLbl.setBorder(null);
                descuentoLbl.setToolTipText(null);
            }
            valid = false;
            descuentoLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Descuento invalido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            valid = false;
            descuentoLbl.setBorder(Application.BORDER_ERROR);
            JOptionPane.showMessageDialog(panel, "Descuento invalido", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return valid;
    }

    public Cliente take() {
        Cliente e = new Cliente();
        e.setId(id.getText());
        e.setNombre(nombre.getText());
        e.setTelefono(telefono.getText());
        e.setEmail(email.getText());
        e.setDescuento(Double.parseDouble(descuento.getText()));
        return e;
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.TELEFONO, TableModel.EMAIL, TableModel.DESCUENTO};
                list.setModel(new TableModel(cols, model.getList()));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(3).setPreferredWidth(150);
                break;
            case Model.CURRENT:
                id.setText(model.getCurrent().getId());
                nombre.setText(model.getCurrent().getNombre());
                telefono.setText(model.getCurrent().getTelefono());
                email.setText(model.getCurrent().getEmail());
                descuento.setText("" + model.getCurrent().getDescuento());
                if (model.getMode() == Application.MODE_EDIT) {
                    id.setEnabled(false);
                    delete.setEnabled(true);
                } else {
                    id.setEnabled(true);
                    delete.setEnabled(false);
                }
                idLbl.setBorder(null);
                idLbl.setToolTipText(null);
                nombreLbl.setBorder(null);
                nombreLbl.setToolTipText(null);
                emailLbl.setBorder(null);
                emailLbl.setToolTipText(null);
                telefonoLbl.setBorder(null);
                telefonoLbl.setToolTipText(null);
                descuentoLbl.setBorder(null);
                descuentoLbl.setToolTipText(null);
                break;
            case Model.FILTER:
                searchNombre.setText(model.getFilter().getNombre());
                break;
        }
        this.panel.revalidate();
    }
}