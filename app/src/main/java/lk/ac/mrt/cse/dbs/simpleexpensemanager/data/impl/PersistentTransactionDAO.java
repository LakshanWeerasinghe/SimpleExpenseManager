package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private Database database;

    public PersistentTransactionDAO(Database database) {
        this.database = database;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.KEY_ACCOUNT_NO, accountNo); // Account No
        values.put(Database.KEY_EXPENSE_TYPE, expenseType.name()); // Bank Name
        values.put(Database.KEY_AMOUNT, amount); // Holder Name
        values.put(Database.KEY_DATE, new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(date)); // Holder Name

        db.insert(Database.TABLE_TRANSACTIONS, null, values);
        db.close();
        Log.d("Log :", values.toString());
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query(Database.TABLE_TRANSACTIONS, null, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(cursor.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            cursor.getString(2),
                            ExpenseType.valueOf(cursor.getString(3)),
                            Double.parseDouble(cursor.getString(4))
                    );
                    // Adding account to list
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query(Database.TABLE_TRANSACTIONS, null, null, null, null, null, null, limit + "");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(cursor.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            cursor.getString(2),
                            ExpenseType.valueOf(cursor.getString(3)),
                            Double.parseDouble(cursor.getString(4))
                    );
                    // Adding account to list
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return transactionList;
    }
}
