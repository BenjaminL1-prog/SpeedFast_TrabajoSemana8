package dao;

import model.Repartidor;
import model.ZonaDeCarga;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public boolean create(Repartidor repartidor) {

        String sql =
                "INSERT INTO repartidor (nombre) " +
                        "VALUES (?)";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(
                                sql,
                                java.sql.Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            sentencia.setString(
                    1,
                    repartidor.getNombre()
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            if (filasAfectadas == 0) {
                return false;
            }

            try (ResultSet clavesGeneradas =
                         sentencia.getGeneratedKeys()) {

                if (clavesGeneradas.next()) {

                    repartidor.setId(
                            clavesGeneradas.getInt(1)
                    );
                }
            }

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar el repartidor en MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }

    public List<Repartidor> readAll() {

        List<Repartidor> repartidores =
                new ArrayList<>();

        String sql =
                "SELECT id, nombre " +
                        "FROM repartidor";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);
                ResultSet resultado =
                        sentencia.executeQuery()
        ) {

            while (resultado.next()) {

                int id =
                        resultado.getInt("id");

                String nombre =
                        resultado.getString("nombre");

                Repartidor repartidor =
                        new Repartidor(
                                id,
                                nombre,
                                new ZonaDeCarga()
                        );

                repartidores.add(repartidor);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar los repartidores desde MySQL."
            );

            e.printStackTrace();
        }

        return repartidores;
    }

    public List<Repartidor> listarTodos() {

        return readAll();
    }

    public boolean update(Repartidor repartidor) {

        String sql =
                "UPDATE repartidor " +
                        "SET nombre = ? " +
                        "WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                    1,
                    repartidor.getNombre()
            );

            sentencia.setInt(
                    2,
                    repartidor.getId()
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar el repartidor en MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }

    public boolean delete(int id) {

        String sql =
                "DELETE FROM repartidor " +
                        "WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(
                    1,
                    id
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar el repartidor de MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }
}