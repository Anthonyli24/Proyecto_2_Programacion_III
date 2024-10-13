package pos.presentation.Historico;

import pos.presentation.AbstractTableModel;
import pos.logic.Factura;
import java.util.List;

public class TableModel extends AbstractTableModel<Factura> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Factura> rows) {
        super(cols, rows);
    }
    public static final int CODIGO = 0;
    public static final int FECHA = 1;
    public static final int NOMBRE_CLIENTE = 2;
    public static final int NOMBRE_CAJERO = 3;
    public static final int TOTAL = 4;

    @Override
    protected Object getPropetyAt(Factura e, int col) {
        switch (cols[col]){
            case NOMBRE_CLIENTE: return e.getNombreCli();
            case NOMBRE_CAJERO: return e.getNombreCaje();
            case FECHA: return e.getFecha();
            case CODIGO: return e.getCodigoFactura();
            case TOTAL: return e.getImporteTotal();
            default: return "";
        }
    }

    public Object getValueAt(int row, int col) {
        Factura e = rows.get(row);
        return getPropetyAt(e, col);
    }

    @Override
    protected void initColNames(){
        colNames = new String[5];
        colNames[FECHA] = "Fecha";
        colNames[CODIGO] = "Codigo";
        colNames[NOMBRE_CLIENTE] = "Nombre-Cliente";
        colNames[NOMBRE_CAJERO] = "Nombre-Cajero";
        colNames[TOTAL] = "Total";
    }
}
