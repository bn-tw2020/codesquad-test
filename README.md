# 구현과정 상세 설명

## 플레이어 이동 구현하기 

<details>
<summary>문제 및 요구사항</summary>
<div markdown="1">

* 2단계: 플레이어 이동 구현하기

* 1단계 스테이지 2의 지도를 읽고 사용자 입력을 받아서 캐릭터를 움직이게 하는 프로그램을 작성하시오.


입력 명령
```
- w: 위쪽
- a: 왼쪽
- s: 아래쪽
- d: 오른쪽
- q: 프로그램 종료
  요구사항
  처음 시작하면 스테이지 2의 지도를 출력한다.
  간단한 프롬프트 (예: SOKOBAN> )를 표시해 준다.
  하나 이상의 문자를 입력받은 경우 순서대로 처리해서 단계별 상태를 출력한다.
  벽이나 공등 다른 물체에 부딪히면 해당 명령을 수행할 수 없습니다 라는 메시지를 출력하고 플레이어를 움직이지 않는다.
  동작 예시
  Stage 2

  #######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
#   O  # 
########

SOKOBAN> ddzw (엔터)

#######
###  O  ###
#    o    #
# Oo  PoO #
###  o  ###
#   O  # 
########

D: 오른쪽으로 이동합니다.

#######
###  O  ###
#    o    #
# Oo  PoO #
###  o  ###
#   O  # 
########

D: (경고!) 해당 명령을 수행할 수 없습니다!

#######
###  O  ###
#    o    #
# Oo  PoO #
###  o  ###
#   O  # 
########

Z: (경고!) 해당 명령을 수행할 수 없습니다!

#######
###  O  ###
#    o    #
# Oo  PoO #
###  o  ###
#   O  # 
########

W: 위로 이동합니다.

SOKOBAN> q
Bye~
```
2단계 코딩 요구사항

너무 크지 않은 함수 단위로 구현하고 중복된 코드를 줄이도록 노력한다.

마찬가지로 Readme.md 파일과 작성한 소스 코드를 모두 기존 secret gist에 올려야 한다.
전역변수의 사용을 자제한다.
객체 또는 배열을 적절히 활용한다.

</div>
</details>


## 구조

📕 목차
1. Player Class
2. Sokoban

## Player Class

1. 소코반 맵에서 플레이어를 관리할 객체

```java
public class Player {

  private int y, x;

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

## Sokoban Class

1. 소코반 게임을 시작한다. 스테이지 2를 2차원 배열로 변환 후, 사용자로부터 명령어를 입력받아 움직인다.
```java
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
```

2. 문자열로 입력받은 맵을 2차원 배열에 숫자로 변환 시켜 준다.

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
                else playMap[i-1][j] = 7;
            }
        }
        return playMap;
    }
```

3. 스테이지 1번, 스테이지 2번을 설정한다.

```java
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
```

4. 맵에 대해서 기본 데이터를 정리하고 초기 플레이어 위치를 설정한다.
```java
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
    }
```

5. 문자열로 입력 받은 맵을 2차원 배열로 변환하고, 스테이지 단을 출력한다.
```java
    private int[][] getPlayMap(ArrayList<ArrayList<String>> map) {
        int[][] playMap = convertMap(map.get(1));
        System.out.println(map.get(1).get(0));
        return playMap;
    }
```

6. 2차원 배열을 기본으로 맵을 문자로 표기해준다.
```java
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
```

7. 플레이어가 갈 수 있는 범위를 확인한다.
```java
private boolean canGo(int y, int x, int n, int m, int[][] playMap) {
    if (y >= 0 && x >= 0 && y < n && x < m && playMap[y][x] != 0 && playMap[y][x] != 2) return true;
    return false;
}
```

8. 플레이어가 상하좌우 명령어를 받아 움직임과 처리한다.
```java
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
```