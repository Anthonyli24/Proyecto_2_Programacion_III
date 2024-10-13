package pos.presentation.Cajero;

import pos.presentation.AbstractTableModel;
import pos.logic.Cajero;
import java.util.List;

public class TableModel extends AbstractTableModel<Cajero> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Cajero> rows) {
        super(cols, rows);
    }

    public static final int ID=0;
    public static final int NOMBRE=1;

    @Override
    protected Object getPropetyAt(Cajero e, int col) {
        switch (cols[col]){
            case ID: return e.getID();
            case NOMBRE: return e.getNombre();
            default: return "";
        }
    }

    @Override
    protected void initColNames(){
        colNames = new String[5];
        colNames[ID]= "ID";
        colNames[NOMBRE]= "Nombre";
    }
}