/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.BestTimesFile;
import model.BestTimes;
import model.Game;
import model.Cell;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que implementa a versión gráfica
 *
 * @author Jorge Val Gil
 */
public class VisTeisMinasWindow extends javax.swing.JFrame {
//obxeto de clase bestimes

    BestTimesFile besttimes = new BestTimesFile();

    //atributos long para obter os tempos
    private long startTime = 0;
    private long stopTime = 0;

    //obxeto xogo que mantén a partida
    private Game game;

    //array bidimensional, almacena as referencias aos obxectos toggleButton
    private JToggleButton[][] cellButtons;

    //os cara ou cruz son diferentes ao resto de modos
    boolean caraoucruz;

    /**
     * Creates new form VisTeisMinasWindow
     */
    public VisTeisMinasWindow() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Método que mostra ao usuario un diálogo para seleccionar o nivel de xogo
     */
    public void startNewGame() {
        //recuperamos as os mellores tempos ao arraylist da clase bestimes
        try {
            besttimes.recuperarBestTimes();
        } catch (IOException ex) {
            Logger.getLogger(VisTeisMinasWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VisTeisMinasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        Object[] buttons = {"Cara ou cruz", "Modo 50-50", "Alto", "Medio", "Baixo", "Principiante"};
        //JOptionPane para seleccionar a dificultado do xogo
        int gamemode = JOptionPane.showOptionDialog(this, "Selecciona o nivel de dificultade", "Nivel da partida", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, null);
        switch (gamemode) {
            case 0:
                caraoucruz = true;
                game = new Game(1, 2, 1);
                break;
            case 1:
                caraoucruz = false;
                game = new Game(10, 10, 50);
                break;
            case 2:
                caraoucruz = false;
                game = new Game(10, 10, 40);
                break;
            case 3:
                caraoucruz = false;
                game = new Game(8, 8, 20);
                break;
            case 4:
                caraoucruz = false;
                game = new Game(6, 6, 8);
                break;
            default:
                caraoucruz = false;
                game = new Game(4, 4, 2);
        }
        //teremos que limpar o panel antes de colocar os botóns da nova partida
        jPanelPrincipal.removeAll();
        //GridLayout de raws*colums
        jPanelPrincipal.setLayout(new GridLayout(game.getRaws(), game.getColumns()));
        //dámoslle os valores ao array bidimensional
        cellButtons = new JToggleButton[game.getRaws()][game.getColumns()];
        for (int i = 0; i < game.getRaws(); i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                //por cada cela do xogo, creamos un botón ao que lle asignamos valores e gardámolo no jPanel
                JToggleButton button = new JToggleButton();
                button.setMinimumSize(new Dimension(45, 45));
                //creo un listener para capturar os eventos de clicar e clicar botón dereito sobre os botóns
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        VisTeisMinasWindow.this.cellButtonActionPerformed(e);
                    }
                });
                button.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        VisTeisMinasWindow.this.cellButtonMouseClicked(e);
                    }
                });
                button.setName("cell-" + i + "-" + j);
                jPanelPrincipal.add(button);
                cellButtons[i][j] = button;
            }
        }
        //chamamos a pack pra que se vexan todos os obxetos na ventana
        this.pack();
        //gardamos o tempo de inicio de xogo
        this.startTime = System.currentTimeMillis();

    }

    /**
     * Método que actualiza os JToogleButtons das celas que están destapadas.
     * Mostra unha mina ou o número de minas adxacentes.
     */
    private void updatePanel() {
        for (int i = 0; i < game.getRaws(); i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                //por cada cela
                Cell cell1 = game.getCell(i, j);
                JToggleButton button = cellButtons[i][j];
                if (cell1.getState() == Cell.uncovered) {
                    //se está destapada, deshabilitamos o botón
                    button.setEnabled(false);
                    //se a cela está minada poñémoslle de icono imaxe da bomba
                    if (cell1.isMined()) {
                        button.setIcon(new ImageIcon(this.getClass().getResource("bomb.png")));
                        if (caraoucruz) {
                            button.setIcon(new ImageIcon(this.getClass().getResource("cruz.png")));
                        }
                    } else {
                        //se non está minada poñemoslle a cantidde de bombas adxacentes
                        button.setText(String.valueOf(game.getAdjacentMines(cell1)));
                        if (caraoucruz) {
                            button.setIcon(new ImageIcon(this.getClass().getResource("cara.png")));
                        }
                    }
                }
            }
        }
    }

    /**
     * Método que permite xogar outra nova partida ao finalizar a partida actual
     *
     * @param message mensaxe que recibe de openCell(), o rematar a partida.
     * Mensaxe de gañar ou perder
     */
    private void finishGame(String message) {
        //JOptionPane para xogar unha nova partida
        int newGame = JOptionPane.showConfirmDialog(this, message, "Xogar unha nova partida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (newGame == 0) {
            //se seleccionamos YES, chamamos a startNewGame
            startNewGame();
        } else {
            //se non, facemos dispose
            this.dispose();
        }
    }

    /**
     * Método que abre unha cela, controlamos se está minada e controlamos se
     * seguen quedando boas celas
     *
     * @param cell cela para abrir
     */
    private void openCell(Cell cell) {
        //se a cela está minada
        if (cell.isMined()) {
            //chamamos a openallmines, actualizamos o panel e enviamos unha mensaxe a finishGame indicando que o usuario perdeu
            game.openAllMines();
            updatePanel();
            //obtemos o momento no que perdemos
            this.stopTime = System.currentTimeMillis();

            if (!caraoucruz) {
                //pasamos de milisegundos a segundos 
                long time = (stopTime - startTime) / 1000;
                //enviamos unha mensaxe que incluía a tempo a finishgame
                finishGame("Boom! Perdeche! Tempo: " + time + " segundos.");
            } else {
                finishGame("Elixiche cruz! Perdeches!");
            }

        } else {
            //se non está minada, chamamos a openCell e actualizamos o panel
            game.openCell(cell);
            updatePanel();
            //se checkCellsToOpen nos devolve false, o usuario gañaría a partida
            if (!game.checkCellsToOpen()) {
                //obtemos o momento no que gañamos
                this.stopTime = System.currentTimeMillis();
                if (!caraoucruz) {
                    //pasamos de milisegundos a segundos
                    long time = (stopTime - startTime) / 1000;
                    //comparamos os tempos
                    if (!caraoucruz && besttimes.compareBestTimes(time)) {
                        //se estamos dentro, pois introducimos o noso nome
                        String nome = JOptionPane.showInputDialog(this, "Eres un dos máis rápidos! Introduce o teu nome: ", "Mellores puntuacións", JOptionPane.INFORMATION_MESSAGE);
                        BestTimes myBestTime = new BestTimes(time, nome);
                        //borramos a peor e gardamos o noso tempo
                        besttimes.deleteandadd(myBestTime);
                    }
                    //enviamos unha mensaxe que incluía a tempo a finishgame
                    finishGame("Ganaches! Tempo: " + time + " segundos.");
                } else {
                    finishGame("Elixiche cara! Ganaches!");
                }

            }
        }
    }

    /**
     * Captura o clic sobre un ToggleButtton
     *
     * @param evt
     */
    private void cellButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JToggleButton button = (JToggleButton) evt.getSource();
        //con String Tokenizer, eliminamos os guións do nome do botón. Quedaríanos cell, valor de i, valor de j
        StringTokenizer st = new StringTokenizer(button.getName(), "-");
        //saltamonos a cell
        st.nextToken();
        //gardamos o valor de i nun int
        int i = Integer.valueOf(st.nextToken());
        //gardamos o valor de j nun int
        int j = Integer.valueOf(st.nextToken());
        //xa podemos obter a cela
        Cell cell1 = game.getCell(i, j);
        //abrimos a cela
        openCell(cell1);
    }

    /**
     * Captura o clic dereito sobre un ToggleButtton
     *
     * @param evt
     */
    private void cellButtonMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getButton() != 1) {
            JToggleButton button = (JToggleButton) evt.getSource();
            //con String Tokenizer, eliminamos os guións do nome do botón. Quedaríanos cell, valor de i, valor de j
            StringTokenizer st = new StringTokenizer(button.getName(), "-");
            //saltamonos a cell
            st.nextToken();
            //gardamos o valor de i nun int
            int i = Integer.valueOf(st.nextToken());
            //gardamos o valor de j nun int
            int j = Integer.valueOf(st.nextToken());
            //xa podemos obter a cela
            Cell cell1 = game.getCell(i, j);
            switch (cell1.getState()) {
                case 1:
                    //se a cela está tapada, poñemoslle ao botón unha bandeira de icono e cambiamos o estado da cela a marcada
                    button.setIcon(new ImageIcon(getClass().getResource("red_flag.png")));
                    cell1.setState(Cell.checked);
                    break;
                case 2:
                    //se a cela está marcada, quitamos o icono da bndeira e cambiamos o estado da cela a tapada        
                    button.setIcon(null);
                    cell1.setState(Cell.covered);
                    break;
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanelPrincipal = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemNewGame = new javax.swing.JMenuItem();
        jMenuItemBestTime = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Posto", "Nome", "Tempo"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setText("Máis Rápidos");

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(117, Short.MAX_VALUE))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VisTeisMinas");
        setMinimumSize(new java.awt.Dimension(800, 600));

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        jMenu1.setText("Ficheiro");

        jMenuItemNewGame.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemNewGame.setText("Nova Partida");
        jMenuItemNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewGameActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemNewGame);

        jMenuItemBestTime.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemBestTime.setText("Mellores Tempo");
        jMenuItemBestTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBestTimeActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemBestTime);

        jMenuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemExit.setText("Sair");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemExit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewGameActionPerformed
        //Botón nova partida da barra do menu, chama a startNewGame
        startNewGame();
    }//GEN-LAST:event_jMenuItemNewGameActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        //Botón sair da barra de menu, fai dispose
        dispose();
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemBestTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBestTimeActionPerformed
        //botón do menu que mostra nunha tabla dun jdialog, os mellores tempos
        //obtemos o modelo do jtable
        DefaultTableModel modeltempo = (DefaultTableModel) this.jTable1.getModel();
        modeltempo.setRowCount(0);
        //variable posto
        int posto = 1;
        try {
            //recorremos os mellores tempos xa ordeados
            besttimes.recuperarBestTimes();
        } catch (IOException ex) {
            Logger.getLogger(VisTeisMinasWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VisTeisMinasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int j = 0; j < besttimes.showBestTimes().size(); j++) {
            Object[] messageTable = new Object[3];
            //na primeira columna colocamos o posto
            messageTable[0] = posto;
            //na segunda columna colocamos o nome
            messageTable[1] = besttimes.showBestTimes().get(j).getName();
            //na terceira columna colocamos o tempo
            messageTable[2] = besttimes.showBestTimes().get(j).getSeconds();
            modeltempo.addRow(messageTable);
            //incrementamos en 1 a posto
            posto++;
        }
        //colocamos o jdialog, facémolo visible e facemos pack
        jDialog1.setLocationRelativeTo(null);
        jDialog1.setVisible(true);
        jDialog1.pack();
    }//GEN-LAST:event_jMenuItemBestTimeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VisTeisMinasWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VisTeisMinasWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VisTeisMinasWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VisTeisMinasWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VisTeisMinasWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemBestTime;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemNewGame;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
