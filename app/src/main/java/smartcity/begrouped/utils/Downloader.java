package smartcity.begrouped.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

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

import static smartcity.begrouped.utils.GlobalMethodes.isNumeric;

/**
 * Created by LENOVO on 04/05/2015.
 */
public class Downloader extends AsyncTask{
    private Context context;
    private AsyncResponse asyncResponse;
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

    public Downloader(Context context,AsyncResponse asyncResponse) {
        this.context=context;
        this.asyncResponse=asyncResponse;
        this.progressDialog = new ProgressDialog(context);
    }

    private static String getFromUrl(String url) {
        String chaine="0000";
        boolean start=true;
        InputStream is;
        try {
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
                    if (start) {
                        if (!isNumeric(line.charAt(0)) && !(line.charAt(0)=='{')) return "0000";
                        start = false;
                    }
                    sb.append(line);
                }
                is.close();
                chaine = sb.toString();
            } catch (Exception e) {
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
        return download(url);
    }

    @Override
    protected void onPostExecute(Object object)
    {
        hide();
        String serverResponse=(String) object;
        asyncResponse.executeAfterDownload(serverResponse);
    }
}
