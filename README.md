# Teatro Moro - Sistema de Venta de Entradas

Este proyecto es un sistema de venta de entradas para el **Teatro Moro**, donde los usuarios pueden seleccionar la zona, elegir un asiento, y obtener un descuento basado en su edad o si son estudiantes.

## Descripción

El sistema permite realizar compras de entradas para diferentes zonas del teatro, incluyendo:

- Zona VIP
- Platea Baja
- Platea Alta
- Palcos

Los usuarios pueden obtener descuentos del 10% si son estudiantes y del 15% si son mayores de 60 años.

## Características

- Menú principal con opciones para comprar entradas o salir.
- Validación de asientos disponibles y gestión de reservas.
- Cálculo de precios con descuentos aplicados según la edad y la condición de estudiante.
- Visualización del resumen de la compra, incluyendo detalles del asiento, precio base, descuento y precio final.

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
