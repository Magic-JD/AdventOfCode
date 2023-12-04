package tasks;

import tools.InternetParser;

import java.util.*;

import static java.lang.Math.max;
import static tools.LineUtils.extractInt;

public class Three {


    private static class JoeNumber{
        int number;
        public JoeNumber(int number){
            this.number = number;
        }
    }

    private static Map<String, JoeNumber> map = new HashMap<>();
    private static List<List<Integer>> stars = new ArrayList<>();

    /**
     *
     */
    public static void main(String[] args) {
        int sum = 0;
        List<String> strings = InternetParser.getInput(3);
        for(int i = 0; i < strings.size(); i++){
            int width = strings.get(0).length();
            for(int j = 0; j < width; j++){
                char c = strings.get(i).charAt(j);
                StringBuilder number = new StringBuilder();
                if(c == '*'){
                    stars.add(List.of(i, j));
                }
                if(c >= '0' && c <= '9') {
                    number.append(c);
                    j++;
                    while (j < width && strings.get(i).charAt(j) >= '0' && strings.get(i).charAt(j) <= '9') {
                        number.append(strings.get(i).charAt(j));
                        j++;
                    }
                    var no = number.toString();
                    JoeNumber value = new JoeNumber(extractInt(no));
                    j--;
                    for(int k = 0; k < no.length(); k++){
                        String s = ("" + i) + ":" + (j-k);
                        map.put(s, value);
                    }
                }
            }
        }
        for(List<Integer> locations: stars){
            Set<JoeNumber> ints = new HashSet<>();
            int i = locations.get(0);
            int j = locations.get(1);
            for (int k = j - 1; k <= j+1; k++) {
                for(int l = i - 1; l <= i+1; l++){
                    String s = l  + ":" +  k;
                    var num = map.get(s);
                    if(num != null){
                        ints.add(num);
                    }
                }
            }
            if(ints.size() == 2){
                List<JoeNumber> list = ints.stream().toList();
                sum += list.get(0).number * list.get(1).number;
            }
        }
        System.out.println(sum);
    }

}