package es.upm.ssr.gatv.tfg;


/*
 * Data object that holds all of our information about a StackExchange Site.
 */
public class Entry {

    private String title;
    private String link;
    private String summary;
    private String audio;
    private String imgUrl;
    private String imgMsgUrl;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getImgMsgUrl() {
        return imgMsgUrl;
    }
    public void setImgMsgUrl(String imgMsgUrl){this.imgMsgUrl = imgMsgUrl;}
    public String getAudioUrl(){return audio;}
    public void setAudioUrl(String audio){this.audio = audio;}
    @Override
    public String toString() {
        return "Entry [title=" + title + ", link=" + link + ", summary="
                + summary + ", imgUrl=" + imgUrl + ", imgMsgUrl=" + imgMsgUrl + ", audioUrl=" + audio + "]";
    }
}
