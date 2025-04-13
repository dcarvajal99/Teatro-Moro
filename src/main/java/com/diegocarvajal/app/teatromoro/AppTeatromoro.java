/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

 package com.diegocarvajal.app.teatromoro;


 import java.util.ArrayList;
 import java.util.Scanner;
 
 /**
  *
  * @author d_car
  */

  public class AppTeatromoro {

    static ArrayList<Integer> zonaVIP = new ArrayList<>();
    static ArrayList<Integer> zonaPlateaBaja = new ArrayList<>();
    static ArrayList<Integer> zonaPlateaAlta = new ArrayList<>();
    static ArrayList<Integer> zonaPalcos = new ArrayList<>();
    static ArrayList<EntradaInfo> carrito = new ArrayList<>();

    static int totalIngresos = 0;
    static int entradasVendidas = 0;

    static class EntradaInfo {
        String nombreZona;
        ArrayList<Integer> asientos;
        int precio;
        String descripcionDescuento;

        EntradaInfo(String nombreZona, ArrayList<Integer> asientos, int precio, String descripcionDescuento) {
            this.nombreZona = nombreZona;
            this.asientos = asientos;
            this.precio = precio;
            this.descripcionDescuento = descripcionDescuento;
        }

        public String toString() {
            return "Zona: " + nombreZona + " | Asientos: " + asientos + " | Total pagado: $" + precio +
                   (descripcionDescuento.isEmpty() ? "" : " | Descuentos: " + descripcionDescuento);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        for (int i = 1; i <= 10; i++) {
            zonaVIP.add(i);
            zonaPlateaBaja.add(i);
            zonaPlateaAlta.add(i);
            zonaPalcos.add(i);
        }

        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Comprar entrada");
            System.out.println("2. Ver promociones");
            System.out.println("3. Ver carrito");
            System.out.println("4. Eliminar entradas del carrito");
            System.out.println("5. Ver entradas disponibles");
            System.out.println("6. Mostrar Precios de entradas");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1 -> comprarEntrada(scanner);
                case 2 -> mostrarPromociones();
                case 3 -> mostrarCarrito();
                case 4 -> eliminarEntrada(scanner);
                case 5 -> mostrarEntradasDisponibles();
                case 6 -> mostrarPreciosEntradas();
                case 7 -> {
                    salir = true;
                    System.out.println("Gracias por su visita al Teatro Moro.");
                }
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
        scanner.close();
    }

    static void comprarEntrada(Scanner scanner) {
        System.out.println("\n--- Zonas Disponibles ---");
        System.out.println("1. VIP: " + zonaVIP);
        System.out.println("2. Platea Baja: " + zonaPlateaBaja);
        System.out.println("3. Platea Alta: " + zonaPlateaAlta);
        System.out.println("4. Palcos: " + zonaPalcos);

        int zonaSeleccionada;
        do {
            System.out.print("Seleccione una zona (1-4): ");
            zonaSeleccionada = scanner.nextInt();
        } while (zonaSeleccionada < 1 || zonaSeleccionada > 4);

        String nombreZona = switch (zonaSeleccionada) {
            case 1 -> "VIP";
            case 2 -> "Platea Baja";
            case 3 -> "Platea Alta";
            case 4 -> "Palcos";
            default -> "Desconocida";
        };

        ArrayList<Integer> zona = switch (zonaSeleccionada) {
            case 1 -> zonaVIP;
            case 2 -> zonaPlateaBaja;
            case 3 -> zonaPlateaAlta;
            case 4 -> zonaPalcos;
            default -> new ArrayList<>();
        };

        System.out.print("¿Cuántas personas asistirán?: ");
        int cantidadPersonas = scanner.nextInt();
        if (cantidadPersonas < 1) {
            System.out.println("Debe ingresar al menos una persona.");
            return;
        }

        int precioBase = switch (zonaSeleccionada) {
            case 1 -> 30000;
            case 2 -> 15000;
            case 3 -> 18000;
            case 4 -> 13000;
            default -> 0;
        };

        ArrayList<Integer> asientosElegidos = new ArrayList<>();
        int precioTotal = 0;
        int estudiantes = 0;

        ArrayList<String> descuentosAplicados = new ArrayList<>();

        if (zona.size() < cantidadPersonas) {
            System.out.println("No hay suficientes asientos disponibles en esta zona.");
            return;
        }

        for (int i = 1; i <= cantidadPersonas; i++) {
            System.out.println("\n--- Datos de la Persona " + i + " ---");
            System.out.print("Edad: ");
            int edad = scanner.nextInt();
            System.out.print("¿Es estudiante? (s/n): ");
            boolean esEstudiante = scanner.next().toLowerCase().equals("s");

            int descuento = 0;
            String detalle = "";

            if (edad >= 3) {
                if (esEstudiante) {
                    descuento += (int) (precioBase * 0.10);
                    detalle += "10% estudiante ";
                    estudiantes++;
                }
                if (edad >= 60) {
                    descuento += (int) (precioBase * 0.15);
                    detalle += "15% adulto mayor ";
                }

                precioTotal += precioBase - descuento;
                if (!detalle.isEmpty()) descuentosAplicados.add("Persona " + i + ": " + detalle.trim());

            } else {
                descuentosAplicados.add("Persona " + i + ": entrada gratuita por ser menor de 3 años");
            }

            while (true) {
                System.out.println("Seleccione un asiento disponible para esta persona de la lista: " + zona);
                int asiento = scanner.nextInt();
                if (zona.contains(asiento)) {
                    zona.remove(Integer.valueOf(asiento));
                    asientosElegidos.add(asiento);
                    break;
                } else {
                    System.out.println("Asiento no disponible. Intente nuevamente.");
                }
            }
        }

        if (estudiantes >= 2) {
            System.out.println("Promoción activada: 2 estudiantes pagan 1 entrada.");
            precioTotal -= precioBase;
            descuentosAplicados.add("Promoción estudiantes 2x1 aplicada");
        }

        entradasVendidas += cantidadPersonas;
        totalIngresos += precioTotal;

        String descripcion = String.join(", ", descuentosAplicados);
        carrito.add(new EntradaInfo(nombreZona, asientosElegidos, precioTotal, descripcion));

        System.out.println("\n--- Compra realizada con éxito ---");
        System.out.println("Zona seleccionada: " + nombreZona);
        System.out.println("Asientos asignados: " + asientosElegidos);
        System.out.println("Precio base por entrada: $" + precioBase);
        System.out.println("Descuento total aplicado: $" + (cantidadPersonas * precioBase - precioTotal));
        if (!descripcion.isEmpty()) {
            System.out.println("Detalle de descuentos: " + descripcion);
        }
        System.out.println("Total a pagar: $" + precioTotal);
    }

    static void mostrarPromociones() {
        System.out.println("\n--- Promociones Disponibles ---");
        System.out.println("1. 10% de descuento para estudiantes.");
        System.out.println("2. 15% de descuento para mayores de 60 años.");
        System.out.println("3. Promoción especial: 2 estudiantes pagan solo 1 entrada.");
        System.out.println("4. Niños menores de 3 años entran gratis (requieren asiento).\n");
    }

    static void mostrarCarrito() {
        System.out.println("\n--- Carrito de Compras ---");
        for (int i = 0; i < carrito.size(); i++) {
            System.out.println((i + 1) + ". " + carrito.get(i));
        }
        System.out.println("Total de entradas vendidas: " + entradasVendidas);
        System.out.println("Ingresos totales: $" + totalIngresos);
    }

    static void eliminarEntrada(Scanner scanner) {
        System.out.println("\n--- Eliminar Entrada del Carrito ---");
        if (carrito.isEmpty()) {
            System.out.println("No hay entradas en el carrito.");
            return;
        }
        for (int i = 0; i < carrito.size(); i++) {
            System.out.println((i + 1) + ". " + carrito.get(i));
        }
        System.out.print("Seleccione el número de la entrada a eliminar: ");
        int index = scanner.nextInt();
        if (index > 0 && index <= carrito.size()) {
            EntradaInfo entradaEliminada = carrito.remove(index - 1);
            entradasVendidas -= entradaEliminada.asientos.size();
            totalIngresos -= entradaEliminada.precio;

            ArrayList<Integer> zona = switch (entradaEliminada.nombreZona) {
                case "VIP" -> zonaVIP;
                case "Platea Baja" -> zonaPlateaBaja;
                case "Platea Alta" -> zonaPlateaAlta;
                case "Palcos" -> zonaPalcos;
                default -> new ArrayList<>();
            };
            zona.addAll(entradaEliminada.asientos);
            System.out.println("Entrada eliminada y asientos liberados correctamente.");
        } else {
            System.out.println("Índice inválido. Intente nuevamente.");
        }
    }

    static void mostrarEntradasDisponibles() {
        System.out.println("\n--- Entradas Disponibles por Zona ---");
        System.out.println("1. VIP: " + zonaVIP);
        System.out.println("2. Platea Baja: " + zonaPlateaBaja);
        System.out.println("3. Platea Alta: " + zonaPlateaAlta);
        System.out.println("4. Palcos: " + zonaPalcos);
    }

    static void mostrarPreciosEntradas() {
        System.out.println("\n--- Precios por Zona ---");
        System.out.println("1. VIP: $30.000");
        System.out.println("2. Platea Baja: $15.000");
        System.out.println("3. Platea Alta: $18.000");
        System.out.println("4. Palcos: $13.000");
    }
}
