/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.diegocarvajal.app.teatromoro;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author d_car
 */
public class AppTeatromoro {

   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Integer> zonaVIP = new ArrayList<>();
        ArrayList<Integer> zonaPlateaBaja = new ArrayList<>();
        ArrayList<Integer> zonaPlateaAlta = new ArrayList<>();
        ArrayList<Integer> zonaPalcos = new ArrayList<>();

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
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("\n--- Zonas Disponibles ---");
                    System.out.println("1. VIP: " + zonaVIP);
                    System.out.println("2. Platea Baja: " + zonaPlateaBaja);
                    System.out.println("3. Platea Alta: " + zonaPlateaAlta);
                    System.out.println("4. Palcos: " + zonaPalcos);

                    int zonaSeleccionada = 0;
                    do {
                        System.out.print("Seleccione una zona (1-4): ");
                        zonaSeleccionada = scanner.nextInt();
                    } while (zonaSeleccionada < 1 || zonaSeleccionada > 4);

                    ArrayList<Integer> zona = switch (zonaSeleccionada) {
                        case 1 -> zonaVIP;
                        case 2 -> zonaPlateaBaja;
                        case 3 -> zonaPlateaAlta;
                        case 4 -> zonaPalcos;
                        default -> new ArrayList<>();
                    };

                    int asiento = 0;
                    boolean asientoDisponible = false;
                    do {
                        System.out.print("Seleccione un asiento: ");
                        asiento = scanner.nextInt();
                        if (zona.contains(asiento)) {
                            asientoDisponible = true;
                        } else {
                            System.out.println("Asiento no disponible. Intente nuevamente.");
                        }
                    } while (!asientoDisponible);

                    int edad = -1;
                    while (edad <= 0) {
                        System.out.print("Ingrese su edad: ");
                        edad = scanner.nextInt();
                        if (edad <= 0) {
                            System.out.println("Edad inválida. Ingrese un valor mayor a 0.");
                        }
                    }

                    boolean esEstudiante = false;
                    String respuesta;
                    do {
                        System.out.print("¿Es estudiante? (s/n): ");
                        respuesta = scanner.next().toLowerCase();
                    } while (!respuesta.equals("s") && !respuesta.equals("n"));

                    esEstudiante = respuesta.equals("s");

                    int precioBase = switch (zonaSeleccionada) {
                        case 1 -> 30000;
                        case 2 -> 15000;
                        case 3 -> 18000;
                        case 4 -> 13000;
                        default -> 0;
                    };

                    int descuento = 0;
                    if (esEstudiante) {
                        descuento = (int) (precioBase * 0.10);
                    } else if (edad >= 60) {
                        descuento = (int) (precioBase * 0.15);
                    }

                    int precioFinal = precioBase - descuento;

                    System.out.println("\n--- Resumen de la compra ---");
                    System.out.println("Zona: " + switch (zonaSeleccionada) {
                        case 1 -> "VIP";
                        case 2 -> "Platea Baja";
                        case 3 -> "Platea Alta";
                        case 4 -> "Palcos";
                        default -> "";
                    });
                    System.out.println("Asiento: " + asiento);
                    System.out.println("Precio base: $" + precioBase);
                    System.out.println("Descuento aplicado: $" + descuento);
                    System.out.println("Total a pagar: $" + precioFinal);

                    zona.remove(Integer.valueOf(asiento));
                    break;

                case 2:
                    salir = true;
                    System.out.println("Gracias por su visita al Teatro Moro.");
                    break;

                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    break;
            }
        }

        scanner.close();
    }
}

