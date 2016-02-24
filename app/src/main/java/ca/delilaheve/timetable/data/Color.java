package ca.delilaheve.timetable.data;

public class Color {

    private int r;
    private int g;
    private int b;

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    public int makeColor() {
        return android.graphics.Color.rgb(r, g, b);
    }
}
