package ap.tomassen.online.ruigetweets.entities;

import ap.tomassen.online.ruigetweets.Entity;

/**
 * Created by Eric on 8-5-2017.
 */

public class Link extends Entity {
    private String url;
    private String displayUrl;
    private String extendedUrl;

    public Link(String url, String displayUrl, String extendedUrl) {
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
