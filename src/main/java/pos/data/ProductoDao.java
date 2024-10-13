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
        stm.setDouble(4, e.getPrecioUnitario());
        stm.setString(5, e.getCategoria().getId());
        stm.setDouble(6, e.getExistencias());
        db.executeUpdate(stm);
    }

    public Producto read(String codigo) throws Exception {
        String sql = "select " +
                " * " +
                "from  Producto t " +
                "inner join Categoria c on t.categoria=c.id " +
                "where t.codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs = db.executeQuery(stm);
        CategoriaDao categoriaDao=new CategoriaDao();
        if (rs.next()) {
            Producto r = from(rs, "t");
            r.setCategoria(categoriaDao.from(rs, "c"));
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
        stm.setDouble(3, e.getPrecioUnitario());
        stm.setString(4, e.getCategoria().getId());
        stm.setDouble(5, e.getExistencias());
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
            r.setCategoria(categoriaDao.from(rs, "c"));
            resultado.add(r);
        }
        return resultado;
    }

    public Producto from(ResultSet rs, String alias) throws Exception {
        Producto e = new Producto();
        e.setCodigo(rs.getString(alias + ".codigo"));
        e.setDescripcion(rs.getString(alias + ".descripcion"));
        e.setUnidadMedida(rs.getString(alias + ".unidadMedida"));
        e.setPrecioUnitario(rs.getDouble(alias + ".precioUnitario"));
        e.setExistencias((int) rs.getDouble(alias + ".existencias"));
        return e;
    }
}