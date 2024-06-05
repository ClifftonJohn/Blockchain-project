public class Wallet {
    private double balance;

    public Wallet(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public boolean transfer(Wallet receiver, double amount) {
        if (balance >= amount) {
            balance -= amount;
            receiver.balance += amount;
            return true;
        }
        return false;
    }
}
