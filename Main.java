import java.util.Scanner;

public class Main {
   public static void main(String[] args) {
    Scanner sc=new Scanner(System.in);
    System.out.println("WELOME TO ATM MANAGEMENT SYSTEM");
    User u1=new User();
    u1.start();
    sc.close();
   }
}
