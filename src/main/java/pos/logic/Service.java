package pos.logic;

import pos.data.*;

import java.util.List;

public class Service {
    private static Service theInstance;

    public static Service instance(){
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private ClientesDao clienteDao;
    private CajeroDao cajeroDao;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;
    private LineaDao lineaDao;
    private FacturaDao facturaDao;

    public Service() {
        try{
            clienteDao = new ClientesDao();
            cajeroDao = new CajeroDao();
            productoDao = new ProductoDao();
            categoriaDao = new CategoriaDao();
            lineaDao = new LineaDao();
            facturaDao = new FacturaDao();
        }
        catch(Exception e){
        }
    }

    public void stop(){
    }

    //================= CLIENTES ============
    public void create(Cliente e) throws Exception { clienteDao.create(e); }

    public Cliente read(Cliente e) throws Exception { return clienteDao.read(e.getId()); }

    public void update(Cliente e) throws Exception { clienteDao.update(e); }

    public void delete(Cliente e) throws Exception { clienteDao.delete(e); }

    public List<Cliente> search(Cliente e) {
        try {
            return clienteDao.search(e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //================= CAJEROS ============
    public void create(Cajero e) throws Exception { cajeroDao.create(e); }

    public Cajero read(Cajero e) throws Exception { return cajeroDao.read(e.getID()); }

    public void update(Cajero e) throws Exception { cajeroDao.update(e); }

    public void delete(Cajero e) throws Exception { cajeroDao.delete(e); }

    public List<Cajero> search(Cajero e) {
        try {
            return cajeroDao.search(e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //================= PRODUCTOS ============
    public void create(Producto e) throws Exception { productoDao.create(e); }

    public Producto read(Producto e) throws Exception { return productoDao.read(e.getCodigo()); }

    public void update(Producto e) throws Exception { productoDao.update(e); }

    public void delete(Producto e) throws Exception { productoDao.delete(e); }

    public List<Producto> search(Producto e) {
        try {
            return productoDao.search(e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //================= CATEGORIAS ============
    public List<Categoria> search(Categoria e) {
        try {
            return categoriaDao.search(e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //============= LINEAS =============
    public void create(Linea e) throws Exception { lineaDao.create(e); }

    public Linea read(Linea e) throws Exception { return lineaDao.read(e.getNumero()); }

    public void update(Linea e) throws Exception { lineaDao.update(e); }

    public void delete(Linea e) throws Exception { lineaDao.delete(e); }

    public List<Linea> search(Linea e) {
        try {
            return lineaDao.search(e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //========== FACTURAS ==========
    public void create(Factura e) throws Exception { facturaDao.create(e); }

    public Factura read(Factura e) throws Exception { return facturaDao.read(e.getID()); }

    public void update(Factura e) throws Exception { facturaDao.update(e); }

    public void delete(Factura e) throws Exception { facturaDao.delete(e); }

    public List<Factura> search(Factura e) {
        try {
            return facturaDao.search(e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }





 }