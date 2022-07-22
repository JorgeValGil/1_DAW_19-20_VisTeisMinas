/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.BestTimes;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase que controla e garda os mellores tempos
 *
 * @author Jorge Val Gil
 */
public class BestTimesFile {

    private ArrayList<BestTimes> bestTimes = new ArrayList<>();

    public ArrayList<BestTimes> getBestTimes() {
        return bestTimes;
    }

    /**
     * Método que recupera os mellores tempos dun ficheiro e os garda nun
     * arraylist
     *
     * @throws FileNotFoundException excepción
     * @throws IOException excepción
     * @throws ClassNotFoundException excepción
     */
    public void recuperarBestTimes() throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = null;
        try {
            // Abrimos o fluxo de datos sobre o ficheiro
            in = new ObjectInputStream(new FileInputStream("src/besttimes.TXT"));
            // Lemos a colección de clientes
            bestTimes = (ArrayList<BestTimes>) in.readObject();

        } catch (IOException e) {
        } finally {
            // En calquera caso, producirase ou non unha excepción, pechamos o
            // fluxo de entrada se está aberto
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * Método que compara unha tempo cos tempos do arraylist devolve true se o
     * tempo obtido é menor ca algún dos tempos
     *
     * @param time tempo
     * @return devole true se o tempo e mellor ca algún, false se e peor
     */
    public boolean compareBestTimes(long time) {
        boolean flag = false;
        if (bestTimes.size() < 10) {
            flag = true;
        } else {
            for (BestTimes i : bestTimes) {
                if (i.getSeconds() > time) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * Borra o tempo máis alto e añade o novo tempo no arraylist
     *
     * @param mybest tempo
     */
    public void deleteandadd(BestTimes mybest) {

        long maior = Long.MIN_VALUE;
        int index = -1;
        if (bestTimes.size() < 10) {
            bestTimes.add(mybest);
        } else {
            int indice = 0;
            for (BestTimes i : bestTimes) {
                if (i.getSeconds() > maior) {
                    maior = i.getSeconds();
                    index = indice;
                }
                indice++;
            }
            bestTimes.remove(index);
            bestTimes.add(mybest);
        }

        gardarBestTimes();

    }

    /**
     * Garda o arraylist no ficheiro
     */
    public void gardarBestTimes() {
        // Declaramos o fluxo de datos de saída
        ObjectOutputStream out = null;
        try {
            // Abrimos o fluxo de datos sobre o ficheiro
            out = new ObjectOutputStream(new FileOutputStream("src/besttimes.TXT"));
            // Escribimos na saída os clientes
            out.writeObject(bestTimes);
        } catch (IOException ex) {
        } finally {
            // En calquera caso, producirase ou non unha excepción, pechamos o
            // fluxo de saída se está aberto
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * Método que ordena os tempos e os mostra nun JTable
     *
     * @return devolve os mellores tempos ordeados
     */
    public ArrayList<BestTimes> showBestTimes() {
        Collections.sort(bestTimes);
        return bestTimes;
    }

}
