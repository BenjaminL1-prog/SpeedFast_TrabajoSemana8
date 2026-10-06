# SpeedFast

Sistema de gestión de pedidos desarrollado en Java para la empresa de reparto **SpeedFast**.

El proyecto representa distintos tipos de pedidos y utiliza conceptos fundamentales de **Programación Orientada a Objetos**, especialmente clases abstractas, herencia, polimorfismo, sobrecarga, sobreescritura e interfaces.

En esta versión se incorpora la conexión con una base de datos **MySQL mediante JDBC**, permitiendo almacenar y recuperar información de pedidos, repartidores y entregas de forma persistente.

Además, se implementa un sistema **CRUD completo (Crear, Leer, Actualizar y Eliminar)** utilizando clases DAO, `PreparedStatement` y `ResultSet`, integrando estas operaciones directamente con una interfaz gráfica desarrollada mediante **Java Swing**.

## Descripción

SpeedFast gestiona tres tipos de pedidos:

* **Pedido de Comida:** calcula su tiempo de entrega considerando una base de 15 minutos más 2 minutos por cada kilómetro.
* **Pedido de Encomienda:** calcula su tiempo de entrega considerando una base de 20 minutos más 1,5 minutos por cada kilómetro.
* **Pedido Express:** tiene un tiempo base de 10 minutos y agrega 5 minutos adicionales cuando la distancia supera los 5 km.

El sistema utiliza una jerarquía de clases basada en una clase abstracta `Pedido`, permitiendo reutilizar atributos y métodos comunes y definir un cálculo de tiempo específico para cada tipo de pedido.

También se incorporan interfaces para separar responsabilidades relacionadas con el despacho, la cancelación y el seguimiento de los pedidos.

El control de estados se realiza mediante el enumerador `EstadoPedido`, que permite representar los estados:

```text
PENDIENTE
EN_REPARTO
ENTREGADO
```

La clase `ZonaDeCarga` funciona como un recurso compartido entre los repartidores. Sus métodos utilizan `synchronized` para controlar el acceso concurrente a los pedidos y evitar que dos repartidores retiren simultáneamente el mismo pedido.

La clase `Repartidor` implementa `Runnable`, permitiendo ejecutar las entregas de distintos repartidores de manera concurrente.

## Persistencia con MySQL

En esta versión, la información del sistema se almacena en una base de datos **MySQL** llamada:

```text
speedfast_db
```

La aplicación utiliza **JDBC (Java Database Connectivity)** para establecer la conexión entre Java y MySQL.

La conexión se centraliza mediante la clase:

```text
ConexionDB
```

Esta clase permite reutilizar la conexión a la base de datos desde las distintas clases DAO.

La base de datos contiene las siguientes tablas principales:

```text
repartidor
pedido
entrega
```

Estas tablas permiten almacenar la información de los repartidores, pedidos y entregas realizadas.

## Patrón DAO

Para separar la lógica de acceso a datos de la lógica de negocio, el proyecto utiliza clases **DAO (Data Access Object)**.

Las principales clases DAO son:

* `RepartidorDAO`
* `PedidoDAO`
* `EntregaDAO`

Cada DAO se encarga de realizar las operaciones correspondientes sobre su entidad.

Las operaciones implementadas incluyen:

* **Create:** registrar nuevos datos.
* **Read:** consultar y listar datos almacenados.
* **Update:** modificar registros existentes.
* **Delete:** eliminar registros.

Las consultas utilizan `PreparedStatement` para enviar los parámetros a MySQL y `ResultSet` para recuperar los resultados de las consultas.

Además, se utilizan bloques `try-with-resources` para cerrar automáticamente las conexiones, sentencias y resultados utilizados durante las operaciones con la base de datos.

## CRUD de repartidores

El sistema permite gestionar los repartidores registrados en MySQL.

Desde la interfaz gráfica es posible:

* Registrar un nuevo repartidor.
* Listar los repartidores registrados.
* Editar el nombre de un repartidor.
* Eliminar un repartidor.
* Limpiar los campos del formulario.

La información se muestra mediante un `JTable` con las columnas:

```text
ID
Nombre
```

La gestión de repartidores se realiza mediante la clase:

```text
VentanaGestionRepartidores
```

y utiliza `RepartidorDAO` para realizar las operaciones sobre MySQL.

## CRUD de pedidos

El sistema permite gestionar los pedidos almacenados en la base de datos.

Cada pedido contiene información relacionada con:

* ID.
* Dirección de entrega.
* Tipo de pedido.
* Estado actual.

Los tipos disponibles son:

```text
COMIDA
ENCOMIENDA
EXPRESS
```

Los estados disponibles son:

```text
PENDIENTE
EN_REPARTO
ENTREGADO
```

Desde la interfaz gráfica es posible:

* Registrar pedidos.
* Listar pedidos.
* Editar pedidos.
* Eliminar pedidos.
* Modificar el tipo de pedido.
* Modificar el estado del pedido.

La información se muestra mediante un `JTable`.

La gestión se realiza mediante:

```text
VentanaGestionPedidos
```

utilizando `PedidoDAO` para comunicarse con MySQL.

## CRUD de entregas

El sistema también permite gestionar las entregas asociadas a los pedidos y repartidores.

Cada entrega almacena:

* ID de la entrega.
* ID del pedido.
* ID del repartidor.
* Fecha.
* Hora.

Desde la interfaz gráfica es posible:

* Registrar una entrega.
* Listar entregas.
* Editar una entrega.
* Eliminar una entrega.

La ventana de gestión permite seleccio


