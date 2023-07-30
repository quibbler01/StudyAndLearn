package cn.quibbler.codetop.acm;

import java.util.*;

public class MaxSub {


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        int[] arr = new int[n];
        while (n > 0) {
            arr[n - 1] = in.nextInt();
            --n;
        }

        Arrays.sort(arr);

        System.out.println(arr[arr.length - 1] - arr[0]);
    }

}
