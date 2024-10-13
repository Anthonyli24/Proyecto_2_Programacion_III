package pos.presentation.Historico;

import pos.Application;
import pos.logic.Factura;
import pos.logic.Linea;
import pos.logic.Producto;
import pos.logic.Service;
import pos.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.*;

public class Model extends AbstractModel {
    Factura filter;
    List<Factura> list;
    List<Linea> listalineas;
    Factura current;
    int mode;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
    }

    public Model() {}

    public void init(List<Factura> list) throws Exception {
        this.filter = new Factura();
        List<Factura> rows = new ArrayList<Factura>();
        this.setList(rows);
        List<Linea> rows2 = new ArrayList<Linea>();
        this.setListalineas(rows2);
        this.mode= Application.MODE_CREATE;
    }

    public List<Factura> getList() {
        return list;
    }
    public List<Linea> getListalineas() {
        return listalineas;
    }
    public Factura getCurrent() {
        return current;
    }
    public Factura getFilter() {
        return filter;
    }
    public int getMode() {
        return mode;
    }

    public void setList(List<Factura> list){
        this.list = list;
        firePropertyChange(LIST);
        setCurrent(new Factura());
    }

    public void setListalineas(List<Linea> listalineas){
        this.listalineas = listalineas;
        firePropertyChange(LIST);
    }

    public void setCurrent(Factura current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public void setFilter(Factura filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static final String LIST="list";
    public static final String CURRENT="current";
    public static final String FILTER="filter";
}