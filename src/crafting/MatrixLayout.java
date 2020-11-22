// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.GridLayout;

public class MatrixLayout extends GridLayout
{
    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        synchronized (parent.getTreeLock()) {
            final Insets insets = parent.getInsets();
            final int ncomponents = parent.getComponentCount();
            int nrows = this.getRows();
            int ncols = this.getColumns();
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            }
            else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            final int[] w = new int[ncols];
            final int[] h = new int[nrows];
            for (int i = 0; i < ncomponents; ++i) {
                final int r = i / ncols;
                final int c = i % ncols;
                final Component comp = parent.getComponent(i);
                final Dimension d = comp.getPreferredSize();
                if (w[c] < d.width) {
                    w[c] = d.width;
                }
                if (h[r] < d.height) {
                    h[r] = d.height;
                }
            }
            int nw = 0;
            for (int j = 0; j < ncols; ++j) {
                nw += w[j];
            }
            int nh = 0;
            for (int k = 0; k < nrows; ++k) {
                nh += h[k];
            }
            return new Dimension(insets.left + insets.right + nw + (ncols - 1) * this.getHgap(), insets.top + insets.bottom + nh + (nrows - 1) * this.getVgap());
        }
    }
    
    @Override
    public void layoutContainer(final Container parent) {
        synchronized (parent.getTreeLock()) {
            final Insets insets = parent.getInsets();
            final int ncomponents = parent.getComponentCount();
            int nrows = this.getRows();
            int ncols = this.getColumns();
            if (ncomponents == 0) {
                return;
            }
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            }
            else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            final int hgap = this.getHgap();
            final int vgap = this.getVgap();
            final Dimension pd = this.preferredLayoutSize(parent);
            final double sw = 1.0 * parent.getWidth() / pd.width;
            final double sh = 1.0 * parent.getHeight() / pd.height;
            final int[] w = new int[ncols];
            final int[] h = new int[nrows];
            for (int i = 0; i < ncomponents; ++i) {
                final int r = i / ncols;
                final int c = i % ncols;
                final Component comp = parent.getComponent(i);
                final Dimension d = comp.getPreferredSize();
                d.width *= (int)sw;
                d.height *= (int)sh;
                if (w[c] < d.width) {
                    w[c] = d.width;
                }
                if (h[r] < d.height) {
                    h[r] = d.height;
                }
            }
            int c2 = 0;
            int x = insets.left;
            while (c2 < ncols) {
                int r2 = 0;
                int y = insets.top;
                while (r2 < nrows) {
                    final int j = r2 * ncols + c2;
                    if (j < ncomponents) {
                        parent.getComponent(j).setBounds(x, y, w[c2], h[r2]);
                    }
                    y += h[r2] + vgap;
                    ++r2;
                }
                x += w[c2] + hgap;
                ++c2;
            }
        }
    }
}
