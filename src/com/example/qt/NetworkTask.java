package com.example.qt;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class NetworkTask extends AsyncTask<String, Void, String> {

	public AsyncResponse delegate=null;
	
	@Override
	protected String doInBackground(String... params) {
		String url = params[0];
		// TODO Auto-generated method stub
		String endResult;
		try {
			BasicResponseHandler myHandler = new BasicResponseHandler();
			HttpGet method = new HttpGet(url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(method);
			endResult = myHandler.handleResponse(response);
		} catch (Exception e) {
			return null;
		}
		return endResult;
	}
	
	@Override
	protected void onPostExecute(String result) {
		delegate.processFinish(result);
	}
	
	public interface AsyncResponse {
	    void processFinish(String output);
	}

}
