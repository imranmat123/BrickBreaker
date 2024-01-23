import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class brickBreakerGame extends JFrame implements KeyListener, ActionListener {

    private boolean isGameActive = false;
    private int playerScore = 0;
    private int remainingBricks = 21;
    private Timer gameTimer;
    private int timerDelay = 8;
    private int playerPaddleX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballDirectionX = -1;
    private int ballDirectionY = -2;
    private BrickLayout brickLayout;

    public brickBreakerGame() {
        brickLayout = new BrickLayout(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        gameTimer = new Timer(timerDelay, this);
        gameTimer.start();
    }

    public void paint(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillRect(1, 1, 692, 592);

        brickLayout.draw((Graphics2D) graphics);

        graphics.setColor(Color.yellow);
        graphics.fillRect(0, 0, 3, 592);
        graphics.fillRect(0, 0, 692, 3);
        graphics.fillRect(691, 0, 3, 592);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("serif", Font.BOLD, 25));
        graphics.drawString("Score: " + playerScore, 590, 30);

        graphics.setColor(Color.green);
        graphics.fillRect(playerPaddleX, 550, 100, 8);

        graphics.setColor(Color.yellow);
        graphics.fillOval(ballPosX, ballPosY, 20, 20);

        if (ballPosY > 570) {
            isGameActive = false;
            ballDirectionX = 0;
            ballPosY = 0;
            graphics.setColor(Color.red);
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("Game Over - Score: " + playerScore, 190, 300);

            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("Press Enter to Restart", 190, 340);
        }

        if (remainingBricks == 0) {
            isGameActive = false;
            ballDirectionX = 0;
            ballPosY = 0;
            graphics.setColor(Color.green);
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("You Win! - Score: " + playerScore, 190, 300);

            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("Press Enter to Restart", 190, 340);
        }

        graphics.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        gameTimer.start();
        if (isGameActive) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerPaddleX, 550, 100, 8))) {
                ballDirectionY = -ballDirectionY;
            }
            A:
            for (int i = 0; i < brickLayout.layout.length; i++) {
                for (int j = 0; j < brickLayout.layout[0].length; j++) {
                    if (brickLayout.layout[i][j] > 0) {
                        int brickX = j * brickLayout.brickWidth + 80;
                        int brickY = i * brickLayout.brickHeight + 50;
                        int brickWidth = brickLayout.brickWidth;
                        int brickHeight = brickLayout.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        if (ballRect.intersects(brickRect)) {
                            brickLayout.setBrickValue(0, i, j);
                            remainingBricks--;
                            playerScore += 5;
                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickWidth) {
                                ballDirectionX = -ballDirectionX;
                            } else {
                                ballDirectionY = -ballDirectionY;
                            }
                            break A;
                        }
                    }
                }
            }
            ballPosX += ballDirectionX;
            ballPosY += ballDirectionY;
            if (ballPosX < 0) {
                ballDirectionX = -ballDirectionX;
            }
            if (ballPosY < 0) {
                ballDirectionY = -ballDirectionY;
            }
            if (ballPosX > 670) {
                ballDirectionX = -ballDirectionX;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent event) {
        // Unused
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerPaddleX >= 600) {
                playerPaddleX = 600;
            } else {
                movePaddleRight();
            }
        }
        if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerPaddleX < 10) {
                playerPaddleX = 10;
            } else {
                movePaddleLeft();
            }
        }
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!isGameActive) {
                ballPosX = 120;
                ballPosY = 350;
                ballDirectionX = -1;
                ballDirectionY = -2;
                playerScore = 0;
                playerPaddleX = 310;
                remainingBricks = 21;
                brickLayout = new BrickLayout(3, 7);
                isGameActive = true;
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        // Unused
    }

    public void movePaddleRight() {
        isGameActive = true;
        playerPaddleX += 20;
    }

    public void movePaddleLeft() {
        isGameActive = true;
        playerPaddleX -= 20;
    }
}

class BrickLayout {
    public int layout[][];
    public int brickWidth;
    public int brickHeight;

    public BrickLayout(int row, int col) {
        layout = new int[row][col];
        for (int[] rowArray : layout) {
            for (int i = 0; i < layout[0].length; i++) {
                rowArray[i] = 1;
            }
        }
        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D graphics) {
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                if (layout[i][j] > 0) {
                    graphics.setColor(Color.red);
                    graphics.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    graphics.setStroke(new BasicStroke(3));
                    graphics.setColor(Color.black);
                    graphics.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        layout[row][col] = value;
    }
}
