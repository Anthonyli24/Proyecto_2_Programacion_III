package pos.logic;

import java.util.Objects;

public class Cliente {
    String id;
    String nombre;
    String telefono;
    String email;
    float descuento;

    public Cliente() {this("","","","",0);}

    public Cliente(String id, String nombre, String telefono, String email, float descuento) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.descuento = descuento;
    }

    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getTelefono() {
        return telefono;
    }
    public String getEmail() { return email; }
    public float getDescuento() {
        return descuento;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() { return nombre; }
}