package pos.presentation.Facturar;

import pos.logic.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.LocalDate;

public class Controller {
    view view;
    Model model;

    public Controller(view view, Model model) {
        this.view = view;
        this.model = model;
        model.setClietes(Service.instance().search(new Cliente()));
        model.setCajeros(Service.instance().search(new Cajero()));
        try {
            model.init(Service.instance().search(new Cliente()), Service.instance().search(new Cajero()), Service.instance().search(new Linea()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.setController(this);
        view.setModel(model);
    }

    public void agregarLinea(Producto filter) throws Exception {
        Linea nuevaLinea = new Linea();
        nuevaLinea.setCantidad(1);
        filter.setExistencias(filter.getExistencias() - 1);
        nuevaLinea.setProducto(filter);
        Service.instance().create(nuevaLinea);
        model.setLineas(Service.instance().search(new Linea()));
    }

    public void borrarLinea(Linea linea) throws Exception {
        Producto producto = linea.getProducto();
        producto.setExistencias(producto.getExistencias() + linea.getCantidad());
        Service.instance().update(producto);
        Service.instance().delete(linea);

        model.setLineas(Service.instance().search(new Linea()));
    }

    public double calcularPagoTotal() {
        double total = 0;
        for (Linea linea : model.getLineas()) {
            Producto producto = linea.getProducto();
            int cantidad = linea.getCantidad();
            double precioUnitario = producto.getPrecioUnitario();

            double subtotalLinea = precioUnitario * cantidad;

            double descuento = linea.getDescuento();
            double totalLinea = subtotalLinea * (1 - descuento);

            total += totalLinea;
        }
        return total;
    }


    public Producto buscarProducto(Producto e) throws Exception {
        return Service.instance().read(e);
    }

    public void cancelar() throws Exception {
        // Recorre todas las líneas y restablece las existencias de los productos
        List<Linea> lineas = Service.instance().search(new Linea());
        for (Linea linea : lineas) {
            Producto producto = linea.getProducto();
            producto.setExistencias(producto.getExistencias() + linea.getCantidad());
            Service.instance().update(producto);
        }

        // Limpia todas las líneas y reinicia el filtro del modelo
        for (Linea linea : lineas) {
            Service.instance().delete(linea);
        }

        model.setLineas(Service.instance().search(new Linea()));
        model.setFilter(new Producto());
    }

    public void edit(int row) {
        Linea e = model.getLineas().get(row);
        try {
            model.setCurrent(Service.instance().read(e));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void iniciarLineas() {
        try {
            List<Linea> lineas = Service.instance().search(new Linea());
            model.setLineas(lineas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Cliente buscarClientePorNombre(String nombreCli) throws Exception {
        Cliente filtro = new Cliente();
        filtro.setNombre(nombreCli);  // Asumiendo que tienes un campo nombre en Cliente
        List<Cliente> resultados = Service.instance().search(filtro);

        if (resultados.isEmpty()) {
            throw new Exception("Cliente no encontrado.");
        }

        return resultados.get(0);  // Asumiendo que el nombre es único, devolvemos el primero
    }

    Cajero buscarCajeroPorNombre(String nombreCaje) throws Exception {
        Cajero filtro = new Cajero();
        filtro.setNombre(nombreCaje);  // Asumiendo que tienes un campo nombre en Cajero
        List<Cajero> resultados = Service.instance().search(filtro);

        if (resultados.isEmpty()) {
            throw new Exception("Cajero no encontrado.");
        }

        return resultados.get(0);  // Devolvemos el primero
    }



    public Factura crearFactura(String nombreCli, String nombreCaje) {
        try {
            Cliente cliente = buscarClientePorNombre(nombreCli);
            Cajero cajero = buscarCajeroPorNombre(nombreCaje);

            int numero = Service.instance().search(new Factura()).size() + 1;
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String formattedDate = localDate.format(formatter);
            String fecha = formattedDate;
            String numCodigo = Integer.toString(numero);
            String nombreFactura = "FC0" + numCodigo;

            Factura factura = new Factura(nombreFactura, fecha, cliente, cajero, model.getLineas());

            Service.instance().create(factura);
            return factura;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Producto> listaPrincipalProductos() {
        try {
            return Service.instance().search(new Producto());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Linea> listaPrincipalLineas() {
        try {
            return Service.instance().search(new Linea());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Producto> buscarDescripcion(Producto e) throws Exception {
        return Service.instance().search(e);
    }
}


