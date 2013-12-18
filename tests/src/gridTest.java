import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class gridTest {

    LifeGrid grid;
    @Before
    public void before()
    {
        grid = new LifeGrid(2,2);
    }

    @Test
    public void grid_deathWhereNoLife()
    {
        assertDeath(0,0,0);
    }

    @Test
    public void grid_lifeWhereNoDeath()
    {
        grid.putLifeAt(0,0);
        assertLife(0,0,0);
    }

    @Test
    public void grid_DieByUnderPopulation_NoNeighbours()
    {
        grid.putLifeAt(0,0);
        assertDeath(0,0,1);
    }

    @Test
    public void grid_DieByUnderPopulation_OnlyOnNeighbour()
    {
        grid.putLifeAt(0,0);
        grid.putLifeAt(0,1);
        assertDeath(0,0,1);
        assertDeath(0,1,1);
    }

    @Test
    public void grid_DieByUnderPopulation_AtLeastTwoNeighbours()
    {
        grid.putLifeAt(0,0);
        grid.putLifeAt(0,1);
        grid.putLifeAt(1,0);

        assertLife(0,0,1);
        assertLife(0,1,1);
        assertLife(1,0,1);
        assertLife(1,1,1);
    }

    @Test
    public void grid_DieByOvercrowding_AtLeastThreeNeighbours()
    {
        grid.putLifeAt(0,0);
        grid.putLifeAt(0,1);
        grid.putLifeAt(1,0);
        grid.putLifeAt(1,1);

        assertDeath(0,0,1);
        assertDeath(0,1,1);
        assertDeath(1,0,1);
        assertDeath(1,1,1);
    }

    private void assertLife(int x, int y, int generation)
    {
        assertEquals("Death where life expected!", true, grid.generation(generation).getLifeAt(x,y));
    }

    private void assertDeath(int x, int y, int generation)
    {
        assertEquals("Life where death expected!", false, grid.generation(generation).getLifeAt(x,y));
    }
}
