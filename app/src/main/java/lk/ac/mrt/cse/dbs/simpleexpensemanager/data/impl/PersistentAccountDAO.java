package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private Database database;

    public PersistentAccountDAO(Database database) {
        this.database = database;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query(Database.TABLE_ACCOUNTS, new String[] {Database.KEY_ACCOUNT_NO}, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding account to list
                accountNumbersList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query(Database.TABLE_ACCOUNTS, null, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3))
                );
                // Adding account to list
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query(Database.TABLE_ACCOUNTS, null, Database.KEY_ACCOUNT_NO + "=?",
                new String[] { accountNo }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            );
            cursor.close();
            return account;
        }
        String msg = "Account  Number" + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.KEY_ACCOUNT_NO, account.getAccountNo()); // Account No
        values.put(Database.KEY_BANK_NAME, account.getBankName()); // Bank Name
        values.put(Database.KEY_ACCOUNT_HOLDER_NAME, account.getAccountHolderName()); // Holder Name
        values.put(Database.KEY_BALANCE, account.getBalance()); // Balance

        // Inserting Row
        db.insert(Database.TABLE_ACCOUNTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = database.getWritableDatabase();
        db.delete(Database.TABLE_ACCOUNTS, Database.KEY_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(Database.KEY_BALANCE, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(Database.KEY_BALANCE, account.getBalance() + amount);
                break;
        }
        // updating row
        db.update(Database.TABLE_ACCOUNTS, values, Database.KEY_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
    }
}
