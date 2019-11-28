package edu.ivytech.photogallery;

public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;
    private String mTag;

    public String getCaption() {
        return mCaption;
    }
    public String getTag() {

        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return mCaption;
    }
}
