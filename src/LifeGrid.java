
public class LifeGrid {

    private final int length;
    private final int width;
    boolean[][] grid;

    public LifeGrid(int length, int width)
    {
        this.length = length;
        this.width = width;

        grid = new boolean[length][width];
    }

    public void putLifeAt(int x, int y)
    {
        putAt(x, y, true);
    }

    public boolean getLifeAt(int x, int y)
    {
        return grid[x][y];
    }

    private void putAt(int x, int y, boolean life)
    {
        grid[x][y] = life;
    }

    public LifeGrid generation(int generation)
    {
        LifeGrid nextGenerationGrid = this;

        for (int generationIndex = 0; generationIndex < generation; generationIndex++)
        {
            nextGenerationGrid = new LifeGrid(length, width);
            for (int xIndex = 0; xIndex < width; xIndex++)
            {
                for (int yIndex = 0; yIndex < width; yIndex++)
                {
                    nextGenerationGrid.putAt(xIndex, yIndex, lifeForLoneliness(xIndex, yIndex));
                }
            }
        }

        return nextGenerationGrid;
    }

    private boolean lifeForLoneliness(int x, int y)
    {
        int livingNeighboursCount = 0;
        for (int xIndex = Math.max(x -1, 0); xIndex <= Math.min(x+1, width - 1); xIndex++)
        {
            for (int yIndex = Math.max(y -1, 0); yIndex <= Math.min(y+1, width - 1); yIndex++)
            {
                if ((xIndex != x || yIndex != y) && getLifeAt(xIndex, yIndex))
                {
                    livingNeighboursCount++;
                }
            }
        }

        return livingNeighboursCount > 1;
    }
}
