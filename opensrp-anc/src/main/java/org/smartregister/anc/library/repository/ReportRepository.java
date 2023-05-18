package org.smartregister.anc.library.repository;

import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.anc.library.model.ReportModel;
import org.smartregister.repository.BaseRepository;

public class ReportRepository  extends BaseRepository {
    public static final String TABLE_NAME = "monthly_report";
    public static final String MONTH = "month";
    public static final String BASE_ENTITY_ID = "base_entity_id";
    public static final String KEY = "key";
    public static final String TRIMESTER = "trimester";
    public static final String FIRST_CONTACT = "first_contact";
    public static final String FIRST_CONTACT_ABOVE_15 = "first_contact_above_15";
    public static final String FIRST_CONTACT_ABOVE_20 = "first_contact_above_20";
    public static final String FIRST_CONTACT_ABOVE_25 = "first_contact_above_25";
    public static final String SECOND_TRIMESTER_FIRST_CONTACT = "second_trimester_first_contact";
    public static final String SECOND_TRIMESTER_FIRST_CONTACT_ABOVE_15 = "second_trimester_first_contact_above_15";
    public static final String SECOND_TRIMESTER_FIRST_CONTACT_ABOVE_20 = "second_trimester_first_contact_above_20";
    public static final String SECOND_TRIMESTER_FIRST_CONTACT_ABOVE_25 = "second_trimester_first_contact_above_25";
    public static final String THIRD_TRIMESTER_FIRST_CONTACT = "third_trimester_first_contact";
    public static final String THIRD_TRIMESTER_FIRST_CONTACT_ABOVE_15 = "third_trimester_first_contact_above_15";
    public static final String THIRD_TRIMESTER_FIRST_CONTACT_ABOVE_20 = "third_trimester_first_contact_above_20";
    public static final String THIRD_TRIMESTER_FIRST_CONTACT_ABOVE_25 = "third_trimester_first_contact_above_25";
    public static final String CREATED_AT = "created_at";
    SQLiteDatabase sqLiteDatabase = getWritableDatabase();

    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + "("
            + MONTH + " VARCHAR(255) NOT NULL PRIMARY KEY, "
            + FIRST_CONTACT + " VARCHAR(255) NOT NULL, "
            + FIRST_CONTACT_ABOVE_15 + " VARCHAR(255), "
            + FIRST_CONTACT_ABOVE_20 + " VARCHAR(255), "
            + FIRST_CONTACT_ABOVE_25 + " VARCHAR(255), "
            + SECOND_TRIMESTER_FIRST_CONTACT + " VARCHAR(255), "
            + SECOND_TRIMESTER_FIRST_CONTACT_ABOVE_15 + " VARCHAR(255), "
            + SECOND_TRIMESTER_FIRST_CONTACT_ABOVE_20 + " VARCHAR(255), "
            + SECOND_TRIMESTER_FIRST_CONTACT_ABOVE_25 + " VARCHAR(255), "
            + THIRD_TRIMESTER_FIRST_CONTACT + " VARCHAR(255),"
            + THIRD_TRIMESTER_FIRST_CONTACT_ABOVE_15 + " VARCHAR(255), "
            + THIRD_TRIMESTER_FIRST_CONTACT_ABOVE_20 + " VARCHAR(255), "
            + THIRD_TRIMESTER_FIRST_CONTACT_ABOVE_25 + " VARCHAR(255))";



    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_SQL);
    }

    private ContentValues createValuesFor(ReportModel reportModel) {
        ContentValues values = new ContentValues();
        values.put(MONTH, reportModel.getMonth());
        values.put(FIRST_CONTACT, reportModel.getFirstContact());
        values.put(FIRST_CONTACT_ABOVE_15, reportModel.getFirstContactAbove15());
        values.put(FIRST_CONTACT_ABOVE_20, reportModel.getFirstContactAbove20());
        values.put(FIRST_CONTACT_ABOVE_25, reportModel.getFirstContactAbove25());
        values.put(SECOND_TRIMESTER_FIRST_CONTACT, reportModel.getSecondTrimesterFirstContact());
        values.put(SECOND_TRIMESTER_FIRST_CONTACT_ABOVE_15, reportModel.getSecondTrimesterFirstContactAbove15());
        values.put(SECOND_TRIMESTER_FIRST_CONTACT_ABOVE_20, reportModel.getSecondTrimesterFirstContactAbove20());
        values.put(SECOND_TRIMESTER_FIRST_CONTACT_ABOVE_25, reportModel.getSecondTrimesterFirstContactAbove25());
        values.put(THIRD_TRIMESTER_FIRST_CONTACT, reportModel.getThirdTrimesterFirstContact());
        values.put(THIRD_TRIMESTER_FIRST_CONTACT_ABOVE_15, reportModel.getThirdTrimesterFirstContactAbove15());
        values.put(THIRD_TRIMESTER_FIRST_CONTACT_ABOVE_20, reportModel.getThirdTrimesterFirstContactAbove20());
        values.put(THIRD_TRIMESTER_FIRST_CONTACT_ABOVE_25, reportModel.getThirdTrimesterFirstContactAbove25());
        return values;
    }


    public void saveMonthlyReport(ReportModel reportModel){
        sqLiteDatabase.insertWithOnConflict(TABLE_NAME,null,createValuesFor(reportModel),SQLiteDatabase.CONFLICT_REPLACE);
    }

    public  static void saveReport(ReportModel reportModel)
    {

    }

}
