package ua.antonk.googlecustomsearch.database.entities;

import java.util.List;

/**
 * Created by Anton on 02.08.2015.
 */

public class Item {

    private class PageMap{
        public List<CustomImage> cse_image;
        public List<CustomImage> cse_thumbnail;
    }

    private class CustomImage{
        public String src;
    }

    private String title;
    private String link;
    private PageMap pagemap;

    private boolean isChecked;

    private String imagePath;
    private String thumbPath;

    public Item(String title, String link, String image, String thumb){
        this.title = title;
        this.link = link;
        this.imagePath = image;
        this.thumbPath = thumb;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getImageLink(){
        String link = null;
        try{
            link = pagemap.cse_image.get(0).src;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return link;
    }

    public String getThumbnailLink(){
        String link = null;
        try{
            link = pagemap.cse_thumbnail.get(0).src;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return link;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public PageMap getPagemap(){
        return pagemap;
    }
}