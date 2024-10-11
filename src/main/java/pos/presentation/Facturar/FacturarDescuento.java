package pos.presentation.Facturar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FacturarDescuento extends JDialog {
    private JTextField textField1;
    private JButton okButton;
    private JButton cancelarButton;
    private JPanel panel;

    Model model;
    Controller controller;

    public JPanel getPanel() {
        return panel;
    }

    public FacturarDescuento() {
        setContentPane(panel);
        setModal(true);
        pack();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double descuento = Double.parseDouble(textField1.getText());
                    if (descuento < 0) {
                        JOptionPane.showMessageDialog(null, "El descuento no puede ser negativo.", "Error de Descuento", JOptionPane.ERROR_MESSAGE);
                    } else {
                        model.getCurrent().setDescuento(descuento);
                        controller.iniciarLineas();
                        JOptionPane.showMessageDialog(null, "Descuento aplicado correctamente.", "Descuento Aplicado", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un valor numérico válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Descuento cancelado.", "Descuento Cancelado", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
    }
    public void setModel(Model model) {this.model = model;}
    public void setController(Controller controller) {this.controller = controller;}
}