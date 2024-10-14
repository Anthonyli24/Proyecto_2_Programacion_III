package pos.presentation.Cajero;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import pos.logic.Cajero;
import pos.Application;
import javax.swing.*;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JLabel searchNombreLbl;
    private JTextField searchNombre;
    private JButton search;
    private JTable list;
    private JLabel idLbl;
    private JTextField id;
    private JLabel nombreLbl;
    private JTextField nombre;
    private JButton save;
    private JButton delete;
    private JButton clear;
    private JButton report;

    public JPanel getPanel() {
        return panel;
    }

    public View() {
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Cajero filter = new Cajero();
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
                    Cajero n = take();
                    try {
                        controller.save(n);
                        JOptionPane.showMessageDialog(panel, "Registro Aplicado", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "Cajero ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
                    JOptionPane.showMessageDialog(panel, "No se pudo eliminar el cajero" , "Error", JOptionPane.ERROR_MESSAGE);
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
        return valid;
    }

    public Cajero take() {
        Cajero e = new Cajero();
        e.setID(id.getText());
        e.setNombre(nombre.getText());
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
            case pos.presentation.Cajero.Model.LIST:
                list.setModel(new DefaultTableModel(new Object[]{"Código", "Descripción", "Precio", "Existencias"}, 0));
                int[] cols = {pos.presentation.Cajero.TableModel.ID, pos.presentation.Cajero.TableModel.NOMBRE};
                list.setModel(new TableModel(cols, model.getList()));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                break;
            case pos.presentation.Cajero.Model.CURRENT:
                id.setText(model.getCurrent().getID());
                nombre.setText(model.getCurrent().getNombre());

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
                break;
            case Model.FILTER:
                searchNombre.setText(model.getFilter().getNombre());
                break;
        }
        this.panel.revalidate();
    }
}