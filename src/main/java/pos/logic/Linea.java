package pos.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Linea {
    String numeroFactura;
    String numero;
    private Producto producto;
    int cantidad;
    double descuento;

    public Linea() {
        numeroFactura = "";
        numero = " ";
        producto = new Producto();
        cantidad = 0;
        descuento = 0;
    }

    public Linea(String numF,String num,Producto producto, int cantidad, double descuento,Factura factura) {
        this.numeroFactura = numF;
        this.numero = num;
        this.producto = producto;
        this.cantidad = cantidad;
        this.descuento = descuento;
    }

    public String getNumeroFactura() { return numeroFactura; }
    public String getNumero() {return numero;}
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getDescuento() { return round(descuento, 2); }
    public double getImporte() { return round(sacarImporte(), 2 );}

    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }
    public void setNumero(String numero) {this.numero = numero;};
    public void setProducto(Producto producto) { this.producto = producto; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double sacarImporte() {
        double neto = producto.getPrecioUnitario() - getDescuento();
        return cantidad * neto;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Linea)) return false;
        Linea linea = (Linea) obj;
        return Objects.equals(this.producto.getCodigo(), linea.producto.getCodigo()) &&
                this.cantidad == linea.cantidad;
    }

    @Override
    public int hashCode() { return Objects.hash(producto.getCodigo(), cantidad); }

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