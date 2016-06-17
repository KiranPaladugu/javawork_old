package com.pack.brick.samp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class BrickGame extends JFrame implements WindowStateListener, WindowFocusListener, WindowListener {
    private JPanel panel;
    private JPanel result;
    private JPanel bricks[][];
    private JPanel bat;
    private JButton lable;
    private int brickX = 2;
    private int brickY = 2;
    private int brickW = 40;
    private int brickH = 20;
    private int batWidth = 70;
    private int batHeight = 10;
    private int ballWidth = 20;
    private int ballHeight = 20;
    private int initX = 25;
    private int initY = 25;
    private int X = 0;
    private int Y = 0;
    
    private JLabel pauseLable;
    private JPanel glassPane;
    private boolean paused, left;
    private boolean gameOver, levelUp, up;
    private volatile int visibleCount;
    private int lives = 5, scoreTotal = 0;
    private RoundPanel ball;
    private Score scorer;
    private BallMotion motion;
    
    private JLabel lbl_score, lbl_lives, score, lifes;
    private JLabel remain_lbl, remain;
    

    public BrickGame() {
        initTheme();
        initView();
        initBricks();
        initBat();
        initBall();
        initPauseAction();
        initFrame();
        initBallMotion();
    }

    private void initBallMotion() {
        motion = new BallMotion();
    }

    private void initTheme() {
        String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(windowsTheme);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFrame() {
        int wid = 690;
        int het = 435;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - wid) / 2;
        int y = (dim.height - het) / 2;
        setBounds(x, y, wid, het);
        setSize(wid, het);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(690, 435);
        this.setVisible(true);
    }

    private void initView() {
        scorer = new Score();
        MouseController mouse = new MouseController();
        panel = new JPanel(true);
        panel.setLayout(null);
        panel.setBounds(1, 1, 480, 400);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.setLayout(null);
        this.add(panel);
        result = new JPanel();
        initResult();
        result.setBounds(482, 1, 200, 400);
        result.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.add(result);
        this.addWindowFocusListener(this);
        this.addWindowListener(this);
        this.addWindowStateListener(this);
        this.setResizable(false);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);

    }

    class MouseController extends MouseAdapter {
        int previousX;
        int previousY;

        public void mouseMoved(MouseEvent e) {
            moveBatWithMouse(e.getPoint().x);
        }

    }

    private void initResult() {
        lbl_score = new JLabel("Score:");
        score = new JLabel("" + scoreTotal);
        lbl_lives = new JLabel("Lives");
        lifes = new JLabel("" + lives);
        remain_lbl = new JLabel("RemainingBricks :");
        remain = new JLabel("" + visibleCount);
        result.setLayout(new GridLayout(3, 2));
        result.add(lbl_score);
        result.add(score);
        result.add(lbl_lives);
        result.add(lifes);
        result.add(remain_lbl);
        result.add(remain);
    }

    private void initPauseAction() {
        pauseLable = new JLabel("Game is Paused");
        pauseLable.setFont(new Font("Arial", Font.BOLD, 25));
        pauseLable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        pauseLable.setForeground(Color.BLUE);
        glassPane = (JPanel) this.getGlassPane();
        glassPane.setLayout(null);
        pauseLable.setVisible(true);
        pauseLable.setBounds(150, 125, 200, 50);
        glassPane.add(pauseLable, BorderLayout.CENTER);
    }

    public void initBricks() {
        bricks = new JPanel[10][10];
        X = initX;
        for (int x = 0; x < 10; x++) {
            Y = initY;
            for (int y = 0; y < 10; y++) {
                bricks[x][y] = new JPanel();
                bricks[x][y].setBounds(X, Y, brickW, brickH);
                Y += brickY;
                Y += brickH;
                bricks[x][y].setFocusable(false);
                bricks[x][y].setBackground(Color.ORANGE);
                bricks[x][y].addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        mouseReleasedOnBrick(e);
                    }

                    public void mousePressed(MouseEvent e) {
                        mousePressedOnBrick(e);
                    }
                });
                bricks[x][y].setBorder(BorderFactory.createRaisedBevelBorder());
                panel.add(bricks[x][y]);
                visibleCount++;
            }
            X += brickX;
            X += brickW;
        }
        remain.setText("" + visibleCount);
    }

    public void initBat() {
        lable = new JButton();
        lable.requestFocusInWindow();
        bat = new JPanel();
        bat.add(lable);
        bat.setSize(8, 2);
        bat.setBackground(Color.red);
        bat.setBorder(BorderFactory.createLineBorder(Color.RED));
        bat.requestFocusInWindow();
        X -= 250;
        Y += 135;
        bat.setBounds(X, Y, batWidth, batHeight);
        panel.add(bat);
        lable.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    // move left
                    moveBatRight();
                    break;
                case KeyEvent.VK_LEFT:
                    // move right;
                    moveBatLeft();
                    break;
                case KeyEvent.VK_SPACE:
                    // space is pressed
                    actionButtonPressed();
                    break;
                case KeyEvent.VK_ENTER:
                    // Enter is pressed.
                case KeyEvent.VK_ESCAPE:
                    // Esc is pressed.
                    pauseLable.setText("Game is Paused");
                    pauseTheGame();
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                }
            }
        });
    }

    private void initBall() {
        ball = new RoundPanel("");
        ball.setBackground(Color.BLACK);
        ball.setBorder(BorderFactory.createLineBorder(Color.yellow));
        ball.setForeground(Color.yellow);
        ball.setBounds(X + 25, Y - 20, ballWidth, ballHeight);
        ball.setFocusable(false);
        panel.add(ball);
    }

    public void moveBatRight() {
        if (!paused) {
            Rectangle rect;
            rect = bat.getBounds();
            int x = (int) rect.getX() + 10;
            if (x <= 420) {
                bat.setBounds(x, (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            } else {

            }
        }
    }

    public void moveBatLeft() {
        if (!paused) {
            Rectangle rect;
            rect = bat.getBounds();
            int x = (int) rect.getX() - 10;
            if (x >= 5) {
                bat.setBounds(x, (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            } else {

            }
        }
    }

    private void moveBatWithMouse(int x) {
        if (!paused) {
            Rectangle rect;
            rect = bat.getBounds();
            if ((x <= 420) && (x >= 2)) {
                bat.setBounds(x, (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            } else {

            }
        }
    }

    private void mouseReleasedOnBrick(MouseEvent e) {
        JPanel p = (JPanel) e.getSource();
        p.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private void mousePressedOnBrick(MouseEvent e) {
        JPanel p = (JPanel) e.getSource();
        p.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    private void actionButtonPressed() {
    }

    private void pauseTheGame() {

        paused = !paused;
        if (paused) {
            // pause.getRootPane().setGlassPane(glass);
            glassPane.setVisible(true);
            //System.out.println("Pause called.");
            // pauseLable.requestFocus();
        } else {
            glassPane.setVisible(false);
            System.out.println("Resume called.");
            lable.requestFocus();
        }
    }

    class BallMotion implements Runnable {
        public int getX() {
            return x;
        }

        public void bounceToUp() {
            yop = ydec;
            up = true;
        }

        public void bounceToDown() {
            yop = yinc;
            up = false;
        }

        public void bounceToLeft() {
            xop = xdec;
            left = true;
        }

        public void bounceToRight() {
            xop = xinc;
            left = false;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getXop() {
            return xop;
        }

        public void setXop(int xop) {
            this.xop = xop;
        }

        public int getYop() {
            return yop;
        }

        public void setYop(int yop) {
            this.yop = yop;
        }

        Thread ballThread;
        int x = ball.getX();
        int y = ball.getY();
        volatile int xop = 1;
        volatile int yop = -1;
        volatile int xinc = 1;
        volatile int yinc = 1;
        volatile int xdec = -1;
        volatile int ydec = -1;

        public int getXinc() {
            return xinc;
        }

        public void setXinc(int xinc) {
            this.xinc = xinc;
        }

        public int getYinc() {
            return yinc;
        }

        public void setYinc(int yinc) {
            this.yinc = yinc;
        }

        public int getXdec() {
            return xdec;
        }

        public void setXdec(int xdec) {
            this.xdec = xdec;
        }

        public int getYdec() {
            return ydec;
        }

        public void setYdec(int ydec) {
            this.ydec = ydec;
        }

        public BallMotion() {
            ballThread = new Thread(this);
            ballThread.start();
        }

        public void run() {
            up = true;
            left = false;
            while (true) {
                if ((!gameOver) && (!paused) && (!levelUp) && (!gameOver)) {
                    if (((x >= 2) && (x <= 465)) && ((y >= 2) && (y <= 365))) {
                        if (x <= 4) {
                            bounceToRight();
                        } else if (x >= 462) {
                            bounceToLeft();
                        }
                        if (y <= 4) {
                            bounceToDown();
                        } else if (y >= 363) {
                            checkForBat(x, y);
                            bounceToUp();
                        }
                        x = (x + xop);
                        y = (y + yop);
                        updateBallPosition(x, y);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (levelUp) {
                    showLevelUpMessage();
                }
            }
        }
    }

    private void checkForBat(int x, int y) {
        if (y >= 363) {
            if (isBottomLeftOfBall(x, y)) {
                int a = getLocationOfBat(x, y);
                if (a == 0) {
                    changeMotion(1, 1, true, true);
                } else if (a > 0) {
                    changeMotion(2, 2, true, true);
                } else {
                    changeMotion(2, 2, true, true);
                }
            } else if (isBottomMiddleOfBall(x, y)) {
                int a = getLocationOfBat(x, y);
                if (a == 0) {
                    changeMotion(1, 1, true, true);
                } else if (a > 0) {
                    changeMotion(2, 2, false, true);
                } else {
                    changeMotion(2, 2, false, true);
                }
            } else if (isBottomRightOfBall(x, y)) {
                int a = getLocationOfBat(x, y);
                if (a == 0) {
                    changeMotion(1, 1, true, true);
                } else if (a > 0) {
                    changeMotion(2, 2, false, true);
                } else {
                    changeMotion(2, 2, false, true);
                }
            } else {
                if (lives > 0) {
                    decreaseLives();
                    pauseLable.setText("Life Gone..!");
                    paused = false;
                    pauseTheGame();
                } else {
                    pauseLable.setText("GAME OVER!..");
                    paused = false;
                    gameOver = true;
                    pauseTheGame();
                }
            }
        }
    }

    private void decreaseLives() {
        lives--;
        lifes.setText("" + lives);
    }

    private int getLocationOfBat(int x, int y) {
        int loc = 0;
        Component comp = panel.getComponentAt(x, y);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if (!p.equals(bat)) {
                    int totH = bat.getX() + batWidth;
                    int a = (x - bat.getX());
                    int b = (totH - bat.getX());
                    int c = (a - b);
                    if (c > 25) {
                        loc = -1;
                    } else if (c < -25) {
                        loc = 1;
                    }
                }
            }
        }
        return loc;
    }

    private void changeMotion(int vChange, int hChange, boolean changeVerticle, boolean changeHorizontal) {
        if (changeVerticle) {
            if (vChange > 0) {
                motion.yinc = vChange;
                motion.ydec = -(vChange);
            }
        }
        if (changeHorizontal) {
            if (hChange > 0) {
                motion.xinc = (hChange);
                motion.xdec = -(hChange);
            }
        }
    }

    private boolean isBat(int xx, int yy) {
        boolean flag = false;
        Component comp = panel.getComponentAt(xx, yy);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if (!p.equals(bat)) {

                } else {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean isBottomLeftOfBall(int x, int y) {
        int xx = x;
        int yy = (y + (ballHeight / 2));
        return isBat(xx, yy);
    }

    private boolean isBottomMiddleOfBall(int x, int y) {
        int xx = (x + (ballWidth / 2));
        int yy = (y + ballHeight);
        return isBat(xx, yy);
    }

    private boolean isBottomRightOfBall(int x, int y) {
        int xx = (x + ballWidth);
        int yy = (y + ballHeight);
        return isBat(xx, yy);
    }

    private class Score {
        public void increaseScore() {
            synchronized (this) {
                scoreTotal += 10;
                score.setText("" + scoreTotal);
                visibleCount--;
                remain.setText("" + visibleCount);
            }
        }
    }

    private void updateBallPosition(int x, int y) {
        ball.setBackground(Color.MAGENTA);
        if (checkAtTopLeft(x, y)) {
        } else if (checkAtTopMiddle(x, y)) {
        } else if (checkAtTopRight(x, y)) {
        } else if (checkAtBottomLeft(x, y)) {
        } else if (checkAtBottomMiddle(x, y)) {
        } else if (checkAtBottomRight(x, y)) {
        } else if (checkAtLeftMiddle(x, y)) {
        } else if (checkAtRightMiddle(x, y)) {
        }
        ball.setBackground(Color.BLACK);
        ball.setBounds(x, y, ballWidth, ballHeight);
    }

    private boolean checkAtRightMiddle(int x, int y) {
        boolean flag = false;
        int xx = (x + ballWidth);
        int yy = (y + (ballHeight) / 2);
        Component comp = panel.getComponentAt(xx, yy);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if ((!p.equals(bat)) && (!p.equals(ball)) && (!p.equals(panel))) {
                    new BrickFallAnimation(comp);
                    scorer.increaseScore();
                    flag = true;
                    this.repaint();
                    motion.bounceToRight();
                }
                if (visibleCount == 0) {
                    levelUp = true;
                }
            }
        }
        return flag;
    }

    private boolean checkAtLeftMiddle(int x, int y) {
        boolean flag = false;
        int xx = (x + (ballWidth / 2));
        int yy = (y + ballHeight);
        Component comp = panel.getComponentAt(xx, yy);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if ((!p.equals(bat)) && (!p.equals(ball)) && (!p.equals(panel))) {
                    new BrickFallAnimation(comp);
                    scorer.increaseScore();
                    flag = true;
                    this.repaint();
                    motion.bounceToRight();
                }
                if (visibleCount == 0) {
                    levelUp = true;
                }
            }
        }
        return flag;
    }

    private boolean checkAtBottomMiddle(int x, int y) {
        boolean flag = false;
        int xx = x + (ballWidth / 2);
        int yy = y + (ballHeight / 2);
        Component comp = panel.getComponentAt(xx, yy);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if ((!p.equals(bat)) && (!p.equals(ball)) && (!p.equals(panel))) {
                    new BrickFallAnimation(comp);
                    scorer.increaseScore();
                    this.repaint();
                    flag = true;
                    // move UP
                    motion.bounceToUp();
                    if (visibleCount == 0) {
                        levelUp = true;
                    }
                }
            }
        }
        return flag;
    }

    private boolean checkAtTopMiddle(int x, int y) {
        boolean flag = false;
        int xx = x + (ballWidth / 2);
        Component comp = panel.getComponentAt(xx, y);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if ((!p.equals(bat)) && (!p.equals(ball)) && (!p.equals(panel))) {
                    new BrickFallAnimation(comp);
                    scorer.increaseScore();
                    flag = true;
                    this.repaint();
                    // move down
                    motion.bounceToDown();
                    if (visibleCount == 0) {
                        levelUp = true;
                    }
                }
            }
        }
        return flag;
    }

    private boolean checkAtBottomRight(int x, int y) {
        boolean flag = false;
        int xx = x + ballWidth;
        int yy = y + ballHeight;
        Component comp = panel.getComponentAt(xx - 1, yy - 1);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if ((!p.equals(bat)) && (!p.equals(ball)) && (!p.equals(panel))) {
                    new BrickFallAnimation(comp);
                    scorer.increaseScore();
                    flag = true;
                    this.repaint();
                    if (up) {
                        // move down
                        motion.bounceToUp();
                        if (left) {
                            // now move left
                            motion.bounceToRight();
                        } else {
                            // now move right
                            motion.bounceToLeft();
                        }
                    } else {
                        // move Up
                        motion.bounceToDown();
                        if (left) {
                            // now move right
                            motion.bounceToRight();
                        } else {
                            // now move left
                            motion.bounceToLeft();
                        }
                    }
                    if (visibleCount == 0) {
                        levelUp = true;
                    }
                }
            }
        }
        return flag;
    }

    private boolean checkAtBottomLeft(int x, int y) {
        boolean flag = false;
        int yy = y + ballHeight;
        Component comp = panel.getComponentAt(x - 1, yy - 1);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if ((!p.equals(bat)) && (!p.equals(ball)) && (!p.equals(panel))) {
                    new BrickFallAnimation(comp);
                    scorer.increaseScore();
                    flag = true;
                    this.repaint();
                    if (up) {
                        // move down
                        motion.bounceToUp();
                        if (left) {
                            // now move left
                            motion.bounceToLeft();
                        } else {
                            // now move right
                            motion.bounceToRight();
                        }
                    } else {
                        // move Up
                        motion.bounceToDown();
                        if (left) {
                            // now move right
                            motion.bounceToRight();
                        } else {
                            // now move left
                            motion.bounceToLeft();
                        }
                    }
                    if (visibleCount == 0) {
                        levelUp = true;
                    }
                }
            }
        }
        return flag;
    }

    private boolean checkAtTopRight(int x, int y) {
        boolean flag = false;
        int xx = x + ballWidth + 1;
        Component comp = panel.getComponentAt(xx, y + 1);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if ((!p.equals(bat)) && (!p.equals(ball)) && (!p.equals(panel))) {
                    new BrickFallAnimation(comp);
                    scorer.increaseScore();
                    flag = true;
                    this.repaint();
                    if (up) {
                        // move down
                        motion.bounceToDown();
                        if (left) {
                            // now move left
                            motion.bounceToLeft();
                        } else {
                            // now move right
                            motion.bounceToRight();
                        }
                    } else {
                        // move Up
                        motion.bounceToUp();
                        if (left) {
                            // now move right
                            motion.bounceToRight();
                        } else {
                            // now move left
                            motion.bounceToLeft();
                        }
                    }
                    if (visibleCount == 0) {
                        levelUp = true;
                    }
                }
            }
        }
        return flag;
    }

    private boolean checkAtTopLeft(int x, int y) {
        boolean flag = false;
        Component comp = panel.getComponentAt(x + 1, y + 1);
        if (comp != null) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if ((!p.equals(bat)) && (!p.equals(ball)) && (!p.equals(panel))) {
                    new BrickFallAnimation(comp);
                    scorer.increaseScore();
                    flag = true;
                    this.repaint();
                    if (up) {
                        if (left) {
                            // move down
                            motion.bounceToDown();
                            // now move left
                            motion.bounceToLeft();
                        } else {
                            // move up
                            motion.bounceToUp();
                            // now move right
                            motion.bounceToRight();
                        }
                    }
                    // move down
                    motion.bounceToDown();
                    // now move right
                    motion.bounceToRight();
                    if (visibleCount == 0) {
                        levelUp = true;
                    }
                }
            }
        }
        return flag;
    }

    private void showLevelUpMessage() {
        pauseLable.setText("Level UP .. !");
        paused = false;
        pauseTheGame();
    }

    public static void main(String[] args) {
        new BrickGame();
    }

    public void windowStateChanged(WindowEvent e) {

    }

    public void windowGainedFocus(WindowEvent e) {
    }

    public void windowLostFocus(WindowEvent e) {
        pauseLable.setText("Game is Paused");
        paused = false;
        pauseTheGame();
    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {
        pauseLable.setText("Game is Paused");
        paused = false;
        pauseTheGame();
    }

    public void windowOpened(WindowEvent e) {

    }

    class BrickFallAnimation implements Runnable {
        Component comp;
        int x, y;

        public BrickFallAnimation(Component comp) {
            this.comp = comp;
            Thread t = new Thread(this);
            if (comp != null) {
                x = comp.getX();
                y = comp.getY();
                t.start();
            }
        }

        public void run() {
            comp.setBackground(Color.RED);
            while ((x > 2) && (x < 455) && (y <= 365) && (y > 2)) {
                if (left) {
                    y -= 3;
                    x -= 3;
                } else {
                    y -= 3;
                    x += 3;
                }
                comp.setBounds(x, y, comp.getWidth(), comp.getHeight());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            comp.setVisible(false);
            panel.remove(comp);
        }
    }
}
