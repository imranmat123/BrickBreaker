import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        brickBreakerGame game = new brickBreakerGame();
        game.setSize(700, 600);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setLocationRelativeTo(null);
        game.setResizable(false);
        game.setTitle("Brick Breaker Game");
        game.setVisible(true);
    }
}