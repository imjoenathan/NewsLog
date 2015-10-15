import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class DBfetch {
	

	static  JSONObject get (String catogory) throws ClientProtocolException, IOException, JSONException{
		
		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 nameValuePairs.add(new BasicNameValuePair("category", catogory));
		 HttpParams httpParameters = new BasicHttpParams();
		 HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
		 HttpConnectionParams.setSoTimeout(httpParameters, 15000);	
		 HttpClient httpclient = new DefaultHttpClient(httpParameters);
		 HttpPost httppost = new HttpPost("http://10.184.223.128/newslog/serviceSetArticles.php");
		 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		 HttpResponse response = httpclient.execute(httppost);
		 HttpEntity entity = response.getEntity();
		 String result = EntityUtils.toString(entity);
		 JSONObject ret = new JSONObject(result);
		 System.out.println("rrrrrrrrrr" + result);
		 return ret;
		 
	}
	
	static void update (String user, String password) throws ClientProtocolException, IOException{
	
		 ArrayList<NameValuePair> nameValuePairs =  new ArrayList<NameValuePair>();
		 nameValuePairs.add(new BasicNameValuePair("username", user));	
		 nameValuePairs.add(new BasicNameValuePair("password", password));
		 HttpParams httpParameters = new BasicHttpParams();
		 HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
		 HttpConnectionParams.setSoTimeout(httpParameters, 15000);	
		 HttpClient httpclient = new DefaultHttpClient(httpParameters);
		 HttpPost httppost = new HttpPost("http://10.184.223.128/newslog/serviceSetArticles.php");
		 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		 HttpResponse response = httpclient.execute(httppost);
	
	}
}
