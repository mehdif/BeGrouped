package smartcity.begrouped.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by LENOVO on 04/05/2015.
 */
public class Downloader extends AsyncTask{
    private Context context;
    private ProgressDialog progressDialog;

    public boolean isOnline(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

    public Downloader(Context context) {
        this.context=context;
    }

    private static String getFromUrl(String url) {
        String chaine="0000";
        InputStream is = null;
        try {
            chaine = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                chaine = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.getMessage());
            }
        } catch (UnsupportedEncodingException e) {

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }
        return chaine;
    }

    private String download(String url) {
        if (!isOnline()) return "0002";
        return getFromUrl(url);
    }

    private void show() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    private void hide() {
        progressDialog.dismiss();
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        show();
    }

    @Override
    protected String doInBackground(Object[] params) {
        String url=params[0].toString();
        Log.v("Aymen",url);
        return download(url);
    }

    @Override
    protected void onPostExecute(Object object)
    {
        hide();
    }
}
