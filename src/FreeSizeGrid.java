import java.util.HashMap;

/**
 * Created by marcop on 12/19/13.
 */
public class FreeSizeGrid extends Grid {

    private HashMap<Integer, HashMap<Integer, LifeCell>> width;

    public FreeSizeGrid()
    {
        width = new HashMap<Integer, HashMap<Integer, LifeCell>>();
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
        return (width.get(x).get(y));
    }

    @Override
    public Grid generation(int generation) {
        FreeSizeGrid nextGenerationGrid = this;
        FreeSizeGrid currentGenerationGrid;

        for (int generationIndex = 0; generationIndex < generation; generationIndex++)
        {
            currentGenerationGrid = nextGenerationGrid;
            nextGenerationGrid = new FreeSizeGrid();

            for (Integer cellColumn : width.keySet())
            {
                for (Integer cellRow : width.get(cellColumn).keySet())
                {
                    LifeCell cell = currentGenerationGrid.getLifeCell(cellColumn, cellRow);

                    for (int xIndex = cellColumn - 1; xIndex <= cellColumn + 1; xIndex++)
                    {
                        for (int yIndex = cellRow - 1; yIndex <= cellRow + 1; yIndex++)
                        {
                            //Celle embrione
                            if (!nextGenerationGrid.getLifeAt(xIndex, yIndex))
                            {
                                nextGenerationGrid.putCellAt(xIndex, yIndex, new LifeCell());
                            }

                            if (currentGenerationGrid.getLifeAt(xIndex, yIndex) && (xIndex != cellColumn || yIndex != cellRow))
                            {
                                cell.neighbours++;
                            }
                        }
                    }

                    cell.isAlive = cell.willSurvive();

                    nextGenerationGrid.putCellAt(cellColumn, cellRow, cell);
                }
            }
        }

        return nextGenerationGrid;
    }

    private void putCellAt(int xIndex, int yIndex, LifeCell cell) {
        if (!width.containsKey(xIndex))
        {
            width.put(xIndex, new HashMap<Integer, LifeCell>());
        }

        if (!width.get(xIndex).containsKey(yIndex))
        {
            width.get(xIndex).put(yIndex, cell);
        }
    }

    public class LifeCell {
        public int neighbours = 0;
        public boolean isAlive = false;

        public boolean willSurvive()
        {
            boolean keepAlive = neighbours == 2 && isAlive;
            boolean toLife = neighbours == 3;
            boolean toDeath = neighbours > 3;

            return (keepAlive || toLife) && !toDeath;
        }
    }
}
