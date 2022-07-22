/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.VisTeisMinasMenu;
import view.VisTeisMinasWindow;

/**
 * Clase principal
 *
 * @author Jorge Val Gil
 */
public class VisTeisMinas {

    /**
     * Método main
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //se pasan como parámetro text
        if (args.length == 1 && args[0].equals("text")) {
            //creamos un obxeto VisTeisMinasMenu para xogar en modo texto
            VisTeisMinasMenu visteisminas1 = new VisTeisMinasMenu();
            //chamámos a startNewGame
            visteisminas1.startNewGame();
        } else {
            //eliximos o lookandfeel
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(VisTeisMinasWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            //se non lle pasan "text" como parametro xogaremos en modo gráfico
            VisTeisMinasWindow game = new VisTeisMinasWindow();
            game.startNewGame();
        }

    }

}
