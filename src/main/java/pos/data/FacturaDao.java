package pos.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import pos.logic.Factura;

public class FacturaDao {
    Database db;

    public FacturaDao() {
        db = Database.instance();
    }

    public void create(Factura e) throws Exception {
        String sql = "INSERT INTO Factura (codigoFactura, fecha, nombreCli, nombreCaje) VALUES (?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigoFactura());
        stm.setString(2, e.getFecha());
        stm.setString(3, e.getNombreCli());
        stm.setString(4, e.getNombreCaje());
        db.executeUpdate(stm);
    }

    public Factura read(String id) throws Exception {
        String sql = "SELECT * FROM Factura WHERE codigoFactura = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs);
        } else {
            throw new Exception("Factura no existe");
        }
    }

    public void update(Factura e) throws Exception {
        String sql = "UPDATE Factura SET fecha = ?, nombreCli = ?, nombreCaje = ? WHERE codigoFactura = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getFecha());
        stm.setString(2, e.getNombreCli());
        stm.setString(3, e.getNombreCaje());
        stm.setString(4, e.getCodigoFactura());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Factura no existe");
        }
    }

    public void delete(Factura e) throws Exception {
        String sql = "DELETE FROM Factura WHERE codigoFactura = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigoFactura());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Factura no existe");
        }
    }

    public List<Factura> search(Factura e) throws Exception {
        List<Factura> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Factura WHERE fecha LIKE ? OR codigoFactura LIKE ? OR nombreCli LIKE ? OR nombreCaje LIKE ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getFecha() + "%");
        stm.setString(2, "%" + e.getCodigoFactura() + "%");
        stm.setString(3, "%" + e.getNombreCli() + "%");
        stm.setString(4, "%" + e.getNombreCaje() + "%");

        ResultSet rs = db.executeQuery(stm);
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
        return e;
    }
}
