package com.diegocarvajal.app.teatromoro;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author d_car
 */
public class AppTeatromoro {

    
    static boolean[][] zonaVIP = new boolean[5][10]; 
    static boolean[][] zonaPlateaBaja = new boolean[5][10];
    static boolean[][] zonaPlateaAlta = new boolean[5][10];
    static boolean[][] zonaPalcos = new boolean[5][10];
    static ArrayList<EntradaInfo> carrito = new ArrayList<>();

    static int totalIngresos = 0;
    static int entradasVendidas = 0;
    static String nombreTeatro = "Teatro Moro";

    static class EntradaInfo {
        String nombreZona;
        ArrayList<String> asientos; 
        int precio;
        String descripcionDescuento;

        EntradaInfo(String nombreZona, ArrayList<String> asientos, int precio, String descripcionDescuento) {
            this.nombreZona = nombreZona;
            this.asientos = asientos;
            this.precio = precio;
            this.descripcionDescuento = descripcionDescuento;
        }

        @Override
        public String toString() {
            return "Zona: " + nombreZona + " | Asientos: " + asientos + " | Total pagado: $" + precio +
                   (descripcionDescuento.isEmpty() ? "" : " | Descuentos: " + descripcionDescuento);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        initializeSeats();

        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Comprar entrada");
            System.out.println("2. Ver promociones");
            System.out.println("3. Ver entradas disponibles");
            System.out.println("4. Mostrar Precios de entradas");
            System.out.println("5. Emitir boleta");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = readValidInteger(scanner, 1, 6);
            if (opcion == -1) {
                System.out.println("Opción inválida. Intente nuevamente.");
                continue;
            }

            switch (opcion) {
                case 1 -> comprarEntrada(scanner);
                case 2 -> mostrarPromociones();
                case 3 -> mostrarEntradasDisponibles();
                case 4 -> mostrarPreciosEntradas();
                case 5 -> emitirBoleta();
                case 6 -> {
                    salir = true;
                    System.out.println("Gracias por su visita al Teatro Moro.");
                }
            }
        }
        scanner.close();
    }

    
    static void initializeSeats() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                zonaVIP[i][j] = true;
                zonaPlateaBaja[i][j] = true;
                zonaPlateaAlta[i][j] = true;
                zonaPalcos[i][j] = true;
            }
        }
    }

    
    static int readValidInteger(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = scanner.nextInt();
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.print("Por favor, ingrese un número entre " + min + " y " + max + ": ");
            } catch (Exception e) {
                System.out.print("Entrada inválida. Ingrese un número entre " + min + " y " + max + ": ");
                scanner.nextLine(); 
            }
        }
    }

    static void comprarEntrada(Scanner scanner) {
        System.out.println("\n--- Zonas Disponibles ---");
        System.out.println("1. VIP");
        System.out.println("2. Platea Baja");
        System.out.println("3. Platea Alta");
        System.out.println("4. Palcos");

        System.out.print("Seleccione una zona (1-4): ");
        int zonaSeleccionada = readValidInteger(scanner, 1, 4);

        String nombreZona = switch (zonaSeleccionada) {
            case 1 -> "VIP";
            case 2 -> "Platea Baja";
            case 3 -> "Platea Alta";
            case 4 -> "Palcos";
            default -> "Desconocida";
        };

        boolean[][] zona = switch (zonaSeleccionada) {
            case 1 -> zonaVIP;
            case 2 -> zonaPlateaBaja;
            case 3 -> zonaPlateaAlta;
            case 4 -> zonaPalcos;
            default -> new boolean[5][10];
        };

        
        mostrarMapaAsientos(zona, nombreZona, zonaSeleccionada);

        System.out.print("¿Cuántas personas asistirán?: ");
        int cantidadPersonas = readValidInteger(scanner, 1, Integer.MAX_VALUE);
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

        
        int asientosDisponibles = contarAsientosDisponibles(zona);
        if (asientosDisponibles < cantidadPersonas) {
            System.out.println("No hay suficientes asientos disponibles en esta zona.");
            return;
        }

        ArrayList<String> asientosElegidos = new ArrayList<>();
        int precioTotal = 0;
        int estudiantes = 0;
        ArrayList<String> descuentosAplicados = new ArrayList<>();

        for (int i = 1; i <= cantidadPersonas; i++) {
            System.out.println("\n--- Datos de la Persona " + i + " ---");
            System.out.print("Edad: ");
            int edad = readValidInteger(scanner, 0, 150);
            System.out.print("¿Es estudiante? (s/n): ");
            scanner.nextLine(); 
            String inputEstudiante = scanner.nextLine().toLowerCase();
            boolean esEstudiante = inputEstudiante.equals("s");

            int descuento = 0;
            StringBuilder detalle = new StringBuilder();

            if (edad >= 3) {
                if (esEstudiante) {
                    descuento += (int) (precioBase * 0.10);
                    detalle.append("10% estudiante ");
                    estudiantes++;
                }
                if (edad >= 60) {
                    descuento += (int) (precioBase * 0.15);
                    detalle.append("15% adulto mayor ");
                }
                precioTotal += precioBase - descuento;
                if (!detalle.isEmpty()) descuentosAplicados.add("Persona " + i + ": " + detalle.toString().trim());
            } else {
                descuentosAplicados.add("Persona " + i + ": entrada gratuita por ser menor de 3 años");
            }

            
            while (true) {
                System.out.print("Seleccione un asiento (ej. A1): ");
                String asiento = scanner.nextLine().toUpperCase();
                if (validarAsiento(asiento) && isAsientoDisponible(zona, asiento)) {
                    marcarAsiento(zona, asiento, false); 
                    asientosElegidos.add(asiento);
                    mostrarMapaAsientos(zona, nombreZona, zonaSeleccionada); 
                    break;
                } else {
                    System.out.println("Asiento no válido o no disponible. Intente nuevamente.");
                }
            }
        }

        if (estudiantes >= 2) {
            precioTotal -= precioBase;
            descuentosAplicados.add("Promoción estudiantes 2x1 aplicada");
        }

        entradasVendidas += cantidadPersonas;
        totalIngresos += precioTotal;

        String descripcion = String.join(", ", descuentosAplicados);
        EntradaInfo entrada = new EntradaInfo(nombreZona, asientosElegidos, precioTotal, descripcion);
        carrito.add(entrada);

        
        System.out.println("\n--- Compra realizada con éxito ---");
        System.out.println("+" + "-".repeat(50) + "+");
        System.out.printf("| %-48s |%n", "RESUMEN DE COMPRA");
        System.out.println("+" + "-".repeat(50) + "+");
        System.out.printf("| %-48s |%n", "Teatro: " + nombreTeatro);
        System.out.printf("| %-48s |%n", "Zona: " + nombreZona);
        System.out.printf("| %-48s |%n", "Asientos: " + asientosElegidos);
        System.out.printf("| %-48s |%n", "Total Pagado: $" + precioTotal);
        if (!descripcion.isEmpty()) {
            String[] lineas = dividirTexto(descripcion, 48);
            System.out.printf("| %-48s |%n", "Descuentos:");
            for (String linea : lineas) {
                System.out.printf("| %-48s |%n", linea);
            }
        }
        System.out.println("+" + "-".repeat(50) + "+\n");
    }

    
    static void mostrarMapaAsientos(boolean[][] zona, String nombreZona, int zonaSeleccionada) {
        System.out.println("\n--- Mapa de Asientos: " + nombreZona + " ---");
        System.out.println("Leyenda: ⬜ (Verde) = Disponible | ⬛ (Rojo) = Ocupado");
        
        
        switch (zonaSeleccionada) {
            case 1 -> System.out.println("       [Escenario]");
            case 2 -> System.out.println("........ (Distancia al escenario)");
            case 3 -> {
                System.out.println("........ (Distancia al escenario)");
                System.out.println("........");
            }
            case 4 -> System.out.println("[Palcos - Lateral]");
        }
        System.out.println("======================");
        
        
        System.out.print("    ");
        int colStart = (zonaSeleccionada == 4) ? 1 : 1;
        int colEnd = (zonaSeleccionada == 4) ? 10 : 10;
        for (int j = colStart; j <= colEnd; j++) {
            if (j == 6) System.out.print("  "); 
            System.out.printf("%2d", j);
        }
        System.out.println();
        System.out.println("  +" + "-".repeat(25) + "+"); 
        
        
        for (int i = 0; i < 5; i++) {
            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + " |");
            for (int j = 0; j < 10; j++) {
                if (j == 5) System.out.print(" "); 
                String symbol = zona[i][j] ? "\u001B[32m⬜\u001B[0m" : "\u001B[31m⬛\u001B[0m";
                System.out.print(symbol);
            }
            System.out.println("|");
        }
        System.out.println("  +" + "-".repeat(25) + "+"); 
        System.out.println("======================");
    }

    
    static int contarAsientosDisponibles(boolean[][] zona) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                if (zona[i][j]) count++;
            }
        }
        return count;
    }

    
    static boolean validarAsiento(String asiento) {
        if (asiento.length() < 2) return false;
        char row = asiento.charAt(0);
        if (row < 'A' || row > 'E') return false;
        try {
            int col = Integer.parseInt(asiento.substring(1));
            return col >= 1 && col <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    
    static boolean isAsientoDisponible(boolean[][] zona, String asiento) {
        char row = asiento.charAt(0);
        int col = Integer.parseInt(asiento.substring(1)) - 1;
        int rowIndex = row - 'A';
        return zona[rowIndex][col];
    }

    
    static void marcarAsiento(boolean[][] zona, String asiento, boolean estado) {
        char row = asiento.charAt(0);
        int col = Integer.parseInt(asiento.substring(1)) - 1;
        int rowIndex = row - 'A';
        zona[rowIndex][col] = estado;
    }

    static void mostrarPromociones() {
        System.out.println("\n--- Promociones Disponibles ---");
        System.out.println("1. 10% de descuento para estudiantes.");
        System.out.println("2. 15% de descuento para mayores de 60 años.");
        System.out.println("3. Promoción especial: 2 estudiantes pagan solo 1 entrada.");
        System.out.println("4. Niños menores de 3 años entran gratis (requieren asiento).\n");
    }

    
    static void mostrarEntradasDisponibles() {
        System.out.println("\n--- Entradas Disponibles por Zona ---");
        System.out.println("Leyenda: ⬜ (Verde) = Disponible | ⬛ (Rojo) = Ocupado\n");

        
        System.out.println("--- Palcos Izquierdo ---");
        mostrarMapaAsientosPalcos(zonaPalcos, true);

        
        System.out.println("--- Palcos Derecho ---");
        mostrarMapaAsientosPalcos(zonaPalcos, false);

        
        System.out.println("--- Platea Alta ---");
        mostrarMapaAsientosSinLeyenda(zonaPlateaAlta);

        
        System.out.println("--- Platea Baja ---");
        mostrarMapaAsientosSinLeyenda(zonaPlateaBaja);

        
        System.out.println("--- VIP ---");
        mostrarMapaAsientosSinLeyenda(zonaVIP);

        
        System.out.println("         [Escenario]");
        System.out.println("======================");
    }

    
    static void mostrarMapaAsientosSinLeyenda(boolean[][] zona) {
        System.out.print("    ");
        for (int j = 1; j <= 10; j++) {
            if (j == 6) System.out.print("  "); 
            System.out.printf("%2d", j);
        }
        System.out.println();
        System.out.println("  +" + "-".repeat(25) + "+"); 
        for (int i = 0; i < 5; i++) {
            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + " |");
            for (int j = 0; j < 10; j++) {
                if (j == 5) System.out.print(" "); 
                String symbol = zona[i][j] ? "\u001B[32m⬜\u001B[0m" : "\u001B[31m⬛\u001B[0m";
                System.out.print(symbol);
            }
            System.out.println("|");
        }
        System.out.println("  +" + "-".repeat(25) + "+"); 
        System.out.println();
    }

    
    static void mostrarMapaAsientosPalcos(boolean[][] zona, boolean isLeft) {
        System.out.print("    ");
        int colStart = isLeft ? 1 : 6;
        int colEnd = isLeft ? 5 : 10;
        for (int j = colStart; j <= colEnd; j++) {
            System.out.printf("%2d", j);
        }
        System.out.println();
        System.out.println("  +" + "-".repeat(isLeft ? 13 : 13) + "+"); 
        for (int i = 0; i < 5; i++) {
            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + " |");
            for (int j = colStart - 1; j < colEnd; j++) {
                String symbol = zona[i][j] ? "\u001B[32m⬜\u001B[0m" : "\u001B[31m⬛\u001B[0m";
                System.out.print(symbol);
            }
            System.out.println("|");
        }
        System.out.println("  +" + "-".repeat(isLeft ? 13 : 13) + "+"); 
        System.out.println();
    }

    static void mostrarPreciosEntradas() {
        System.out.println("\n--- Precios por Zona ---");
        System.out.println("1. VIP: $30.000");
        System.out.println("2. Platea Baja: $15.000");
        System.out.println("3. Platea Alta: $18.000");
        System.out.println("4. Palcos: $13.000");
    }

    static void emitirBoleta() {
        System.out.println("\n--- Boleta de Compra ---");
        if (carrito.isEmpty()) {
            System.out.println("No hay entradas para emitir boleta.");
            return;
        }

        for (int i = 0; i < carrito.size(); i++) {
            EntradaInfo entrada = carrito.get(i);
            System.out.println("+" + "-".repeat(50) + "+");
            System.out.printf("| %-48s |%n", "BOLETA #" + (i + 1));
            System.out.println("+" + "-".repeat(50) + "+");
            System.out.printf("| %-48s |%n", "Teatro: " + nombreTeatro);
            System.out.printf("| %-48s |%n", "Zona: " + entrada.nombreZona);
            System.out.printf("| %-48s |%n", "Asientos: " + entrada.asientos);
            System.out.printf("| %-48s |%n", "Total Pagado: $" + entrada.precio);
            if (!entrada.descripcionDescuento.isEmpty()) {
                String[] lineas = dividirTexto(entrada.descripcionDescuento, 48);
                System.out.printf("| %-48s |%n", "Descuentos:");
                for (String linea : lineas) {
                    System.out.printf("| %-48s |%n", linea);
                }
            }
            System.out.println("+" + "-".repeat(50) + "+\n");
        }
    }

    static String[] dividirTexto(String texto, int longitudMaxima) {
        ArrayList<String> lineas = new ArrayList<>();
        while (texto.length() > longitudMaxima) {
            int espacio = texto.lastIndexOf(' ', longitudMaxima);
            if (espacio == -1) espacio = longitudMaxima;
            lineas.add(texto.substring(0, espacio));
            texto = texto.substring(espacio).trim();
        }
        lineas.add(texto);
        return lineas.toArray(new String[0]);
    }
}
