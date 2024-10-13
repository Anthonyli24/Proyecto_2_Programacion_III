package pos.presentation.Facturar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FacturarCobrar extends JDialog {
    private JTextField textEfectivo;
    private JTextField textTarjeta;
    private JTextField textCheque;
    private JTextField textSimpe;
    private JLabel importe;
    private JButton okButton;
    private JButton cancelarButton;
    private JPanel panel;
    private boolean pagoExitoso = false;

    Model model;
    Controller controller;

    public JPanel getPanel() {
        return panel;
    }

    public FacturarCobrar(Controller controller) {
        setContentPane(panel);
        setModal(true);
        pack();
        this.controller = controller;
        setImporte(controller);
        String var = "0";
        textEfectivo.setText(var);
        textTarjeta.setText(var);
        textCheque.setText(var);
        textSimpe.setText(var);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double efectivo = Double.parseDouble(textEfectivo.getText().trim());
                    double tarjeta = Double.parseDouble(textTarjeta.getText().trim());
                    double cheque = Double.parseDouble(textCheque.getText().trim());
                    double simpe = Double.parseDouble(textSimpe.getText().trim());
                    double importeValue = Double.parseDouble(importe.getText().trim());

                    double totalPagado = efectivo + tarjeta + cheque + simpe;

                    double vuelto = totalPagado - importeValue;

                    if (totalPagado >= importeValue) {
                        JOptionPane.showMessageDialog(null, "El pago se ha efectuado correctamente. Su vuelto es: " + vuelto , "Pago Exitoso", JOptionPane.INFORMATION_MESSAGE);
                        pagoExitoso = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "La suma de los medios de pago no es suficiente para cubrir el importe.", "Error de Pago", JOptionPane.WARNING_MESSAGE);
                    }

                    if (pagoExitoso) { dispose(); }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese valores numéricos válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cerrar la ventana si se cancela
            }
        });
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setImporte(Controller contro) {
        double pagoTotal = 0;
        try {
            pagoTotal = contro.PagoTotal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String pagoTotalStr = String.valueOf(pagoTotal);
        importe.setText(pagoTotalStr);
    }
    public boolean getPagoExitoso() {
        return pagoExitoso;
    }
}
