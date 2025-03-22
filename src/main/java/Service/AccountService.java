package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public Account login(Account account){
        Account storedAccount = accountDAO.getAccountByUsername(account.getUsername());

        if(storedAccount != null && storedAccount.getPassword().equals(account.getPassword())){
            return storedAccount;
        }

        return null;

    }

    public Account addAccount(Account account){

        if(account == null || account.username.isEmpty() || account.password.length() < 4
                || getAccountByUsername(account.username) != null) {
            return null;
        }

        return accountDAO.insertAccount(account);
    }

    public Account getAccountByUsername(String username){
        return accountDAO.getAccountByUsername(username);
    }

    public boolean accountExists(int id){
        return accountDAO.accountExists(id);
    }
}
