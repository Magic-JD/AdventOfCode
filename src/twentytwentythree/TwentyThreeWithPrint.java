package twentytwentythree;

import tools.InternetParser;

import java.util.*;

public class TwentyThreeWithPrint {

    public static final String testData = """   
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
            """;


    /**
     *
     */
    public static void main(String[] args) {
        List<String> testInput = Arrays.stream(testData.split("\n")).toList();
        List<String> mainInput = InternetParser.getInput(23);
        run(testInput, "154", System.currentTimeMillis());
        run(mainInput, "???", System.currentTimeMillis());
    }

    public static void run(List<String> input, String expectedOutput, long startTime) {
        int height = input.size();
        int width = input.get(0).length();
        char[][] map = new char[height][width];
        for (int i = 0; i < height; i++) {
            String s = input.get(i);
            char[] charArray = s.toCharArray();
            System.arraycopy(charArray, 0, map[i], 0, charArray.length);
        }
        map[0][1] = '#';
        Node startNode = new Node(new HashSet<>(), new Coord(1, 1));
        ArrayDeque<Node> queue = new ArrayDeque<>(List.of(startNode));
        long longestPath = 0L;
        long currentWalk = 1L;
        List<Node> next = new ArrayList<>();
        while (!queue.isEmpty()) {
            Node node = queue.pop();
            //printMap(height, width, map, node);
            Coord coord = node.coord;
             if (coord.x == height - 1) {
                 longestPath = currentWalk;
             } else {
                List<Coord> coords = new ArrayList<>();
                if(map[coord.x-1][coord.y] != '#'){
                    Coord coord1 = new Coord(coord.x - 1, coord.y);
                    if(!node.currentlyVisited.contains(coord1)){
                        coords.add(coord1);
                    }
                }
                if(map[coord.x+1][coord.y] != '#'){
                    Coord coord1 = new Coord(coord.x + 1, coord.y);
                    if(!node.currentlyVisited.contains(coord1)){
                        coords.add(coord1);
                    }
                }
                if(map[coord.x][coord.y+1] != '#'){
                    Coord coord1 = new Coord(coord.x, coord.y + 1);
                    if(!node.currentlyVisited.contains(coord1)){
                        coords.add(coord1);
                    }
                }
                if(map[coord.x][coord.y-1] != '#'){
                    Coord coord1 = new Coord(coord.x, coord.y-1);
                    if(!node.currentlyVisited.contains(coord1)){
                        coords.add(coord1);
                    }
                }
                if(coords.size() == 1){
                    Coord coord1 = coords.get(0);
                    next.add(new Node(node.currentlyVisited, coord1));
                } else {
                    next.addAll(coords.stream().map(co -> new Node(new HashSet<>(node.currentlyVisited), co)).toList());
                }
            }
            if (queue.isEmpty()) {
                currentWalk += 1;
                queue.addAll(next);
                next = new ArrayList<>();
            }
        }

        var answer = "" + longestPath;
        showAnswer(answer, expectedOutput, startTime);
    }

    private static void printMap(int height, int width, char[][] map, Node node) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String s = String.valueOf(map[i][j]);
                Coord obj = new Coord(i, j);
                if(node.coord.equals(obj)){
                    s = "0";
                   s = (ANSI_RED + s + ANSI_RESET);
                } else if( node.currentlyVisited.contains(obj)){
                    s = "X";
                    s = (ANSI_BLUE + s + ANSI_RESET);
                }
                System.out.print(s);
            }
            System.out.print("\n");
        }
        System.out.println();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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


    public static final String ANSI_RESET = "\u001b[0m";
    public static final String ANSI_BLACK = "\u001b[30m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_YELLOW = "\u001b[33m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE = "\u001b[37m";


    private static class Node {
        private Set<Coord> currentlyVisited;
        private Coord coord;


        public Node(Set<Coord> previouslyVisited, Coord coord) {
            this.currentlyVisited = previouslyVisited;
            currentlyVisited.add(coord);
            this.coord=coord;
        }
    }

    private record Coord(int x, int y) {
    }
}