package pos.presentation.Clientes;

import java.beans.PropertyChangeListener;
import pos.presentation.AbstractModel;
import pos.logic.Cliente;
import pos.Application;
import java.util.List;

public class Model extends AbstractModel {
    private Cliente filter;
    private List<Cliente> list;
    private Cliente current;
    private int mode;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
    }

    public Model() {
        this.filter = new Cliente();
        this.current = new Cliente();
        this.mode = Application.MODE_CREATE;
    }

    public void init(List<Cliente> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public List<Cliente> getList() { return list; }
    public Cliente getCurrent() { return current; }
    public Cliente getFilter() { return filter; }
    public int getMode() { return mode; }

    public void setMode(int mode) { this.mode = mode; }

    public void setList(List<Cliente> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public void setCurrent(Cliente current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public void setFilter(Cliente filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";
}