package ap.tomassen.online.ruigetweets.entity;

/**
 * Created by Eric on 8-5-2017.
 */

public class Hashtag extends Entity {
    private String text;

    public Hashtag(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
