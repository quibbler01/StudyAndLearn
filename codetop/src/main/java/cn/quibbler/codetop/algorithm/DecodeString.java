package cn.quibbler.codetop.algorithm;

import java.util.Stack;

/**
 * <a href="https://leetcode.cn/problems/decode-string/description/">给定一个经过编码的字符串，返回它解码后的字符串。</a>
 */
public class DecodeString {

    public static void main(String[] args) {
        String ans = decodeString("3[a2[c]]");

        System.out.println("ans = " + ans);
    }

    /**
     * 输入：s = "3[a]2[bc]"
     * 输出："aaabcbc"
     *
     * @param s
     * @return
     */
    public static String decodeString(String s) {
        StringBuilder sb = new StringBuilder();

        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; ++i) {
            if (cs[i] != ']') {
                sb.append(cs[i]);
            } else {
                StringBuilder tmp = new StringBuilder();
                while (sb.length() > 0) {
                    char ct = sb.charAt(sb.length() - 1);
                    sb.deleteCharAt(sb.length() - 1);
                    if (ct == '[') {
                        break;
                    } else {
                        tmp.insert(0, ct);
                    }
                }
                StringBuilder repeatNumSB = new StringBuilder();
                while (sb.length() > 0) {
                    char ct = sb.charAt(sb.length() - 1);
                    if (ct >= '0' && ct <= '9') {
                        sb.deleteCharAt(sb.length() - 1);
                        repeatNumSB.insert(0, ct);
                    }else{
                        break;
                    }
                }
                int repeat = Integer.parseInt(repeatNumSB.toString());

                while (repeat > 0) {
                    sb.append(tmp.toString());
                    --repeat;
                }
            }
        }
        return sb.toString();
    }

}
