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
        try
        {
            return (width.get(x).get(y));
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

            for (Integer cellColumn : width.keySet())
            {
                System.out.println("Computing column " + cellColumn);
                for (Integer cellRow : width.get(cellColumn).keySet())
                {
                    System.out.println("Computing row " + cellRow);
                    LifeCell currentGenerationCell = currentGenerationGrid.getLifeCell(cellColumn, cellRow);

                    for (int xIndex = cellColumn - 1; xIndex <= cellColumn + 1; xIndex++)
                    {
                        for (int yIndex = cellRow - 1; yIndex <= cellRow + 1; yIndex++)
                        {
                            //Celle embrione
                            LifeCell embrionicCell = nextGenerationGrid.getLifeCell(xIndex, yIndex);
                            if (embrionicCell == null)
                            {
                                System.out.println("Put embrion cell at " + xIndex + ", "+ yIndex);
                                nextGenerationGrid.putCellAt(xIndex, yIndex, new LifeCell());
                            }

                            LifeCell neighbourCell = currentGenerationGrid.getLifeCell(xIndex, yIndex);
                            if (neighbourCell != null && neighbourCell.isAlive && (xIndex != cellColumn || yIndex != cellRow))
                            {
                                System.out.println("Put a neighbour in " + xIndex + ", " + yIndex +" for cell at " + cellRow + ", " + cellColumn);
                                currentGenerationCell.neighbours++;
                            }
                        }
                    }

                    LifeCell nextGenerationCell = new LifeCell();

                    nextGenerationCell.isAlive = currentGenerationCell.willSurvive();
                    currentGenerationCell.neighbours = 0;

                    System.out.println("Put a " + nextGenerationCell.isAlive +" for generation " + generationIndex + " cell at " + cellRow + ", " + cellColumn);
                    nextGenerationGrid.putCellAt(cellColumn, cellRow, nextGenerationCell);
                }
            }

            currentGenerationGrid = nextGenerationGrid;
        }

        return nextGenerationGrid;
    }

    private void putCellAt(int xIndex, int yIndex, LifeCell cell) {
        if (!width.containsKey(xIndex))
        {
            width.put(xIndex, new HashMap<Integer, LifeCell>());
        }

        width.get(xIndex).put(yIndex, cell);
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

            return (keepAlive || toLife) && !toDeath;
        }
    }
}
