package Domain;

public class User {

    private String nombre, password;
    private int portNumber;

    public User() {

    }

    public User(String nombre, String password, int portNumber) {
        this.nombre = nombre;
        this.password = password;
        this.portNumber = portNumber;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

}
