/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 * Clase encargada do xogo
 *
 * @author Jorge Val Gil
 */
public class Game {

    //matriz de obxetos cell
    private Cell[][] cells;
    //filas da matriz
    private int raws;
    //columnas da matriz
    private int columns;

    /**
     * Get de raws
     *
     * @return devolve raws
     */
    public int getRaws() {
        return raws;
    }

    /**
     * Get de Columns
     *
     * @return devolve columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Método que devolve a fila e unha columna dunha cela
     *
     * @param raw fila dunha cela
     * @param column columna dunha cela
     * @return Devolve a fila e unha columna dunha cela
     */
    public Cell getCell(int raw, int column) {
        return cells[raw][column];
    }

    /*
    Méotodo que obtén a lista de celas adxacentes dunha cela
     */
    private ArrayList<Cell> getAdjacentCells(Cell cell) {
        //arraylist das celas adxacentes
        ArrayList<Cell> cellslist = new ArrayList<>();
        //fila superior, fila inferior, columna esquerda, columna dereita.
        int rawup, rawdown, columnleft, columnright;

        //se a fila é 0, a fila superior pasaría a ser 0. Senón valerá a fila actual menos 1
        if (cell.getRaw() == 0) {
            rawup = 0;
        } else {
            rawup = cell.getRaw() - 1;
        }

        //se a fila é igual ao número de filas menos 1, a fila inferior pasaría a ter o valor actual. Senón valerá a fila actual máis 1
        if (cell.getRaw() == raws - 1) {
            rawdown = cell.getRaw();
        } else {
            rawdown = cell.getRaw() + 1;
        }

        //se a columna é 0, a columna da esquerda pasaría a ser 0. Senón valerá a columna actual menos 1
        if (cell.getColumn() == 0) {
            columnleft = 0;
        } else {
            columnleft = cell.getColumn() - 1;
        }

        //se a columna é igual ao número de columnas menos 1, a columna da dereita pasaría a ter o valor actual. Senón valerá a columna actual máis 1
        if (cell.getColumn() == columns - 1) {
            columnright = cell.getColumn();
        } else {
            columnright = cell.getColumn() + 1;
        }

        //recorremos as celas que están neses rangos de posición e gardámolos no arraylist creado anteriormente
        for (int i = rawup; i <= rawdown; i++) {
            for (int j = columnleft; j <= columnright; j++) {
                cellslist.add(this.cells[i][j]);
            }
        }
        //devolvemos dito arraylist
        return cellslist;
    }

    /**
     * Obtén o número de minas que hay nas celas adxacentes dunha cela
     *
     * @param cell cela
     * @return devolve o número de minas que hay nas celas adxacentes dunha cela
     */
    public int getAdjacentMines(Cell cell) {
        int mines = 0;
        //recorre o arraylist do método getAdjacentCells
        for (Cell celllist : getAdjacentCells(cell)) {
            //se unha cela ten mina, incrementse o número de minas
            if (celllist.isMined()) {
                mines++;
            }
        }
        //unha vez se saia do bucle, devólvese o número de minas
        return mines;
    }

    /**
     * Destapa unha cela. Se non ten minas adxacentes volverá tamén as
     * destapará.
     *
     * @param cell
     */
    public void openCell(Cell cell) {
        //destapa unha cela
        cell.setState(Cell.uncovered);
        //se non ten minas adxacentes e non están destapadas, pois tamén as destapará
        if (getAdjacentMines(cell) == 0) {
            for (Cell celllist : getAdjacentCells(cell)) {
                if (celllist.getState() != Cell.uncovered) {
                    //en cada cela que se vaia destapando, chamarase a este mesmo método para faga as mesmas comprobacións e siga destapando se é posible
                    openCell(celllist);
                }
            }
        }
    }

    /**
     * Destapa tods as celas que teñan mina. Usarase cando o xogador perda
     */
    public void openAllMines() {
        //recorremos todas as celas
        for (int i = 0; i < raws; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cellmined = getCell(i, j);
                //as celas que teñan mina, destápanse
                if (cellmined.isMined()) {
                    cellmined.setState(Cell.uncovered);
                }
            }
        }
    }

    /**
     * Comproba se quedan celas sen minas por destapar. Úsase saber se o xogador
     * gañou a partida
     *
     * @return devolve true se aínda quedan celas boas por abrir e false se non
     * quedan
     */
    public boolean checkCellsToOpen() {
        //iniciamos un boolean a false
        boolean goodcell = false;
        //recorremos todas as celas
        for (int i = 0; i < raws; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cellmined = getCell(i, j);
                //se existe algunha cela sen mina e sen destapar, devolvemos true
                if (!cellmined.isMined() && cellmined.getState() != Cell.uncovered) {
                    goodcell = true;
                }
            }
        }
        return goodcell;
    }

    /**
     * Coloca as minas no xogo
     */
    private void fillMines(int mines) {
        //contador de minas
        int minesingame = 0;
        //mentres as minas que se van colocando sexan menos que as minas que se vaian a colocar
        while (minesingame < mines) {
            //fila aleatoria
            int cellRaw = new java.util.Random().nextInt(raws);
            //columna aleatoria
            int cellColumn = new java.util.Random().nextInt(columns);
            //creamos unha cela con ese valores
            Cell randomcell = this.cells[cellRaw][cellColumn];
            //se non está minada, marcámola como minada e sumámola no contador
            if (!randomcell.isMined()) {
                randomcell.setMined(true);
                minesingame++;
            }
        }
    }

    /**
     * Crea unha nova partida
     *
     * @param raws numero de filas
     * @param columns numero de columnas
     * @param mines numero de minas
     */
    public Game(int raws, int columns, int mines) {
        //dámoslle os valores a raws e columns
        this.columns = columns;
        this.raws = raws;
        //inicializamos a matriz
        this.cells = new Cell[raws][columns];
        for (int i = 0; i < raws; i++) {
            for (int j = 0; j < columns; j++) {
                //creamos cada unha das celas, sen minar e tapadas, e asignamoslle a posición
                this.cells[i][j] = new Cell(false, Cell.covered, i, j);
            }
        }
        //Colocamos as minas
        fillMines(mines);
    }
}
