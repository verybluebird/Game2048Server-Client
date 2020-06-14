package model;

import java.awt.*;


public class Tile {
    public int value;

    public Tile(int value) {
        this.value = value;
    }

    public Tile() {
        this.value = 0;
    }

    public boolean isEmpty() {
        return (this.value == 0);
    }

    //возвращающий новый цвет
    public Color getFontColor() {
        if (value < 16) return new Color(0x857C99);
        else return new Color(0xF8F2F9);
    }

    //возвращающий цвет плитки в зависимости от ее веса
    public Color getTileColor() {
        switch (value) {
            case 0:
                return new Color(0xF0ECFF);
            case 2:
                return new Color(0xFFFFF264, true);
            case 4:
                return new Color(0xFFFFD465, true);
            case 8:
                return new Color(0xFFFFA554, true);
            case 16:
                return new Color(0xFFFF7F78, true);
            case 32:
                return new Color(0xFFE480C1, true);
            case 64:
                return new Color(0xFFDE8CF6, true);
            case 128:
                return new Color(0xFFA881ED, true);
            case 256:
                return new Color(0xFF8573E2, true);
            case 512:
                return new Color(0xFF5566D7, true);
            case 1024:
                return new Color(0xFF3052D9, true);
            case 2048:
                return new Color(0xFF17367E, true);
            default:
                return new Color(0xff0000);
        }
    }
}
