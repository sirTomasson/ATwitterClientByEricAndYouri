package ap.tomassen.online.ruigetweets.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric on 8-5-2017.
 */

public class HashTag extends Entity {
    private String text;

    public HashTag(JSONObject hashTagObj)throws JSONException{
        super();
        text = hashTagObj.getString("text");
        JSONArray indicesArr = hashTagObj.getJSONArray("indices");
        int indices[] = {indicesArr.getInt(0), indicesArr.getInt(1) };
        setIndices(indices);
    }

    public HashTag(int[] indices, String text) {
        super(indices);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
