package pos.presentation.Cajero;

import java.beans.PropertyChangeListener;
import pos.presentation.AbstractModel;
import java.util.ArrayList;
import pos.logic.Cajero;
import pos.Application;
import java.util.List;

public class Model extends AbstractModel {
    private Cajero filter;
    private List<Cajero> list;
    private Cajero current;
    private int mode;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
    }

    public Model() { }

    public void init(){
        filter = new Cajero();
        List<Cajero> rows = new ArrayList<>();
        this.setList(rows);
        mode = Application.MODE_CREATE;
    }

    public void setList(List<Cajero> list){
        this.list = list;
        firePropertyChange(LIST);
        setCurrent(new Cajero());
    }

    public List<Cajero> getList() { return list; }
    public Cajero getCurrent() { return current; }
    public Cajero getFilter() { return filter; }
    public int getMode() { return mode; }

    public void setCurrent(Cajero current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public void setFilter(Cajero filter) { this.filter = filter; }
    public void setMode(int mode) { this.mode = mode; }

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";
}