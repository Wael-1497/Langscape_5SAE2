package tn.sae.langscape;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    private static final String DATABASE_NAME = "bokify.db";
    private static final int DATABASE_VERSION = 3;
    private static final double PRICE_PER_KILOMETER = 0.5;
    public double calculatePrice(double distance) {
        // Apply pricing logic (for example, price per kilometer)
        return distance * PRICE_PER_KILOMETER;
    }

    // Table Name
    public static final String TABLE_USERS = "users";
    public static final String TABLE_TRANSPORT = "transport";
    public static final String TABLE_CHECKOUT = "checkout";
    // Columns for the "checkout" table
    public static final String COLUMN_CHECKOUT_ID = "id";
    public static final String COLUMN_CHECKOUT_DATE = "date";
    public static final String COLUMN_CHECKOUT_TIME = "time";
    public static final String COLUMN_CHECKOUT_DISTANCE = "distance";
    public static final String COLUMN_CHECKOUT_PRIX = "prix";
    private static final String CREATE_TABLE_CHECKOUT = "CREATE TABLE " + TABLE_CHECKOUT + "(" +
            COLUMN_CHECKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CHECKOUT_DATE + " TEXT NOT NULL, " +
            COLUMN_CHECKOUT_TIME + " TEXT NOT NULL, " +
            COLUMN_CHECKOUT_DISTANCE + " REAL NOT NULL, " +
            COLUMN_CHECKOUT_PRIX + " REAL NOT NULL);";


    public static final String COLUMN_TRANSPORT_ID = "id";
    public static final String COLUMN_TRANSPORT_FROM = "from_location";
    public static final String COLUMN_TRANSPORT_DESTINATION = "destination";
    public static final String COLUMN_TRANSPORT_FROM_LAT = "fromLatitude";
    public static final String COLUMN_TRANSPORT_FROM_LONG = "fromLongitude";
    public static final String COLUMN_TRANSPORT_DEST_LAT = "destLatitude";
    public static final String COLUMN_TRANSPORT_DEST_LONG = "destLongitude";



    private static final String CREATE_TABLE_TRANSPORT = "CREATE TABLE " + TABLE_TRANSPORT + "(" +
            COLUMN_TRANSPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TRANSPORT_FROM + " TEXT NOT NULL, " +
            COLUMN_TRANSPORT_DESTINATION + " TEXT NOT NULL, " +
            COLUMN_TRANSPORT_FROM_LAT + " REAL NOT NULL, " +
            COLUMN_TRANSPORT_FROM_LONG + " REAL NOT NULL, " +
            COLUMN_TRANSPORT_DEST_LAT + " REAL NOT NULL, " +
            COLUMN_TRANSPORT_DEST_LONG + " REAL NOT NULL);";




    // Table columns
    public static final String _ID = "_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_PHONE = "phone";

    // Creating table query
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_NAME + " TEXT NOT NULL, " +
            COLUMN_USER_EMAIL + " TEXT NOT NULL, " +
            COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
            COLUMN_USER_PHONE + " TEXT NOT NULL);";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_TRANSPORT);
        db.execSQL(CREATE_TABLE_CHECKOUT); // Add the creation of the "checkout" table

    }
    public boolean addCheckout(String date, String time, double distance, double prix) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CHECKOUT_DATE, date);
        contentValues.put(COLUMN_CHECKOUT_TIME, time);
        contentValues.put(COLUMN_CHECKOUT_DISTANCE, distance);
        contentValues.put(COLUMN_CHECKOUT_PRIX, prix);

        long result = db.insert(TABLE_CHECKOUT, null, contentValues);

        boolean isSuccess = result != -1;

        db.close();

        return isSuccess;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORT);
        onCreate(db);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USERS, null, null);
        if(rowsDeleted > 0) {
            Log.d("DB", "Deleted " + rowsDeleted + " users from the database.");
        } else {
            Log.d("DB", "No users found to delete.");
        }
        db.close();
    }

    public boolean checkUserExists(String username, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_NAME};
        String selection = COLUMN_USER_NAME + "=?" + " AND " + COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = {username, email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }
    public boolean updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(newPassword.getBytes(StandardCharsets.UTF_8));
            String safePasswordHash = Base64.encodeToString(hash, Base64.NO_WRAP);
            contentValues.put(COLUMN_USER_PASSWORD, safePasswordHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }


        int updateStatus = db.update(TABLE_USERS, contentValues, COLUMN_USER_NAME + "=?", new String[] {username});
        db.close();


        return updateStatus > 0;

    }
    public boolean addTransport(String from, String destination, double fromLatitude, double fromLongitude, double destLatitude, double destLongitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TRANSPORT_FROM, from);
        contentValues.put(COLUMN_TRANSPORT_DESTINATION, destination);
        contentValues.put(COLUMN_TRANSPORT_FROM_LAT, fromLatitude);
        contentValues.put(COLUMN_TRANSPORT_FROM_LONG, fromLongitude);
        contentValues.put(COLUMN_TRANSPORT_DEST_LAT, destLatitude);
        contentValues.put(COLUMN_TRANSPORT_DEST_LONG, destLongitude);

        long result = db.insert(TABLE_TRANSPORT, null, contentValues);

        boolean isSuccess = result != -1;

        db.close();

        return isSuccess;
    }








}
