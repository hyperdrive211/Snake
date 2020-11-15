import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created By Jonathon on 14/11/2020
 * Update Comments About Program Here
 **/
public class GamePanel extends JPanel implements ActionListener {


    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;

    final int x[] = new int [GAME_UNITS];
    final int y[] = new int [GAME_UNITS];

    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;

    char direction = 'R';
    boolean running = false;

    Timer timer;
    Random random;

    GamePanel(){
       random = new Random();
       this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
       this.setBackground(Color.black);
       this.setFocusable(true);
       this.addKeyListener(new MyKeyAdapter());
       startGame();

    }

    public void startGame(){
         newApple();
         running = true;
         timer = new Timer(DELAY, this);
         timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            String score = String.format("Score: %d", applesEaten);
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(score, (SCREEN_WIDTH - metrics.stringWidth(score)) /2, SCREEN_HEIGHT);
        }
        else{
            gameOver(g);
        }

    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        System.out.println("" + appleX);
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        System.out.println(""+ appleY);

    }

    public void move(){
      for(int i = bodyParts; i > 0; i--){
          x[i] = x[i-1];
          y[i] = y[i -1];
      }

      switch(direction){
          case 'U':
              y[0] = y[0] - UNIT_SIZE;
              break;
          case 'D':
              y[0] = y[0] + UNIT_SIZE;
              break;
          case 'L':
              x[0] = x[0] - UNIT_SIZE;
              break;
          case 'R':
              x[0] = x[0] + UNIT_SIZE;
              break;
      }
    }

    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }

    public void checkCollisions(){

        //checks if head collides with the body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y [0] == y[i])){
                running = false;
            }
        }

        //check if head touches left border
        if(x[0] < 1){
            running = false;
        }

        //check if head touches the right border
        if(x[0] == SCREEN_WIDTH){
            running = false;
        }

        //check if head touches the top border
        if(y[0] == SCREEN_HEIGHT){
            running = false;
        }

        //check if head touches top border
        if(y[0] < 0){
            running = false;
        }

        if(!running){
            System.out.println("Value X at stop " +String.valueOf(x[0]));
            System.out.println("Value of Y at stop " +String.valueOf(y[0]));
            timer.stop();
        }

    }

    public void gameOver(Graphics g ){

      //Display the score
        String score = String.format("Score: %d", applesEaten);
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metricScore = getFontMetrics(g.getFont());
        g.drawString(score, (SCREEN_WIDTH - metricScore.stringWidth(score)) /2, SCREEN_HEIGHT);

      //Game over text
        String msg = "GAME OVER";
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(msg, (SCREEN_WIDTH - metrics.stringWidth(msg))/ 2, SCREEN_HEIGHT / 2);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
           switch(e.getKeyCode()) {
               case KeyEvent.VK_LEFT:
                   if (direction != 'R') {
                       direction = 'L';
                   }
                   break;
               case KeyEvent.VK_RIGHT:
                   if (direction != 'L') {
                       direction = 'R';
                   }
                   break;
               case KeyEvent.VK_UP:
                   if (direction != 'D') {
                       direction = 'U';
                   }
                   break;
                   case KeyEvent.VK_DOWN:
                       if(direction != 'U'){
                           direction = 'D';
                       }
                       break;
           }
        }
    }
}
