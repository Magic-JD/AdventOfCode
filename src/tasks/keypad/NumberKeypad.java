package tasks.keypad;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberKeypad extends Keypad {

    private final Map<Character, Coord> coords = new HashMap<>();

    public NumberKeypad() {
        super(3, 2, 3, 2, new Coord(3, 0));
        coords.put('7', new Coord(0, 0));
        coords.put('8', new Coord(0, 1));
        coords.put('9', new Coord(0, 2));
        coords.put('4', new Coord(1, 0));
        coords.put('5', new Coord(1, 1));
        coords.put('6', new Coord(1, 2));
        coords.put('1', new Coord(2, 0));
        coords.put('2', new Coord(2, 1));
        coords.put('3', new Coord(2, 2));
        coords.put('0', new Coord(3, 1));
        coords.put('A', new Coord(3, 2));
    }

    public List<String> move(char a){
        StringBuilder sb = new StringBuilder();
        Coord current = new Coord(i, j);
        Coord next = coords.get(a);
        if(current.equals(next)){
            return List.of("A");
        }
        if(current.i() == next.i()){
            if(current.j() > next.j()) moveDistanceLeft(current, next, sb);
            else moveDistanceRight(current, next, sb);
            sb.append('A');
            return List.of(sb.toString());
        }
        if(current.j() == next.j()){
            if(current.i() > next.i()) moveDistanceUp(next, current, sb);
            else moveDistanceDown(next, current, sb);
            sb.append('A');
            return List.of(sb.toString());
        }
        if (current.i() == 3 && next.j() == 0) {
            moveDistanceUp(next, current, sb);
            moveDistanceLeft(current, next, sb);
        } else {
            moveDistanceLeft(current, next, sb);
            moveDistanceUp(next, current, sb);
        }
        if(current.j() == 0 && next.i() == 3){
            moveDistanceRight(current, next, sb);
            moveDistanceDown(next, current, sb);
        } else {
            moveDistanceDown(next, current, sb);
            moveDistanceRight(current, next, sb);
        }
        sb.append('A');
        return List.of(sb.toString());
    }

}

