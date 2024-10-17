package pos.presentation.Estadistica;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import java.beans.PropertyChangeListener;
import java.awt.event.ComponentAdapter;
import java.beans.PropertyChangeEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import java.awt.event.ActionEvent;
import pos.logic.Categoria;
import pos.logic.Service;
import pos.logic.Rango;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class View implements PropertyChangeListener {
    private JPanel panel1;
    private JComboBox<String> comboBoxAnniosDesde;
    private JComboBox<String> comboBoxMesDesde;
    private JComboBox<String> comboBoxAnniosHasta;
    private JComboBox<String> comboBoxMesHasta;
    private JComboBox<String> categorias;
    private JButton agregarCategoria;
    private JTable table1;
    public JPanel PanelGrafico;
    private JLabel desdeLabel;
    private JLabel hastaLabel;
    private JButton deleteAll;
    private JButton deleteCategoria;
    private JLabel categoriaLabel;
    private JButton agregarAll;

    private Model model;
    private Controller controller;

    public JPanel getPanel1() { return panel1; }

    public View() {
        DefaultCategoryDataset emptyDataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createLineChart(
                "Estadísticas de Categorías",
                "Fechas",
                "Valores",
                emptyDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        PanelGrafico.setLayout(new BorderLayout());
        PanelGrafico.add(chartPanel, BorderLayout.CENTER);
        PanelGrafico.revalidate();
        PanelGrafico.repaint();

        panel1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                comboBoxAnniosDesde.removeAllItems();
                comboBoxAnniosHasta.removeAllItems();
                comboBoxMesDesde.removeAllItems();
                comboBoxMesHasta.removeAllItems();
                fillYears(comboBoxAnniosDesde);
                fillYears(comboBoxAnniosHasta);
                fillMonths(comboBoxMesDesde);
                fillMonths(comboBoxMesHasta);
            }
        });

        agregarCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int annoDesde = Integer.parseInt((String) comboBoxAnniosDesde.getSelectedItem());
                int mesDesde = Integer.parseInt((String) comboBoxMesDesde.getSelectedItem());
                int annoHasta = Integer.parseInt((String) comboBoxAnniosHasta.getSelectedItem());
                int mesHasta = Integer.parseInt((String) comboBoxMesHasta.getSelectedItem());

                if (annoDesde > annoHasta || (annoDesde == annoHasta && mesDesde > mesHasta)) {
                    JOptionPane.showMessageDialog(null, "La fecha de inicio no puede ser posterior a la fecha de fin.", "Error de Fechas", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Rango fecha = new Rango(annoDesde, mesDesde, annoHasta, mesHasta);
                model.setRango(fecha);

                int nuevoColCount = ((fecha.getAnnoHasta() - fecha.getAnnoDesde()) * 12 + fecha.getMesHasta() - fecha.getMesDesde())+2;
                int columnasActuales = model.getColumnCount();

                Categoria categoriaSeleccionada = (Categoria) categorias.getSelectedItem();

                if (categoriaSeleccionada != null ) {
                    List<Categoria> categoriasActuales = model.getCategorias();
                    if (categoriasActuales.contains(categoriaSeleccionada) && nuevoColCount == columnasActuales) {
                        JOptionPane.showMessageDialog(null, "La categoría ya ha sido agregada.", "Categoría Duplicada", JOptionPane.WARNING_MESSAGE);
                    } else {
                        model.agregarCategoria(categoriaSeleccionada);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una categoría válida.", "Error de Categoría", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    controller.ActualizarData();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        agregarAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String annoDesdeStr = (String) comboBoxAnniosDesde.getSelectedItem();
                String mesDesdeStr = (String) comboBoxMesDesde.getSelectedItem();
                String annoHastaStr = (String) comboBoxAnniosHasta.getSelectedItem();
                String mesHastaStr = (String) comboBoxMesHasta.getSelectedItem();

                int annoDesde = Integer.parseInt(annoDesdeStr);
                int mesDesde = Integer.parseInt(mesDesdeStr);
                int annoHasta = Integer.parseInt(annoHastaStr);
                int mesHasta = Integer.parseInt(mesHastaStr);

                if (annoDesde > annoHasta || (annoDesde == annoHasta && mesDesde > mesHasta)) {
                    JOptionPane.showMessageDialog(null, "La fecha de inicio no puede ser posterior a la fecha de fin.", "Error de Fechas", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Rango fecha = new Rango(annoDesde, mesDesde, annoHasta, mesHasta);
                model.setRango(fecha);

                List<Categoria> todasLasCategorias = Service.instance().search(new Categoria());

                for (Categoria categoria : todasLasCategorias) {
                    if (!model.getCategorias().contains(categoria)) {
                        model.agregarCategoria(categoria);
                    }
                }
                try {
                    controller.ActualizarData();
                    JOptionPane.showMessageDialog(null, "Todas las categorías han sido agregadas exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrió un error al actualizar los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    model.eliminarCategoria(selectedRow);
                    table1.setModel(model.getTableModel());
                    table1.revalidate();
                    table1.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una categoría para eliminar.");
                }
            }
        });

        deleteAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.eliminarTodasCategorias();
                table1.setModel(model.getTableModel());
            }
        });
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    private void fillYears(JComboBox<String> comboBox) {
        for (int year = 2019; year <= 2024; year++) {
            comboBox.addItem(String.valueOf(year));
        }
    }

    private void fillMonths(JComboBox<String> comboBox) {
        for (int i = 1; i <= 12; i++) {
            comboBox.addItem(String.valueOf(i));
        }
    }

    public void setController(Controller controller) { this.controller = controller; }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.DATA:
                table1.setModel(model.getTableModel());
                ChartPanel chartPanel = model.createLineChart();
                PanelGrafico.removeAll();
                PanelGrafico.setLayout(new BorderLayout());
                PanelGrafico.add(chartPanel, BorderLayout.CENTER);
                PanelGrafico.revalidate();
                PanelGrafico.repaint();
                break;
            case Model.CATEGORASCOMBOBOX:
                categorias.setModel(new DefaultComboBoxModel(model.getCategoriasCombobox().toArray()));
                categorias.revalidate();
                categorias.repaint();
                break;
        }
        this.panel1.revalidate();
    }
}
