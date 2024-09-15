package proyectobases11;

public class Usuario {

    public String username, accion, fecha;

    public Usuario() {
    }

    public Usuario(String username, String accion, String fecha) {
        this.username = username;
        this.accion = accion;
        this.fecha = fecha;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "username=" + username + ", accion=" + accion + ", fecha=" + fecha;
    }

}
