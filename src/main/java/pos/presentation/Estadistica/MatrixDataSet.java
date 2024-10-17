package pos.presentation.Estadistica;

import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetGroup;
import java.util.Arrays;
import java.util.List;

public class MatrixDataSet implements CategoryDataset {
    List<String> rowKeys;
    List<String> columnKeys;
    float[][] data;

    public MatrixDataSet(String[] row, String[] column, float[][] data) {
        rowKeys = Arrays.asList(row);
        columnKeys = Arrays.asList(column);
        this.data = data;
    }

    @Override
    public Comparable getRowKey(int rowIndex) {
        return rowKeys.get(rowIndex);
    }

    @Override
    public int getRowIndex(Comparable rowKey) {
        return rowKeys.indexOf(rowKey);
    }

    @Override
    public List getRowKeys() {
        return rowKeys;
    }

    @Override
    public Comparable getColumnKey(int columnIndex) {
        return columnKeys.get(columnIndex);
    }

    @Override
    public int getColumnIndex(Comparable columnKey) {
        return columnKeys.indexOf(columnKey);
    }

    @Override
    public List getColumnKeys() {
        return columnKeys;
    }

    @Override
    public Number getValue(Comparable rowKey, Comparable columnKey) {
        int rowIndex = getRowIndex(rowKey);
        int columnIndex = getColumnIndex(columnKey);
        if (rowIndex < 0 || columnIndex < 0) {
            return 0;
        }
        return data[rowIndex][columnIndex];
    }

    @Override
    public int getRowCount() {
        return rowKeys.size();
    }

    @Override
    public int getColumnCount() {
        return columnKeys.size();
    }

    @Override
    public Number getValue(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void addChangeListener(DatasetChangeListener listener) {}

    @Override
    public void removeChangeListener(DatasetChangeListener listener) {}

    @Override
    public DatasetGroup getGroup() {
        return null;
    }

    @Override
    public void setGroup(DatasetGroup group) {}
}