package com.hucmuaf.locket_mobile;

public class Image {
   private String id;
   private String urlImage;
   private String caption;

    public Image() {
    }

    public Image(String id, String urlImage, String caption) {
        this.id = id;
        this.urlImage = urlImage;
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
