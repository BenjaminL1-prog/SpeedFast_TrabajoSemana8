package dao;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public boolean create(Pedido pedido) {

        String sql =
                "INSERT INTO pedido (direccion, tipo, estado) " +
                        "VALUES (?, ?, ?)";

        String tipo = obtenerTipo(pedido);

        if (tipo == null) {
            return false;
        }

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
                    pedido.getDireccionEntrega()
            );

            sentencia.setString(
                    2,
                    tipo
            );

            sentencia.setString(
                    3,
                    pedido.getEstado().name()
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            if (filasAfectadas == 0) {
                return false;
            }

            try (ResultSet clavesGeneradas =
                         sentencia.getGeneratedKeys()) {

                if (clavesGeneradas.next()) {

                    int idGenerado =
                            clavesGeneradas.getInt(1);

                    pedido.setIdPedido(idGenerado);
                }
            }

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar el pedido en MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }

    public boolean guardar(Pedido pedido) {

        return create(pedido);
    }

    public List<Pedido> readAll() {

        List<Pedido> pedidos =
                new ArrayList<>();

        String sql =
                "SELECT id, direccion, tipo, estado " +
                        "FROM pedido";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);
                ResultSet resultado =
                        sentencia.executeQuery()
        ) {

            while (resultado.next()) {

                Pedido pedido =
                        crearPedidoDesdeResultado(resultado);

                if (pedido != null) {
                    pedidos.add(pedido);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar los pedidos desde MySQL."
            );

            e.printStackTrace();
        }

        return pedidos;
    }

    public List<Pedido> listarTodos() {

        return readAll();
    }

    public boolean update(Pedido pedido) {

        String sql =
                "UPDATE pedido " +
                        "SET direccion = ?, tipo = ?, estado = ? " +
                        "WHERE id = ?";

        String tipo =
                obtenerTipo(pedido);

        if (tipo == null) {
            return false;
        }

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                    1,
                    pedido.getDireccionEntrega()
            );

            sentencia.setString(
                    2,
                    tipo
            );

            sentencia.setString(
                    3,
                    pedido.getEstado().name()
            );

            sentencia.setInt(
                    4,
                    pedido.getIdPedido()
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar el pedido en MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }

    public boolean delete(int id) {

        String sql =
                "DELETE FROM pedido " +
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
                    "Error al eliminar el pedido de MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }

    public boolean actualizarEstado(
            int idPedido,
            String estado
    ) {

        String sql =
                "UPDATE pedido " +
                        "SET estado = ? " +
                        "WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                    1,
                    estado
            );

            sentencia.setInt(
                    2,
                    idPedido
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar el estado del pedido en MySQL."
            );

            e.printStackTrace();

            return false;
        }
    }

    private String obtenerTipo(Pedido pedido) {

        if (pedido instanceof PedidoComida) {
            return "COMIDA";

        } else if (pedido instanceof PedidoEncomienda) {
            return "ENCOMIENDA";

        } else if (pedido instanceof PedidoExpress) {
            return "EXPRESS";
        }

        return null;
    }

    private Pedido crearPedidoDesdeResultado(
            ResultSet resultado
    ) throws SQLException {

        int id =
                resultado.getInt("id");

        String direccion =
                resultado.getString("direccion");

        String tipo =
                resultado.getString("tipo");

        String estado =
                resultado.getString("estado");

        Pedido pedido;

        switch (tipo.toUpperCase()) {

            case "COMIDA":

                pedido =
                        new PedidoComida(
                                id,
                                direccion,
                                0.0
                        );

                break;

            case "ENCOMIENDA":

                pedido =
                        new PedidoEncomienda(
                                id,
                                direccion,
                                0.0
                        );

                break;

            case "EXPRESS":

                pedido =
                        new PedidoExpress(
                                id,
                                direccion,
                                0.0
                        );

                break;

            default:
                return null;
        }

        pedido.setEstado(estado);

        return pedido;
    }
}