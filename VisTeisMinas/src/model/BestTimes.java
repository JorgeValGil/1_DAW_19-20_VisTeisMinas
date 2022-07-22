/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 * Clase que garda e controla as mellores puntuacións
 *
 * @author Jorge Val Gil
 */
public class BestTimes implements Serializable, Comparable<BestTimes> {

    private long seconds;
    private String name;

    /**
     * Get de seconds
     *
     * @return seconds
     */
    public long getSeconds() {
        return seconds;
    }

    /**
     * Set de seconds
     *
     * @param seconds segundos
     */
    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    /**
     * Get de name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set de nome
     *
     * @param name nome
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Contructor valeiro
     */
    public BestTimes() {
    }

    /**
     * Constructor
     *
     * @param seconds segundos
     * @param name nome
     */
    public BestTimes(long seconds, String name) {
        this.seconds = seconds;
        this.name = name;
    }

    /**
     * Método que usaremos para ordenador o arraylist polo tempo de menor a
     * maior
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(BestTimes o) {
        if (o.getSeconds() < seconds) {
            return 1;
        } else if (o.getSeconds() > seconds) {
            return -1;
        } else {
            return 0;
        }
    }

}
