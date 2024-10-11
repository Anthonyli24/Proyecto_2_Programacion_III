package pos.presentation.Facturar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FacturarCantidad extends JDialog {
    private JPanel panel;
    private JTextField textField1;
    private JButton okButton;
    private JButton cancelarButton;

    pos.presentation.Facturar.Model model;
    pos.presentation.Facturar.Controller controller;

    public FacturarCantidad() {
        setContentPane(panel);
        setModal(true);
        pack();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int cantidad = Integer.parseInt(textField1.getText());
                    if (cantidad < 0) {
                        JOptionPane.showMessageDialog(panel, "La cantidad no puede ser negativa.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }else{
                        if(cantidad < model.getCurrent().getProducto().getExistencias()) {
                            model.getCurrent().setCantidad(cantidad);
                            model.getCurrent().getProducto().setExistencias(model.getCurrent().getProducto().getExistencias()-cantidad);
                            controller.iniciarLineas();
                            JOptionPane.showMessageDialog(null, "Cantidad aplicada.", "Cantidad Aplicada", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }else{
                            JOptionPane.showMessageDialog(panel, "No hay existencias", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public void setModel(Model model) { this.model = model; }
    public void setController(Controller controller) { this.controller = controller; }
}
