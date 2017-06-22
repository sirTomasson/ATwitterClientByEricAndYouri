package ap.tomassen.online.ruigetweets.model;

/**
 * an entity super class
 */
public class Entity {
    private int[] indices;

    public Entity() {

    }

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
