//Pablo Guisasola Ruiz
//1ºDAW

import java.util.Scanner;

public class HundirLaFlota {
    static final int FILAS = 11;
    static final int COLUMNAS = 11;
    static final int INTENTOS_TOTALES = 30;
    static public int BARCOS_TOTALES = 6;
    static public int TOTAL_CASILLAS_BARCOS = 17;
    static public int DISPARO_ESPECIAL = 3;
    static public int CASILLAS_INICIALES= 17;

    public static void main (String[] args){
        Scanner teclado = new Scanner(System.in);

        char[][] tablero = crearTablero(FILAS,COLUMNAS);
        colocarBarcos(tablero);

        int intentos = INTENTOS_TOTALES;
        boolean juegoTerminado = false;

        while (intentos > 0 && !juegoTerminado) {

            System.out.println("Intentos restantes: " + intentos);

            mostrarTablero(tablero, false);

            System.out.print("Introduce una coordenada (si escribes la coordenada con ! al final sera un disparo en cruz, tienes 3 disparos) o escribe la palabra debug (puedes escribir cuantos para saber cuantos barcos te quedan) ");
            String entradaUsuario = teclado.nextLine();

            if (entradaUsuario.equalsIgnoreCase("DEBUG")) {
                System.out.println("MODO DEBUG:");
                mostrarTablero(tablero, true);
                continue;
            }

            boolean disparoValido = procesarDisparo(tablero, entradaUsuario);

            if (disparoValido) {
                intentos--;
                if (intentos<1){
                    System.out.println("Vaya... te has quedado sin intentos");
                }
                //Comprobamos si ha ganado o no
                if (haGanado(tablero)) {
                    System.out.println("ENHORABUENA HAS HUNDIDO TODA LA FLOTA");
                    mostrarTablero(tablero, true);
                    juegoTerminado = true;
                }
            }
            AVISO(tablero, entradaUsuario, true);
        }
    }

    public static char[][] crearTablero(int filas, int columnas) {
        char[][] tablero = new char[filas][columnas];

        for (int contFilas = 0; contFilas < filas; contFilas++) {
            for (int contColumnas = 0; contColumnas < columnas; contColumnas++) {
                tablero[contFilas][contColumnas] = '-';
            }
        }
        return tablero;
    }
    //Aqui estamos definiendo los barcos, con su tamaño y su letra correspondiente
    public static void colocarBarcos(char[][] tablero) {
        colocarBarcoAleatorio(tablero, 5, 'P',false); //Portaaviones
        colocarBarcoAleatorio(tablero, 4, 'Z',true); //Acorazado
        colocarBarcoAleatorio(tablero, 3, 'B',true); //Buque
        colocarBarcoAleatorio(tablero, 3, 'B',true); //Buque
        colocarBarcoAleatorio(tablero, 1, 'L',true); //Lancha
        colocarBarcoAleatorio(tablero, 1, 'L',true);//Lancha
    }

    public static void colocarBarcoAleatorio(char[][] tablero, int tamano, char letraBarco,boolean esHorizontal) {
        boolean colocarBarcoBien = false;
        int limFila = tablero.length;
        int limColumna = tablero[0].length;

        while (!colocarBarcoBien){
            //Generamos las filas y las columnas aleatoriamente
            int filaAleatoria = (int) (Math.random() * limFila);
            int columnaAleatoria = (int) (Math.random() * limColumna);

            //Miramos si la posicion es valida o no
            boolean posicionValida = true;

            if (esHorizontal){
                if (tamano + columnaAleatoria > limColumna){
                    posicionValida = false;
                }else {
                    for (int colocamientoColumna = 0; colocamientoColumna < tamano; colocamientoColumna++) {
                        //miramos si no hay ningun barco que nos obstaculice el paso y que todo sea agua
                        if (tablero[filaAleatoria][columnaAleatoria + colocamientoColumna] != '-') {
                            posicionValida = false;
                            break;
                        }
                    }
                }
            }else {
                if (tamano + filaAleatoria > limFila){
                    posicionValida = false;
                }else {
                    for (int colocamientoFila = 0; colocamientoFila < tamano ; colocamientoFila++) {
                        if (tablero[filaAleatoria + colocamientoFila][columnaAleatoria] != '-') {
                            posicionValida = false;
                            break;
                        }
                    }
                }
            }
            //despues de las pruebas, colocamos los barcos
            if (posicionValida) {
                if (esHorizontal) {
                    for (int desplazamiento = 0; desplazamiento < tamano; desplazamiento++) {
                        tablero[filaAleatoria][columnaAleatoria + desplazamiento] = letraBarco;
                    }
                } else {
                    for (int desplazamiento = 0; desplazamiento < tamano; desplazamiento++) {
                        tablero[filaAleatoria + desplazamiento][columnaAleatoria] = letraBarco;
                    }
                }
                //marcamos que es true para salir del bucle while
                colocarBarcoBien= true;
            }
        }
    }

    public static void mostrarTablero(char[][] tablero, boolean esModoDebug) {
        int totalFilas = tablero.length;
        int totalColumnas = tablero[0].length;

        //Los numeros
        System.out.print("  ");
        for (int columna = 0; columna < totalColumnas; columna++) {
            System.out.print(columna + " ");
        }
        System.out.println();

        for (int fila = 0; fila < totalFilas; fila++) {
            //Las letras
            char letraFila = (char) ('A' + fila);
            System.out.print(letraFila + " ");

            for (int columna = 0; columna < totalColumnas; columna++) {
                char contenidoCasilla = tablero[fila][columna];

                if (esModoDebug) {
                    //Mostramos todo
                    System.out.print(contenidoCasilla + " ");
                } else {
                    //Ocultamos
                    if (esBarco(contenidoCasilla)) {
                        System.out.print("- ");
                    } else {
                        System.out.print(contenidoCasilla + " "); //Mostramos solo A o X
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public static boolean esBarco(char tipoBarco) {
        return tipoBarco == 'P' || tipoBarco == 'Z' || tipoBarco == 'B' || tipoBarco == 'L';
    }

    public static boolean procesarDisparo(char[][] tablero, String coordenadaUsuario) {
        boolean disparoEnCruz = false;

        if (coordenadaUsuario.length() > 0) {
            String ultimoCaracter = coordenadaUsuario.substring(coordenadaUsuario.length() - 1);
            if (ultimoCaracter.equals("!")) {
                if (DISPARO_ESPECIAL > 0) {
                    disparoEnCruz = true;
                    //quitamos el ! para poder leer la coordenada bien
                    coordenadaUsuario = coordenadaUsuario.substring(0, coordenadaUsuario.length() - 1);
                } else {
                    System.out.println("No te quedan disparos especiales crack, disparo normal en curso...");
                }
            }
        }

        if (coordenadaUsuario.length() < 2) {
            System.out.println("Formato incorrecto");
            return false;
        }

        if (coordenadaUsuario.equalsIgnoreCase("cuantos")) {
            cuantosBarcosHay(tablero, coordenadaUsuario, true);
            return false;
        }

        String coordenadaMayus = coordenadaUsuario.toUpperCase();
        char letra = coordenadaMayus.charAt(0);

        if (letra < 'A' || letra > 'K') {
            System.out.println("Fila incorrecta");
            return false;
        }
        int fila = letra - 'A';

        String parteNumerica = coordenadaMayus.substring(1);
        if (parteNumerica.length() == 0) {
            System.out.println("Falta el numero");
            return false;
        }

        for (int contador = 0; contador < parteNumerica.length(); contador++) {
            char caracter = parteNumerica.charAt(contador);
            if (caracter < '0' || caracter > '9') {
                System.out.println("Numero mal introducido, introduce uno valido");
                return false;
            }
        }

        int columna = Integer.parseInt(parteNumerica);

        if (columna < 0 || columna >= COLUMNAS) {
            System.out.println("Columna fuera del rango!");
            return false;
        }

        if (disparoEnCruz) {
            DISPARO_ESPECIAL--;
            System.out.println("Disparo en cruz utilizado. Quedan " + DISPARO_ESPECIAL + " disparos en cruz disponibles");
            //generamos la cruz con posiciones, arriba, izquierda centro...
            int[] disparoFila = {0, -1, 1, 0, 0};
            int[] disparoColumna = {0, 0, 0, -1, 1};

            for (int contador = 0; contador < 5; contador++) {
                int numeroFila = fila + disparoFila[contador];
                int numeroColumna = columna + disparoColumna[contador];

                if (numeroFila >= 0 && numeroFila < FILAS && numeroColumna >= 0 && numeroColumna < COLUMNAS) {
                    gestionarDisparo(tablero, numeroFila, numeroColumna);
                }
            }
            return true;
        } else {
            return gestionarDisparo(tablero, fila, columna);
        }
    }
    public static boolean gestionarDisparo(char[][] tablero, int contFilas, int contColumnas) {
        if (tablero[contFilas][contColumnas] == 'A' || tablero[contFilas][contColumnas] == 'X') {
            return false;
        }
        if (tablero[contFilas][contColumnas] == '-') {
            System.out.println("Agua...");
            tablero[contFilas][contColumnas] = 'A';
        } else {
            char tipo = tablero[contFilas][contColumnas];
            tablero[contFilas][contColumnas] = 'X';
            TOTAL_CASILLAS_BARCOS--;
            if (comprobarSiHundido(tablero, tipo)) {
                System.out.println("Hundido el barco de tipo: " + tipo);
                BARCOS_TOTALES--;
            } else {
                System.out.println("Tocado..");
            }
        }
        return true;
    }
    public static boolean comprobarSiHundido(char[][] tablero, char tipoBarco) {
        for (int contFilas = 0; contFilas < tablero.length; contFilas++) {
            for (int contColumnas = 0; contColumnas < tablero[0].length; contColumnas++) {
                if (tablero[contFilas][contColumnas] == tipoBarco) {
                    return false; //Significa que aun quedan partes
                }
            }
        }
        return true; //Significan que ya no quedan partes
    }

    public static boolean haGanado(char[][] tablero) {
        for (int contFilas = 0; contFilas < tablero.length; contFilas++) {
            for (int contColumnas = 0; contColumnas < tablero[0].length; contColumnas++) {
                char contenidoCasilla = tablero[contFilas][contColumnas];
                if (esBarco(contenidoCasilla)) {
                    return false; // Si encontramos un barco, no ha ganado aún
                }
            }
        }
        return true; // Si terminamos los bucles sin encontrar barcos, ha ganado
    }

    public static void cuantosBarcosHay(char[][] tablero, String coordenadaUsuario, boolean esModoCuantos){
        esModoCuantos = false;
        if (coordenadaUsuario.equalsIgnoreCase("cuantos")){
            esModoCuantos = true;
            int numeroBarcos = BARCOS_TOTALES;
            System.out.println("Quedan: " + numeroBarcos + " barcos");
        }
    }
    public static boolean AVISO (char[][] tablero, String coordenadaUsuario, boolean mitadPosiciones){

        if (TOTAL_CASILLAS_BARCOS == CASILLAS_INICIALES / 2) {
            System.out.println("¡ACABAS DE TOCAR " + (TOTAL_CASILLAS_BARCOS) + " DE " + CASILLAS_INICIALES + "!");
            return true;
        }
        return false;
    }

}