package com.diegocarvajal.app.teatromoro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AppTeatromoro {
    private static final int FILAS = 5;
    private static final int COLUMNAS = 10;

    enum Zona {
        VIP("VIP", 30000, 1),
        PLATEA_BAJA("Platea Baja", 15000, 2),
        PLATEA_ALTA("Platea Alta", 18000, 3),
        PALCOS("Palcos", 13000, 4);

        final String nombre;
        final int precioBase;
        final int opcionMenu;

        Zona(String nombre, int precioBase, int opcionMenu) {
            this.nombre = nombre;
            this.precioBase = precioBase;
            this.opcionMenu = opcionMenu;
        }

        static Zona fromOpcion(int opcion) {
            return Arrays.stream(values())
                    .filter(z -> z.opcionMenu == opcion)
                    .findFirst()
                    .orElseThrow();
        }
    }

    static class EntradaInfo {
        final Zona zona;
        final ArrayList<String> asientos;
        final int precio;
        final String descuentos;

        EntradaInfo(Zona zona, ArrayList<String> asientos, int precio, String descuentos) {
            this.zona = zona;
            this.asientos = asientos;
            this.precio = precio;
            this.descuentos = descuentos;
        }
    }

    private final boolean[][][] zonas = new boolean[Zona.values().length][FILAS][COLUMNAS];
    private final ArrayList<EntradaInfo> carrito = new ArrayList<>();
    private int totalIngresos = 0;
    private int entradasVendidas = 0;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            AppTeatromoro app = new AppTeatromoro();
            app.initializeSeats();
            app.run(scanner);
        }
    }

    private void initializeSeats() {
        for (boolean[][] zona : zonas) {
            for (boolean[] fila : zona) {
                Arrays.fill(fila, true);
            }
        }
    }

    private void run(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Comprar entrada\n2. Ver promociones\n3. Ver entradas disponibles");
            System.out.println("4. Mostrar Precios de entradas\n5. Emitir boleta\n6. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = leerEnteroValido(scanner, 1, 6);
            switch (opcion) {
                case 1 -> comprarEntrada(scanner);
                case 2 -> mostrarPromociones();
                case 3 -> mostrarEntradasDisponibles();
                case 4 -> mostrarPreciosEntradas();
                case 5 -> emitirBoleta();
                case 6 -> {
                    System.out.println("Gracias por su visita al Teatro Moro.");
                    return;
                }
            }
        }
    }

    private int leerEnteroValido(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int valor = scanner.nextInt();
                if (valor >= min && valor <= max) return valor;
                System.out.printf("Entrada inválida. Ingrese un número entre %d y %d: ", min, max);
            } catch (Exception e) {
                System.out.printf("Entrada inválida. Ingrese un número entre %d y %d: ", min, max);
                scanner.nextLine();
            }
        }
    }

    private void comprarEntrada(Scanner scanner) {
        System.out.println("\n--- Zonas Disponibles ---");
        for (Zona zona : Zona.values()) {
            System.out.printf("%d. %s%n", zona.opcionMenu, zona.nombre);
        }
        System.out.print("Seleccione una zona (1-4): ");
        Zona zona = Zona.fromOpcion(leerEnteroValido(scanner, 1, 4));
        boolean[][] asientosZona = zonas[zona.ordinal()];

        mostrarMapaAsientos(zona, asientosZona, true);
        System.out.print("¿Cuántas personas asistirán?: ");
        int cantidadPersonas = leerEnteroValido(scanner, 1, Integer.MAX_VALUE);

        if (contarAsientosDisponibles(asientosZona) < cantidadPersonas) {
            System.out.println("No hay suficientes asientos disponibles en esta zona.");
            return;
        }

        ArrayList<String> asientosElegidos = new ArrayList<>();
        int precioTotal = 0;
        int estudiantes = 0;
        ArrayList<String> descuentosAplicados = new ArrayList<>();

        scanner.nextLine();
        for (int i = 1; i <= cantidadPersonas; i++) {
            System.out.printf("\n--- Datos de la Persona %d ---%n", i);
            System.out.print("Edad: ");
            int edad = leerEnteroValido(scanner, 0, 150);
            scanner.nextLine();

            boolean esEstudiante;
            while (true) {
                System.out.print("¿Es estudiante? (s/n): ");
                String input = scanner.nextLine().toLowerCase();
                if (input.equals("s") || input.equals("n")) {
                    esEstudiante = input.equals("s");
                    break;
                }
                System.out.println("Entrada inválida. Ingrese 's' o 'n'.");
            }

            int descuento = 0;
            StringBuilder detalle = new StringBuilder();
            if (edad >= 3) {
                if (esEstudiante) {
                    descuento += (int) (zona.precioBase * 0.10);
                    detalle.append("10% estudiante ");
                    estudiantes++;
                }
                if (edad >= 60) {
                    descuento += (int) (zona.precioBase * 0.15);
                    detalle.append("15% adulto mayor ");
                }
                precioTotal += zona.precioBase - descuento;
                if (!detalle.isEmpty()) {
                    descuentosAplicados.add("Persona " + i + ": " + detalle.toString().trim());
                }
            } else {
                descuentosAplicados.add("Persona " + i + ": entrada gratuita (menor de 3 años)");
            }

            while (true) {
                System.out.print("Seleccione un asiento (ej. A1): ");
                String asiento = scanner.nextLine().toUpperCase();
                if (validarAsiento(asiento) && isAsientoDisponible(asientosZona, asiento)) {
                    marcarAsiento(asientosZona, asiento, false);
                    asientosElegidos.add(asiento);
                    mostrarMapaAsientos(zona, asientosZona, true);
                    break;
                }
                System.out.println("Asiento no válido o no disponible. Intente nuevamente.");
            }
        }

        if (estudiantes >= 2) {
            precioTotal -= zona.precioBase;
            descuentosAplicados.add("Promoción estudiantes 2x1 aplicada");
        }

        entradasVendidas += cantidadPersonas;
        totalIngresos += precioTotal;

        String descripcion = String.join(", ", descuentosAplicados);
        carrito.add(new EntradaInfo(zona, asientosElegidos, precioTotal, descripcion));
        mostrarBoleta(zona, asientosElegidos, precioTotal, descripcion, carrito.size());
    }

    private void mostrarMapaAsientos(Zona zona, boolean[][] asientos, boolean mostrarLeyenda) {
        System.out.printf("\n--- Mapa de Asientos: %s ---%n", zona.nombre);
        if (mostrarLeyenda) {
            System.out.println("Leyenda: D = Disponible | X = Ocupado");
            switch (zona) {
                case VIP -> System.out.println("       [Escenario]");
                case PLATEA_BAJA -> System.out.println("........ (Dist Consolidaciónancia al escenario)");
                case PLATEA_ALTA -> System.out.println("........ (Distancia al escenario)\n........");
                case PALCOS -> System.out.println("[Palcos - Lateral]");
            }
        }
        System.out.println("======================");

        System.out.print("    ");
        for (int j = 1; j <= COLUMNAS; j++) {
            if (j == 6) System.out.print("  ");
            System.out.printf("%2d", j);
        }
        System.out.println();
        System.out.println("  +" + "-".repeat(25) + "+");

        for (int i = 0; i < FILAS; i++) {
            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + " |");
            for (int j = 0; j < COLUMNAS; j++) {
                if (j == 5) System.out.print(" ");
                System.out.print(asientos[i][j] ? "D" : "X");
            }
            System.out.println("|");
        }
        System.out.println("  +" + "-".repeat(25) + "+");
        System.out.println(mostrarLeyenda ? "======================" : "");
    }

    private int contarAsientosDisponibles(boolean[][] zona) {
        int count = 0;
        for (boolean[] fila : zona) {
            for (boolean asiento : fila) {
                if (asiento) count++;
            }
        }
        return count;
    }

    private boolean validarAsiento(String asiento) {
        if (asiento.length() < 2) return false;
        char row = asiento.charAt(0);
        if (row < 'A' || row > 'E') return false;
        try {
            int col = Integer.parseInt(asiento.substring(1));
            return col >= 1 && col <= COLUMNAS;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isAsientoDisponible(boolean[][] zona, String asiento) {
        char row = asiento.charAt(0);
        int col = Integer.parseInt(asiento.substring(1)) - 1;
        return zona[row - 'A'][col];
    }

    private void marcarAsiento(boolean[][] zona, String asiento, boolean estado) {
        char row = asiento.charAt(0);
        int col = Integer.parseInt(asiento.substring(1)) - 1;
        zona[row - 'A'][col] = estado;
    }

    private void mostrarPromociones() {
        System.out.println("\n--- Promociones Disponibles ---");
        System.out.println("1. 10% de descuento para estudiantes");
        System.out.println("2. 15% de descuento para mayores de 60 años");
        System.out.println("3. Promoción 2x1 para estudiantes");
        System.out.println("4. Entrada gratuita para menores de 3 años");
    }

    private void mostrarEntradasDisponibles() {
        System.out.println("\n--- Entradas Disponibles por Zona ---");
        System.out.println("Leyenda: D = Disponible | X = Ocupado");
        for (Zona zona : Zona.values()) {
            System.out.printf("\n--- %s ---%n", zona.nombre);
            mostrarMapaAsientos(zona, zonas[zona.ordinal()], false);
        }
        System.out.println("         [Escenario]");
        System.out.println("======================");
    }

    private void mostrarPreciosEntradas() {
        System.out.println("\n--- Precios por Zona ---");
        for (Zona zona : Zona.values()) {
            System.out.printf("%d. %s: $%,d%n", zona.opcionMenu, zona.nombre, zona.precioBase);
        }
    }

    private void emitirBoleta() {
        if (carrito.isEmpty()) {
            System.out.println("\nNo hay entradas para emitir boleta.");
            return;
        }
        for (int i = 0; i < carrito.size(); i++) {
            EntradaInfo entrada = carrito.get(i);
            mostrarBoleta(entrada.zona, entrada.asientos, entrada.precio, entrada.descuentos, i + 1);
        }
    }

    private void mostrarBoleta(Zona zona, ArrayList<String> asientos, int precioTotal, String descripcion, int boletaNum) {
        System.out.printf("\n--- Boleta #%d ---%n", boletaNum);
        System.out.println("+" + "-".repeat(50) + "+");
        System.out.printf("| %-48s |%n", "BOLETA #" + boletaNum);
        System.out.println("+" + "-".repeat(50) + "+");
        System.out.printf("| %-48s |%n", "Teatro: Teatro Moro");
        System.out.printf("| %-48s |%n", "Zona: " + zona.nombre);
        System.out.printf("| %-48s |%n", "Asientos: " + asientos);
        System.out.printf("| %-48s |%n", "Total Pagado: $" + precioTotal);
        if (!descripcion.isEmpty()) {
            System.out.printf("| %-48s |%n", "Descuentos:");
            for (String linea : dividirTexto(descripcion, 48)) {
                System.out.printf("| %-48s |%n", linea);
            }
        }
        System.out.println("+" + "-".repeat(50) + "+");
    }

    private String[] dividirTexto(String texto, int longitudMaxima) {
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