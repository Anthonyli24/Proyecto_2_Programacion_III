package pos.presentation.Productos;

import javax.swing.table.AbstractTableModel;
import pos.logic.Producto;
import pos.Application;
import java.util.List;
import javax.swing.*;

public class TableModel extends AbstractTableModel implements javax.swing.table.TableModel {
    List<Producto> rows;
    int[] cols;

    public TableModel(int[] cols, List<Producto> rows){
        this.cols=cols;
        this.rows=rows;
        initColNames();
    }

    public int getRowCount() {
        return rows.size();
    }
    public int getColumnCount() {
        return cols.length;
    }
    public String getColumnName(int col){
        return colNames[cols[col]];
    }

    public static final int CODIGO=0;
    public static final int DESCRIPCION=1;
    public static final int UNIDAD=2;
    public static final int PRECIO=3;
    public static final int CATEGORIA=4;
    public static final int EXISTENCIAS = 5;
    public static final int IMAGEN = 6;

    @Override
    public Class<?> getColumnClass(int col) {
        switch (cols[col]) {
            case IMAGEN: return Icon.class;
            default: return super.getColumnClass(col);
        }
    }
    public Object getValueAt(int row, int col) {
        Producto e = rows.get(row);
        switch (cols[col]){
            case CODIGO: return e.getCodigo();
            case DESCRIPCION: return e.getDescripcion();
            case UNIDAD: return e.getUnidadMedida();
            case PRECIO: return e.getPrecioUnitario();
            case CATEGORIA: return e.getCategoria().getNombre();
            case EXISTENCIAS: return e.getExistencias();
            case IMAGEN: return getImagen(e.getCategoria().getId());
            default: return "";
        }
    }
    private Icon getImagen(String id) {
        try {
            return new ImageIcon(Application.class.getResource("/pos/presentation/icons/categorias/" + id + ".png"));
        }
        catch (Exception e) { return null;}
    }
    public Producto getRowAt(int row) {
        return rows.get(row);
    }

    String[] colNames = new String[7];
    private void initColNames(){
        colNames[CODIGO]= "Codigo";
        colNames[DESCRIPCION]= "Descripcion";
        colNames[UNIDAD]= "Unidad";
        colNames[PRECIO]= "Precio";
        colNames[CATEGORIA]= "Categoria";
        colNames[EXISTENCIAS]= " Existencias ";
        colNames[IMAGEN] = "";
    }
}