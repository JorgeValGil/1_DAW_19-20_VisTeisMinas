/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.Cell;
import model.Game;
import java.util.Scanner;

/**
 * Clase que implementa a interfaz do xogo en modo texto
 *
 * @author Jorge Val Gil
 */
public class VisTeisMinasMenu {

    /**
     * Número de filas por defecto, 6
     */
    public static final int RAWS = 6;

    /**
     * Número de columnas por defecto, 6
     */
    public static final int COLUMNS = 6;

    /**
     * Número de minas por defecto, 6
     */
    public static final int MINES = 8;

    /**
     * Mostra o estado do panel do xogo
     *
     * @param game xogo
     */
    private void showPanel(Game game) {
        System.out.println();
        System.out.println("--- VisTeisMinas ---");
        System.out.println("--- Estado do xogo ---");
        //colocamos as numeracions das columnas
        System.out.print("   ");
        for (int columnnumber = 0; columnnumber < 6; columnnumber++) {
            System.out.print(columnnumber + " ");
        }
        System.out.println();
        //recorremos as filas
        for (int i = 0; i < 6; i++) {
            //colocamos as separacións das filas
            System.out.println("  -------------");
            //numeramos as filas
            System.out.print(i + " |");
            //recorremos as columnas, e comprobamos a estado de cada cela
            for (int j = 0; j < 6; j++) {
                Cell cell = game.getCell(i, j);
                String cellState = "";
                switch (cell.getState()) {
                    //se está tapada, colocaremos un espazo en branco
                    case 1:
                        cellState = " ";
                        break;
                    //se está marcada, colocaremos unha exclamación
                    case 2:
                        cellState = "!";
                        break;
                    default:
                        //se está minada, colocaremos un asterisco
                        if (cell.isMined()) {
                            cellState = "*";
                            break;
                        }
                        //se está destapada, colocaremos a cantidade de bombas adxacentes
                        cellState = Integer.toString(game.getAdjacentMines(cell));
                        break;
                }
                //logo do que coloquemos, introduciremos unha barra vertical para separar as celas visualmente
                System.out.print(cellState + "|");
            }
            //o rematar unha fila damos salto de liña
            System.out.println();
        }
        //colocamos por último outra separación
        System.out.println("  -------------");
    }

    /**
     * Método que inicia o xogo
     */
    public void startNewGame() {
        //boolean para xogar outra partida ou sair
        boolean playOtherGame = false;
        Scanner sc = new Scanner(System.in);
        while (!playOtherGame) {
            //boolean de sair, victoria ou derrota
            boolean exit = false;
            //creamos o xogo
            Game game = new Game(RAWS, COLUMNS, MINES);
            //bucle principal do xogo
            while (!exit) {
                Cell cell;
                //mostramos o panel
                showPanel(game);
                //mostramoslle as opcions ao usuario
                System.out.println("¿Que desexas facer? s para Sair, m para Marcar unha cela, d para Desmarcar unha cela, a para abrir unha cela");
                //switch das opcions
                char options = sc.nextLine().charAt(0);
                switch (options) {
                    //s, cambiamos o valor de exit, o que nos fara saír deste bucle, e ir ao bucle de xogar outra partida ou non
                    case 's':
                        exit = true;
                        break;
                    //caso de abrir, marcar ou desmarcar
                    case 'a':
                    case 'm':
                    case 'd':
                        int raw = 0;
                        int column = 0;
                        //boolean que controla que os datos da fila e da columna sexan certos
                        boolean valid = false;
                        while (!valid) {
                            System.out.println("Introduce a fila:");
                            raw = sc.nextInt();
                            System.out.println("Introduce a columna:");
                            column = sc.nextInt();
                            sc.nextLine();
                            //se os datos son correctos e estan nos rangos, cambiamos o valor de valid e saimos do bucle
                            if (raw >= 0 && raw < RAWS && column >= 0 && column < COLUMNS) {
                                valid = true;
                            } else {
                                //se os datos non son correcto informamos ao usuario
                                System.out.println("Os datos non son validos! O valor da fila ten que ser entre 0 e " + RAWS + ", o valor da columna ten que ser entre 0 e " + COLUMNS);
                            }
                        }
                        //creamos a celda
                        cell = game.getCell(raw, column);
                        //se a celda non está destapada
                        if (cell.getState() != Cell.uncovered) {
                            switch (options) {
                                //se pulsaramos m, cambiamos o estado a marcada
                                case 'm':
                                    cell.setState(Cell.checked);
                                    break;
                                case 'd':
                                    //se pulsaramos d, quitamos a marca, volvemos a pola tapada
                                    cell.setState(Cell.covered);
                                    break;
                                //se pulsamos abrir
                                case 'a':
                                    //abrimos a cela
                                    game.openCell(cell);
                                    //se está minada o xogo remata, o usuario perde
                                    if (cell.isMined()) {
                                        game.openAllMines();
                                        showPanel(game);
                                        System.out.println("Perdiches!! O xogo rematou");
                                        exit = true;
                                        break;
                                    }
                                    //comprobamos se queda algunha cela boa para abrir, para saber se o usuario gañou
                                    if (!game.checkCellsToOpen()) {
                                        showPanel(game);
                                        System.out.println("Parabéns! Destapaches todas as celas");
                                        exit = true;
                                        break;
                                    }
                            }
                        } else {
                            //se a cela xa se encontra destapada, mostramos unha mensaxe por pantalla
                            System.out.println("A cela xa se encontra destapada!");
                        }
                    //se non se introduce s, m, d ou a, mostramos mensaxe por pantalla
                    default:
                        System.out.println("O caracter introducido non é correcto!Debes introducir s, m, d ou a.");
                        break;
                }
            }
            //boolean para controlar que o usuario introduza s ou n
            boolean switchNewGame = false;
            while (!switchNewGame) {
                System.out.println("Queres xogar de novo?(s/n)");
                char newgame = sc.nextLine().charAt(0);
                switch (newgame) {
                    case 's':
                        switchNewGame = true;
                        break;
                    case 'n':
                        switchNewGame = true;
                        //o introducir n, cambiamos o valor de playothergame e o programa ramata
                        playOtherGame = true;
                        break;
                    default:
                        System.out.println("Introduce s para xogar de novo ou n para sair");
                        break;
                }
            }

        }
    }

}
