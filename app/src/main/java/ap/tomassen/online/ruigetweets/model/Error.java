package ap.tomassen.online.ruigetweets.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Error {
    private String message;
    private int code;

    public Error(JSONObject twitterError) {
        try {
            message = twitterError.getString("message");
            code = twitterError.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
