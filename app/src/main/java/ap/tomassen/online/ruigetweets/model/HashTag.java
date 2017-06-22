package ap.tomassen.online.ruigetweets.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Hash tag entity from a tweet
 */
public class HashTag extends Entity {
    private String text;

    /**
     * create hash tag object based on a JSONoObject
     * @param hashTagObj
     * @throws JSONException
     */
    public HashTag(JSONObject hashTagObj)throws JSONException{
        super();
        text = hashTagObj.getString("text");
        JSONArray indicesArr = hashTagObj.getJSONArray("indices");
        int indices[] = {indicesArr.getInt(0), indicesArr.getInt(1) };
        setIndices(indices);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
