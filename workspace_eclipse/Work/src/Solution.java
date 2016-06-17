/* ********************************************************************************
 * All rights reserved.
 ******************************************************************************* */

public class Solution {
    public static int answer(long x) {

        if (x > 9 || x < -9) {
            String stringValue = String.valueOf(x);
            long sum = 0;
            for (char ch : stringValue.toCharArray()) {
                sum = sum + Integer.parseInt(ch + "");
            }
            return answer(sum);
        } else {
            return (int) x;
        }
    }

    public static int answer(int x) {
        if (x > 9 || x < -9) {
            String stringValue = String.valueOf(x);
            int sum = 0;
            for (char ch : stringValue.toCharArray()) {
                sum = sum + Integer.parseInt(ch + "");
            }
            return answer(sum);
        } else {
            return (int) x;
        }

    }

    public static void main(String[] args) {
        System.out.println(answer(Long.MAX_VALUE));
    }
}
