public class FixedSizeGrid extends Grid {

    private final int height;
    private final int width;
    boolean[][] grid;

    public FixedSizeGrid(int width, int height)
    {
        this.height = height;
        this.width = width;

        grid = new boolean[width][height];
    }

    @Override
    public void putLifeAt(int x, int y)
    {
        putAt(x, y, true);
    }

    @Override
    public boolean getLifeAt(int x, int y)
    {
        return grid[x][y];
    }

    private void putAt(int x, int y, boolean life)
    {
        grid[x][y] = life;
    }

    @Override
    public Grid generation(int generation)
    {
        FixedSizeGrid nextGenerationGrid = this;
        FixedSizeGrid currentGenerationGrid;

        for (int generationIndex = 0; generationIndex < generation; generationIndex++)
        {
            currentGenerationGrid = nextGenerationGrid;
            nextGenerationGrid = new FixedSizeGrid(width, height);
            for (int xIndex = 0; xIndex < width; xIndex++)
            {
                for (int yIndex = 0; yIndex < height; yIndex++)
                {
                    nextGenerationGrid.putAt(xIndex, yIndex, currentGenerationGrid.lifeTo(xIndex, yIndex));
                }
            }
        }

        return nextGenerationGrid;
    }

    private boolean lifeTo(int x, int y)
    {
        int livingNeighboursCount = 0;
        for (int xIndex = Math.max(x - 1, 0); xIndex <= Math.min(x + 1, width - 1); xIndex++)
        {
            for (int yIndex = Math.max(y - 1, 0); yIndex <= Math.min(y + 1, height - 1); yIndex++)
            {
                if ((xIndex != x || yIndex != y) && getLifeAt(xIndex, yIndex))
                {
                    livingNeighboursCount++;
                }
            }
        }

        boolean keepAlive = livingNeighboursCount > 1 && livingNeighboursCount < 3 && getLifeAt(x,y);
        boolean toLife = livingNeighboursCount == 3;
        boolean toDeath = livingNeighboursCount > 3;

        return (keepAlive || toLife) && !toDeath;
    }

//    @Override
//    public String toString() {
//        for (int xIndex = 0; xIndex < width; xIndex++)
//        {
//            for (int yIndex = 0; yIndex < height; yIndex++)
//            {
//
//
//            }
//        }
//    }
}
