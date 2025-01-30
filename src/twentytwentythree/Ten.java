package twentytwentythree;

import tools.Direction;
import tools.InternetParser;

import java.util.*;

import static tools.Direction.*;

public class Ten {

    public static final String testData = """         
            ..........
            .S------7.
            .|F----7|.
            .||OOOO||.
            .||OOOO||.
            .|L-7F-J|.
            .|II||II|.
            .L--JL--J.
            ..........
                                                         """;
    public static final String testData2 = """         
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...

                                                                     """;

    public static final String testData3 = """         
            .F7FSF7F7F7F7F7F---7
            .|LJ||||||||||||F--J
            .L-7LJLJ||||||LJL-7.
            F--JF--7||LJLJ.F7FJ.
            L---JF-JLJ....FJLJ..
            ...F-JF---7...L7....
            ..FJF7L7F-JF7..L---7
            ..L-JL7||F7|L7F-7F7|
            .....FJ|||||FJL7||LJ
            .....L-JLJLJL--JLJ..
                                                                     """;


    private static final class Pipe {
        private final int x;
        private final int y;
        private List<Direction> d;

        private boolean isInLoop = false;
        private boolean isOutsideLeftLoop = false;
        private boolean isOutsideRightLoop = false;

        private Direction cameFrom = null;
        private char c;

        private Pipe(int x, int y, List<Direction> d, char c) {
            this.x = x;
            this.y = y;
            this.d = d;
            this.c = c;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        public List<Direction> d() {
            return d;
        }

        public void setD(List<Direction> d) {
            this.d = d;
        }

        public void setCameFrom(Direction d) {
            cameFrom = d;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Pipe) obj;
            return this.x == that.x &&
                    this.y == that.y &&
                    Objects.equals(this.d, that.d);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, d);
        }

        @Override
        public String toString() {
            return "Pipe[" +
                    "x=" + x + ", " +
                    "y=" + y + ", " +
                    "d=" + d + ']';
        }
    }

    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> testInput2 = Arrays.stream(testData2.split("\n")).toList();
        List<String> testInput3 = Arrays.stream(testData3.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(10);
        run(testInput, "4", System.currentTimeMillis());
        run(testInput2, "8", System.currentTimeMillis());
        run(testInput3, "10", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        Pipe start = null;
        Pipe[][] field = new Pipe[input.size()][input.get(0).length()];
        for (int y = 0; y < input.size(); y++) {
            String s = input.get(y);
            for (int x = 0; x < s.length(); x++) {
                char c = s.charAt(x);
                Pipe pipe = new Pipe(x, y, getDirections(c), c);
                if (c == 'S') {
                    start = pipe;
                    start.isInLoop = true;

                }
                field[y][x] = pipe;
            }
        }
        List<Direction> directionForStart = findDirectionForStart(start, field);
        start.setD(directionForStart);
        start.isInLoop = true;
        Direction nextDirection = directionForStart.get(0).ordinal() < directionForStart.get(1).ordinal() ? directionForStart.get(0) : directionForStart.get(1);
        Pipe current = findNext(nextDirection, start, field);
        while (current != start) {
            current.isInLoop = true;
            nextDirection = findNextDirection(nextDirection, current);
            current = findNext(nextDirection, current, field);
        }
        Pipe[][] enlargedPipes = new Pipe[field.length*2][field[0].length*2];
        Arrays.stream(field).flatMap(Arrays::stream).filter(pipe -> pipe.isInLoop).forEach(pipe -> {
            int x = pipe.x *2;
            int y = pipe.y * 2;
            Pipe pipe2 = new Pipe(x, y, pipe.d, pipe.c);
            pipe2.isInLoop = true;
            enlargedPipes[y][x] = pipe2;
            pipe.d.forEach(direction -> {
                switch (direction){
                    case N -> {
                        Pipe pipe1 = new Pipe(x, y - 1, List.of(N, S), '|');
                        pipe1.isInLoop=true;
                        enlargedPipes[y-1][x] = pipe1;
                    }
                    case E -> {
                        Pipe pipe1 = new Pipe(x + 1, y, List.of(E, W), '-');
                        pipe1.isInLoop=true;
                        enlargedPipes[y][x+1] = pipe1;
                    }
                    case S -> {
                        Pipe pipe1 = new Pipe(x, y + 1, List.of(N, S), '|');
                        pipe1.isInLoop = true;
                        enlargedPipes[y+1][x] = pipe1;
                    }
                    case W -> {
                        Pipe pipe1 = new Pipe(x - 1, y, List.of(E, W), '-');
                        pipe1.isInLoop = true;
                        enlargedPipes[y][x-1] = pipe1;
                    }
                }
            });
        });
        for (int i = 0; i < enlargedPipes.length; i++) {
            for (int j = 0; j < enlargedPipes[0].length; j++) {
                if(enlargedPipes[i][j] == null){
                    List<Direction> list = new ArrayList<>(List.of(N, S, E, W));
                    if (i == 0) {
                        list.remove(N);
                    }
                    if (i == enlargedPipes.length - 1) {
                        list.remove(S);
                    }
                    if (j == 0) {
                        list.remove(W);
                    }
                    if (j == enlargedPipes[0].length - 1) {
                        list.remove(E);
                    }
                    enlargedPipes[i][j] = new Pipe(j, i, list, '.');
                }
            }
        }

        ArrayDeque<Pipe> queue = new ArrayDeque<>();
        Arrays.stream(enlargedPipes[0]).filter(p -> !p.isInLoop).forEach(p -> {
            p.isOutsideLeftLoop = true;
            queue.add(p);
        });
        Arrays.stream(enlargedPipes[enlargedPipes.length-1]).filter(p -> !p.isInLoop).forEach(p -> {
            p.isOutsideLeftLoop = true;
            queue.add(p);
        });
        Arrays.stream(enlargedPipes).map(a -> a[0]).filter(p -> !p.isInLoop).forEach(p -> {
            p.isOutsideLeftLoop = true;
            queue.add(p);
        });
        Arrays.stream(enlargedPipes).map(a -> a[a.length-1]).filter(p -> !p.isInLoop).forEach(p -> {
            p.isOutsideLeftLoop = true;
            queue.add(p);
        });

        while (!queue.isEmpty()){
            Pipe p = queue.pop();
            p.d().forEach(direction -> {
                Pipe next = findNext(direction, p, enlargedPipes);
                if(!next.isInLoop && !next.isOutsideLeftLoop){
                    next.isOutsideLeftLoop = true;
                    queue.add(next);
                }
            });
        }
        int count = 0;

        for (int i = 0; i < enlargedPipes.length; i+= 2) {
            for (int j = 0; j < enlargedPipes[0].length; j+= 2) {
                Pipe pipe = enlargedPipes[i][j];
                if(pipe.isOutsideLeftLoop && !pipe.isInLoop){
                    System.out.print("O");
                } else {
                    System.out.print(pipe.c);
                }
                if(!pipe.isOutsideLeftLoop && pipe.c == '.'){
                    count++;
                }

            }
            System.out.print("\n");
        }

        List<Pipe> list = Arrays.stream(field).flatMap(Arrays::stream).toList();
        String answer = "" + count;
        showAnswer(answer, expectedOutput, startTime);
    }

    private static List<Direction> findDirectionForStart(Pipe start, Pipe[][] field) {
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Pipe next = findNext(direction, start, field);
            if (next != null) {
                if (next.d().stream().anyMatch(d -> d == direction.getOpposite())) {
                    directions.add(direction);
                }
            }
        }
        return directions;
    }

    private static Direction findNextDirection(Direction from, Pipe pipe) {
        return pipe.d().stream().filter(d -> d != from.getOpposite()).findAny().orElseThrow();
    }

    private static Pipe findNext(Direction to, Pipe pipe, Pipe[][] field) {
        try {
            return switch (to) {
                case N -> field[pipe.y - 1][pipe.x];
                case S -> field[pipe.y + 1][pipe.x];
                case E -> field[pipe.y][pipe.x + 1];
                case W -> field[pipe.y][pipe.x - 1];
            };
        } catch (IndexOutOfBoundsException e) {
            //System.out.println("This element goes out the field - should only happen at start");
            return null;
        }
    }


    private static List<Direction> getDirections(char c) {
        return switch (c) {
            case '|' -> List.of(N, S);
            case '-' -> List.of(E, W);
            case 'L' -> List.of(N, E);
            case 'J' -> List.of(N, W);
            case '7' -> List.of(S, W);
            case 'F' -> List.of(S, E);
            default -> Collections.emptyList();

        };
    }

    public static void showAnswer(String answer, String expectedOutput, long startTime) {
        if (expectedOutput.equals("???")) {
            System.out.println("ACTUAL ANSWER");
            System.out.println("The actual output is : " + answer);
        } else {
            System.out.println("TEST CASE");
            System.out.println("Current answer = " + answer + ". Expected answer = " + expectedOutput);
            if (answer.equals(expectedOutput)) {
                System.out.println("CORRECT");
            } else {
                System.out.println("INCORRECT");
            }
        }
        System.out.println("Runtime: " + (System.currentTimeMillis() - startTime));
        System.out.println("-----------------------------------------------------------");
    }

}