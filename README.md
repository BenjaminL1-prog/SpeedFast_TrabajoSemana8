# SpeedFast

Sistema de gestión de pedidos desarrollado en Java para la empresa de reparto **SpeedFast**.

El proyecto representa distintos tipos de pedidos y utiliza conceptos fundamentales de **Programación Orientada a Objetos**, especialmente clases abstractas, herencia, polimorfismo, sobrecarga, sobreescritura e interfaces.

En esta semana se incorpora el uso de **programación concurrente y sincronización de procesos**, permitiendo que distintos repartidores trabajen simultáneamente sobre una zona de carga compartida mediante `Runnable`, `ExecutorService` y métodos `synchronized`.

## Descripción

SpeedFast gestiona tres tipos de pedidos:

* **Pedido de Comida:** calcula su tiempo de entrega considerando una base de 15 minutos más 2 minutos por cada kilómetro.
* **Pedido de Encomienda:** calcula su tiempo de entrega considerando una base de 20 minutos más 1,5 minutos por cada kilómetro.
* **Pedido Express:** tiene un tiempo base de 10 minutos y agrega 5 minutos adicionales cuando la distancia supera los 5 km.

El sistema utiliza una jerarquía de clases basada en una clase abstracta `Pedido`, permitiendo reutilizar atributos y métodos comunes y definir un cálculo de tiempo específico para cada tipo de pedido.

Además, se incorporan interfaces para separar responsabilidades relacionadas con el despacho, la cancelación y el seguimiento de los pedidos.

En esta versión se incorpora el control de estados mediante el enumerador `EstadoPedido`, que permite representar los estados `PENDIENTE`, `EN_REPARTO` y `ENTREGADO`.

También se incorpora la clase `ZonaDeCarga`, que funciona como un recurso compartido entre los repartidores. Sus métodos de acceso utilizan `synchronized` para controlar el acceso concurrente a los pedidos y evitar que dos repartidores retiren el mismo pedido.

La clase `Repartidor` implementa `Runnable` y permite ejecutar las entregas de varios repartidores de manera concurrente. Cada repartidor accede a la misma `ZonaDeCarga`, retira pedidos disponibles y actualiza su estado durante el proceso de entrega.

## Estructura del proyecto

```text
src
├── main
│   └── Main.java
│
├── model
│   ├── Pedido.java
│   ├── PedidoComida.java
│   ├── PedidoEncomienda.java
│   ├── PedidoExpress.java
│   ├── Repartidor.java
│   ├── EstadoPedido.java
│   └── ZonaDeCarga.java
│
└── interfaces
    ├── Despachable.java
    ├── Cancelable.java
    └── Rastreable.java
```

### Clases principales

**Pedido**

Clase abstracta que contiene los atributos generales de un pedido:

* `idPedido`
* `direccionEntrega`
* `distanciaKm`
* `estado`

También implementa el método `mostrarResumen()` y declara el método abstracto `calcularTiempoEntrega()`.

Además, implementa las interfaces `Despachable`, `Cancelable` y `Rastreable`, incorporando las funcionalidades de despacho, cancelación y visualización del historial de operaciones.

El estad
