package edu.ivytech.photogallery;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RSSFeed {
    private static RSSFeed rssFeed;

    private String title= null;
    private String pubDate = null;
    private ArrayList<GalleryItem> items;

    private SimpleDateFormat dateInFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    public final static String NEW_FEED = "edu.ivytech.photogallery.NEW_FEED";

    public static RSSFeed get() {
        if (rssFeed == null) {
            rssFeed = new RSSFeed();
        }
        return rssFeed;
    }

    private RSSFeed() {
        items = new ArrayList<GalleryItem>();
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public long getPubDateMillis() {
        try {
            Date date = dateInFormat.parse(pubDate.trim());
            return date.getTime();
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public int addItem(GalleryItem item) {
        items.add(item);
        return items.size();
    }

    public GalleryItem getItem(int index) {
        return items.get(index);
    }

    public ArrayList<GalleryItem> getAllItems() {
        return items;
    }

}
