/**
 * Created by marcop on 12/19/13.
 */
public abstract class Grid {
    public abstract void putLifeAt(int x, int y);

    public abstract boolean getLifeAt(int x, int y);

    public abstract Grid generation(int generation);
}
