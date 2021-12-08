package domain;


import java.util.ArrayList;
import java.util.Scanner;

public class Sokoban {

    static Player player = new Player();

    public void start() {
        ArrayList<ArrayList<String>> map = setUp();
        int[][] playMap = getPlayMap(map);
        printMapInformation(map, playMap);
        int n = playMap.length;
        int m = playMap[0].length;

        printMap(playMap);
        while(true) {
            System.out.print("SOKOBAN> ");
            Scanner scan = new Scanner(System.in);
            String input = scan.next();

            if(input.equals("q")) {
                System.out.println("Bye~");
                return;
            }
            move(playMap, n, m, input);
        }

    }

    private void move(int[][] playMap, int n, int m, String input) {
        for (int i = 0; i < input.length(); i++) {
            char command = input.charAt(i);
            if(command == 'd') { // 오른쪽
                int ny = player.getY();
                int nx = player.getX() + 1;
                if(canGo(ny, nx, n, m, playMap)) {
                    playMap[player.getY()][player.getX()] = 7;
                    player.setX(nx);
                    playMap[ny][nx] = 3;
                    printMap(playMap);
                    System.out.println(Character.toUpperCase(command) + ": 오른쪽으로 이동합니다.");
                }
                else {
                    printMap(playMap);
                    System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                }
            }

            else if(command == 's') { // 아래
                int ny = player.getY() + 1;
                int nx = player.getX();
                if(canGo(ny, nx, n, m, playMap)) {
                    playMap[player.getY()][player.getX()] = 7;
                    player.setY(ny);
                    playMap[ny][nx] = 3;
                    printMap(playMap);
                    System.out.println(Character.toUpperCase(command) + ": 아래쪽으 이동합니다.");
                }
                else {
                    printMap(playMap);
                    System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                }
            }

            else if(command == 'a') { // 왼쪽
                int ny = player.getY();
                int nx = player.getX() - 1;
                if(canGo(ny, nx, n, m, playMap)) {
                    playMap[player.getY()][player.getX()] = 7;
                    player.setX(nx);
                    playMap[ny][nx] = 3;
                    printMap(playMap);
                    System.out.println(Character.toUpperCase(command) + ": 오른쪽으로 이동합니다.");
                }
                else {
                    printMap(playMap);
                    System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                }
            }

            else if(command == 'w') { // 위쪽
                int ny = player.getY() - 1;
                int nx = player.getX();
                if(canGo(ny, nx, n, m, playMap)) {
                    playMap[player.getY()][player.getX()] = 7;
                    player.setY(ny);
                    playMap[ny][nx] = 3;
                    printMap(playMap);
                    System.out.println(Character.toUpperCase(command) + ": 오른쪽으로 이동합니다.");
                }
                else {
                    printMap(playMap);
                    System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                }
            }
            else {
                printMap(playMap);
                System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
            }
        }
    }

    private boolean canGo(int y, int x, int n, int m, int[][] playMap) {
        if (y >= 0 && x >= 0 && y < n && x < m && playMap[y][x] != 0 && playMap[y][x] != 2) return true;
        return false;
    }

    private void printMap(int[][] playMap) {
        System.out.println();
        for (int i = 0; i < playMap.length; i++) {
            for (int j = 0; j < playMap[i].length; j++) {
                if(playMap[i][j] == 0) System.out.print('#');
                else if(playMap[i][j] == 1) System.out.print('O');
                else if(playMap[i][j] == 2) System.out.print('o');
                else if(playMap[i][j] == 3) System.out.print('P');
                else  System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }

    private int[][] getPlayMap(ArrayList<ArrayList<String>> map) {
        int[][] playMap = convertMap(map.get(1));
        System.out.println(map.get(1).get(0));
        return playMap;
    }

    private void printMapInformation(ArrayList<ArrayList<String>> map, int[][] playMap) {
        int hole = 0, ball = 0, y = -1, x = -1;
        for (int j = 0; j < playMap.length; j++) {
            for (int k = 0; k < playMap[j].length; k++) {
                if(playMap[j][k] == 1) hole++;
                else if(playMap[j][k] == 2) ball++;
                else if(playMap[j][k] == 3) {
                    player.setY(j);
                    player.setX(k);
                }
            }
        }
//        System.out.println("가로크기: " + map.get(1).get(1).length());
//        System.out.println("세로크기: " + (map.get(1).size() - 1));
//        System.out.println("구멍의 수 " + hole);
//        System.out.println("공의 수 " + ball);
//        System.out.println("플레이어 위치 (" + y + ", " + x + ")");
//        System.out.println();
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
