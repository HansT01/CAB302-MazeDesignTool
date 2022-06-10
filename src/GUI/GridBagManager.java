package GUI;

import java.awt.*;

public class GridBagManager extends GridBagConstraints {
    public int outerPaddingSize = 20;
    public int innerPaddingSize = 5;

    /**
     * Creates a GridBagConstraints object for objects on the main frame.
     * Notable features include a larger padding size and padding around the outer elements.
     * @param x x location of the grid bag layout
     * @param y y location of the grid bag layout
     * @return GridBagConstraints object
     */
    public GridBagConstraints CreateOuterGBC(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.insets = new Insets((y==0) ? outerPaddingSize : 0, (x==0) ? outerPaddingSize : 0, outerPaddingSize, outerPaddingSize);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }

    /**
     * Creates a GridBagConstraints object for objects inside a panel.
     * Notable features include smaller padding size and no padding around the outer elements.
     * @param x x location of the grid bag layout
     * @param y y location of the grid bag layout
     * @return GridBagConstraints object
     */
    public GridBagConstraints CreateInnerGBC(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets((y!=0) ? innerPaddingSize : 0, (x!=0) ? innerPaddingSize : 0, 0, 0);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }
}
