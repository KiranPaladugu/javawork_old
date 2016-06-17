import java.util.*;

/* ********************************************************************************
 * All rights reserved.
 ******************************************************************************* */

public class AlphaNumericSortTest {
    public static void main(String args[]){
        String data[]={"R1A01","R2B05","R1A02","R1D05","R6A01","R1A05","R1B02"};
//        String data[]={"CXP123456/1","CXP123456","CXP234567","CXP987654","CXP659874","CXP34567","CXP123v67"};
        List<String> list = new ArrayList<String>();
        for(int i=0;i<data.length;i++){
            list.add(data[i]);
        }
        Collections.sort(list);
        System.out.println(list);
        
        final String first = "16c";
        final String second = "16dSP1";
        int result = first.compareToIgnoreCase(second);
        System.out.println(result +" :");
        System.out.println("RESULT:"+compare("RA1A01", "R96A01"));
    }
    
    public static int compare(Object firstObjToCompare, Object secondObjToCompare) {
        String firstString = firstObjToCompare.toString();
        String secondString = secondObjToCompare.toString();
 
        if (secondString == null || firstString == null) {
            return 0;
        }
 
        int lengthFirstStr = firstString.length();
        int lengthSecondStr = secondString.length();
 
        int index1 = 0;
        int index2 = 0;
 
        while (index1 < lengthFirstStr && index2 < lengthSecondStr) {
            char ch1 = firstString.charAt(index1);
            char ch2 = secondString.charAt(index2);
 
            char[] space1 = new char[lengthFirstStr];
            char[] space2 = new char[lengthSecondStr];
 
            int loc1 = 0;
            int loc2 = 0;
 
            do {
                space1[loc1++] = ch1;
                index1++;
 
                if (index1 < lengthFirstStr) {
                    ch1 = firstString.charAt(index1);
                } else {
                    break;
                }
            } while (Character.isDigit(ch1) == Character.isDigit(space1[0]));
 
            do {
                space2[loc2++] = ch2;
                index2++;
 
                if (index2 < lengthSecondStr) {
                    ch2 = secondString.charAt(index2);
                } else {
                    break;
                }
            } while (Character.isDigit(ch2) == Character.isDigit(space2[0]));
 
            String str1 = new String(space1);
            String str2 = new String(space2);
 
            int result;
 
            if (Character.isDigit(space1[0]) && Character.isDigit(space2[0])) {
                Integer firstNumberToCompare = new Integer(Integer
                        .parseInt(str1.trim()));
                Integer secondNumberToCompare = new Integer(Integer
                        .parseInt(str2.trim()));
                result = firstNumberToCompare.compareTo(secondNumberToCompare);
            } else {
                result = str1.compareTo(str2);
            }
 
            if (result != 0) {
                return result;
            }
        }
        return lengthFirstStr - lengthSecondStr;
    }
}
