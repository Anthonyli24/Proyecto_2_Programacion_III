package pos.logic;

import java.util.Objects;

public class Producto {
    private String codigo;
    private String descripcion;
    private String unidadMedida;
    private double precioUnitario;
    private int existencias;
    private Categoria categoria;

    public Producto(String cod, String des, String UniMe, double precio, int exis, Categoria cat) {
        this.codigo = cod;
        this.descripcion = des;
        this.unidadMedida = UniMe;
        this.precioUnitario = precio;
        this.existencias = exis;
        this.categoria = cat;
    }

    public Producto() {this("", "", "", 0, 0, new Categoria(""));}

    public String getCodigo() {return codigo;}
    public String getDescripcion() {return descripcion;}
    public String getUnidadMedida() {return unidadMedida;}
    public double getPrecioUnitario() {return precioUnitario;}
    public int getExistencias() {return existencias;}
    public Categoria getCategoria() {return categoria;}

    public void setCodigo(String codigo) {this.codigo = codigo;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setUnidadMedida(String unidadMedida) {this.unidadMedida = unidadMedida;}
    public void setPrecioUnitario(double precioUnitario) {this.precioUnitario = precioUnitario;}
    public void setExistencias(int existencias) {this.existencias = existencias;}
    public void setCategoria(Categoria categoria) {this.categoria = categoria;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(codigo, producto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}