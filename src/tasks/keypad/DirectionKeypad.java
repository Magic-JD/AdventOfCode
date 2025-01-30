package tasks.keypad;

import java.math.BigInteger;
import java.util.*;

public class DirectionKeypad extends Keypad {

    private final Map<String, BigInteger> directionsMap = new HashMap<>();

    private DirectionKeypad keypad;

    private final Map<Character, Coord> coords = Map.of(
            '^', new Coord(0, 1),
            'A', new Coord(0, 2),
            '<', new Coord(1, 0),
            'V', new Coord(1, 1),
            '>', new Coord(1, 2)
    );
    public DirectionKeypad(DirectionKeypad nextKeypad) {
        super(0, 2, 1, 2, new Coord(0, 0));
        this.keypad = nextKeypad;
    }

    public BigInteger count(char a){
        return move(a).stream().map(next -> {
            if(directionsMap.containsKey(next)){
                return directionsMap.get(next);
            }
            if(keypad == null){
                return BigInteger.valueOf(next.length());
            }
            BigInteger reduce = next.chars().mapToObj(c -> keypad.count((char) c)).reduce(BigInteger.ZERO, BigInteger::add);
            directionsMap.put(next, reduce);
            return reduce;
        }).sorted(Comparator.reverseOrder()).findAny().orElseThrow();

    }

    public List<String> move(char a){
        Coord current = new Coord(i, j);
        return move(a, current);
    }

    public List<String> move(char a, Coord current){
        StringBuilder sb = new StringBuilder();
        Coord next = coords.get(a);
        if(current.equals(next)) return List.of("A");
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

        if (current.i() == 0 && next.j() == 0) {
            moveDistanceDown(next, current, sb);
            moveDistanceLeft(current, next, sb);
            moveDistanceUp(next, current, sb);
            moveDistanceRight(current, next, sb);
        } else if (current.j() == 0 && next.i() == 0) {
            moveDistanceLeft(current, next, sb);
            moveDistanceDown(next, current, sb);
            moveDistanceRight(current, next, sb);
            moveDistanceUp(next, current, sb);
        } else {
            moveDistanceLeft(current, next, sb);
            moveDistanceDown(next, current, sb);
            moveDistanceUp(next, current, sb);
            moveDistanceRight(current, next, sb);
        }
        sb.append('A');
        return List.of(sb.toString());
    }
}
