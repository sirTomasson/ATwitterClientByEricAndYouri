package ap.tomassen.online.ruigetweets.entities;

import ap.tomassen.online.ruigetweets.Entity;

/**
 * Created by Eric on 8-5-2017.
 */

public class Hashtag extends Entity {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
