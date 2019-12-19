package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "170681C";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    //accounts table
    public static final String TABLE_ACCOUNTS = "accounts";

    //columns
    public static final String KEY_ACCOUNT_NO = "accountNo";
    public static final String KEY_BANK_NAME = "bankName";
    public static final String KEY_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String KEY_BALANCE = "balance";

    //transactions table
    public static final String TABLE_TRANSACTIONS = "transactions";

    //columns
    private static final String KEY_TRANSACTION_ID = "id";
    public static final String KEY_EXPENSE_TYPE = "expenseType";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "date";


    /*
    Create Table queries
     */
    private static final String ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
            + KEY_ACCOUNT_NO + " TEXT PRIMARY KEY," + KEY_BANK_NAME + " TEXT,"
            + KEY_ACCOUNT_HOLDER_NAME + " TEXT," + KEY_BALANCE + " REAL" + ")";

    private static final String TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
            + KEY_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT," + KEY_ACCOUNT_NO + " TEXT,"
            + KEY_EXPENSE_TYPE + " TEXT," + KEY_AMOUNT + " REAL," + "FOREIGN KEY(" + KEY_ACCOUNT_NO +
            ") REFERENCES "+ TABLE_ACCOUNTS +"(" + KEY_ACCOUNT_NO + ") )";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACCOUNTS_TABLE);
        db.execSQL(TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ACCOUNTS + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_TRANSACTIONS + "'");

        // Create tables again
        onCreate(db);
    }
}
