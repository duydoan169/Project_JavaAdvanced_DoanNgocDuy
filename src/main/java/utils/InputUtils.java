package utils;

import java.util.Scanner;

public class InputUtils {
    static Scanner sc = new Scanner(System.in);
    public static String readLine(String message){
        System.out.print(message);
        return sc.nextLine().trim();
    }
}
