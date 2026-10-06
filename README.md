# SpeedFast

Sistema de gestión de pedidos desarrollado en Java para la empresa de reparto **SpeedFast**.

El proyecto representa distintos tipos de pedidos y utiliza conceptos fundamentales de **Programación Orientada a Objetos**, especialmente clases abstractas, herencia, polimorfismo, sobrecarga, sobreescritura e interfaces.

En esta versión se incorpora el uso de **programación concurrente y sincronización de procesos**, permitiendo que distintos repartidores trabajen simultáneamente sobre una zona de carga compartida mediante `Runnable`, `ExecutorService` y métodos `synchronized`.

Además, se incorpora una **interfaz gráfica de usuario (GUI)** desarrollada con **Java Swing**, permitiendo registrar, visualizar y gestionar pedidos mediante ventanas y componentes gráficos.

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

## Interfaz gráfica

La aplicación incorpora una interfaz gráfica desarrollada con **Java Swing**, permitiendo interactuar con el sistema sin depender exclusivamente de la consola.

La ventana principal contiene las siguientes opciones:

* **Registrar pedido:** permite ingresar un nuevo pedido indicando su ID, dirección y tipo.
* **Listar pedidos:** muestra los pedidos registrados mediante una tabla.
* **Asignar repartidor / Iniciar entrega:** permite iniciar el proceso de entregas concurrentes.

### Registro de pedidos

La ventana de registro permite ingresar:

* ID del pedido.
* Dirección de entrega.
* Tipo de pedido mediante un `JComboBox`.

El sistema valida que el ID sea numérico, mayor que cero, que la dirección no esté vacía y que no exista otro pedido con el mismo ID.

Una vez registrado correctamente, el pedido se agrega a la lista de pedidos y a la zona de carga compartida.

### Lista de pedidos

La ventana de listado utiliza un `JTable` junto con un `DefaultTableModel` para mostrar la información de los pedidos registrados.

La tabla permite visualizar:

* ID.
* Dirección.
* Tipo de pedido.
* Distancia.
* Estado actual.

También cuenta con una opción para **actualizar la tabla**, permitiendo visualizar los cambios de estado producidos durante las entregas.

## Controlador de pedidos

La clase `ControladorPedidos` centraliza la gestión de los pedidos y permite compartir la misma información entre las distintas ventanas de la aplicación.

Sus principales responsabilidades son:

* Mantener la lista de pedidos.
* Crear y agregar nuevos pedidos.
* Validar IDs duplicados.
* Buscar pedidos por ID.
* Obtener el siguiente ID disponible.
* Mantener la zona de carga compartida.
* Iniciar las entregas concurrentes mediante varios repartidores.

Los pedidos iniciales utilizados en el sistema corresponden a seis ejemplos creados previamente, sobre los cuales se pueden agregar nuevos pedidos desde la interfaz gráfica.

## Programación concurrente

El sistema utiliza tres repartidores:

* **Camila**
* **Luis**
* **Pedro**

Los tres trabajan sobre la misma instancia de `ZonaDeCarga`.

La ejecución concurrente se realiza mediante `ExecutorService` y un grupo de tres hilos. Cada repartidor implementa `Runnable`, retira pedidos pendientes de la zona de carga y realiza la entrega.

El acceso sincronizado mediante `synchronized` evita que dos repartidores retiren simultáneamente el mismo pedido.

Durante el proceso, los estados de los pedidos cambian de:

```text
PENDIENTE
    ↓
EN_REPARTO
    ↓
ENTREGADO
```

## Estructura del proyecto

```text
src
├── main
│   └── Main.java
│
├── controller
│   └── ControladorPedidos.java
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
├── view
│   ├── VentanaPrincipal.java
│   ├── VentanaRegistroPedido.java
│   └── VentanaListaPedidos.java
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

**PedidoComida**

Hereda de `Pedido` y representa los pedidos de comida. Implementa su propio cálculo de tiempo de entrega y permite asignar un repartidor de forma automática o manual.

**PedidoEncomienda**

Hereda de `Pedido` y representa los pedidos de encomienda. Implementa su propio cálculo de tiempo de entrega y permite asignar un repartidor de forma automática o manual.

**PedidoExpress**

Hereda de `Pedido` y representa los pedidos express. Su tiempo de entrega depende de la distancia y permite asignar un repartidor de forma automática o manual.

**EstadoPedido**

Enumeración utilizada para representar el estado actual de cada pedido:

```text
PENDIENTE
EN_REPARTO
ENTREGADO
```

**ZonaDeCarga**

Representa la zona de carga compartida por los repartidores. Utiliza métodos `synchronized` para controlar el acceso concurrente a los pedidos.

**Repartidor**

Implementa `Runnable` y representa a los repartidores encargados de retirar y entregar pedidos de manera concurrente.

**ControladorPedidos**

Centraliza la gestión de los pedidos y sirve como intermediario entre la interfaz gráfica y las clases del modelo.

**VentanaPrincipal**

Ventana principal de la aplicación. Permite acceder al registro de pedidos, listado de pedidos e inicio de las entregas.

**VentanaRegistroPedido**

Ventana gráfica utilizada para registrar nuevos pedidos mediante campos de texto y un `JComboBox`.

**VentanaListaPedidos**

Ventana gráfica que muestra los pedidos mediante un `JTable` y permite actualizar la información mostrada.

**Main**

Clase principal encargada de iniciar la aplicación gráfica mediante `SwingUtilities.invokeLater()`.

## Tecnologías utilizadas

* **Java**
* **Java Swing**
* **Programación Orientada a Objetos**
* **Programación concurrente**
* **Runnable**
* **ExecutorService**
* **Sincronización mediante `synchronized`**
* **JFrame**
* **JTable**
* **DefaultTableModel**
* **JComboBox**
* **JOptionPane**

## Ejecución

La aplicación se inicia ejecutando la clase:

```text
main.Main
```

Al iniciar, se muestra la ventana principal de SpeedFast desde la cual se puede acceder a las distintas funcionalidades del sistema.

