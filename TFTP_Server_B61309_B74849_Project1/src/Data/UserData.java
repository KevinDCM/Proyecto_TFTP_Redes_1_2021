package Data;

import Domain.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserData extends DataBaseConf {

    public UserData() {

    }

    public boolean insert(User u) throws ClassNotFoundException, SQLException {

        if (usuarioExiste(u.getNombre())) {

            return false;
        } else {

            String llamadaSQL = "{CALL PERSONAS.sp_INSERTAR_USUARIO(?,?)}";

            Connection conn = getSQLConnection();

            CallableStatement callState = conn.prepareCall(llamadaSQL);
            callState.setString(1, u.getNombre());
            callState.setString(2, u.getPassword());

            try {

                callState.execute();

                return true;

            } catch (SQLException e) {
                System.out.println("Exception reported. Insertion failure detected X");
                return false;
            }
        }

    }

    public boolean usuarioExiste(String n) throws ClassNotFoundException, SQLException {

        Connection conn = getSQLConnection();

        Statement state = conn.createStatement();

        try {

            String SQL = "SELECT COUNT(*) as result FROM Personas.usuarios WHERE nombre='" + n + "'";

            ResultSet rs = state.executeQuery(SQL);

            rs.next();

            return (rs.getString("result")).equals("1");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public User getUsuario(String n, String p) throws ClassNotFoundException, SQLException {

        Connection conn = getSQLConnection();
        Statement state = conn.createStatement();

        try {

            String SQL = "SELECT * FROM Personas.usuarios WHERE nombre = '" + n + "' AND password = '" + p + "'";
            ResultSet rs = state.executeQuery(SQL);

            rs.next();

            User u = new User();

            u.setNombre(rs.getString("nombre"));
            u.setPassword(rs.getString("password"));
            u.setPortNumber(rs.getShort("port_number"));

            return u;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}