package pos.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Linea {
    String numero;
    Factura factura;
    private Producto producto;
    int cantidad;
    double descuento;

    public Linea() {
        numero = " ";
        producto = new Producto();
        factura = new Factura();
        cantidad = 0;
        descuento = 0;
    }

    public Linea(String num,Producto producto, int cantidad, double descuento,Factura factura ) {
        this.numero = num;
        this.factura = factura;
        this.producto = producto;
        this.cantidad = cantidad;
        this.descuento = descuento;

    }
    public String getNumero() {return numero;}
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getDescuento() { return round(descuento, 2); }
    public double getImporte() { return round(sacarImporte(), 2 );}
    public Factura getFactura() { return factura; }

    public void setNumero(String numero) {this.numero = numero;};
    public void setProducto(Producto producto) { this.producto = producto; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setDescuento(double descuento) { this.descuento = (descuento / 100) * producto.getPrecioUnitario(); }
    public void setFactura(Factura factura) { this.factura = factura; }

    public double sacarImporte() {
        double neto = producto.getPrecioUnitario() - getDescuento();
        return cantidad * neto;
    }

    @Override
    public String toString() {
        return numero + " "+producto.getCodigo() + " " + producto.getDescripcion() + " " + cantidad + " " +
                producto.getPrecioUnitario() + " " + sacarImporte();
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
