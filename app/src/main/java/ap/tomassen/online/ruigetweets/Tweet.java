package ap.tomassen.online.ruigetweets;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Eric on 8-5-2017.
 */

public class Tweet {
    private int id;
    private Date createdAt;
    private String text;
    private int reTweetCount;
    private int favoriteCount;

    public Tweet(int id, Date createdAt, String text, int reTweetCount, int favoriteCount) {
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
        this.reTweetCount = reTweetCount;
        this.favoriteCount = favoriteCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getReTweetCount() {
        return reTweetCount;
    }

    public void setReTweetCount(int reTweetCount) {
        this.reTweetCount = reTweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
