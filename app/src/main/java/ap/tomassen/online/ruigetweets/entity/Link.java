package ap.tomassen.online.ruigetweets.entity;

/**
 * Created by Eric on 8-5-2017.
 */

public class Link extends Entity {
    private String url;
    private String displayUrl;
    private String extendedUrl;

    public Link(int[] indices, String url, String displayUrl, String extendedUrl) {
        super(indices);
        this.url = url;
        this.displayUrl = displayUrl;
        this.extendedUrl = extendedUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getExtendedUrl() {
        return extendedUrl;
    }

    public void setExtendedUrl(String extendedUrl) {
        this.extendedUrl = extendedUrl;
    }
}
