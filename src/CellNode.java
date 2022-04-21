public class CellNode implements Comparable<CellNode> {
    private final Cell cell;
    private CellNode parent = null;
    private int pathCost = Integer.MAX_VALUE;
    private double combinedCost = Integer.MAX_VALUE;

    /**
     * Constructs and initializes the cell node.
     * @param cell The cell the node belongs to.
     */
    public CellNode(Cell cell)
    {
        this.cell = cell;
    }

    /**
     * Getter for the cell in the node.
     * @return cell.
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * Getter for parent node.
     * @return parent node.
     */
    public CellNode getParent() {
        return parent;
    }

    /**
     * Setter for parent node.
     * @param parent parent node.
     */
    public void setParent(CellNode parent) {
        this.parent = parent;
    }

    /**
     * Getter for path cost.
     * @return path cost.
     */
    public int getPathCost() {
        return pathCost;
    }

    /**
     * Setter for path cost.
     * The path cost is the number total distance travelled from the start cell to reach this node.
     * @param pathCost path cost.
     */
    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    /**
     * Getter for combined cost.
     * @return combined cost.
     */
    public double getCombinedCost() {
        return combinedCost;
    }

    /**
     * Setter for the combined cost.
     * The combined cost is the path cost + the displacement to end cell.
     * @param combinedCost combined cost.
     */
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

    /**
     * Equals method.
     * @param o comparison object.
     * @return true if both cells are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellNode node = (CellNode) o;
        return cell == node.cell;
    }

    /**
     * String representation of a cell node.
     * @return string representation of the cell.
     */
    @Override
    public String toString() {
        return this.cell.toString();
    }
}
