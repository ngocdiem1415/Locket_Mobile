package com.hucmuaf.locket_mobile;

public class Activity {
   private String id;
   private String idImage;
   private String idUser;
   private String icon;

    public Activity() {
    }

    public Activity(String id, String idImage, String idUser, String icon) {
        this.id = id;
        this.idImage = idImage;
        this.idUser = idUser;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
