package edu.ivytech.photogallery;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class FileIO {
    private static FileIO fileIO;
    private static String URL_STRING = "https://www.flickr.com/services/feeds/photos_public.gne?format=rss2&tags=tree";
    final String FILENAME = "news_feed.xml";
    private RSSFeed feed;
    private Context context;
    private Application app;

    public static FileIO getFileIO(Application a) {

        if (fileIO == null) {
            fileIO = new FileIO(a);
        }

        return fileIO;
    }

    private FileIO(Application app) {
        this.app = app;
        context = app.getApplicationContext();
    }



    public void downloadFeed() {

        if (isNetworkConnected()) {
            try {
                URL url = new URL(URL_STRING);
                InputStream in = url.openStream();
                FileOutputStream out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);
                while (bytesRead != -1) {
                    out.write(buffer, 0, bytesRead);
                    bytesRead = in.read(buffer);
                }
                out.close();
                in.close();


            } catch (IOException e) {
                Log.e ("News reader", e.toString());
            }
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //code for newer android versions.  This check the active network to see if it is
            //able to connect to the internet and if it is an unmetered connection
            //this will prevent us from wasting metered bandwidth
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)) {
                return true;
            } else {
                return false;
            }

        } else
        {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        }


    }
    public RSSFeed readFile() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            RSSFeedHandler theRssHandler = new RSSFeedHandler();
            xmlreader.setContentHandler(theRssHandler);

            FileInputStream in = context.openFileInput(FILENAME);

            InputSource is = new InputSource(in);
            xmlreader.parse(is);

            feed = theRssHandler.getFeed();

            return feed;
        } catch(Exception e) {
            Log.e("News Reader",e.toString());
            return null;
        }
    }


}
