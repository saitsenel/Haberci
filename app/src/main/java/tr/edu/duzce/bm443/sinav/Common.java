package tr.edu.duzce.bm443.sinav;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static String dateFormatter(String date){
        String formattedDate = null;
        String oldstring = date;
        try {
            Date Olddate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(oldstring);


            formattedDate = new SimpleDateFormat("d-MMMM-y").format(Olddate);
            System.out.println(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

}
