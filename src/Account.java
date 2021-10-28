import java.util.AbstractCollection;
import java.util.ArrayList;

public class Account {

    /**
     * The name of the account
     */
    private String name;

    // not used in this example, future add in
    //private double balance;

    /**
     * The account ID number
     */
    private String uuid;

    /**
     * The User object that owns this account.
     */
    private User holder;

    /**
     * The list of transactions for this account
     */
    private ArrayList<Transaction> transactions;

    /**
     * Create a new Account
     * @param name the name of the account
     * @param holder the User object taht holds this account
     * @param theBank the bank that issues this account
     */
    public Account(String name, User holder, Bank theBank){

        // set the account name and holder
        this.name = name;
        this.holder = holder;

        // get new account UUID
        this.uuid = theBank.getNewAccountUUID();

        // init transactions
        this.transactions = new ArrayList<Transaction>();

        // add to holder and bank lists
        // holder.addAccount(this);
        // theBank.addAccount(this);

    }

    /**
     * Get the account Id
     * @return  the uuid
     */
    public String getUUID(){
        return this.uuid;
    }

    /**
     *  Get summary line for the account
     * @return  the string summary
     */
    public String getSummaryLine(){

        // get the account's balance
        double balance = this.getBalance();

        // format the summary line, depending on the whether the balance is negative
        if (balance >= 0){
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : $ - (%.02f) : %s", this.uuid, balance, this.name);
        }
    }

    public double getBalance(){
        double balance = 0;
        for (Transaction t : this.transactions){
            balance += t.getAmount();
        }
        return balance;
    }

    /**
     * Prints the transaction history
     */
    public void printTransHistory() {
        System.out.printf("\nTransaction history for the account %s\n", this.uuid);
        for (int t = this.transactions.size()-1; t >= 0; t--) {
            System.out.printf(this.transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Add a new transaction in this account
     * @param amount    the amount transacted
     * @param memo  the transaction memo
     */
    public void addTransaction(double amount, String memo){
        // Create a new transaction object and add it to our list
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans);
    }

    /**
     * Add a new transaction in this account, less memo
     * @param amount    the amount transacted
     */
    public void addTransaction(double amount){
        // Create a new transaction object and add it to our list
        Transaction newTrans = new Transaction(amount, this);
        this.transactions.add(newTrans);
    }

}
