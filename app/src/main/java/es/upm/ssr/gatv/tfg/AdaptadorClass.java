package es.upm.ssr.gatv.tfg;



import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.HashMap;
import java.util.List;



/*
 * Custom Adapter class that is responsible for holding the list of sites after they
 * get parsed out of XML and building row views to display them on the screen.
 */
public class AdaptadorClass extends ArrayAdapter<Entry> {

    ImageLoader imageLoader;
    DisplayImageOptions options;


    public AdaptadorClass(Context ctx, int textViewResourceId, List<Entry> entries) {
        super(ctx, textViewResourceId, entries);

        //Setup the ImageLoader, we'll use this to display our images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        //Setup options for ImageLoader so it will handle caching for us.
        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();
    }


    /*
     * (non-Javadoc)
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     *
     * This method is responsible for creating row views out of a StackSite object that can be put
     * into our ListView
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        RelativeLayout row = (RelativeLayout)convertView;
        Log.i("EntrySites", "getView pos = " + pos);
        if(null == row){
            //No recycled View, we have to inflate one.
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout)inflater.inflate(R.layout.row_site, null);
        }

        //Get our View References
        final ImageView iconImg = (ImageView)row.findViewById(R.id.iconImg);
        TextView nameTxt = (TextView)row.findViewById(R.id.nameTxt);
        TextView aboutTxt = (TextView)row.findViewById(R.id.aboutTxt);
        final ProgressBar indicator = (ProgressBar)row.findViewById(R.id.progress);






        //Setup a listener we can use to swtich from the loading indicator to the Image once it's ready
        ImageLoadingListener listener = new ImageLoadingListener(){

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                indicator.setVisibility(View.INVISIBLE);
                iconImg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub

            }
        };





        if ((getItem(pos).getImgUrl()) != null) {

            //Initially we want the progress indicator visible, and the image invisible
            indicator.setVisibility(View.VISIBLE);
            iconImg.setVisibility(View.INVISIBLE);

            //Load the image and use our options so caching is handled.
            imageLoader.displayImage(getItem(pos).getImgUrl(), iconImg, options, listener);
            String imgUrl  = getItem(pos).getImgUrl();
            Log.i("EntrySites", "getView imgUrl  = " + imgUrl);
        }
        else if((getItem(pos).getLink()) != null){

            Bitmap imgBitmap = retrieveVideoFrameFromVideo(getItem(pos).getLink().toString());

            final Bitmap thumbnail = ThumbnailUtils.extractThumbnail(imgBitmap,80,80);
            iconImg.setImageBitmap(thumbnail);
            //iconImg.setScaleType(ImageView.ScaleType.FIT_XY);
            String imgLink  = getItem(pos).getLink();
            Log.i("EntrySites", "getView imgLink  = " + imgLink);
        }

        //Set the relavent text in our TextViews
        nameTxt.setText(getItem(pos).getTitle());
        aboutTxt.setText(getItem(pos).getSummary());



        return row;


    }

    public static Bitmap retrieveVideoFrameFromVideo(String videoPath)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {  e.printStackTrace();}
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}

