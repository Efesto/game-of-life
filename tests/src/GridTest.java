import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GridTest {

//    http://codingdojo.org/cgi-bin/wiki.pl?KataGameOfLife

//    1. Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
//    2. Any live cell with more than three live neighbours dies, as if by overcrowding.
//    3. Any live cell with two or three live neighbours lives on to the next generation.
//    4. Any dead cell with exactly three live neighbours becomes a live cell.

    LifeGrid grid;
    @Before
    public void before()
    {
        grid = new LifeGrid(3,3);
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
    public void grid_DieByUnderPopulation_OnlyOneNeighbour()
    {
        grid.putLifeAt(0,0);
        grid.putLifeAt(0,1);
        assertDeath(0,0,1);
        assertDeath(0,1,1);
    }

    @Test
    public void grid_SurviveByUnderPopulation_AtLeastTwoNeighbours()
    {
        grid.putLifeAt(0,0);
        grid.putLifeAt(0,1);
        grid.putLifeAt(1,0);

        assertLife(0,0,1);
        assertLife(0,1,1);
        assertLife(1,0,1);
    }

    @Test
    public void grid_DieByOvercrowding_lifeWithThreeNeighbours()
    {
        //**.
        //**.
        //*?.

        grid.putLifeAt(0,0);
        grid.putLifeAt(0,1);
        grid.putLifeAt(1,0);
        grid.putLifeAt(1,1);

        assertLife(0, 0, 1);
        assertLife(0, 1, 1);
        assertLife(1, 0, 1);
        assertLife(1, 1, 1);
    }

    @Test
    public void grid_DieByOvercrowding_moreThanThreeNeighbours()
    {
        grid.putLifeAt(1,1);

        grid.putLifeAt(0,0);
        grid.putLifeAt(1,0);
        grid.putLifeAt(2,0);
        grid.putLifeAt(0,1);

        assertLife(0,0,1);
        assertLife(2,0,1);
        assertLife(0,1,1);

        assertDeath(1, 0, 1);
        assertDeath(1,1,1);
    }

    @Test
    public void grid_BornByOvercrowding_lifeWithExactlyThreeNeighbours()
    {
        grid.putLifeAt(0,0);
        grid.putLifeAt(1,0);
        grid.putLifeAt(1,1);
        grid.putLifeAt(0,2);
        grid.putLifeAt(1,2);

        assertLife(0, 0, 1);
        assertLife(1, 0, 1);
        assertDeath(2, 0, 1);

        assertDeath(1, 1, 1);
        assertDeath(1, 1, 1);
        assertLife(2, 1, 1);

        assertLife(0, 2, 1);
        assertLife(1, 2, 1);
        assertDeath(2, 2, 1);
    }

    @Test
    public void grid_secondGeneration()
    {
        // ***     *.*     ...
        // *.* ==> *.* ==> ...
        // ...     ...     ...

        grid.putLifeAt(0,0);
        grid.putLifeAt(1,0);
        grid.putLifeAt(2,0);

        grid.putLifeAt(0,1);
        grid.putLifeAt(2,1);

        assertDeath(0,0,2);
        assertDeath(0,1,2);
        assertDeath(0,2,2);
        assertDeath(1,0,2);
        assertDeath(1,1,2);
        assertDeath(1,2,2);
        assertDeath(2,0,2);
        assertDeath(2,1,2);
        assertDeath(2,2,2);
    }

    private void assertLife(int x, int y, int generation)
    {
        assertEquals("Death where life expected at " + x + " " + y + " !", true, grid.generation(generation).getLifeAt(x,y));
    }

    private void assertDeath(int x, int y, int generation)
    {
        assertEquals("Life where death expected at " + x + " " + y + " !", false, grid.generation(generation).getLifeAt(x,y));
    }
}
