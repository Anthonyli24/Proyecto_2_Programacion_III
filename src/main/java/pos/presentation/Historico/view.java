package pos.presentation.Historico;

import pos.Application;
import pos.logic.Factura;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class view implements PropertyChangeListener {

    private JPanel panelHistorico;
    private JButton reporte_Button;
    private JButton buscarButton;
    private JTextField search_txt;
    private JLabel cliente_lbl;
    private JTable TablaFacturas;
    private JTable TablaLineas;

    public JPanel getPanel() {
        return panelHistorico;
    }

    public view() {
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Factura filter = new Factura();
                    filter.setNombreCli(search_txt.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelHistorico, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        reporte_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.print();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelHistorico, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        TablaFacturas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = TablaFacturas.getSelectedRow();
                controller.edit(row);
                String codeFilter = (String) TablaFacturas.getValueAt(row, 0);
                try {
                    controller.buscarFactura(codeFilter);
                    search_txt.setText("");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        panelHistorico.addMouseListener(new MouseAdapter() {
        });
        panelHistorico.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                try {
                    controller.loadFacturas();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        if (search_txt.getText().isEmpty()) {
            valid = false;
            cliente_lbl.setBorder(Application.BORDER_ERROR);
            cliente_lbl.setToolTipText("ID requerido");
        } else {
            cliente_lbl.setBorder(null);
            cliente_lbl.setToolTipText(null);
        }
        return valid;
    }

    public Factura take() {
        Factura f = new Factura();
        f.setCodigoFactura(search_txt.getText());
        return f;
    }

    pos.presentation.Historico.Model model;
    pos.presentation.Historico.Controller controller;

    public void setModel(pos.presentation.Historico.Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case pos.presentation.Historico.Model.LIST:
                int[] cols = {TableModel.CODIGO, TableModel.FECHA, TableModel.NOMBRE_CLIENTE, TableModel.NOMBRE_CAJERO, TableModel.TOTAL};
                TablaFacturas.setModel(new TableModel(cols, model.getList()));
                TablaFacturas.setRowHeight(30);

                TableColumnModel columnModel = TablaFacturas.getColumnModel();
                columnModel.getColumn(3).setPreferredWidth(150);  // Columna del nombre del cajero

                int[] cols2 = {TableModel2.CODIGO, TableModel2.ARTICULO, TableModel2.CATEGORIA, TableModel2.CANTIDAD, TableModel2.PRECIO, TableModel2.DESCUENTO, TableModel2.NETO, TableModel2.IMPORTE};
                TablaLineas.setModel(new TableModel2(cols2, model.getListalineas()));
                TablaLineas.setRowHeight(30);
                TableColumnModel columnModel2 = TablaLineas.getColumnModel();
                columnModel2.getColumn(6).setPreferredWidth(100);
                break;

            case pos.presentation.Historico.Model.CURRENT:
                search_txt.setText(model.getCurrent().getCodigoFactura());
                cliente_lbl.setBorder(null);
                cliente_lbl.setToolTipText(null);
                break;

            case pos.presentation.Historico.Model.FILTER:
                search_txt.setText(model.getFilter().getCodigoFactura());
                break;
        }
        this.panelHistorico.revalidate();
    }
}
