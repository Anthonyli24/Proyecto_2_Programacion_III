package pos.presentation.Productos;

import java.beans.PropertyChangeListener;
import pos.presentation.AbstractModel;
import pos.logic.Categoria;
import java.util.ArrayList;
import pos.logic.Producto;
import pos.Application;
import java.util.List;

public class Model extends AbstractModel {
    Producto filter;
    List<Producto> list;
    Producto current;
    int mode;

    List<Categoria> categorias;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(CATEGORIAS);
    }

    public Model() { }

    public void init(){
        filter = new Producto();
        List<Producto> rows = new ArrayList<Producto>();
        this.setList(rows);
        mode= Application.MODE_CREATE;
    }

    public void setList(List<Producto> list){
        this.list = list;
        firePropertyChange(LIST);
        setCurrent(new Producto());
    }

    public List<Producto> getList() { return list; }
    public Producto getCurrent() { return current; }
    public Producto getFilter() { return filter; }
    public int getMode() { return mode; }

    public void setCurrent(Producto current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public void setFilter(Producto filter) { this.filter = filter; }

    public void setMode(int mode) { this.mode = mode; }

    public List<Categoria> getCategorias() { return categorias; }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
        firePropertyChange(CATEGORIAS);
    }

    public static final String LIST="list";
    public static final String CURRENT="current";
    public static final String CATEGORIAS="categorias";
}