package cn.quibbler.codetop;

import java.text.NumberFormat;

public class CodeTop {

    public static void main(String[] args) {
        int cnt = 0;
        for (int i = 5; i <= 100; i += 5) {
            ++cnt;
            if (i % 25 == 0) {
                ++cnt;
            }
        }
        //System.out.println("cnt = " + cnt);


        Math.max(2, 3);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(false);

        System.out.println("stringAdd() = " + stringAdd("0", "999"));

        System.out.printf("%.3f",99.4444343425244);
    }


    private static String stringAdd(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int len = Math.max(num1.length(), num2.length());
        int add = 0;
        for (int i = 0; i < len; ++i) {
            int a = 0, b = 0;
            if (i < num1.length()) {
                a = num1.charAt(num1.length() - 1 - i) - '0';
            }
            if (i < num2.length()) {
                b = num2.charAt(num2.length() - 1 - i) - '0';
            }
            int sum = a + b + add;
            if (sum >= 10) {
                add = sum / 10;
                sum %= 10;
            } else {
                add = 0;
            }
            sb.insert(0, sum);
        }

        if (add > 0) {
            sb.insert(0, add);
        }

        return sb.toString();
    }

}