package pos.presentation.Facturar;

import pos.logic.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Controller {
    private view view;
    private Model model;

    public Controller(view view, Model model) throws Exception {
        model.init(Service.instance().search(new Cliente()), Service.instance().search(new Cajero()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void AgregarLinea(Producto filter) throws Exception {
        Linea nuevo = new Linea();
        String numCodigo = Integer.toString(Service.instance().search(new Factura()).size() + 1);
        String numeroFactura = "FC0" + numCodigo;
        nuevo.setNumero(Integer.toString(Service.instance().NumeroLinea()));
        nuevo.setCantidad(1);
        filter.setExistencias(filter.getExistencias() - 1);
        Service.instance().update(filter);
        nuevo.setProducto(filter);
        nuevo.setNumeroFactura(numeroFactura);
        Service.instance().create(nuevo);
        model.addLinea(nuevo);
    }

    public void BorrarLinea(Linea linea) throws Exception {
        if (linea != null) {
            Producto pro = BuscarProducto(model.getCurrent().getProducto());
            pro.setExistencias(linea.getProducto().getExistencias() + 1);
            Service.instance().update(pro);
            model.removeLinea(linea);
            Service.instance().delete(linea);
        } else {
            throw new Exception("La línea a eliminar no puede ser nula.");
        }
    }

    public Producto BuscarProducto(Producto e) throws Exception {
        model.setFilter(e);
        return Service.instance().read(model.getFilter());
    }

    public double PagoTotal() throws Exception {
        Linea nuevo = new Linea();
        String numCodigo = Integer.toString(Service.instance().search(new Factura()).size() + 1);
        String numeroFactura = "FC0" + numCodigo;
        nuevo.setNumeroFactura(numeroFactura);
        return Service.instance().PagoTotal(nuevo);
    };

    public void cancelar() throws Exception {
        for (Linea linea : model.getLineas()) {
            Producto producto = linea.getProducto();
            producto.setExistencias(producto.getExistencias() + linea.getCantidad());
            Service.instance().update(producto);
        }
        String numeroFactura = model.getLineas().get(0).getNumeroFactura();
        Service.instance().deleteLineas(numeroFactura);

        model.setLineas(new ArrayList<>());
        model.setFilter(new Producto());
    }

    public void iniciarLineas(Linea e) throws Exception {
        List<Linea> lineas = Service.instance().search(e);
        model.setLineas(lineas);
    }

    public void crearFactura(String nombreCli, String nombreCaje) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String fecha = LocalDate.now().format(formatter);
        String numCodigo = Integer.toString(Service.instance().search(new Factura()).size() + 1);
        String numeroFactura = "FC0" + numCodigo;
        Factura factura = new Factura(numeroFactura, fecha, nombreCli, nombreCaje);
        Service.instance().create(factura);
    }

    public void edit(int row) {
        Linea e = model.getLineas().get(row);
        try {
            model.setCurrent(Service.instance().read(e));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Producto> ListaPrincipalProductos() { return Service.instance().search(new Producto()); }
    public List<Producto> buscarDescripcion(Producto e) throws Exception { return Service.instance().search(e); }
}
