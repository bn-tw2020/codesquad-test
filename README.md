# 구현과정 상세 설명

## 추가 기능구현

<details>
<summary>문제 및 요구사항</summary>
<div markdown="1">

* 4단계: 추가기능 구현

* 다양한 추가기능을 구현해본다.

  전부다 구현하지 않아도 무방하다.

  https://www.cbc.ca/kids/games/play/sokoban 를 참고하자.

## 요구사항


### 저장하기 불러오기 기능

1. 1 - 5: 세이브 슬롯 1 - 5 선택
2. S 키로 현재 진행상황을 저장한다.
3. L 키로 세이브 슬롯에서 진행상황을 불러온다.

```
S> 2S
2번 세이브 슬로 상태
2번 세이브에 진행상황을 저장합니다.
S> 3L
3번 세이브에서 진행상황을 불러옵니다.
```

### 지도 데이터 변환하기 프로그램

1. 지도 데이터 map.txt 를 읽어서 일반 텍스트 에디터로 읽을 수 없는 map_enc.txt로 변환하는 프로그램을 추가로 작성한다.
2. 3 단계에서 구현한 게임이 map.txt 가 아닌 map_enc.txt 를 불러와서 실행할 수 있도록 수정한다.

### 되돌리기 기능 및 되돌리기 취소 기능 구현
1. u키를 누르면 한 턴 되돌리기, U키를 누르면 되돌리기 취소하기를 구현한다.

</div>
</details>


## 구조

📕 목차
1. sokobanApplication
2. Player Class
3. Sokoban Class

## sokobanApplication Class

1. 소코반 프로젝트를 시작한다.

```java
public class sokobanApplication {
    public static void main(String[] args) {
        Sokoban sokoban = new Sokoban();
        sokoban.start();
    }
}
```

## Player Class

1. 소코반에서 플레이어를 관리하 위치, 턴 객체

```java
package domain;

public class Player {

    private int y, x;
    private int turn;

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
```

### Sokoban Class

1. 게임 캐릭터는 고정적으로 움직여서 때문에 전역으로 설정한다.

```java
static Player player = new Player();
```

2. 소코반 게임 메인로직이 시작되는 부분이다.

  스테이지 별로 깰 수 있으며, 최대 5 스테이지이다.

```java
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
```

3. 스테이지에서 게임이 끝났는지 확인한다.

  상자가 하나라도 있으면 게임이 끝나지 않는다.

```java
    private boolean GameEnd(int[][] playMap, int n, int m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(playMap[i][j] == 2) return false;
            }
        }
        return true;
    }
```

4. 초기 맵으로 복원시키기 위해 맵을 복사한다.

```java
    private void resetMap(int[][] playMap, int n, int m, int[][] defaultMap) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                playMap[i][j] = defaultMap[i][j];

        printMapInformation(playMap);
    }
```

5. 원본 맵(스테이지)를 r버튼으로 불러올 수 있게 저장한다.

```java
    private int[][] getDefaultMap(int[][] playMap, int n, int m) {
        int[][] defaultMap = new int[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                defaultMap[i][j] = playMap[i][j];
        return defaultMap;
    }
```

6. 플레이어가 a(왼쪽), s(아래), d(오른쪽), w(위쪽)으로 움직일 수 있도록 설정한다.

  두 개의 상자가 연속적인 경우, O, o를 분리 시키는 법 등의 메인 로직이 담겨 있다.

```java
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
```

7. 맵에서 플레이어가 움직일 수 있는지 확인한다.

```java
    private boolean canGo(int y, int x, int n, int m, int[][] playMap) {
        if (y >= 0 && x >= 0 && y < n && x < m && playMap[y][x] != 0) return true;
        return false;
    }
```

8. 움직일 때 마다 스테이지 현재 상태를 출력한다.

```java
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
```

9. 2차원 배열로 변환된 맵을 가져오고, 스테이지 단계를 출력한다.

```java
    private int[][] getPlayMap(ArrayList<ArrayList<String>> map, int stage) {
        int[][] playMap = convertMap(map.get(stage));
        System.out.println(map.get(stage).get(0));
        return playMap;
    }
```

10. 스테이지의 첫 플레이어의 위치를 찾고, 빈 곳으로 설정한다.

```java
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
```

11. 텍스트(text)파일로 저장된 맵(스테이지)를 불러온다.

```java
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
```

12. 2차원 배열로 문자열 맵을 변환시킨다.

```java
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
```