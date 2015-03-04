package com.mycompany.ultimetable;

import java.io.*;
import java.util.*;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DownloadData extends Service {
    private static final String filename = "data.date";
    private static final String URL = "http://www.timetable.ul.ie/tt2.asp?T1=";
    private String studentID;

    public DownloadData() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LOG", "Starting Service...");
         if(intent !=  null)
         {
             //Bundle bundle = intent.getExtras();
             studentID = intent.getStringExtra("STUDENT_ID"); //Get studnet ID from the intent
             if(studentID != "")
             {
                 new FetchWebsiteData().execute();
             }
         }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, Void> {
        private ArrayList<ArrayList<Session>> days;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            mProgressDialog = new ProgressDialog(MainActivity.this);
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            fetchData();
            //Write data to the file
            Log.d("LOG", "Writing data to file...");
            if (days != null) {
                File file = new File(getApplicationContext().getFilesDir(), filename);
                ObjectOutputStream oos = null;

                try {
                    oos = new ObjectOutputStream(new FileOutputStream(file));
                    oos.writeObject(days);
                }
                catch (IOException ex) {
                }
                finally {
                    //Close Object IOStream
                    try {
                        oos.close();
                        Log.d("LOG", "File was written successfully!");
                    }
                    catch (IOException ex) {
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //write data to the file
//            mProgressDialog.dismiss();
        }


        //Method for fetching data from website using JSoup
        private void fetchData() {
            Log.d("LOG", "Trying to download data...");
            String slotEntry;
            Document doc = null;
            try {
                //Make http connection and fetch HTML document
                doc = Jsoup.connect(URL+studentID).get();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            days = new ArrayList<ArrayList<Session>>();
            Element table = doc.select("table").get(0); // Select the first table
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) { //first row is the Weekdays names so skip it.
                Element row = rows.get(i);
                Elements cols = row.select("td"); //Select each column

                //Iterate through each column.
                //Each colmun is a day of the week.
                for (int j = 0; j < cols.size(); j++) {
                    //Create a new Arraylist to hold the Session for a new days
                    ArrayList<Session> list = new ArrayList<Session>();

                    Elements p = cols.get(j).select("p");
                    //Get individual sessions within <p></p> tags from each column.
                    //Each column contains all the schedules for that day.
                    for (int k = 0; k < p.size(); k++) {
                        slotEntry = p.get(k).text();
                        slotEntry = slotEntry.trim();
                        if (!slotEntry.equals("")) {
                            addSession(list, slotEntry);//j: Weekday
                        }
                    }
                    days.add(list);
                }
            }

        }


        private void addSession(ArrayList<Session> list, String slotEntry) {
            // TODO Auto-generated method stub
        /*
        From this input string we are extracting only relevent fields below.
        Leaving out the running weeks information.
        */
            String dataFields[];
            String startTime, endTime, module, type, groupID, roomCode;
            //boolean weeksRunning [] = new boolean[14];
            slotEntry = slotEntry.replaceAll("Wks:", "");
            slotEntry = slotEntry.replaceAll(",| - |  ", " ");
            //System.out.println(slotEntry);
            dataFields = slotEntry.split(" ");

            startTime = dataFields[0].trim();
            endTime = dataFields[1].trim();
            module = dataFields[2].trim();
            type = dataFields[3].trim();
            groupID = dataFields[4].trim();
            roomCode = dataFields[5].trim();

            Session s = new Session(startTime, endTime, module, type, groupID, roomCode);
            list.add(s);
        }
    }
}
