package ap.tomassen.online.ruigetweets.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric on 8-5-2017.
 */

public class Link extends Entity {
    private String url;
    private String displayUrl;
    private String expandedUrl;

    public Link(JSONObject linkObj) throws JSONException{
        url = linkObj.getString("url");
        displayUrl = linkObj.getString("display_url");
        expandedUrl = linkObj.getString("expanded_url");
    }

    public Link(int[] indices, String url, String displayUrl, String expandedUrl) {
        super(indices);
        this.url = url;
        this.displayUrl = displayUrl;
        this.expandedUrl = expandedUrl;
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

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }
}
