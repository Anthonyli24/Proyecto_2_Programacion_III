package pos.presentation.Facturar;

import pos.logic.*;
import pos.presentation.AbstractModel;
import java.beans.PropertyChangeListener;
import java.util.List;

public class Model extends AbstractModel {
    private Producto filter;
    private List<Cliente> clientes;
    private List<Cajero> cajeros;
    private List<Linea> lineas;
    private Linea current;
    private int articulos;
    private double subtotal;
    private double descuentos;
    private double total;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LISTCLIENTES);
        firePropertyChange(LISTCAJEROS);
        firePropertyChange(LISTLINEAS);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
        firePropertyChange(ARTICULOS);
        firePropertyChange(SUBTOTAL);
        firePropertyChange(DESCUENTOS);
        firePropertyChange(TOTAL);
    }

    public Model() {}

    public void init(List<Cliente> list, List<Cajero> list2,List<Linea> list3){
       this.clientes = list;
       this.cajeros = list2;
       this.lineas = list3;
       this.current = new Linea();
       this.filter = new Producto();
       this.articulos = 0;
       this.subtotal = 0;
       this.descuentos = 0;
       this.total = 0;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
    public List<Cajero> getCajeros() {
        return cajeros;
    }
    public List<Linea> getLineas() {return lineas;}
    public Producto getFilter() {
        return filter;
    }
    public Linea getCurrent() {
        return current;
    }
    public int getArticulos() { return articulos; }
    public double getSubtotal() { return subtotal; }
    public double getDescuentos() { return descuentos;}
    public double getTotal() { return total; }

    public void setCurrent(Linea Current) {
        this.current = Current;
        firePropertyChange(CURRENT);
    }

    public void setClietes(List<Cliente> list){
        this.clientes = list;
        firePropertyChange(LISTCLIENTES);
    }

    public void setCajeros(List<Cajero> list){
        this.cajeros = list;
        firePropertyChange(LISTCAJEROS);
    }

    public void setLineas(List<Linea> list){
        this.lineas = list;
        firePropertyChange(LISTLINEAS);
    }

    public void setFilter(Producto filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }

    public void setArticulos(int articulos) { this.articulos = articulos; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public void setDescuentos(double descuentos){ this.descuentos = descuentos; }
    public void setTotal(double total) { this.total = total; }

    public static final String LISTCLIENTES="listClientes";
    public static final String LISTLINEAS="listLineas";
    public static final String LISTCAJEROS="listCajeros";
    public static final String CURRENT="current";
    public static final String FILTER="filter";
    public static final String ARTICULOS="articulos";
    public static final String SUBTOTAL="subtotal";
    public static final String DESCUENTOS="descuentos";
    public static final String TOTAL="total";
}