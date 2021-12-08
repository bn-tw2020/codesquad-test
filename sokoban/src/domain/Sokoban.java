package domain;


import java.util.ArrayList;

public class Sokoban {
    public void start() {
        ArrayList<ArrayList<String>> map = setUp();

        for (int i = 0; i < map.size(); i++) {
            int[][] playMap = convertMap(map.get(i));
            System.out.println(map.get(i).get(0));
            System.out.println();
            for (int j = 1; j < map.get(i).size(); j++) {
                System.out.println(map.get(i).get(j));
            }
            System.out.println();

            int hole = 0, ball = 0, y = -1, x = -1;
            for (int j = 0; j < playMap.length; j++) {
                for (int k = 0; k < playMap[j].length; k++) {
                    if(playMap[j][k] == 1) hole++;
                    else if(playMap[j][k] == 2) ball++;
                    else if(playMap[j][k] == 3) {
                        y = j; x = k;
                    }
                }
            }
            System.out.println("가로크기: " + map.get(i).get(1).length());
            System.out.println("세로크기: " + (map.get(i).size() - 1));
            System.out.println("구멍의 수 " + hole);
            System.out.println("공의 수 " + ball);
            System.out.println("플레이어 위치 (" + y + ", " + x + ")");
            System.out.println();
        }
    }

    private ArrayList<ArrayList<String>> setUp() {
        ArrayList<ArrayList<String>> map = new ArrayList<ArrayList<String>>();
        map.add(new ArrayList<String>());
        map.add(new ArrayList<String>());

        map.get(0).add("Stage 1");
        map.get(0).add("#####");
        map.get(0).add("#OoP#");
        map.get(0).add("#####");

        map.get(1).add("Stage 2");
        map.get(1).add("  #######  ");
        map.get(1).add("###  O  ###");
        map.get(1).add("#    o    #");
        map.get(1).add("# Oo P oO #");
        map.get(1).add("###  o  ###");
        map.get(1).add(" #   O  #  ");
        map.get(1).add(" ########  ");
        return map;
    }

    private int[][] convertMap(ArrayList<String> map) {
        int n = map.size() - 1;
        int m = map.get(1).length();
        int[][] playMap = new int[n][m];

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < m; j++) {
                char c = map.get(i).charAt(j);
                if (c == '#') playMap[i-1][j] = 0;
                else if (c == 'O') playMap[i-1][j] = 1;
                else if (c == 'o') playMap[i-1][j] = 2;
                else if (c == 'P') playMap[i-1][j] = 3;
                else playMap[i-1][j] = 7;
            }
        }
        return playMap;
    }

}
