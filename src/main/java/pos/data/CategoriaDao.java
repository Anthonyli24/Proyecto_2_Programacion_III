package pos.data;

import java.sql.PreparedStatement;
import pos.logic.Categoria;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.List;

public class CategoriaDao {
    Database db;

    public CategoriaDao() {
        db = Database.instance();
    }

    public List<Categoria> search(Categoria e) throws Exception {
        List<Categoria> resultado = new ArrayList<Categoria>();
        String sql = "select * " +
                "from " +
                "Categoria t " +
                "where t.nombre like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getNombre() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            Categoria r= from(rs, "t.");
            resultado.add(r);
        }
        return resultado;
    }

    public Categoria read(String id) throws Exception {
        String sql = "SELECT * FROM Categoria WHERE id=?"; // No necesitas un alias aquí
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs, ""); // Aquí puedes pasar un string vacío ya que no usas alias
        } else {
            throw new Exception("Categoría no existe");
        }
    }

    public Categoria from(ResultSet rs, String alias) throws Exception {
        Categoria e = new Categoria();
        e.setId(rs.getString("id")); // Cambia "t.id" a "id"
        e.setNombre(rs.getString(alias + "nombre")); // Asegúrate de que el alias esté aplicado correctamente
        return e;
    }
}