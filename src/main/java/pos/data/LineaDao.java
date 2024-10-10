package pos.data;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import pos.logic.Cajero;
import pos.logic.Factura;
import pos.logic.Linea;
import pos.logic.Cliente;
import java.util.List;

public class LineaDao {

    Database db;

    public LineaDao() { db = Database.instance(); }

    public void create(Linea l) throws Exception {
        String sql = "insert into " +
                "Lineas " +
                "(id, idFactura, producto, cantidad, descuento) " +
                "values(?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, l.getNumero());
        stm.setString(2, l.getFactura().getID());
        stm.setString(3, l.getProducto().getCodigo());
        stm.setInt(4, l.getCantidad());
        stm.setDouble(5, l.getDescuento());
        db.executeUpdate(stm);
    }

    public Linea read(String num) throws Exception {
        String sql = "select " +
                "* " +
                "from  Lineas L " +
                "inner join Facturas f on L.idFactura=f.id " +
                "inner join Producto p on L.producto=p.codigo " +
                "where L.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, num);
        ResultSet rs = db.executeQuery(stm);
        FacturaDao facturasDao=new FacturaDao();
        ProductoDao productoDao=new ProductoDao();
        if (rs.next()) {
            Linea r = from(rs, "L");
            r.setFactura(facturasDao.from(rs, "f"));
            r.setProducto(productoDao.from(rs, "p"));
            return r;
        } else {
            throw new Exception("Linea no existe");
        }
    }

    public void update(Linea e) throws Exception {
        String sql = "update " +
                "Lineas " +
                "set id=?, idFactura=?, producto=?, cantidad=?, descuento=? " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getNumero());
        stm.setString(2, e.getFactura().getID());
        stm.setString(3, e.getProducto().getCodigo());
        stm.setInt(4, e.getCantidad());
        stm.setDouble(5, e.getDescuento());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Linea no existe");
        }
    }

    public void delete(Linea e) throws Exception {
        String sql = "delete " +
                "from Lineas L " +
                "where L.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getNumero());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Linea no existe");
        }
    }

    public List<Linea> search(Linea e) throws Exception {
        List<Linea> resultado = new ArrayList<Linea>();
        String sql = "select * " +
                "from " +
                "Lineas L " +
                "inner join Facturas f on L.idFactura=f.id " +
                "inner join Producto p on L.producto=p.codigo " +
                "where L.id like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getNumero() + "%");
        ResultSet rs = db.executeQuery(stm);
        FacturaDao facturasDao=new FacturaDao();
        ProductoDao productoDao=new ProductoDao();
        while (rs.next()) {
            Linea l = from(rs, "L");
            l.setFactura(facturasDao.from(rs, "f"));
            l.setProducto(productoDao.from(rs, "p"));
            resultado.add(l);
        }
        return resultado;
    }

    public Linea from(ResultSet rs, String alias) throws Exception {
        Linea l = new Linea();
        l.setNumero(rs.getString(alias + ".id"));
        l.setCantidad(rs.getInt(alias + ".cantidad"));
        l.setDescuento(rs.getDouble(alias + ".descuento"));

        return l;
    }
}
