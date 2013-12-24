import java.util.Arrays;
import java.util.HashMap;

public class FreeSizeGrid extends Grid {

    private HashMap<Integer, HashMap<Integer, LifeCell>> rows;

    public FreeSizeGrid()
    {
        rows = new HashMap<Integer, HashMap<Integer, LifeCell>>();
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
            return (rows.get(y).get(x));
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public Grid generation(int generation) {
        FreeSizeGrid currentGenerationGrid = this.getWithEmbrionalCells();
        FreeSizeGrid nextGenerationGrid = currentGenerationGrid;

        for (int generationIndex = 0; generationIndex < generation; generationIndex++)
        {
            nextGenerationGrid = new FreeSizeGrid();

            for (Integer yInGrid : currentGenerationGrid.rows.keySet())
            {
                for (Integer xInGrid : currentGenerationGrid.rows.get(yInGrid).keySet())
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

            currentGenerationGrid = nextGenerationGrid.getWithEmbrionalCells();
        }

        return nextGenerationGrid;
    }

    private FreeSizeGrid getWithEmbrionalCells()
    {
        FreeSizeGrid grid = new FreeSizeGrid();
        for (Integer yInGrid : rows.keySet())
        {
            for (Integer xInGrid : rows.get(yInGrid).keySet())
            {
                grid.putCellAt(xInGrid, yInGrid, getLifeCell(xInGrid, yInGrid));
                if (grid.getLifeAt(xInGrid, yInGrid))
                {
                    for (int xIndex = xInGrid - 1; xIndex <= xInGrid + 1; xIndex++)
                    {
                        for (int yIndex = yInGrid - 1; yIndex <= yInGrid + 1; yIndex++)
                        {
                            LifeCell embrionalCell = getLifeCell(xIndex, yIndex);
                            if (embrionalCell == null)
                            {
                                grid.putCellAt(xIndex, yIndex, new LifeCell());
                            }
                        }
                    }
                }
            }
        }

        return grid;
    }

    private void putCellAt(int xIndex, int yIndex, LifeCell cell) {
        if (!rows.containsKey(yIndex))
        {
            rows.put(yIndex, new HashMap<Integer, LifeCell>());
        }

        rows.get(yIndex).put(xIndex, cell);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        Integer[] rowIndexes = rows.keySet().toArray(new Integer[0]);
        Arrays.sort(rowIndexes);
        for (Integer yInGrid : rowIndexes)
        {
            Integer[] columnIndexes = rows.get(yInGrid).keySet().toArray(new Integer[0]);
            Arrays.sort(columnIndexes);
            for (Integer xInGrid : columnIndexes)
            {
                LifeCell cell = getLifeCell(xInGrid, yInGrid);
                string.append(cell.isAlive ? "*" : " ");
            }

            string.append("\n");
        }

        return string.toString();
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
