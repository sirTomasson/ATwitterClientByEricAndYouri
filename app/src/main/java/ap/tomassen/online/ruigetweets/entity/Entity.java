package ap.tomassen.online.ruigetweets.entity;

/**
 * Created by Eric on 8-5-2017.
 */

public class Entity {
    private int[] indices;


    public Entity(int[] indices) {
        this.indices = indices;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }
}