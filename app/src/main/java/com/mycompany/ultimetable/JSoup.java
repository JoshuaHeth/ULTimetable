package com.mycompany.ultimetable;

/**
 * Created by Loaner on 03/03/2015.
 */
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JSoup {

    private static ArrayList<Session> sessionList = new ArrayList<Session>();

    public static void main(String[] args) throws IOException{
        // TODO Auto-generated method stub
        String slotEntry;

        //Make http connection and fetch HTML document
        Document doc = Jsoup.connect("http://www.timetable.ul.ie/tt2.asp?T1=13061283").get();

        Element table = doc.select("table").get(0); // Select the first table
        Elements rows = table.select("tr");

        for (int i = 1; i < rows.size(); i++) { //first row is the Weekdays names so skip it.
            Element row = rows.get(i);
            Elements cols = row.select("td"); //Select each column

            //Iterate through each column
            for(int j = 0; j < cols.size(); j++)
            {
                Elements p = cols.get(j).select("p");
                //Get individual sessions within <p></p> tags
                for(int k = 0; k < p.size(); k++)
                {
                    slotEntry = p.get(k).text();
                    slotEntry = slotEntry.trim();
                    if(!slotEntry.equals(""))
                    {
                        addSession(j, slotEntry);//j: Weekday
                    }
                }
            }

            showTimeTable();
        }
    }

    private static void showTimeTable() {
        // TODO Auto-generated method stub

        for(int i = 0; i < sessionList.size(); i++)
            System.out.println(sessionList.get(i).toString());
    }

    private static void addSession(int day, String slotEntry) {
        // TODO Auto-generated method stub

        String dataFields [];
        String startTime, endTime, module, type, groupID, roomCode;
        boolean weeksRunning [] = new boolean[14];
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

        for(int i = 6; i < dataFields.length; i++)
        {
            String range[] = dataFields[i].split("-");

            for(int j = Integer.parseInt(range[0]) - 1; j < Integer.parseInt(range[1]); j++)
            {
                weeksRunning[j] = true;
            }

        }

        Session s = new Session(day, startTime, endTime, module, type, groupID, roomCode, weeksRunning);
        sessionList.add(s);
    }

}
