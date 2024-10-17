package pos.presentation.Estadistica;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.beans.PropertyChangeListener;
import org.jfree.chart.axis.NumberAxis;
import pos.presentation.AbstractModel;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import pos.logic.Categoria;
import pos.logic.Rango;
import java.util.List;

public class Model extends AbstractModel {
    List<Categoria> categoriasCombobox;
    List<Categoria> categorias;
    List<String> anniodesde;
    List<String> annioHasta;
    List<String> mesDesde;
    List<String> mesHasta;
    Rango rango;
    String[] rows;
    String[] cols;
    float[][] data;

    public Model(){
        this.cols = new String[0];
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CATEGORASCOMBOBOX);
        firePropertyChange(CATEGORIAS);
        firePropertyChange(ANNIOD);
        firePropertyChange(ANNIOH);
        firePropertyChange(RANGO);
        firePropertyChange(MESDESDE);
        firePropertyChange(MESHASTA);
    }

    public void Init(List<Categoria> cats) { this.categorias = cats;}

    public int getRowCount() { return rows.length; }
    public int getColumnCount() { return cols.length + 1; }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rows[rowIndex];
        } else {
            return data[rowIndex][columnIndex - 1];
        }
    }

    public String columnName(int column) {
        if (column == 0) {
            return "Categoria";
        } else {
            return cols[column - 1];
        }
    }

    public void eliminarTodasCategorias() {
        getCategorias().clear();
        cols = new String[0];
        rows = new String[0];
        data = new float[0][0];
        firePropertyChange(DATA);
    }

    public void eliminarCategoria(int index) {
        if (index < 0 || index >= getCategorias().size()) {
            return;
        }
        getCategorias().remove(index);

        String[] newRows = new String[rows.length - 1];
        float[][] newData = new float[data.length - 1][data[0].length];

        for (int i = 0, j = 0; i < rows.length; i++) {
            if (i != index) {
                newRows[j] = rows[i];
                newData[j] = data[i];
                j++;
            }
        }
        this.rows = newRows;
        this.data = newData;
        firePropertyChange(DATA);
    }

    public TableModel getTableModel() {
        return new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return rows.length;
            }

            @Override
            public int getColumnCount() {
                return cols.length + 1;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    return rows[rowIndex];
                } else {
                    return data[rowIndex][columnIndex - 1];
                }
            }

            @Override
            public String getColumnName(int column) {
                if (column == 0) {
                    return "Categoria";
                } else {
                    return cols[column - 1];
                }
            }
        };
    }

    public CategoryDataset createDataset() { return new MatrixDataSet(rows, cols, data); }

    public ChartPanel createLineChart() {
        CategoryDataset dataset = createDataset();
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Estadísticas de Categorías",
                "Fechas",
                "Valores",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        CategoryPlot plot = lineChart.getCategoryPlot();

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        return chartPanel;
    }

    public void agregarCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        firePropertyChange(CATEGORIAS);
    }

    public List<Categoria> getCategoriasCombobox(){ return categoriasCombobox; }
    public Rango getRango() { return rango; }
    public List<Categoria> getCategorias() { return categorias; }

    public void setCategoriasCombobox(List<Categoria> categorias) {
        this.categoriasCombobox = categorias;
        firePropertyChange(CATEGORASCOMBOBOX);
    }

    public void setData(String[] newRows, String[] newCols, float[][] newData) {
        this.rows = newRows;
        this.cols = newCols;
        this.data = newData;
        firePropertyChange(DATA);
    }

    public void setRango(Rango rango) {
        this.rango = rango;
        firePropertyChange(RANGO);
    }

    public static final String CATEGORASCOMBOBOX = "CategoriasTodas";
    public static final String CATEGORIAS = "Categorias";
    public static final String RANGO = "rango";
    public static final String ANNIOD = "AnnioDesde";
    public static final String ANNIOH = "AnnioHasta";
    public static final String MESDESDE = "mesdesde";
    public static final String MESHASTA = "meshasta";
    public static final String DATA = "data";
}