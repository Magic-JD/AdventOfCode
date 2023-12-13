package tools;

public enum Direction {


    N,
    E,
    S,
    W;

    public Direction getOpposite(){
        return switch (this){
            case N -> S;
            case S -> N;
            case E -> W;
            case W -> E;
        };
    }
}
