package com.daryll.mains;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Traccar {

    static final String JEREMY = "334413";
    static final String GLENN = "460388";
    static final String CAFE = "140893";
    static final String JORDAN = "293467";
    static final String LUNGAY = "900015";
    static final String VEL = "027045736964";
    static final String DEL = "027045538766";

    static final String[] list = {JEREMY, GLENN, CAFE, JORDAN, LUNGAY, VEL, DEL};
    static URL url = null;

    public static void main(String[] args) {

        try {
            url = new URL("http://35.190.170.7:5055");
        } catch (MalformedURLException e) {
            System.err.println(e.toString());
        }

        ExecutorService executor = Executors.newFixedThreadPool(7);

        for (String id : list) {
            executor.execute(new Send(id));
        }

        executor.shutdown();
    }

    static class Send implements Runnable {
        private final String ID;

        public Send(String id) {
            ID = id;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            try {

                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write("id="+ID);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                int status = conn.getResponseCode();
                System.out.println(ID+" Status= "+status);

            } catch (MalformedURLException | ConnectException | SocketTimeoutException ne) {
                System.err.println(ne.toString());

            } catch (IOException io) {
                System.err.println(io.toString());

            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
}
