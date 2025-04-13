# 🎭 Sistema de Venta de Entradas - Teatro Moro

Este proyecto en Java simula un sistema de venta de entradas para el Teatro Moro. Permite a los usuarios seleccionar zonas del teatro, elegir asientos, aplicar descuentos y ver promociones vigentes, todo a través de una interfaz de línea de comandos.

## 🧰 Funcionalidades

- Selección de zona: VIP, Platea Baja, Platea Alta, Palcos.
- Asignación de asientos según disponibilidad.
- Aplicación de descuentos:
  - 10% para estudiantes.
  - 15% para adultos mayores (60+).
  - Entrada gratuita para menores de 3 años.
  - Promoción especial: 2 estudiantes pagan solo 1 entrada.
- Visualización de carrito de compras.
- Eliminación de entradas y liberación de asientos.
- Visualización de precios y promociones actuales.
- Resumen de entradas vendidas e ingresos generados.

## 🏟️ Zonas del Teatro

| Zona          | Precio Base |
|---------------|--------------|
| VIP           | $30.000      |
| Platea Baja   | $15.000      |
| Platea Alta   | $18.000      |
| Palcos        | $13.000      |

Cada zona cuenta con 10 asientos numerados del 1 al 10. Los asientos se actualizan en tiempo real a medida que se venden o liberan.

## 📦 Estructura del Proyecto

- `AppTeatromoro.java`: Contiene toda la lógica del sistema, incluyendo las estructuras de datos, menú interactivo, lógica de compra y carrito.

## ▶️ Cómo ejecutar

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tuusuario/teatromoro.git
   cd teatromoro
## Requisitos

- **Java 8 o superior**
- **Maven** (si utilizas Maven para gestionar las dependencias)

## Instalación

1. Clona este repositorio en tu máquina local:

    ```bash
    git clone https://github.com/dcarvajal99/Teatro-Moro.git
    ```

2. Navega al directorio del proyecto:

    ```bash
    cd Teatro-Moro
    ```

3. Si deseas usar Maven, puedes compilar el proyecto con el siguiente comando:

    ```bash
    mvn clean install
    ```

4. Ejecuta el archivo principal `TeatroMoro.java` para iniciar el sistema:

    ```bash
    java TeatroMoro
    ```

## Contribución

Si deseas contribuir a este proyecto, sigue estos pasos:

1. Haz un fork de este repositorio.
2. Crea una rama para tu nueva funcionalidad (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza tus cambios y haz commit (`git commit -am 'Agregado nueva funcionalidad'`).
4. Haz push a tu rama (`git push origin feature/nueva-funcionalidad`).
5. Crea un Pull Request.

## Licencia

Este proyecto está bajo la licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.
