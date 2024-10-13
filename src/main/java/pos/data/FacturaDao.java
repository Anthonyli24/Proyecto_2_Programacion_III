package pos.data;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import pos.logic.Factura;
import java.util.List;

public class FacturaDao {
    Database db;

    public FacturaDao() {
        db = Database.instance();
    }

    public void create(Factura e) throws Exception {
        String sql = "insert into " +
                "Factura " +
                "(codigoFactura, fecha, nombreCli, nombreCaje, importeTotal) " +
                "values (?, ?, ?, ? , ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigoFactura());
        stm.setString(2, e.getFecha());
        stm.setString(3, e.getNombreCli());
        stm.setString(4, e.getNombreCaje());
        stm.setDouble(5, e.getImporteTotal());
        db.executeUpdate(stm);
    }

    public Factura read(String id) throws Exception {
        String sql = "select " +
                " * " +
                "from Factura " +
                "where codigoFactura = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs);
        } else {
            throw new Exception("Factura con codigo " + id.toString() + " no encontrada");
        }
    }

    public void update(Factura e) throws Exception {
        String sql = "Update " +
                "Factura " +
                "set fecha = ?, nombreCli = ?, nombreCaje = ? ,importeTotal = ? " +
                "where codigoFactura = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getFecha());
        stm.setString(2, e.getNombreCli());
        stm.setString(3, e.getNombreCaje());
        stm.setDouble(4, e.getImporteTotal());
        stm.setString(5, e.getCodigoFactura());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Factura no existe");
        }
    }

    public void delete(Factura e) throws Exception {
        String sql = "delete " +
                "from Factura " +
                "where codigoFactura = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigoFactura());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Factura no existe");
        }
    }

    public List<Factura> search(Factura e) throws Exception {
        List<Factura> resultado = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM Factura " +
                "WHERE codigoFactura LIKE ? " +
                "ORDER BY CAST(SUBSTRING(codigoFactura, 3) AS UNSIGNED) ASC"; // Para MySQL
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getCodigoFactura() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            resultado.add(from(rs));
        }
        return resultado;
    }

    public List<Factura> searchByCliente(String nombreCliente) throws Exception {
        List<Factura> resultado = new ArrayList<>();
        String sql = "select " +
                "* " +
                "from Factura " +
                "where nombreCli like ?";

        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + nombreCliente + "%");

        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            resultado.add(from(rs));
        }
        return resultado;
    }

    public Factura from(ResultSet rs) throws Exception {
        Factura e = new Factura();
        e.setCodigoFactura(rs.getString("codigoFactura"));
        e.setFecha(rs.getString("fecha"));
        e.setNombreCli(rs.getString("nombreCli"));
        e.setNombreCaje(rs.getString("nombreCaje"));
        e.setImporteTotal(rs.getDouble("importeTotal"));
        return e;
    }
}