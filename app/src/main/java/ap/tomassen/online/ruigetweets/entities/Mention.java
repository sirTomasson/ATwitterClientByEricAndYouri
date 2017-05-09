package ap.tomassen.online.ruigetweets.entities;

import ap.tomassen.online.ruigetweets.Entity;

/**
 * Created by Eric on 8-5-2017.
 */

public class Mention extends Entity{
    private int userId;
    private String name;
    private String screenName;

    public int getUserId() {
        return userId;
    }

    public Mention(int userId, String name, String screenName) {
        this.userId = userId;
        this.name = name;
        this.screenName = screenName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
