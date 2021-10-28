import java.util.Scanner;

public class ATM {


    public static void main(String[] args){

        // init scanner
        Scanner sc = new Scanner(System.in);

        // init Bank
        Bank theBank = new Bank("Bank of Heath");

        // add a user, which also creates a savings account
        User aUser =  theBank.addUser("John", "Doe", "1234");

        // add a checking account for our user
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {

            // stay in the login prompt until successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);

            // stay in main menu until user quits
            ATM.printUserMenu(curUser, sc);
        }
    }

    /**
     * Main menu input/output prompt
     */
    public static User mainMenuPrompt(Bank theBank, Scanner sc) {

        // inits
        String userID;
        String pin;
        User authUser;

        // prompt the user ID/pin combo until a correct one is reached
        do {
            System.out.printf("\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter User ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();

            // try to get the user object corresponding to the ID and pin combo
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null){
                System.out.println("Incorrect user ID/Pin combination. " + "Please try again.");
            }
        } while (authUser == null); // continue looping until successful login

        return authUser;
    }

    /**
     * Logged in user menu
     * @param theUser   the logged-in User object
     * @param sc    the scanner object used for user input
     */
    public static void printUserMenu(User theUser, Scanner sc){

        // print a summary of the user's accounts
        theUser.printAccountsSummary();

        // init
        int choice = 0;

        // user menu
        do {
            System.out.printf("Welcome %s. What would you like to do?\n", theUser.getFirstName());
            System.out.println(" 1) Show account transaction history.");
            System.out.println(" 2) Withdrawl");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            System.out.println("Enter your choice: ");
            if(sc.hasNextInt()){ choice = sc.nextInt();} else ATM.printUserMenu(theUser, sc);
            if (choice < 1 || choice > 5) {System.out.println("Invalid choice. Please choose 1-5");}

        } while (choice < 1 || choice > 5);

        // process the choice
        switch (choice) {
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
        }

        // redisplay this menu unless the user wants to quit
        if (choice != 5){
            ATM.printUserMenu(theUser, sc);
        }

    }

    /**
     * Show the transaction history for an account
     * @param theUser   the logged-in User object
     * @param sc    the Scanner object used for user input
     */
    public static void showTransHistory(User theUser, Scanner sc){
        int theAcct = 0;

        // get account whose transaction history to look at
        do {
            System.out.printf("Enter the number (1 - %d) of the account\n" +
                    " whose transactions you want to see: ", theUser.numAccounts());
            if (sc.hasNextInt()){ theAcct = sc.nextInt()-1;} else ATM.showTransHistory(theUser, sc);
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

        // print the transaction history
        theUser.printAcctTransHistory(theAcct);
    }

    /**
     * Process transferring funds from one account to another
     * @param theUser the logged-in user object
     * @param sc    the scanner object used for user input
     */
    public static void transferFunds(User theUser, Scanner sc) {

        // inits
        int fromAcct = 0;
        int toAcct = 0;
        double amount = 0;
        double acctBal;

        // get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer from: ", theUser.numAccounts());
            if (sc.hasNextInt()) {fromAcct = sc.nextInt();}
                if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                    System.out.println("Invalid account. Please try again.");
                }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        sc.nextLine();

        // get the account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer from: ", theUser.numAccounts());
            if (sc.hasNext()) {toAcct = sc.nextInt();}
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        sc.nextLine();

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            if (sc.hasNextDouble()){amount = sc.nextDouble();}
                if (amount < 0) {
                    System.out.println("Amount must be greater than zero.");
                } else if (amount > acctBal) {
                    System.out.printf("Amount must not be greater than\nBalance of: $%.02f.\n", acctBal);
                }
        } while (amount < 0 || amount < acctBal);

        // finally do the transfer
        theUser.addAcctTransaction(fromAcct, (-1*amount), String.format(
                "Transfer to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(fromAcct, amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(toAcct)));
    }

    /**
     * Process a fund withdrawl from an account
     * @param theUser   the logged-in user object
     * @param sc    the scanner object user for user input
     */
    public static void withdrawFunds(User theUser, Scanner sc){

        // inits
        int fromAcct = 0;
        double amount = 0;
        double acctBal;
        String memo = "";

        // get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to withdraw from: ", theUser.numAccounts());
            if (sc.hasNextInt()) {fromAcct = sc.nextInt();}
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        sc.nextLine();

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): $", acctBal);
            if (sc.hasNextDouble()){amount = sc.nextDouble();}
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\nBalance of: $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount < acctBal);

        sc.nextLine();

        // get a memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdrawl
        theUser.addAcctTransaction(fromAcct, (-1*amount), memo);
    }

    /**
     * Process a fund deposit to an account
     * @param theUser   the logged in User Account
     * @param sc    the scanner object used for user input
     */
    public static void depositFunds(User theUser, Scanner sc){

        // inits
        int toAcct = 0;
        double amount = 0;
        double acctBal;
        String memo = "";

        // get the account to transfer from
        do {
            System.out.printf("Enter the number of the account to deposit to (1 - %d) ", theUser.numAccounts());
            if (sc.hasNextInt()) {toAcct = (sc.nextInt()-1);}
            //toAcct = sc.nextInt();
            if (toAcct < 0 || toAcct > theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        //sc.nextLine();

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to deposit: $");
            if (sc.hasNextDouble()){amount = sc.nextDouble();}
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
           }
        } while (amount < 0);

        sc.nextLine();

        // get a memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdrawl
        theUser.addAcctTransaction(toAcct, amount, memo);
    }

}
