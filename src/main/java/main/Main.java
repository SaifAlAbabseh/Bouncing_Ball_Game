package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


class BounceBallGame extends JFrame {

    // Ball Props
    private int ballX = 300;
    private int ballY = 10;
    private boolean moveUp = false;
    private boolean moveLeft = false;
    private final int ballWidth = 20;
    private final int ballHeight = 20;

    // Line Props
    private int lineX = 225;
    private final int lineY = 402; // We made it final, as It shouldn't move up-down, It's only left-right
    private final int lineWidth = 150;
    private final int lineHeight = 5;

    // Ball Speed
    private final int ballSpeed = 4;

    // Line Speed
    private final int lineSpeed = 10;

    private final GamePanel gamePanel;
    private Listener listener;
    private Timer ballAnimationTimer;

    public BounceBallGame() {
        gamePanel = new GamePanel();
        add(gamePanel);
        initFrame();
        int result = JOptionPane.showOptionDialog(this, "Start the game?", "Welcome", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"START"}, null);
        if(result == 0) startGame();
        else System.exit(0);
    }

    private void initFrame() {
        setTitle("Ball Game");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private class GamePanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            setBackground(Color.WHITE);
            g2d.setPaint(Color.RED);
            g2d.fillOval(ballX, ballY, ballWidth, ballHeight);
            g2d.setPaint(Color.BLACK);
            g2d.fillRect(lineX, lineY, lineWidth, lineHeight);
        }

        private void animateBall() {
            ballAnimationTimer = new Timer(10, e -> {
                // Ball Info
                int ballLeftSidePos = ballX;
                int ballRightSidePos = ballX + ballWidth;
                int ballTopSidePos = ballY;
                int ballBottomSidePos = ballY + ballHeight;
                int ballMiddlePos = ballX + ballWidth / 2;

                // Line Info
                int lineLeftSidePos = lineX;
                int lineRightSidePos = lineX + lineWidth;
                int lineTopSidePos = lineY;

                if(ballTopSidePos > getHeight()) endGame();
                else {
                    if(ballRightSidePos > getWidth()) moveLeft = true;
                    if(ballLeftSidePos < 0) moveLeft = false;
                    if(moveLeft) ballX -= ballSpeed;
                    else ballX += ballSpeed;
                    if((ballBottomSidePos == lineTopSidePos) && (ballMiddlePos > lineLeftSidePos && ballMiddlePos < lineRightSidePos)) moveUp = true;
                    if(ballTopSidePos < 0) moveUp = false;
                    if(moveUp) ballY -= ballSpeed;
                    else ballY += ballSpeed;
                }
                repaint();
            });
            ballAnimationTimer.start();
        }
    }

    private void endGame() {
        ballAnimationTimer.stop();
        this.removeKeyListener(listener);
        int result = JOptionPane.showOptionDialog(this, "You've lost, wanna restart?", "Game Ended", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"RESTART"}, null);
        if(result == 0) startGame();
        else System.exit(0);
    }

    private void startGame() {
        // Reset Variables
        ballX = 300;
        ballY = 10;
        moveUp = false;
        moveLeft = false;
        lineX = 225;
        gamePanel.repaint();
        gamePanel.animateBall();
        listener = new Listener();
        this.addKeyListener(listener);
    }

    private class Listener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                if(lineX > 0) lineX -= lineSpeed;
            }
            else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if(lineX + lineWidth < gamePanel.getWidth()) lineX += lineSpeed;
            }
            gamePanel.repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {}
    }
}


public class Main {

    public static void main(String[] args) {
        new BounceBallGame();
    }
}
