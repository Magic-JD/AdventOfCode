package tasks.keypad;


import java.util.List;

public abstract class Keypad {
    public int i;
    public int j;

    private int height;
    private int width;
    private Coord deathCoord;

    public Keypad(int i, int j, int height, int width, Coord deathCoord) {
        this.i = i;
        this.j = j;
        this.height = height;
        this.width = width;
        this.deathCoord = deathCoord;
    }

    private void validate(){
        if(i < 0 || i > height || j < 0 || j > width) throw new RuntimeException("Out of bounds");
        if(new Coord(i, j).equals(deathCoord)) throw new RuntimeException("Death coord");
    }

    public void moveUp(StringBuilder sb) {
        i--;
        sb.append('^');
        validate();
    }

    public void moveDown(StringBuilder sb) {
        i++;
        sb.append('V');
        validate();
    }

    public void moveLeft(StringBuilder sb) {
        j--;
        sb.append('<');
        validate();
    }

    public void moveRight(StringBuilder sb) {
        j++;
        sb.append('>');
        validate();
    }

    public void moveDistanceDown(Coord next, Coord current, StringBuilder sb) {
        if(next.i > current.i){
            for (int k = 0; k < next.i - current.i; k++) {
                moveDown(sb);
            }
        }
    }

    public void moveDistanceLeft(Coord current, Coord next, StringBuilder sb) {
        if(next.j < current.j){
            for (int k = 0; k < current.j - next.j; k++) {
                moveLeft(sb);
            }
        }
    }

    public void moveDistanceRight(Coord current, Coord next, StringBuilder sb) {
        if(next.j > current.j){
            for (int k = 0; k < next.j - current.j; k++) {
                moveRight(sb);
            }
        }
    }

    public void moveDistanceUp(Coord next, Coord current, StringBuilder sb) {
        if(next.i < current.i){
            for (int k = 0; k < current.i - next.i; k++) {
                moveUp(sb);
            }
        }
    }
    public record Coord(int i, int j) {
    }

    public abstract List<String> move(char a);
}


