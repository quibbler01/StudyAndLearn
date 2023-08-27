package cn.quibbler.codetop.algorithm;

public class CountPoints {

    public int countPoints(String rings) {
        int[][] cnts = new int[10][3];

        for (int i = 0; i < rings.length() / 2;i++) {
            char color = rings.charAt(i*2);
            int index = rings.charAt(i*2+1) - '0';
            System.out.println("color = " + color + " index = " + index);
            if (color == 'R') {
                cnts[index][0] = 1;
            } else if (color == 'G') {
                cnts[index][1] = 1;
            }else if (color == 'B') {
                cnts[index][2] = 1;
            }
        }

        int cnt = 0;
        for (int[] c : cnts) {
            if (c[0] == 1 && c[1] == 1 && c[2] == 1) cnt++;
        }
        return cnt;
    }

}
