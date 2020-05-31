package view;

import controller.Controller;
import entity.Tile;
import model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class View extends JPanel  {
    public static final int WINNING_TILE = 2048;
    private JPanel gridPanel; //Jpanel containing the grid
    private JPanel topPanel; // Jpanel containing the top bar which hosts the score
    private JLabel scoreLabel;
    JFrame game=new JFrame();
    JPanel mainPanel = new JPanel();
    ImageIcon img = new ImageIcon("src/resources/2048.png");
    public void run() {
        ViewKey viewKey= new ViewKey();
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

    private static final Color BG_COLOR = new Color(0xC5BDD5);
    private static final String FONT_NAME = "Arial";
    private static final int TILE_SIZE = 96;
    private static final int TILE_MARGIN = 11;//ширина между плитками

    private Controller controller;
    private Model model;
    public boolean isGameWon = false;
    public boolean isGameLost = false;

    public View(Controller controller) {
        setFocusable(true);
        this.controller = controller;
        this.model = model;
        addKeyListener(controller);
    }
    public Model get_model(){
        return this.model;
    }

    /**
     * A popup that informs the player when they have won the game, and takes in the
     * player's name for the score board
     */

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                drawTile(g, controller.getGameTiles()[y][x], x, y);
            }
        }


        g.drawString("Score: " + controller.getScore(), 140, 465);

        if (isGameWon) {

            JOptionPane.showMessageDialog(this, "You've won!");
        } else if (isGameLost) {

            JOptionPane.showMessageDialog(this, "You've lost :(");
        }
    }

//    public static class TileView {
//        public JLabel tileLabel;
//
//        TileView(Tile tile) {
//            ImageIcon img = new ImageIcon("src/resources/" + tile.value + ".png");
//            tileLabel = new JLabel(img);
//        }
//
//
//    }

//    public TileView getTileView(Tile tile) {
//        return new TileView(tile);
//    }

    private void drawTile(Graphics g2, Tile tile, int x, int y) {
//            gridPanel.add(getTileView(tile).tileLabel);
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);
        setLayout(new BorderLayout());

//        repaint();


        Graphics2D g = ((Graphics2D) g2);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int value = tile.value;

        g.setColor(tile.getTileColor());
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 8, 8);
        g.setColor(tile.getFontColor());
        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
        final Font font = new Font(FONT_NAME, Font.BOLD, size);
        g.setFont(font);

        String s = String.valueOf(value);
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

        if (value != 0)
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
    }

    private static int offsetCoors(int arg) {
        return arg * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN;
    }



}


