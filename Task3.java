
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class Task3 extends JPanel implements ActionListener, KeyListener {

    private final int UNIT_SIZE = 20;
    private final int GAME_WIDTH = 600;
    private final int GAME_HEIGHT = 400;
    private LinkedList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public Task3() {
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        snake = new LinkedList<>();
        snake.add(new Point(UNIT_SIZE, UNIT_SIZE));
        generateFood();
        running = true;
        timer = new Timer(100, this);
        timer.start();
    }

    public void generateFood() {
        Random random = new Random();
        food = new Point(random.nextInt(GAME_WIDTH / UNIT_SIZE) * UNIT_SIZE,
                random.nextInt(GAME_HEIGHT / UNIT_SIZE) * UNIT_SIZE);
    }

    public void move() {
        Point head = snake.getFirst();
        Point newHead = switch (direction) {
            case 'U' -> new Point(head.x, head.y - UNIT_SIZE);
            case 'D' -> new Point(head.x, head.y + UNIT_SIZE);
            case 'L' -> new Point(head.x - UNIT_SIZE, head.y);
            case 'R' -> new Point(head.x + UNIT_SIZE, head.y);
            default -> head;
        };

        if (newHead.equals(food)) {
            snake.addFirst(newHead);
            generateFood();
        } else {
            snake.addFirst(newHead);
            snake.removeLast();
        }
    }

    public void checkCollision() {
        Point head = snake.getFirst();
        if (head.x < 0 || head.x >= GAME_WIDTH || head.y < 0 || head.y >= GAME_HEIGHT
                || snake.subList(1, snake.size()).contains(head)) {
            running = false;
            timer.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Game Over!", GAME_WIDTH / 2 - 30, GAME_HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> {
                if (direction != 'R')
                    direction = 'L';
            }
            case KeyEvent.VK_RIGHT -> {
                if (direction != 'L')
                    direction = 'R';
            }
            case KeyEvent.VK_UP -> {
                if (direction != 'D')
                    direction = 'U';
            }
            case KeyEvent.VK_DOWN -> {
                if (direction != 'U')
                    direction = 'D';
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        Task3 gamePanel = new Task3();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}