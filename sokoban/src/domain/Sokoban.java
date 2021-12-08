package domain;


import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Sokoban {

    static Player player = new Player();

    public void start() {
        System.out.println("소코반의 세계의 오신 것을 환영합니다!");
        System.out.println("^오^\n");
        ArrayList<ArrayList<String>> map = readMap();

        for (int stage = 0; stage < map.size(); stage++) {

            int[][] playMap = getPlayMap(map, stage);
            printMapInformation(playMap);
            int n = playMap.length;
            int m = playMap[0].length;

            int[][] defaultMap = getDefaultMap(playMap, n, m);
            int defaultY = player.getY(), defaultX = player.getX();
            player.setTurn(0);

            printMap(playMap);
            while(true) {
                System.out.print("SOKOBAN> ");
                Scanner scan = new Scanner(System.in);
                String input = scan.next();

                if(input.equals("r")) {
                    resetMap(playMap, n, m, defaultMap);
                    player.setY(defaultY); player.setX(defaultX);
                    printMap(playMap);
                    System.out.println("스테이지를 초기화 했습니다.");
                    continue;
                }
                else if(input.equals("q")) {
                    System.out.println("Bye~");
                    return;
                }
                move(playMap, n, m, input, map, stage);
                if(GameEnd(playMap, n, m)) break;
            }
        }
        System.out.println("전체 게임을 클리어하셨습니다!");
        System.out.println("축하드립니다!");
    }

    private boolean GameEnd(int[][] playMap, int n, int m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(playMap[i][j] == 2) return false;
            }
        }
        return true;
    }

    private void resetMap(int[][] playMap, int n, int m, int[][] defaultMap) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                playMap[i][j] = defaultMap[i][j];

        printMapInformation(playMap);
    }

    private int[][] getDefaultMap(int[][] playMap, int n, int m) {
        int[][] defaultMap = new int[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                defaultMap[i][j] = playMap[i][j];
        return defaultMap;
    }

    private void move(int[][] playMap, int n, int m, String input, ArrayList<ArrayList<String>> map, int stage) {
        for (int i = 0; i < input.length(); i++) {
            char command = input.charAt(i);

            if(command == 'd') { // 오른쪽
                int ny = player.getY();
                int nx = player.getX() + 1;
                if(canGo(ny, nx, n, m, playMap)) {
                    if(playMap[ny][nx] == 1) {
                        int nny = ny;
                        int nnx = nx + 1;
                        if(playMap[nny][nnx] == 1) {
                            printMap(playMap);
                            System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                            continue;
                        }
                    }
                    player.setTurn((player.getTurn() + 1));
                    if(playMap[ny][nx] == 2) { // o
                        int nny = ny;
                        int nnx = nx + 1;
                        if(playMap[nny][nnx] == 1) {
                            playMap[nny][nnx] = 9;
                            playMap[ny][nx] = 7;
                        }
                        else if(playMap[nny][nnx] == 7) {
                            playMap[nny][nnx] = 2;
                            playMap[ny][nx] = 7;
                        }
                    }
                    else if(playMap[ny][nx] == 9) {
                        int nny = ny;
                        int nnx = nx + 1;
                        if(playMap[nny][nnx] == 0 || playMap[nny][nnx] == 9 || playMap[nny][nnx] == 1) {
                            player.setTurn((player.getTurn() - 1));
                            printMap(playMap);
                            System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                            continue;
                        }
                        playMap[nny][nnx] = 2;
                        playMap[ny][nx] = 1;
                    }
                    player.setX(nx);
                    printMap(playMap);
                    if(GameEnd(playMap, n, m)) {
                        System.out.println("빠밤! " + map.get(stage).get(0) + " 클리어!");
                        System.out.println("턴수: " + player.getTurn());
                        System.out.println();
                        return;
                    }
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
                    if(playMap[ny][nx] == 1) {
                        int nny = ny + 1;
                        int nnx = nx;
                        if(playMap[nny][nnx] == 1) {
                            printMap(playMap);
                            System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                            continue;
                        }
                    }
                    player.setTurn((player.getTurn() + 1));
                    if(playMap[ny][nx] == 2) { // o
                        int nny = ny + 1;
                        int nnx = nx;
                        if(playMap[nny][nnx] == 1) {
                            playMap[nny][nnx] = 9;
                            playMap[ny][nx] = 7;
                        }
                        else if(playMap[nny][nnx] == 7) {
                            playMap[nny][nnx] = 2;
                            playMap[ny][nx] = 7;
                        }
                    }
                    else if(playMap[ny][nx] == 9) {
                        int nny = ny + 1;
                        int nnx = nx;
                        if(playMap[nny][nnx] == 0 || playMap[nny][nnx] == 9 || playMap[nny][nnx] == 1) {
                            player.setTurn((player.getTurn() - 1));
                            printMap(playMap);
                            System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                            continue;
                        }
                        playMap[nny][nnx] = 2;
                        playMap[ny][nx] = 1;
                    }
                    player.setY(ny);
                    printMap(playMap);
                    if(GameEnd(playMap, n, m)) {
                        System.out.println("빠밤! " + map.get(stage).get(0) + " 클리어!");
                        System.out.println("턴수: " + player.getTurn());
                        System.out.println();
                        return;
                    }
                    System.out.println(Character.toUpperCase(command) + ": 아래쪽으로 이동합니다.");
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
                    if(playMap[ny][nx] == 1) {
                        int nny = ny;
                        int nnx = nx - 1;
                        if(playMap[nny][nnx] == 1) {
                            printMap(playMap);
                            System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                            continue;
                        }
                    }
                    player.setTurn((player.getTurn() + 1));
                    if(playMap[ny][nx] == 2) { // o
                        int nny = ny;
                        int nnx = nx - 1;
                        if(playMap[nny][nnx] == 1) {
                            playMap[nny][nnx] = 9;
                            playMap[ny][nx] = 7;
                        }
                        else if(playMap[nny][nnx] == 7) {
                            playMap[nny][nnx] = 2;
                            playMap[ny][nx] = 7;
                        }
                    }
                    else if(playMap[ny][nx] == 9) {
                        int nny = ny;
                        int nnx = nx - 1;
                        if(playMap[nny][nnx] == 0 || playMap[nny][nnx] == 9 || playMap[nny][nnx] == 1) {
                            player.setTurn((player.getTurn() - 1));
                            printMap(playMap);
                            System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                            continue;
                        }
                        playMap[nny][nnx] = 2;
                        playMap[ny][nx] = 1;
                    }
                    player.setX(nx);
                    printMap(playMap);
                    if(GameEnd(playMap, n, m)) {
                        System.out.println("빠밤! " + map.get(stage).get(0) + " 클리어!");
                        System.out.println("턴수: " + player.getTurn());
                        System.out.println();
                        return;
                    }
                    System.out.println(Character.toUpperCase(command) + ": 왼쪽으로 이동합니다.");
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
                    if(playMap[ny][nx] == 1) {
                        int nny = ny - 1;
                        int nnx = nx;
                        if(playMap[nny][nnx] == 1) {
                            printMap(playMap);
                            System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                            continue;
                        }
                    }
                    player.setTurn((player.getTurn() + 1));
                    if(playMap[ny][nx] == 2) { // o
                        int nny = ny - 1;
                        int nnx = nx;
                        if(playMap[nny][nnx] == 1) {
                            playMap[nny][nnx] = 9;
                            playMap[ny][nx] = 7;
                        }
                        else if(playMap[nny][nnx] == 7) {
                            playMap[nny][nnx] = 2;
                            playMap[ny][nx] = 7;
                        }
                    }
                    else if(playMap[ny][nx] == 9) {
                        int nny = ny - 1;
                        int nnx = nx;
                        if(playMap[nny][nnx] == 0 || playMap[nny][nnx] == 9 || playMap[nny][nnx] == 1) {
                            player.setTurn((player.getTurn() - 1));
                            printMap(playMap);
                            System.out.println(Character.toUpperCase(command) + ": (경고!) 해당 명령을 수행할 수 없습니다!");
                            continue;
                        }
                        playMap[nny][nnx] = 2;
                        playMap[ny][nx] = 1;
                    }
                    player.setY(ny);
                    printMap(playMap);
                    if(GameEnd(playMap, n, m)) {
                        System.out.println("빠밤! " + map.get(stage).get(0) + " 클리어!");
                        System.out.println("턴수: " + player.getTurn());
                        System.out.println();
                        return;
                    }
                    System.out.println(Character.toUpperCase(command) + ": 위쪽으로 이동합니다.");
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
        if (y >= 0 && x >= 0 && y < n && x < m && playMap[y][x] != 0) return true;
        return false;
    }

    private void printMap(int[][] playMap) {
        System.out.println();
        for (int i = 0; i < playMap.length; i++) {
            for (int j = 0; j < playMap[i].length; j++) {
                if(playMap[i][j] == 0) System.out.print('#');
                else if(i == player.getY() && j == player.getX()) System.out.print('P');
                else if(playMap[i][j] == 1) System.out.print('O');
                else if(playMap[i][j] == 2) System.out.print('o');
                else if(playMap[i][j] == 9) System.out.print('0');
                else  System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }

    private int[][] getPlayMap(ArrayList<ArrayList<String>> map, int stage) {
        int[][] playMap = convertMap(map.get(stage));
        System.out.println(map.get(stage).get(0));
        return playMap;
    }

    private void printMapInformation(int[][] playMap) {
        for (int j = 0; j < playMap.length; j++) {
            for (int k = 0; k < playMap[j].length; k++) {
                if(playMap[j][k] == 3) {
                    player.setY(j);
                    player.setX(k);
                    playMap[j][k] = 7;
                }
            }
        }
    }

    private ArrayList<ArrayList<String>> readMap() {
        ArrayList<ArrayList<String>> map = new ArrayList<ArrayList<String>>();

        File file = new File("sokoban/src/map.txt");
        int idx = -1;
        try{
            BufferedReader inFiles = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF8"));
            String line = "";
            while((line = inFiles.readLine()) != null) {
                if(line.trim().length() > 0) {
                    if(line.contains("Stage")) {
                        map.add(new ArrayList<String>());
                        idx++;
                        map.get(idx).add(line);
                    }
                    else {
                        map.get(idx).add(line);
                    }
                }
            }
            inFiles.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                else if (c == '0') playMap[i-1][j] = 9;
                else playMap[i-1][j] = 7;
            }
        }
        return playMap;
    }

}
