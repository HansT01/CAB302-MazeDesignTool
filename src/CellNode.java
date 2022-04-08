public class CellNode implements Comparable<CellNode> {
    private Cell cell;
    private CellNode parent = null;
    private int pathCost = Integer.MAX_VALUE;
    private double combinedCost = Integer.MAX_VALUE;

    public CellNode(Cell cell)
    {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public CellNode getParent() {
        return parent;
    }

    public void setParent(CellNode parent) {
        this.parent = parent;
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public double getCombinedCost() {
        return combinedCost;
    }

    public void setCombinedCost(double combinedCost) {
        this.combinedCost = combinedCost;
    }

    /**
     * Compares the combined cost of this node and a target node.
     * @param targetNode The target node.
     * @return 0 if same, -1 if less than, 1 if more than.
     */
    @Override
    public int compareTo(CellNode targetNode) {
        if (combinedCost == targetNode.combinedCost) {
            return 0;
        } else if (combinedCost < targetNode.combinedCost) {
            return -1;
        } else {
            return 1;
        }
    }
}