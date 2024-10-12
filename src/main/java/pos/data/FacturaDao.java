package pos.data;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;

import pos.logic.*;

import java.util.List;

public class FacturaDao {
    Database db;

    public FacturaDao() {
        db = Database.instance();
    }

    public void create(Factura e) throws Exception {
        String sql = "insert into " +
                "Facturas " +
                "(id ,fecha, cliente,cajero) " +
                "values(?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getID());
        stm.setString(2, e.getFecha());
        stm.setString(3, e.getCliente().getId());
        stm.setString(4, e.getCajero().getID());
        db.executeUpdate(stm);
    }

    public Factura read(String id) throws Exception {
        String sql = "select " +
                "* " +
                "from  Facturas f " +
                "inner join Clientes c on f.cliente=c.id " +
                "inner join Cajeros ca on f.cajero=ca.id " +
                "where f.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        ClientesDao clientesDao=new ClientesDao();
        CajeroDao cajeroDao=new CajeroDao();
        if (rs.next()) {
            Factura r = from(rs, "f");
            r.setCliente(clientesDao.from(rs, "c"));
            r.setCajero(cajeroDao.from(rs, "ca"));
            return r;
        } else {
            throw new Exception("Factura no existe");
        }
    }

    public void update(Factura e) throws Exception {
        String sql = "update " +
                "Facturas " +
                "set id=?, fecha=?, cliente=?, cajero=?" +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getID());
        stm.setString(2, e.getFecha());
        stm.setString(3, e.getCliente().getId());
        stm.setString(4, e.getCajero().getID());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Factura no existe");
        }
    }

    public void delete(Factura e) throws Exception {
        String sql = "delete " +
                "from Facturas " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getID());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Factura no existe");
        }
    }

    public List<Factura> search(Factura e) throws Exception {
        List<Factura> resultado = new ArrayList<Factura>();
        String sql = "select * " +
                "from " +
                "Factura f " +
                "inner join Clientes c on f.cliente=c.id " +
                "inner join Cajeros ca on f.cajero=ca.id " +
                "where f.cliente like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getCliente().getId() + "%");
        ResultSet rs = db.executeQuery(stm);
        ClientesDao clientesDao=new ClientesDao();
        CajeroDao cajeroDao=new CajeroDao();
        while (rs.next()) {
            Factura r = from(rs, "f");
            r.setCliente(clientesDao.from(rs, "c"));
            r.setCajero(cajeroDao.from(rs, "ca"));
            resultado.add(r);
        }
        return resultado;
    }

    public Factura from(ResultSet rs, String alias) throws Exception {
        Factura f = new Factura();
        f.setID(rs.getString(alias + ".id"));
        f.setFecha(rs.getString(alias + ".fecha"));
        return f;
    }

}
