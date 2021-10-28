import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Bank {

    private String name;

    private ArrayList<User> users;

    private ArrayList<Account> accounts;

    /**
     * Create a new Bank object with empty lists of users and accounts
     * @param name the name of the bank
     */
    public Bank(String name) {
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }

    public String getNewUserUUID(){

        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;

        // continuing looping until we get a unique ID
        do {
            // generate the number
            uuid = "";
            for (int c = 0; c < len; c++){
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            // check to make sure it's unique
            nonUnique = false;
            for (User u : this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique == true);

        return uuid;
    }

    public String getNewAccountUUID(){

        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;

        // continuing looping until we get a unique ID
        do {
            // generate the number
            uuid = "";
            for (int c = 0; c < len; c++){
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            // check to make sure it's unique
            nonUnique = false;
            for (Account a : this.accounts){
                if (uuid.compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique == true);

        return uuid;
    }

    /**
     * Add an account
     * @param anAccount the account to add
     */
    public void addAccount(Account anAccount) {
        this.accounts.add(anAccount);
    }

    /**
     * Create a new user of the bank
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param pin the user's pin
     * @return the new User object
     */
    public User addUser(String firstName, String lastName, String pin) {

        // create a new user object and add it to our list
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        // create a savings account for the user and add to User and Bank account lists
        Account newAccount = new Account("Savings", newUser, this);
        newUser.addAccount(newAccount);
        this.accounts.add(newAccount);

        return newUser;
    }

    /**
     *  Get the User object associated with a particular userID and pin, if they are valid
     * @param userID    the UUID of the suer to log in
     * @param pin   the pin of the user
     * @return  the User object, if the login is successful, or null, if it is not
     */
    public User userLogin(String userID, String pin) {

        // search through the lists of users
        for (User u: users)

            // check userId and pin
            if (u.getUUID().contains(userID) && u.validatePin(pin)){
                return u;
            }

        // if userID and/or pin is not a match
        return null;

    }

    public String getName(){
        return this.name;
    }
}
