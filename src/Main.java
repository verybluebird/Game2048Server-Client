import controller.Controller;
import model.Model;
import view.BoardView;

import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        Model model=new Model();
        Controller controller=new Controller(model);
        JFrame game=new JFrame();
        JPanel mainPanel = new JPanel();
        ImageIcon img = new ImageIcon("src/resources/2048.png");
        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(450, 510);
        game.setResizable(false);

        game.setIconImage(img.getImage());

        game.add(controller.getView());
        BoardView boardView = new BoardView();

        mainPanel.add(boardView, BorderLayout.CENTER);


        mainPanel.revalidate();
        mainPanel.repaint();

        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}
