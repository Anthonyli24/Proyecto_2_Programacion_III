package pos.logic;

public class Cajero {
    private String ID;
    private String Nombre;

    public Cajero(String ID, String Nombre) {
        this.ID = ID;
        this.Nombre = Nombre;
    }
    public Cajero() {
        this("","");
    }

    public String getID() {
        return ID;
    }
    public String getNombre() {
        return Nombre;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String toString() {
        return Nombre;
    }
}