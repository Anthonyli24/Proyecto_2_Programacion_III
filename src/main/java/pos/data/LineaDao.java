package pos.data;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import pos.logic.Linea;
import java.util.List;

public class LineaDao {
    Database db;

    public LineaDao() {
        db = Database.instance();
    }

    public void create(Linea linea) throws Exception {
        String sql = "insert into " +
                "Linea " +
                "(numeroFactura, numero, Producto_codigo, cantidad, descuento, importe) " +
                "values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, linea.getNumeroFactura());
            stm.setString(2, linea.getNumero());
            stm.setString(3, linea.getProducto().getCodigo());
            stm.setInt(4, linea.getCantidad());
            stm.setDouble(5, linea.getDescuento());
            stm.setDouble(6, linea.getImporte());
            db.executeUpdate(stm);
        }
    }

    public Linea read(String numero) throws Exception {
        String sql = "select " +
                " * " +
                "from Linea l " +
                "inner join Producto p on l.Producto_codigo = p.codigo " +
                "where l.numero = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, numero);
            try (ResultSet rs = db.executeQuery(stm)) {
                if (rs.next()) {
                    Linea linea = from(rs);
                    linea.setProducto(new ProductoDao().read(rs.getString("Producto_codigo")));
                    return linea;
                } else {
                    throw new Exception("Linea no existe");
                }
            }
        }
    }

    public void update(Linea linea) throws Exception {
        String sql = "update " +
                "Linea " +
                "set cantidad=?, descuento=?, importe=? " +
                "where numero=? and numeroFactura=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setInt(1, linea.getCantidad());
            stm.setDouble(2, linea.getDescuento());
            stm.setDouble(3, linea.getImporte());
            stm.setString(4, linea.getNumero());
            stm.setString(5, linea.getNumeroFactura());
            db.executeUpdate(stm);
        }
    }

    public List<Linea> buscarLineasPorNumero(String numeroFactura) throws Exception {
        List<Linea> lineas = new ArrayList<>();
        String sql = "select" +
                " l.*, p.* " +
                "from Linea l " +
                "join Producto p on l.Producto_codigo = p.codigo " +
                "where l.numeroFactura = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, numeroFactura);
            try (ResultSet rs = db.executeQuery(stm)) {
                ProductoDao productoDao = new ProductoDao();
                while (rs.next()) {
                    Linea linea = from(rs);
                    linea.setProducto(productoDao.read(rs.getString("Producto_codigo")));
                    lineas.add(linea);
                }
            }
        }
        return lineas;
    }

    public void delete(String numero) throws Exception {
        String sql = "delete " +
                "from Linea " +
                "where numero=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, numero);
            int affectedRows = stm.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("No se encontró ninguna línea con el número: " + numero);
            }
        }
    }

    public void eliminarLineasPorFactura(String numeroFactura) throws Exception {
        String sql = "delete " +
                "from Linea " +
                "where numeroFactura = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, numeroFactura);
            int affectedRows = stm.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("No se encontraron líneas para la factura: " + numeroFactura);
            }
        }
    }

    public int getLastNumero() throws Exception {
        String sql = "select " +
                "max(cast(numero as unsigned)) as lastNumero " +
                "from Linea";
        try (PreparedStatement stm = db.prepareStatement(sql);
             ResultSet rs = db.executeQuery(stm)) {
            if (rs.next()) {
                return rs.getInt("lastNumero");
            } else {
                return 0;
            }
        }
    }

    public double calcularTotalFactura(String numeroFactura) throws Exception {
        double total = 0;
        String sql = "select " +
                "sum(l.importe) as total " +
                "from Linea l " +
                "where l.numeroFactura = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, numeroFactura);
            try (ResultSet rs = db.executeQuery(stm)) {
                if (rs.next()) {
                    total = rs.getDouble("total");
                }
            }
        }
        return total;
    }

    public Linea from(ResultSet rs) throws Exception {
        Linea linea = new Linea();
        linea.setNumeroFactura(rs.getString("numeroFactura"));
        linea.setNumero(rs.getString("numero"));
        linea.setCantidad(rs.getInt("cantidad"));
        linea.setDescuento(rs.getDouble("descuento"));
        return linea;
    }
}