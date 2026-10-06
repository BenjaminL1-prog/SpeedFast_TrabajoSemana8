package dao;

import model.Entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public boolean create(Entrega entrega) {

        String sql =
                "INSERT INTO entrega " +
                        "(id_pedido, id_repartidor, fecha, hora) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(
                                sql,
                                java.sql.Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            sentencia.setInt(
                    1,
                    entrega.getIdPedido()
            );

            sentencia.setInt(
                    2,
                    entrega.getIdRepartidor()
            );

            sentencia.setDate(
                    3,
                    java.sql.Date.valueOf(
                            entrega.getFecha()
                    )
            );

            sentencia.setTime(
                    4,
                    java.sql.Time.valueOf(
                            entrega.getHora()
                    )
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            if (filasAfectadas == 0) {
                return false;
            }

            try (ResultSet clavesGeneradas =
                         sentencia.getGeneratedKeys()) {

                if (clavesGeneradas.next()) {

                    entrega.setId(
                            clavesGeneradas.getInt(1)
                    );
                }
            }

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar la entrega en MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }

    public boolean guardar(Entrega entrega) {

        return create(entrega);
    }

    public List<Entrega> readAll() {

        List<Entrega> entregas =
                new ArrayList<>();

        String sql =
                "SELECT id, id_pedido, id_repartidor, " +
                        "fecha, hora " +
                        "FROM entrega";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);
                ResultSet resultado =
                        sentencia.executeQuery()
        ) {

            while (resultado.next()) {

                Entrega entrega =
                        new Entrega(
                                resultado.getInt("id_pedido"),
                                resultado.getInt("id_repartidor"),
                                resultado.getDate("fecha")
                                        .toLocalDate(),
                                resultado.getTime("hora")
                                        .toLocalTime()
                        );

                entrega.setId(
                        resultado.getInt("id")
                );

                entregas.add(entrega);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar las entregas desde MySQL."
            );

            e.printStackTrace();
        }

        return entregas;
    }

    public boolean update(Entrega entrega) {

        String sql =
                "UPDATE entrega " +
                        "SET id_pedido = ?, " +
                        "id_repartidor = ?, " +
                        "fecha = ?, " +
                        "hora = ? " +
                        "WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(
                    1,
                    entrega.getIdPedido()
            );

            sentencia.setInt(
                    2,
                    entrega.getIdRepartidor()
            );

            sentencia.setDate(
                    3,
                    java.sql.Date.valueOf(
                            entrega.getFecha()
                    )
            );

            sentencia.setTime(
                    4,
                    java.sql.Time.valueOf(
                            entrega.getHora()
                    )
            );

            sentencia.setInt(
                    5,
                    entrega.getId()
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar la entrega en MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }

    public boolean delete(int id) {

        String sql =
                "DELETE FROM entrega " +
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
                    "Error al eliminar la entrega de MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }
}