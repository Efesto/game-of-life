import java.util.HashMap;

public class FreeSizeGrid extends Grid {

    private HashMap<Integer, HashMap<Integer, LifeCell>> columns;

    public FreeSizeGrid()
    {
        columns = new HashMap<Integer, HashMap<Integer, LifeCell>>();
    }

    @Override
    public void putLifeAt(int x, int y) {
        LifeCell newCell = new LifeCell();
        newCell.isAlive = true;
        putCellAt(x, y, newCell);
    }

    @Override
    public boolean getLifeAt(int x, int y) {
        try
        {
            return getLifeCell(x, y).isAlive;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private LifeCell getLifeCell(int x, int y) {
        try
        {
            return (columns.get(x).get(y));
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public Grid generation(int generation) {
        FreeSizeGrid currentGenerationGrid = this;
        FreeSizeGrid nextGenerationGrid = currentGenerationGrid;

        for (int generationIndex = 0; generationIndex < generation; generationIndex++)
        {
            nextGenerationGrid = new FreeSizeGrid();
            currentGenerationGrid = currentGenerationGrid.getWithEmbrionalCells();

            for (Integer xInGrid : currentGenerationGrid.columns.keySet())
            {
                for (Integer yInGrid : currentGenerationGrid.columns.get(xInGrid).keySet())
                {
                    LifeCell currentGenerationCell = currentGenerationGrid.getLifeCell(xInGrid, yInGrid);

                    for (int xIndex = xInGrid - 1; xIndex <= xInGrid + 1; xIndex++)
                    {
                        for (int yIndex = yInGrid - 1; yIndex <= yInGrid + 1; yIndex++)
                        {
                            LifeCell neighbourCell = currentGenerationGrid.getLifeCell(xIndex, yIndex);
                            if (neighbourCell != null && neighbourCell.isAlive && (xIndex != xInGrid || yIndex != yInGrid))
                            {
                                currentGenerationCell.neighbours++;
                            }
                        }
                    }

                    LifeCell nextGenerationCell = new LifeCell();

                    nextGenerationCell.isAlive = currentGenerationCell.willSurvive();
                    currentGenerationCell.neighbours = 0;

                    nextGenerationGrid.putCellAt(xInGrid, yInGrid, nextGenerationCell);
                }
            }

            currentGenerationGrid = nextGenerationGrid;
        }

        return nextGenerationGrid;
    }

    private FreeSizeGrid getWithEmbrionalCells()
    {
        FreeSizeGrid grid = new FreeSizeGrid();
        for (Integer xInGrid : columns.keySet())
        {
            for (Integer yInGrid : columns.get(xInGrid).keySet())
            {
                grid.putCellAt(xInGrid, yInGrid, getLifeCell(xInGrid, yInGrid));

                for (int xIndex = xInGrid - 1; xIndex <= xInGrid + 1; xIndex++)
                {
                    for (int yIndex = yInGrid - 1; yIndex <= yInGrid + 1; yIndex++)
                    {
                        //Celle embrione per espansione infinita
                        LifeCell embrionicCell = getLifeCell(xIndex, yIndex);
                        if (embrionicCell == null)
                        {
                            grid.putCellAt(xIndex, yIndex, new LifeCell());
                        }
                    }
                }
            }
        }

        return grid;
    }

    private void putCellAt(int xIndex, int yIndex, LifeCell cell) {
        if (!columns.containsKey(xIndex))
        {
            columns.put(xIndex, new HashMap<Integer, LifeCell>());
        }

        columns.get(xIndex).put(yIndex, cell);
    }

    public class LifeCell {
        public int neighbours = 0;
        public boolean isAlive = false;

        @Override
        public String toString() {
            return isAlive ? "A living cell" : "A Cell Body";
        }

        public boolean willSurvive()
        {
            boolean keepAlive = neighbours == 2 && isAlive;
            boolean toLife = neighbours == 3;
            boolean toDeath = neighbours > 3;

            return ((keepAlive || toLife)) && !toDeath;
        }
    }
}
