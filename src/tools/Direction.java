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

    public Direction turnLeft(){
        int newOrdinal = (this.ordinal() + 1) % 4;
        return Direction.values()[newOrdinal];
    }

    public Direction turnRight(){
        int newOrdinal = (this.ordinal() - 1);
        if(newOrdinal < 0){
            newOrdinal = 3;
        }
        return Direction.values()[newOrdinal];
    }
}
