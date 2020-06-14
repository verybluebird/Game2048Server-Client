package view;

import controller.Controller;
import model.Model;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyView extends KeyAdapter {
    Model model;
    View view;
    private static final int WINNING_TILE = 2048;
    public KeyView(Controller controller){
        this.view=controller.getView();
        this.model=controller.getModel();
    }
    public void resetGame() {
        model.score = 0;
        view.isGameLost = false;
        view.isGameWon = false;
        model.resetGameTiles();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            resetGame();
        if (!model.canMove()) view.isGameLost = true;
        if (!view.isGameLost && !view.isGameWon) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) model.left();
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) model.right();
            if (e.getKeyCode() == KeyEvent.VK_UP) model.up();
            if (e.getKeyCode() == KeyEvent.VK_DOWN) model.down();
            if (e.getKeyCode() == KeyEvent.VK_Z) model.rollback(); //отмена хода

        }
        if (model.maxTile == WINNING_TILE) view.isGameWon = true;
        view.repaint();
    }

}
