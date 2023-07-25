import java.util.Scanner;
public class Transaction {
    float amt;
    Scanner sc = new Scanner(System.in);
    public void withdraw(User u) {
        System.out.println("Enter the amount to withdraw:");
        this.amt = sc.nextInt();
        if (u.balance < this.amt) {
            System.out.println("INSUFFICIENT BALANCE");
            return;
        }
        u.balance = u.balance - this.amt;
        System.out.println("DEBITED");
    }

    public float totalAmt(User u) {
        return u.balance;
    }

    public void credit(User u) {
        System.out.println("Enter the amount to credit:");
        this.amt = sc.nextInt();
        u.balance = u.balance + this.amt;
        System.out.println("CREDITED");
    }

    public float[] transfer(User u1, float balance) {
        float[] arr = new float[2];
        System.out.println("Enter amount: ");
        this.amt = sc.nextInt();
        if (u1.balance < this.amt) {
            System.out.println("Insufficient balance");
            arr[0] = u1.balance;
            arr[1] = balance;
            return arr;
        }
        balance = balance + this.amt;
        u1.balance = u1.balance - this.amt;
        arr[0] = u1.balance;
        arr[1] = balance;
        System.out.println("Transaction successful");
        return arr;
    }
}
