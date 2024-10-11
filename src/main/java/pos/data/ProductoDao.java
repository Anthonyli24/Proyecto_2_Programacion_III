package pos.data;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import pos.logic.Producto;
import java.util.List;

public class ProductoDao {
    Database db;

    public ProductoDao() {
        db = Database.instance();
    }

    public void create(Producto e) throws Exception {
        String sql = "insert into " +
                "Producto " +
                "(codigo ,descripcion, unidadMedida,precioUnitario,categoria,existencias) " +
                "values(?,?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigo());
        stm.setString(2, e.getDescripcion());
        stm.setString(3, e.getUnidadMedida());
        stm.setFloat(4, e.getPrecioUnitario());
        stm.setString(5, e.getCategoria().getNombre());
        stm.setFloat(6, e.getExistencias());
        db.executeUpdate(stm);
    }

    public Producto read(String codigo) throws Exception {
        String sql = "select " +
                "* " +
                "from  Producto t " +
                "inner join Categoria c on t.categoria=c.id " +
                "where t.codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs = db.executeQuery(stm);
        CategoriaDao categoriaDao=new CategoriaDao();
        if (rs.next()) {
            Producto r = from(rs, "t");
            r.setCategoria(categoriaDao.from(rs, "c."));
           return r;
        } else {
            throw new Exception("Producto no existe");
        }
    }

    public void update(Producto e) throws Exception {
        String sql = "update " +
                "Producto " +
                "set descripcion=?, unidadMedida=?, precioUnitario=?, categoria=?, existencias=? " +
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getDescripcion());
        stm.setString(2, e.getUnidadMedida());
        stm.setFloat(3, e.getPrecioUnitario());
        stm.setString(4, e.getCategoria().getId());
        stm.setFloat(5, e.getExistencias());
        stm.setString(6, e.getCodigo());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Producto no existe");
        }
    }

    public void delete(Producto e) throws Exception {
        String sql = "delete " +
                "from Producto " +
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigo());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Producto no existe");
        }
    }

    public List<Producto> search(Producto e) throws Exception {
        List<Producto> resultado = new ArrayList<Producto>();
        String sql = "select * " +
                "from " +
                "Producto t " +
                "inner join Categoria c on t.categoria=c.id " +
                "where t.descripcion like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getDescripcion() + "%");
        ResultSet rs = db.executeQuery(stm);
        CategoriaDao categoriaDao=new CategoriaDao();
        while (rs.next()) {
            Producto r = from(rs, "t");
            r.setCategoria(categoriaDao.from(rs, "c."));
            resultado.add(r);
        }
        return resultado;
    }


    public List<Producto> getAll() throws Exception {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto t INNER JOIN Categoria c ON t.categoria = c.id"; // Asegúrate de que la tabla se llama 'Producto'

        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        CategoriaDao categoriaDao = new CategoriaDao();
        while (rs.next()) {
            Producto producto = from(rs, "t"); // Usar el alias "t" para mapear las columnas de Producto
            producto.setCategoria(categoriaDao.from(rs, "c.")); // Establecer la categoría del producto
            productos.add(producto); // Agregar el producto a la lista
        }
        return productos;
    }

    public Producto from(ResultSet rs, String alias) throws Exception {
        Producto e = new Producto();
        e.setCodigo(rs.getString(alias + ".codigo"));
        e.setDescripcion(rs.getString(alias + ".descripcion"));
        e.setUnidadMedida(rs.getString(alias + ".unidadMedida"));
        e.setPrecioUnitario(rs.getFloat(alias + ".precioUnitario"));
        e.setExistencias((int) rs.getFloat(alias + ".existencias"));

        // Recuperar y establecer la categoría del producto
        CategoriaDao categoriaDao = new CategoriaDao();
        String categoriaId = rs.getString(alias + ".categoria"); // Asegúrate de que 'categoria' se recupera correctamente
        e.setCategoria(categoriaDao.read(categoriaId)); // Asumir que tienes un método read en CategoriaDao para recuperar la categoría por ID

        return e;
    }
}