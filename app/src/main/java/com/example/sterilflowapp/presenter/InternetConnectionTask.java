package com.example.sterilflowapp.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import com.example.sterilflowapp.R;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetConnectionTask extends AsyncTask<String,String,String> {
    private Context context;
    private String TAG = "InternetConnectionTask";
    private Boolean internetSucces=false;
    public InternetConnectionTask(Context context){
        this.context=context;
    }
    @Override
    protected String doInBackground(String... params) {
        hasActiveInternetConnection(context);
        return null;
    }

    //Ref:https://stackoverflow.com/questions/6493517/detect-if-android-device-has-internet-connection

    private boolean hasActiveInternetConnection(Context ctx) {
        if (isNetworkAvailable(ctx)) {
            try {
                //Try to connect to google.com to check whether active internet connection is
                //actually working
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                internetSucces=true;
                Log.d(TAG, "NETWORK OK!");
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
            Looper.prepare();
        }
        return false;
    }
    private boolean isNetworkAvailable(Context ctx) {

        //Check whether connection on phone in active
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //Notify user if there is no internet connection
        if(!internetSucces){
            final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(context,R.style.AlertTheme);
            alert.setTitle(context.getString(R.string.noconnection));
            alert.setMessage(context.getString(R.string.noconnectionmessage));
            alert.setPositiveButton("OK",null);
            alert.show();
        }
    }
}
