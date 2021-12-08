# 구현과정 상세 설명

## 지도 데이터 출력하기 

<details>
<summary>문제 및 요구사항</summary>
<div markdown="1">

* 1단계: 지도 데이터 읽어서 2차원 배열에 저장하고 화면에 출력하기

1. 입력: 아래 내용을 문자열로 넘겨서 처리하는 함수를 작성한다.
```
Stage 1
#####
#OoP#
#####
=====
Stage 2
#######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
#   O  # 
########
```
2. 위 값을 읽어 2차원 배열로 변환 저장한다.

```
#	벽(Wall)	0
O	구멍(Hall)	1
o	공(Ball)	2
P	플레이어(Player)	3
=	스테이지 구분	4
```

3. 출력할 내용

* 아래와 같은 형태로 각 스테이지 정보를 출력한다.
  * 플레이어 위치는 배열 [0][0]을 기준으로 처리한다
    * 아래 출력 예시와 상관없이 기준에 맞춰서 얼마나 떨어진지 표시하면 된다
  * 스테이지 구분값은 출력하지 않는다

```
Stage 1

#####
#OoP#
#####

가로크기: 5
세로크기: 3
구멍의 수: 1
공의 수: 1
플레이어 위치 (2, 4)

Stage 2

#######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
#   O  # 
########

가로크기: 11
세로크기: 7
구멍의 수: 4
공의 수: 4
플레이어 위치 (4, 6)
```

1단계 코딩 요구사항

컴파일 또는 실행이 가능해야 한다. (컴파일이나 실행되지 않을 경우 감점 대상)
gist는 하위 폴더 구조를 지원하지 않기 때문에 컴파일 또는 실행에 필요한 소스 코드는 모두 포함하고, 프로젝트 파일 등은 포함하지 않아도 된다.
자기만의 기준으로 최대한 간결하게 코드를 작성한다.

Readme.md에 풀이 과정 및 코드 설명, 실행 결과를 기술하고 코드와 같이 gist에 포함해야 한다.
제출시 gist URL과 revision 번호를 함께 제출한다.

</div>
</details>


## 구조

📕 목차
1. SokobanApplication
2. Sokoban

## SokobanApplication Class

1. 소코반 클래스를 불러 게임을 시작한다.

```java
public class sokobanApplication {
    public static void main(String[] args) {
        Sokoban sokoban = new Sokoban();
        sokoban.start();
    }
}
```

## Sokoban Class

1. 소코반 스테이지를 맵으로 입력 받는다.
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

3. 변환 받은 2차원 배열을 통해 가로, 세로, 구멍, 공, 플레이어의 위치를 출력한다.

```java
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
```