package ap.tomassen.online.ruigetweets.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Link entity from a tweet
 */
public class Link extends Entity {
    private String url;
    private String displayUrl;
    private String expandedUrl;

    public Link(JSONObject linkObj) throws JSONException{
        super();
        url = linkObj.getString("url");
        displayUrl = linkObj.getString("display_url");
        expandedUrl = linkObj.getString("expanded_url");

        JSONArray indicesArr = linkObj.getJSONArray("indices");
        int indices[] = {indicesArr.getInt(0), indicesArr.getInt(1) };
        setIndices(indices);
    }

    public String getUrl() {
        return url;
    }


    public String getDisplayUrl() {
        return displayUrl;
    }


    public String getExpandedUrl() {
        return expandedUrl;
    }

}
