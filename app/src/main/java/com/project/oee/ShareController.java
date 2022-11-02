package com.project.oee;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ShareController {

    Context mContext;

    private static final String KEY_MACHINE= "machine";
    private static final String KEY_OP = "operating_time";
    private static final String KEY_TT = "total_time";
    private static final String KEY_PA = "processed_amount";
    private static final String KEY_IDC = "ideal_cycle";
    private static final String KEY_GC = "good_count";
    private static final String KEY_TC = "total_count";
    private static final String KEY_AVAILABILITY = "availability";
    private static final String KEY_PEFORMANCE = "peformance";
    private static final String KEY_ROC = "rate_of_quality";
    private static final String KEY_OEE = "oee";
    private static final String KEY_DTIME = "downtime";
    private static final String KEY_TDTIME = "total_downtime";
    private static final String KEY_PDTIME = "planned_downtime";
    private static final String KEY_LTIME = "loading_time";
    private static final String KEY_TDELAY = "total_delay";
    private static final String KEY_WHOURS = "working_hours";

    private static final String KEY_FACTORY = "factory";
    private static final String KEY_USER = "username";
    private static final String KEY_DATE = "date";

    public ShareController(Context context) {
        this.mContext = context;
    }

    public void shareText(String factory,String person, String mch, String ot, String tt, String pa, String ict,
                          String gc, String tc, String avail, String per, String roq, String oee, String dtime,
                          String tdtime, String pdtime, String ltime, String tdelay, String whours, String date) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        String shareBodyText =  "Factory: "+ factory +", Officer: "+ person +"\n"+
                                "Machine: "+ mch.toUpperCase() +", Date: "+date+"\n"+
                                "Operating Time: "+ ot +" hours" + ", Total Time: "+ tt + " hours "+"\n"+
                                "Processed Amount: "+ pa +" kilograms" + ", Ideal Cycle Time: "+ ict +" kg/hours" + "\n"+
                                "Good Count: "+ gc + " kg" + ", Total Count: "+ tc + " kg"+"\n"+
                                "Availability(%): "+ avail +", Performance(%): "+ per +", Rate of Quality(%): "+ roq +"\n"+
                                "OEE: "+oee+"\n"+ "ICT Counting"+"\n"+
                                "Downtime: "+ dtime + " %" + ", Total Downtime: "+ tdtime + " hours"+"\n"+
                                "Planned Downtime: "+ pdtime + " hours" + ", Loading Time: "+ ltime + " hours"+"\n"+
                                "Total Delay: "+ tdelay + " hours" + ", Working Hours: "+ whours + " %";
        intent.putExtra(Intent.EXTRA_SUBJECT, factory.toUpperCase());
        intent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        intent.setType("text/plain");
        Intent chooserIntent = Intent.createChooser(intent, "Choose sharing method");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(chooserIntent);
    }

    public void shareTextById(String idc, String idf) {

        SQLiteHandler dbHelper = new SQLiteHandler(mContext);

        HashMap<String, String> fac = dbHelper.getFacByID(idf);
        String factory = fac.get(KEY_FACTORY).toUpperCase();
        String person = fac.get(KEY_USER).toUpperCase();
        String date = fac.get(KEY_DATE);

        HashMap<String, String> rec = dbHelper.getRecByID(idc);
        String mch = rec.get(KEY_MACHINE);
        String ot = rec.get(KEY_OP);
        String tt = rec.get(KEY_TT);
        String pa = rec.get(KEY_PA);
        String ict = rec.get(KEY_IDC);
        String gc = rec.get(KEY_GC);
        String tc = rec.get(KEY_TC);
        String avail = rec.get(KEY_AVAILABILITY);
        String per = rec.get(KEY_PEFORMANCE);
        String roq = rec.get(KEY_ROC);
        String oee = rec.get(KEY_OEE);
        String dtime = rec.get(KEY_DTIME);
        String tdtime = rec.get(KEY_TDTIME);
        String pdtime = rec.get(KEY_PDTIME);
        String ltime = rec.get(KEY_LTIME);
        String tdelay = rec.get(KEY_TDELAY);
        String whours = rec.get(KEY_WHOURS);

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        String shareBodyText =  "Factory: "+ factory +", Officer: "+ person +"\n"+
                "Machine: "+ mch.toUpperCase() +", Date: "+date+"\n"+
                "Operating Time: "+ ot +" hours" + ", Total Time: "+ tt + " hours "+"\n"+
                "Processed Amount: "+ pa +" kilograms" + ", Ideal Cycle Time: "+ ict +" kg/hours" + "\n"+
                "Good Count: "+ gc + " kg" + ", Total Count: "+ tc + " kg"+"\n"+
                "Availability(%): "+ avail +", Performance(%): "+ per +", Rate of Quality(%): "+ roq +"\n"+
                "OEE: "+oee+"\n"+ "ICT Counting"+"\n"+
                "Downtime: "+ dtime + " %" + ", Total Downtime: "+ tdtime + " hours"+"\n"+
                "Planned Downtime: "+ pdtime + " hours" + ", Loading Time: "+ ltime + " hours"+"\n"+
                "Total Delay: "+ tdelay + " hours" + ", Working Hours: "+ whours + " %";
        intent.putExtra(Intent.EXTRA_SUBJECT, factory.toUpperCase());
        intent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        intent.setType("text/plain");
        Intent chooserIntent = Intent.createChooser(intent, "Choose sharing method");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(chooserIntent);
    }


    public void shareFile(String idf){
        export(idf);

        SQLiteHandler dbHelper = new SQLiteHandler(mContext);

        HashMap<String, String> fac = dbHelper.getFacByID(idf);
        String f = fac.get(KEY_FACTORY).toUpperCase();
        String p = fac.get(KEY_USER).toUpperCase();
        String d = fac.get(KEY_DATE);

        File sd = new File(Environment.getExternalStorageDirectory() + File.separator + "OEECalculator");
        String csvFile = f+"_"+d+".xls";
        File directory = new File(sd.getAbsolutePath());

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(directory,csvFile);

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/xls");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fileWithinMyDir));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, f.toUpperCase());
            intentShareFile.putExtra(Intent.EXTRA_TEXT, f.toUpperCase());
            intentShareFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(Intent.createChooser(intentShareFile, "Share Files"));
        }
    }

    public void shareFiles(String fn){
        File sd = new File(Environment.getExternalStorageDirectory() + File.separator + "OEECalculator");
        File directory = new File(sd.getAbsolutePath());

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(directory,fn);

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/xls");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fileWithinMyDir));
            intentShareFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(Intent.createChooser(intentShareFile, "Share Files"));
        }
    }


    public void export(String idf){

        SQLiteHandler dbHelper = new SQLiteHandler(mContext);
        final Cursor cursor = dbHelper.getRecByIDFac(idf);

        HashMap<String, String> fac = dbHelper.getFacByID(idf);
        String f = fac.get(KEY_FACTORY).toUpperCase();
        String p = fac.get(KEY_USER).toUpperCase();
        String d = fac.get(KEY_DATE);

        File sd = new File(Environment.getExternalStorageDirectory() + File.separator + "OEECalculator");
        String csvFile = f+"_"+d+".xls";

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {
            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Data List", 0);

            sheet.addCell(new Label(0, 0, "Factory")); // column and row
            sheet.addCell(new Label(1, 0, "Officer"));
            sheet.addCell(new Label(2, 0, "Date")); // column and row
            sheet.addCell(new Label(0, 2, "Machine"));
            sheet.addCell(new Label(1, 2, "Operating Time(hours)")); // column and row
            sheet.addCell(new Label(2, 2, "Total Time(hours)"));
            sheet.addCell(new Label(3, 2, "Processed Amount(kilograms)")); // column and row
            sheet.addCell(new Label(4, 2, "Ideal Cycle Time(kg/hours)"));
            sheet.addCell(new Label(5, 2, "Good Count(kg)")); // column and row
            sheet.addCell(new Label(6, 2, "Total Count(kg)"));
            sheet.addCell(new Label(7, 2, "Availability")); // column and row
            sheet.addCell(new Label(8, 2, "Performance"));
            sheet.addCell(new Label(9, 2, "Rate of Quality")); // column and row
            sheet.addCell(new Label(10, 2, "OEE"));
            sheet.addCell(new Label(11, 1, "ICT COUNTING")); // column and row
            sheet.addCell(new Label(11, 2, "%Downtime(%)")); // column and row
            sheet.addCell(new Label(12, 2, "Total Downtime(hours)"));
            sheet.addCell(new Label(13, 2, "Planned Downtime(hours)")); // column and row
            sheet.addCell(new Label(14, 2, "Loading Time(hours)"));
            sheet.addCell(new Label(15, 2, "Total Delay(hours)")); // column and row
            sheet.addCell(new Label(16, 2, "%Working Hours(%)")); // column and row

            sheet.addCell(new Label(0, 1, f));
            sheet.addCell(new Label(1, 1, p));
            sheet.addCell(new Label(2, 1, d));
            if (cursor.moveToFirst()) {

                do {

                    String mac = cursor.getString(cursor.getColumnIndex(KEY_MACHINE));
                    String op = cursor.getString(cursor.getColumnIndex(KEY_OP));
                    String tt = cursor.getString(cursor.getColumnIndex(KEY_TT));
                    String pa = cursor.getString(cursor.getColumnIndex(KEY_PA));
                    String idc = cursor.getString(cursor.getColumnIndex(KEY_IDC));
                    String gc = cursor.getString(cursor.getColumnIndex(KEY_GC));
                    String tc = cursor.getString(cursor.getColumnIndex(KEY_TC));
                    String ava = cursor.getString(cursor.getColumnIndex(KEY_AVAILABILITY))+"%";
                    String per = cursor.getString(cursor.getColumnIndex(KEY_PEFORMANCE))+"%";
                    String roc = cursor.getString(cursor.getColumnIndex(KEY_ROC))+"%";
                    String oee = cursor.getString(cursor.getColumnIndex(KEY_OEE));
                    String dtime = cursor.getString(cursor.getColumnIndex(KEY_DTIME));
                    String tdtime = cursor.getString(cursor.getColumnIndex(KEY_TDTIME));
                    String pdtime = cursor.getString(cursor.getColumnIndex(KEY_PDTIME));
                    String ltime = cursor.getString(cursor.getColumnIndex(KEY_LTIME));
                    String tdelay = cursor.getString(cursor.getColumnIndex(KEY_TDELAY));
                    String whours = cursor.getString(cursor.getColumnIndex(KEY_WHOURS));

                    int i = cursor.getPosition() + 3;
                    sheet.addCell(new Label(0, i, mac));
                    sheet.addCell(new Label(1, i, op));
                    sheet.addCell(new Label(2, i, tt));
                    sheet.addCell(new Label(3, i, pa));
                    sheet.addCell(new Label(4, i, idc));
                    sheet.addCell(new Label(5, i, gc));
                    sheet.addCell(new Label(6, i, tc));
                    sheet.addCell(new Label(7, i, ava));
                    sheet.addCell(new Label(8, i, per));
                    sheet.addCell(new Label(9, i, roc));
                    sheet.addCell(new Label(10, i, oee));
                    sheet.addCell(new Label(11, i, dtime));
                    sheet.addCell(new Label(12, i, tdtime));
                    sheet.addCell(new Label(13, i, pdtime));
                    sheet.addCell(new Label(14, i, ltime));
                    sheet.addCell(new Label(15, i, tdelay));
                    sheet.addCell(new Label(16, i, whours));
                } while (cursor.moveToNext());
            }
            //closing cursor
            cursor.close();
            workbook.write();
            workbook.close();
            Toast.makeText(mContext.getApplicationContext(), "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
