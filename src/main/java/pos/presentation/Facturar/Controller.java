package pos.presentation.Facturar;

import pos.logic.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.LocalDate;

public class Controller {
    view view;
    Model model;

    public Controller(view view, Model model) throws Exception {
        model.init(Service.instance().getClientes(),Service.instance().getCajeros(),Service.instance().search(new Linea()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void AgregarLinea(Producto filter,Factura fac) throws Exception {
        Linea nuevo = new Linea();
        nuevo.setCantidad(1);
        filter.setExistencias(filter.getExistencias() - 1);
        nuevo.setProducto(filter);

        Cliente selectedCliente =  view.getSelectedCliente();

        double discount = 0;
        if (selectedCliente != null) {discount = selectedCliente.getDescuento();}

        nuevo.setDescuento(discount);
        nuevo.setNumero(fac.getID());

        fac.agregar(nuevo);
        Service.instance().create(nuevo);

        model.setLineas(fac.getCarrito());
    }

    public void BorrarLinea(Linea linea,Factura fac) throws Exception {
        Producto producto = linea.getProducto();
        producto.setExistencias(producto.getExistencias() + linea.getCantidad());
        Service.instance().update(producto);
        Service.instance().delete(linea);
        fac.eliminar(linea);
        model.setLineas(model.getFcurrent().getCarrito());
    }

    public double calcularPagoTotal(){return Service.instance().PagoTotal(model.getLineas());}

    public Producto BuscarProducto(Producto e) throws Exception {
        model.setFilter(e);
        return Service.instance().read(model.getFilter());
    }

    public void cancelar() throws Exception {
        for (Linea linea : Service.instance().getLineas()) {
            Producto producto = linea.getProducto();
            producto.setExistencias(producto.getExistencias() + linea.getCantidad());
            Service.instance().update(producto);
        }
        Service.instance().getLineas().clear();
        model.setLineas(Service.instance().getLineas());
        model.setFilter(new Producto());
    }

    public void edit(int row){
        Linea e = model.getLineas().get(row);
        try {
            model.setCurrent(Service.instance().read(e));
        } catch (Exception ex) {}
    }

    public  List<Cliente> loadClientes() throws Exception {
        List<Cliente> clientes = Service.instance().getClientes();
        model.setClietes(clientes);
        return clientes;
    }

    public  List<Cajero> loadCajeros() throws Exception {
        List<Cajero> cajeros = Service.instance().getCajeros();
        model.setCajeros(cajeros);
        return cajeros;
    }

    public void iniciarLineas() throws Exception {
        List<Linea> lineas = Service.instance().getLineas();
        model.setLineas(lineas);
    }

    public Factura crearFactura(String nombreCli,String nombreCaje) throws Exception {
        /*int numero = Service.instance().getFacturas().size()+1;
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDate = localDate.format(formatter);
        String fecha = formattedDate;
        String numCodigo = Integer.toString(numero);
        String nombreFactura = "FC0"+ numCodigo;
        Factura factura = new Factura(nombreCli,fecha,nombreFactura, model.getLineas());
        return factura;*/
        return new Factura();
    }

    public List<Producto> ListaPrincipalProductos() throws Exception {
        return Service.instance().getProductos();
    }

    public List<Linea> ListaPrincipalLineas() throws Exception {
        return Service.instance().getLineas();
    }

    public  List<Producto> buscarDescripcion(Producto e) throws Exception {
        return Service.instance().searchDescripcion(e);
    }

    public void CrearFacturaNueva(){
        model.setFcurrent(new Factura());
    }


}