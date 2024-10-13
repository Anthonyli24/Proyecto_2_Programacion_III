package pos.logic;

public class Factura {
    private String codigoFactura;
    private String fecha;
    private String nombreCli;
    private String nombreCaje;
    private double importeTotal;


    public Factura() {
        codigoFactura = "";
        fecha = "";
        nombreCli = "";
        nombreCaje = "";
        importeTotal = 0;
    }

    public Factura(String CodigoFactura, String fecha, String nombreCli, String nombreCaje, double importeTotal) {
        this.codigoFactura = CodigoFactura;
        this.fecha = fecha;
        this.nombreCli = nombreCli;
        this.nombreCaje = nombreCaje;
        this.importeTotal = importeTotal;
    }

    public String getCodigoFactura() {return codigoFactura;}
    public String getFecha() {return fecha;}
    public String getNombreCli() {return nombreCli;}
    public String getNombreCaje() {return nombreCaje;}
    public double getImporteTotal() {return importeTotal;}

    public void setCodigoFactura(String codigoFactura) {this.codigoFactura = codigoFactura;}
    public void setFecha(String fecha) {this.fecha = fecha;}
    public void setNombreCli(String nombreCli) {this.nombreCli = nombreCli;}
    public void setNombreCaje(String nombreCaje) { this.nombreCaje = nombreCaje; }
    public void setImporteTotal(double importeTotal) {this.importeTotal = importeTotal;}
}