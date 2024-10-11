package pos.logic;

import java.util.ArrayList;
import java.util.List;

public class Factura {
    private String ID;
    private String fecha;
    private Cliente cliente;
    private Cajero cajero;
    private List<Linea> lineas;

    public Factura() {
        lineas = new ArrayList<>();
        ID = "";
        fecha = "";
        cliente = new Cliente();
        cajero = new Cajero();
    }

    public Factura(String ID, String fecha,Cliente cliente, Cajero cajero, List<Linea> lineas) {
        this.ID = ID;
        this.fecha = fecha;
        this.cliente = cliente;
        this.cajero = cajero;
        this.lineas = new ArrayList<>(lineas);
    }

    public String getID() {return ID;}
    public String getFecha() {return fecha;}
    public Cliente getCliente() {return cliente;}
    public Cajero getCajero() {return cajero;}
    public List<Linea> getCarrito() {return lineas;}

    public void setID(String ID) {this.ID = ID;}
    public void setFecha(String fecha) {this.fecha = fecha;}
    public void setCliente(Cliente cliente) {this.cliente = cliente;}
    public void setCajero(Cajero cajero) {this.cajero = cajero;}

    public void agregar(Linea obj) {lineas.add(obj);}

    public void eliminar(Linea obj){lineas.remove(obj);}

    public double totalFactura() {
        double total = 0.0;
        for (Linea linea : lineas) {
            total += linea.getImporte();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "ID='" + ID + '\'' +
                ", Fecha='" + fecha + '\'' +
                ", Cliente='" + cliente.getNombre() + '\'' +
                ", Cajero='" + cajero.getNombre() + '\'' +
                '}';
    }

    public void stringFactura() {
        System.out.println(toString());
        for (Linea linea : lineas) {
            System.out.println(linea.toString());
        }
        System.out.println("Total: " + totalFactura());
    }

}
