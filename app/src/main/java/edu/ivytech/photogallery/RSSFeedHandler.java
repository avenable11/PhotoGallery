package edu.ivytech.photogallery;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSFeedHandler extends DefaultHandler {
    private RSSFeed feed;
    private GalleryItem item;

    private boolean feedTitleHasBeenRead = false;
    private boolean feedPubDateHasBeenRead = false;

    private boolean isTitle = false;
    private boolean isDescription = false;
    private boolean isLink = false;
    private boolean isPubDate = false;

    public RSSFeed getFeed() {
        return feed;
    }
    public void startDocument() throws SAXException {
        feed = RSSFeed.get();
        item = new GalleryItem();
    }


    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes attrs) throws SAXException {
        if (qName.equals("item")) {
            item = new GalleryItem();
            return;
        }
        else if (qName.equals("title")) {
            isTitle = true;
            return;
        }
        else if (qName.equals("description")) {
            isDescription = true;
            return;
        }
        else if (qName.equals("link")) {
            isLink = true;
            return;
        }
        else if (qName.equals("pubDate")){
            isPubDate = true;
            return;
        } else if (qName.equals("media:content")) {
            item.setUrl(attrs.getValue("url"));
        }

    }

    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException {
        if(qName.equals("item")) {
            feed.addItem(item);
            return;
        }
    }

    public void characters (char ch[], int start, int length) {
        String s = new String(ch,start, length);
        if (isTitle) {
            if(!feedTitleHasBeenRead) {
                feed.setTitle(s);
                feedTitleHasBeenRead = true;
            }
            else {
                item.setCaption(s);
            }
            isTitle = false;
        }
        else if(isLink) {
           // item.setUrl(s);
            isLink = false;
        }
        else if (isDescription) {
            if (s.startsWith("<")) {
              //  item.setCaption("No description available");
            } else {
              //  item.setCaption(s);
            }
            isDescription = false;
        }
        else if(isPubDate) {
            if(!feedPubDateHasBeenRead) {
                feed.setPubDate(s);
                feedPubDateHasBeenRead = true;
            }
            else {
               // item.setPubDate(s);
            }
            isPubDate = false;
        }
    }

}
