package benw.puttitest;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ben on 8/07/15.
 */
public class DetailActivity extends Activity{
    private String title;
    private String description;
    private String originalImageUrl;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView originalImageImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        title = getIntent().getStringExtra(MainActivity.TAG_TITLE);
        description = getIntent().getStringExtra(MainActivity.TAG_DESCRIPTION);
        originalImageUrl = getIntent().getStringExtra(MainActivity.TAG_ORIGINALIMAGEURL);
        titleTextView = (TextView)findViewById(R.id.textViewTitle);
        titleTextView.setText(title);
        descriptionTextView = (TextView)findViewById(R.id.textViewDescription);
        descriptionTextView.setText(Html.fromHtml(description));
        originalImageImageView = (ImageView)findViewById(R.id.imageView);
        new ImageDownloader(originalImageImageView).execute(originalImageUrl);
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
