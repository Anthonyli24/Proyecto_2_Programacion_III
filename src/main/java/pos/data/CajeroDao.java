package pos.data;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import pos.logic.Cajero;
import java.util.List;

public class CajeroDao {
    Database db;

    public CajeroDao() { db = Database.instance(); }

    public void create(Cajero e) throws Exception {
        String sql = "insert into " +
                "Cajeros " +
                "(id, nombre) " +
                "values (?, ?)";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, e.getID());
            stm.setString(2, e.getNombre());
            db.executeUpdate(stm);
        }
    }

    public Cajero read(String id) throws Exception {
        String sql = "select " +
                " * " +
                "from Cajeros " +
                "where id = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, id);
            ResultSet rs = db.executeQuery(stm);
            if (rs.next()) {
                return from(rs);
            } else {
                throw new Exception("Cajero no existe");
            }
        }
    }

    public void update(Cajero e) throws Exception {
        String sql = "update " +
                "Cajeros " +
                "set nombre = ? " +
                "where id = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, e.getNombre());
            stm.setString(2, e.getID());
            int count = db.executeUpdate(stm);
            if (count == 0) {
                throw new Exception("Cajero no existe");
            }
        }
    }

    public void delete(Cajero e) throws Exception {
        String sql = "delete " +
                "from Cajeros " +
                "where id = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, e.getID());
            int count = db.executeUpdate(stm);
            if (count == 0) {
                throw new Exception("Cajero no existe");
            }
        }
    }

    public List<Cajero> search(Cajero e) throws Exception {
        List<Cajero> resultado = new ArrayList<>();
        String sql = "select " +
                " * " +
                "from Cajeros " +
                "where nombre like ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, "%" + e.getNombre() + "%");
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                resultado.add(from(rs));
            }
        }
        return resultado;
    }

    private Cajero from(ResultSet rs) throws Exception {
        Cajero e = new Cajero();
        e.setID(rs.getString("id"));
        e.setNombre(rs.getString("nombre"));
        return e;
    }
}