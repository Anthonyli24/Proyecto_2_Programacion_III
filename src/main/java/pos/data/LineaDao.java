package pos.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import pos.logic.Linea;
import pos.logic.Producto;

public class LineaDao {
    Database db;

    public LineaDao() {
        db = Database.instance();
    }

    public void create(Linea linea) throws Exception {
        String sql = "INSERT INTO Linea (numeroFactura, numero, Producto_codigo, cantidad, descuento, importe) VALUES (?, ?, ?, ?, ?, ?)";
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
        String sql = "SELECT * FROM Linea l INNER JOIN Producto p ON l.Producto_codigo = p.codigo WHERE l.numero = ?";
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
        String sql = "UPDATE Linea SET cantidad=?, descuento=?, importe=? WHERE numero=? AND numeroFactura=?";
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
        String sql = "SELECT l.*, p.* FROM Linea l JOIN Producto p ON l.Producto_codigo = p.codigo WHERE l.numeroFactura = ?";

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
        String sql = "DELETE FROM Linea WHERE numero=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, numero);
            int affectedRows = stm.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("No se encontró ninguna línea con el número: " + numero);
            }
        }
    }

    public void eliminarLineasPorFactura(String numeroFactura) throws Exception {
        String sql = "DELETE FROM Linea WHERE numeroFactura = ?";

        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, numeroFactura);
            int affectedRows = stm.executeUpdate();

            if (affectedRows == 0) {
                throw new Exception("No se encontraron líneas para la factura: " + numeroFactura);
            }
        }
    }


    public Linea from(ResultSet rs) throws Exception {
        Linea linea = new Linea();
        linea.setNumeroFactura(rs.getString("numeroFactura"));
        linea.setNumero(rs.getString("numero"));
        linea.setCantidad(rs.getInt("cantidad"));
        linea.setDescuento(rs.getDouble("descuento"));
        return linea;
    }

    public int getLastNumero() throws Exception {
        String sql = "SELECT MAX(CAST(numero AS UNSIGNED)) AS lastNumero FROM Linea";
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
        String sql = "SELECT SUM(l.importe) AS total FROM Linea l WHERE l.numeroFactura = ?";

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

}
