package ap.tomassen.online.ruigetweets.model;

/**
 * Created by youri on 17-5-2017.
 */

public class TwitterApi {
    private String token;

    private static TwitterApi api = null;

    private TwitterApi() {
    }

    public static TwitterApi getInstance() {
        if (api == null) {
            api = new TwitterApi();
        }
        return api;
    }
}
