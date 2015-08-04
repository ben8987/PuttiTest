package benw.puttitest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private final static String URL = "http://testing.moacreative.com/job_interview/event.php";

    //JSON Node Names
    public final static String TAG_EVENTS = "Events";
    public final static String TAG_NEWSID = "NewsID";
    public final static String TAG_TITLE = "Title";
    public final static String TAG_SYNOPSIS = "Synopsis";
    public final static String TAG_DESCRIPTION = "Description";
    public final static String TAG_ORIGINALIMAGEURL = "OriginalImageURL";
    public final static String TAG_THUMBNAILIMAGEURL = "thumbnailImageURL";

    ArrayList<HashMap<String, String>> eventsList = new ArrayList<HashMap<String, String>>();
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventsList = new ArrayList<HashMap<String, String>>();
        new JSONParser().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JSONParser extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }


        @Override


        protected JSONObject doInBackground(String... args) {

            JsonUrlConnection mJsonUrlConnection = new JsonUrlConnection();
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("event_type", "interview"));
            JSONObject json = mJsonUrlConnection.getJSONFromUrl(URL, parameters);
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Get JSON Array from URL
                JSONArray android = json.getJSONArray(TAG_EVENTS);
                for (int i = 0; i < android.length(); i++) {
                    JSONObject c = android.getJSONObject(i);

                    // Store JSON item in a Variable
                    String newsID = c.getString(TAG_NEWSID);
                    String title = c.getString(TAG_TITLE);
                    String synopsis = c.getString(TAG_SYNOPSIS);
                    String description = c.getString(TAG_DESCRIPTION);
                    String thumbnailImageUrl = c.getString(TAG_THUMBNAILIMAGEURL);
                    String originalImageUrl = c.getString(TAG_ORIGINALIMAGEURL);

                    // Add value HashMap key to value
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_NEWSID, newsID);
                    map.put(TAG_TITLE, title);
                    map.put(TAG_SYNOPSIS, synopsis);
                    map.put(TAG_DESCRIPTION, description);
                    map.put(TAG_THUMBNAILIMAGEURL, thumbnailImageUrl);
                    map.put(TAG_ORIGINALIMAGEURL, originalImageUrl);

                    // Add map into list
                    eventsList.add(map);
                }
                mListView = (ListView) findViewById(R.id.listView);

                ListAdapter adapter = new SimpleAdapter(MainActivity.this, eventsList,
                        R.layout.list_item,
                        new String[]{TAG_NEWSID, TAG_TITLE, TAG_SYNOPSIS}, new int[]{
                        R.id.news_id, R.id.title, R.id.synopsis});

                mListView.setAdapter(adapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra(TAG_TITLE, eventsList.get(position).get(TAG_TITLE));
                        intent.putExtra(TAG_DESCRIPTION, eventsList.get(position).get(TAG_DESCRIPTION));
                        intent.putExtra(TAG_ORIGINALIMAGEURL, eventsList.get(position).get(TAG_ORIGINALIMAGEURL));

                        intent.setClass(MainActivity.this, DetailActivity.class);
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}
