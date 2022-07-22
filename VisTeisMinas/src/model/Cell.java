/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Clase que implementa cada cela
 *
 * @author Jorge Val Gil
 */
public class Cell {

    // indica se a cela contén unha mina ou non
    private boolean mined;

    //indica o estado da cela
    private int state;
    /**
     * Estado: tapada
     */
    public static final int covered = 1;
    /**
     * Estado: marcada
     */
    public static final int checked = 2;
    /**
     * Estado: destapada
     */
    public static final int uncovered = 3;

    //número da fila na que está colocada a cela
    private int raw;
    //número da columna na que está colocada a cela
    private int column;

    /**
     * Get de Mined
     *
     * @return devolve mined
     */
    public boolean isMined() {
        return mined;
    }

    /**
     * Set de Mined
     *
     * @param mined minada
     */
    public void setMined(boolean mined) {
        this.mined = mined;
    }

    /**
     * Get de State
     *
     * @return devolve state
     */
    public int getState() {
        return state;
    }

    /**
     * Set de state
     *
     * @param state estado
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Get de Raw
     *
     * @return devolve raw
     */
    public int getRaw() {
        return raw;
    }

    /**
     * Set de raw
     *
     * @param raw fila
     */
    public void setRaw(int raw) {
        this.raw = raw;
    }

    /**
     * Get de column
     *
     * @return devove Column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Set de column
     *
     * @param column columna
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Constructor
     *
     * @param mined minada
     * @param state estado
     * @param raw fila
     * @param column columna
     */
    public Cell(boolean mined, int state, int raw, int column) {
        this.mined = mined;
        this.state = state;
        this.raw = raw;
        this.column = column;
    }

}
