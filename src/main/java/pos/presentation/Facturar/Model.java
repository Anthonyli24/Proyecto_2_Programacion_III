package pos.presentation.Facturar;

import java.beans.PropertyChangeListener;
import pos.presentation.AbstractModel;
import java.util.ArrayList;
import java.util.List;
import pos.logic.*;

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

    public Model() {
        this.lineas = new ArrayList<>();
    }

    public void init(List<Cliente> clientes, List<Cajero> cajeros) {
        this.clientes = clientes;
        this.cajeros = cajeros;
        this.current = new Linea();
        this.filter = new Producto();
        this.articulos = 0;
        this.subtotal = 0;
        this.descuentos = 0;
        this.total = 0;
    }

    public List<Cliente> getClientes() { return Service.instance().search(new Cliente()); }
    public List<Cajero> getCajeros() {
        return Service.instance().search(new Cajero());
    }
    public List<Linea> getLineas() {
        return lineas;
    }
    public Producto getFilter() {
        return filter;
    }
    public Linea getCurrent() {
        return current;
    }

    public void setCurrent(Linea current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public void setLineas(List<Linea> lineas) {
        this.lineas = lineas;
        recalculateTotals();
        firePropertyChange(LISTLINEAS);
    }

    public void setFilter(Producto filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }

    public void addLinea(Linea linea) {
        this.lineas.add(linea);
        recalculateTotals();
        firePropertyChange(LISTLINEAS);
    }

    public void removeLinea(Linea linea) {
        if (this.lineas.remove(linea)) {
            firePropertyChange(LISTLINEAS);
            recalculateTotals();
        }
        else{
            System.out.println("No se puede eliminar la linea");
        }
    }

    private void recalculateTotals() {
        this.articulos = lineas.stream().mapToInt(Linea::getCantidad).sum();
        this.subtotal = lineas.stream().mapToDouble(Linea::getImporte).sum();
        this.descuentos = lineas.stream().mapToDouble(Linea::getDescuento).sum();
        this.total = subtotal - descuentos;

        firePropertyChange(ARTICULOS);
        firePropertyChange(SUBTOTAL);
        firePropertyChange(DESCUENTOS);
        firePropertyChange(TOTAL);
    }

    public static final String LISTCLIENTES = "listClientes";
    public static final String LISTLINEAS = "listLineas";
    public static final String LISTCAJEROS = "listCajeros";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";
    public static final String ARTICULOS = "articulos";
    public static final String SUBTOTAL = "subtotal";
    public static final String DESCUENTOS = "descuentos";
    public static final String TOTAL = "total";
}