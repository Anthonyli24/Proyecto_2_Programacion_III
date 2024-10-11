package pos.data;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import pos.logic.Cliente;
import java.util.List;

public class ClientesDao {
    Database db;

    public ClientesDao() { db = Database.instance(); }

    public void create(Cliente e) throws Exception {
        String sql = "insert into " +
                "Clientes " +
                "(id, nombre, telefono, email, descuento) " +
                "values(?, ?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        stm.setString(2, e.getNombre());
        stm.setString(3, e.getTelefono());
        stm.setString(4, e.getEmail());
        stm.setFloat(5, e.getDescuento());
        db.executeUpdate(stm);
    }

    public Cliente read(String id) throws Exception {
        String sql = "select " +
                " * " +
                "from Clientes cli " +
                "where cli.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "cli");
        } else {
            throw new Exception("Cliente no existe");
        }
    }

    public void update(Cliente e) throws Exception {
        String sql = "update " +
                "Clientes cli " +
                "set cli.nombre=?, cli.telefono=?, cli.email=?, cli.descuento=? " +
                "where cli.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getNombre());
        stm.setString(2, e.getTelefono());
        stm.setString(3, e.getEmail());
        stm.setFloat(4, e.getDescuento());
        stm.setString(5, e.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Cliente no existe");
        }
    }

    public void delete(Cliente e) throws Exception {
        String sql = "delete " +
                "from Clientes cli " +
                "where cli.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Cliente no existe");
        }
    }

    public List<Cliente> search(Cliente e) throws Exception {
        List<Cliente> resultado = new ArrayList<Cliente>();
        String sql = "select " +
                " * " +
                "from Clientes cli " +
                "where cli.nombre like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getNombre() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            resultado.add(from(rs, "cli"));
        }
        return resultado;
    }

    public List<Cliente> getAll() throws Exception {
        List<Cliente> resultado = new ArrayList<Cliente>();
        String sql = "select * from Clientes cli";
        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Cliente cliente = from(rs, "cli");  // Crear objeto Cliente usando el método from()
            resultado.add(cliente);  // Añadir cliente a la lista
        }

        return resultado;
    }


    public Cliente from(ResultSet rs, String alias) throws Exception {
        Cliente e = new Cliente();
        e.setId(rs.getString(alias + ".id"));
        e.setNombre(rs.getString(alias + ".nombre"));
        e.setTelefono(rs.getString(alias + ".telefono"));
        e.setEmail(rs.getString(alias + ".email"));
        e.setDescuento(rs.getFloat(alias + ".descuento"));
        return e;
    }
}