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
                "values(?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getID());
        stm.setString(2, e.getNombre());
        db.executeUpdate(stm);
    }

    public Cajero read(String id) throws Exception {
        String sql = "select " +
                "* " +
                "from Cajeros C " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "C");
        } else {
            throw new Exception("Cajero no existe");
        }
    }

    public void update(Cajero e) throws Exception {
        String sql = "update" +
                "Cajeros C " +
                "set C.nombre=? " +
                "where C.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getNombre());
        stm.setString(2, e.getID());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Cajero no existe");
        }
    }

    public void delete(Cajero e) throws Exception {
        String sql = "delete " +
                "from Cajeros C " +
                "where C.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getID());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Cajero no existe");
        }
    }

    public List<Cajero> search(Cajero e) throws Exception {
        List<Cajero> resultado = new ArrayList<Cajero>();
        String sql = "select * " +
                "from " +
                "Cajeros C " +
                "where C.nombre like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getNombre() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            resultado.add(from(rs, "C"));
        }
        return resultado;
    }

    public List<Cajero> getAll() throws Exception {
        List<Cajero> resultado = new ArrayList<>();
        String sql = "select * from Cajeros C";
        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Cajero cajero = from(rs, "C");  // Crear objeto Cajero usando el método from()
            resultado.add(cajero);  // Añadir cajero a la lista
        }

        return resultado;
    }


    public Cajero from(ResultSet rs, String alias) throws Exception {
        Cajero e = new Cajero();
        e.setID(rs.getString(alias + ".id"));
        e.setNombre(rs.getString(alias + ".nombre"));
        return e;
    }
}