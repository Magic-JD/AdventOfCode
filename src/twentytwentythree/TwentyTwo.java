package twentytwentythree;

import tools.InternetParser;

import java.util.*;

public class TwentyTwo {

    public static final String testData = """      
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
                                           """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(22);
        run(testInput, "7", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        var answer = "" + answer(input);
        showAnswer(answer, expectedOutput, startTime);
    }

    private static long answer(List<String> input) {
        List<BrickCoordinates> bricksCoords = input.stream()
                .map(s -> Arrays.stream(s.split("~"))
                        .flatMap(s2 -> Arrays.stream(s2.split(",")))
                        .mapToInt(Integer::parseInt).toArray())
                .map(arr -> new BrickCoordinates(Math.min(arr[0], arr[3]), Math.min(arr[1], arr[4]), Math.min(arr[2], arr[5]), Math.max(arr[0], arr[3]), Math.max(arr[1], arr[4]), Math.max(arr[2], arr[5])))
                .toList();
        int maxX = bricksCoords.stream().mapToInt(b -> Math.max(b.x, b.endX)).max().orElseThrow() + 1;
        int maxY = bricksCoords.stream().mapToInt(b -> Math.max(b.y, b.endY)).max().orElseThrow() + 1;
        int maxZ = bricksCoords.stream().mapToInt(b -> Math.max(b.z, b.endZ)).max().orElseThrow() + 1;
        Brick[][][] cube = new Brick[maxX][maxY][maxZ];
        List<Brick> bricks = new ArrayList<>();
        for (int i = 1; i <= bricksCoords.size(); i++) {
            BrickCoordinates brickCoordinates = bricksCoords.get(i - 1);
            ArrayList<Coordinate> coordinates = new ArrayList<>();
            Brick brick = new Brick(coordinates);
            bricks.add(brick);
            int x = brickCoordinates.x;
            int y = brickCoordinates.y;
            int z = brickCoordinates.z;
            if (x != brickCoordinates.endX) {
                for (int j = x; j <= brickCoordinates.endX; j++) {
                    cube[j][y][z] = brick;
                    coordinates.add(new Coordinate(j, y, z));
                }
            } else if (y != brickCoordinates.endY) {
                for (int j = y; j <= brickCoordinates.endY; j++) {
                    cube[x][j][z] = brick;
                    coordinates.add(new Coordinate(x, j, z));
                }
            } else if (z != brickCoordinates.endZ) {
                for (int j = z; j <= brickCoordinates.endZ; j++) {
                    cube[x][y][j] = brick;
                    coordinates.add(new Coordinate(x, y, j));
                }
            } else {
                cube[x][y][z] = brick;
                coordinates.add(new Coordinate(x, y, z));
            }
        }
        for (int i = 0; i < maxZ; i++) {
            for (int j = 0; j < maxX; j++) {
                for (int k = 0; k < maxY; k++) {
                    Brick brick = cube[j][k][i];
                    if(brick != null){
                        while (brick.canFall(cube)){
                            brick.fall(cube);
                        }
                    }
                }
            }
        }
        for (Brick brick : bricks) {
           if(brick.canFall(cube)){
               throw new RuntimeException();
           }
           brick.setMemory();
        }

        int totalBricksFalling = 0;
        for (Brick brick : bricks) {
            brick.remove(cube);
            List<Brick> modified = new ArrayList<>();
            modified.add(brick);
            PriorityQueue<Brick> lowestFirst = new PriorityQueue<>((o1, o2) -> {
                int min1 = o1.coordinates.stream().mapToInt(c -> c.coords[2]).min().orElseThrow();
                int min2 =  o2.coordinates.stream().mapToInt(c -> c.coords[2]).min().orElseThrow();
                return min1 - min2;
            });
            lowestFirst.addAll(brick.above(cube));
            while (!lowestFirst.isEmpty()){
                Brick b = lowestFirst.poll();
                if(b.canFall(cube)){
                    modified.add(b);
                    totalBricksFalling++;
                    lowestFirst.addAll(b.above(cube));
                    while (b.canFall(cube)){
                        b.fall(cube);
                    }
                }
            }
            modified.forEach(b -> b.remove(cube));
            modified.forEach(Brick::timeTravel);
            modified.forEach(b -> b.replace(cube));
        }

        return totalBricksFalling;
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

    private static class Coordinate {
        private final int[] coords;
        private final int[] rememberCoords;

        public Coordinate(int x, int y, int z) {
            coords = new int[]{x, y, z};
            rememberCoords = new int[]{x, y, z};
        }

        public void fall(Brick[][][] jenga, Brick brick) {
            jenga[coords[0]][coords[1]][coords[2]] = null;
            coords[2]--;
            jenga[coords[0]][coords[1]][coords[2]] = brick;
        }

        public void remove(Brick[][][] jenga) {
            jenga[coords[0]][coords[1]][coords[2]] = null;
        }

        public void replace(Brick[][][] jenga, Brick brick) {
            jenga[coords[0]][coords[1]][coords[2]] = brick;
        }

        public void setRememberCoords(){
            rememberCoords[0] = coords[0];
            rememberCoords[1] = coords[1];
            rememberCoords[2] = coords[2];
        }

        public void timeTravel() {
            coords[0] = rememberCoords[0];
            coords[1] = rememberCoords[1];
            coords[2] = rememberCoords[2];
        }
    }

    private static class Brick {
        List<Coordinate> coordinates;

        public Brick(List<Coordinate> coordinates) {
            this.coordinates = coordinates;
        }

        public void fall(Brick[][][] jenga) {
            coordinates.forEach(c -> c.fall(jenga, this));
        }

        public void remove(Brick[][][] jenga) {
            coordinates.forEach(c -> c.remove(jenga));
        }

        public void replace(Brick[][][] jenga) {
            coordinates.forEach(c -> c.replace(jenga, this));
        }

        public boolean canFall(Brick[][][] jenga) {
            if (coordinates.stream().mapToInt(c -> c.coords[2]).anyMatch(i -> i == 1)) {
                return false;
            }
            return coordinates.stream().allMatch(coords -> {
                Brick brick = jenga[coords.coords[0]][coords.coords[1]][coords.coords[2] - 1];
                return brick == null || brick == this;
            });
        }

        public List<Brick> above(Brick[][][] jenga) {
            return coordinates.stream()
                    .map(coords -> jenga[coords.coords[0]][coords.coords[1]][coords.coords[2] + 1])
                    .filter(Objects::nonNull)
                    .filter(brick -> !brick.equals(this))
                    .toList();
        }

        public void setMemory(){
            coordinates.forEach(Coordinate::setRememberCoords);
        }

        public void timeTravel(){
            for (Coordinate coordinate : coordinates) {
               coordinate.timeTravel();
            }
        }

    }

    private record BrickCoordinates(int x, int y, int z, int endX, int endY, int endZ) {
    }

}