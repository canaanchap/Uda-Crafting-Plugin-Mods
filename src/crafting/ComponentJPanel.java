// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

import java.util.Formatter;
import javax.swing.LayoutStyle;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.GroupLayout;
import java.awt.Font;
import java.awt.Color;
import com.lsd.umc.util.AnsiTable;
import com.lsd.umc.script.ScriptInterface;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JPanel;

public class ComponentJPanel extends JPanel implements Comparable
{
    private final AtomicInteger materialCount;
    private String materialName;
    private String materialShortName;
    private String materialCategory;
    private String materialType;
    private int materialTier;
    private int materialTech;
    private boolean isExpert;
    private boolean isArtist;
    private boolean isComponent;
    private static final HashMap<String, Integer> tradeMap;
    private final CraftingFrame frame;
    private JLabel componentCountLabel;
    private JLabel nameLabel;
    private JLabel typeLabel;
    
    public ComponentJPanel(final CraftingFrame craftingFrame, final boolean listener) {
        this.materialCount = new AtomicInteger();
        this.materialName = "";
        this.materialShortName = "";
        this.materialCategory = "?";
        this.materialType = "";
        this.materialTier = 0;
        this.materialTech = 0;
        this.isExpert = false;
        this.isArtist = false;
        this.isComponent = true;
        this.frame = craftingFrame;
        this.initComponents();
        if (listener) {
            final ComponentJPanel panel = this;
            this.addMouseListener(new MouseListener() {
                public void mouseClicked(final MouseEvent e) {
                }
                
                public void mousePressed(final MouseEvent e) {
                    if (e.isShiftDown()) {
                        ComponentJPanel.this.frame.rightClick(panel);
                    }
                    if (e.isMetaDown()) {
                        ComponentJPanel.this.frame.rightClick(panel);
                    }
                    else {
                        ComponentJPanel.this.frame.leftClick(panel);
                    }
                }
                
                public void mouseReleased(final MouseEvent e) {
                }
                
                public void mouseEntered(final MouseEvent e) {
                }
                
                public void mouseExited(final MouseEvent e) {
                }
            });
        }
    };
    
    public ComponentJPanel(final CraftingFrame craftingFrame) {
        this.materialCount = new AtomicInteger();
        this.materialName = "";
        this.materialShortName = "";
        this.materialCategory = "?";
        this.materialType = "";
        this.materialTier = 0;
        this.materialTech = 0;
        this.isExpert = false;
        this.isArtist = false;
        this.isComponent = true;
        this.frame = craftingFrame;
        this.initComponents();
    }
    
    public boolean isComponent() {
        return this.isComponent;
    }
    
    public void setComponent(final boolean value) {
        this.isComponent = value;
        this.updateLabels();
    }
    
    public boolean isExpert() {
        return this.isExpert;
    }
    
    public boolean isArtist() {
        return this.isArtist;
    }
    
    public void setExpert(final boolean value) {
        this.isExpert = value;
        this.updateLabels();
    }
    
    public void setArtist(final boolean value) {
        this.isArtist = value;
        this.updateLabels();
    }
    
    private void updateLabels() {
        if (this.isExpert && this.isArtist) {
            this.typeLabel.setForeground(AnsiTable.ansiGreen);
            this.typeLabel.setFont(CraftingFrame.FONT_BOLD);
        }
        else if (this.isExpert) {
            this.typeLabel.setForeground(AnsiTable.ansiLightBlue);
            this.typeLabel.setFont(CraftingFrame.FONT_BOLD);
        }
        else if (this.isArtist) {
            this.typeLabel.setForeground(new Color(255, 126, 0));
            this.typeLabel.setFont(CraftingFrame.FONT_BOLD);
        }
        else {
            this.typeLabel.setForeground(AnsiTable.ansiGray);
            this.typeLabel.setFont(CraftingFrame.FONT_PLAIN);
        }
        if (this.materialCount.get() == 0) {
            this.nameLabel.setForeground(AnsiTable.ansiGray);
        }
        else {
            this.nameLabel.setForeground(AnsiTable.ansiBlack);
        }
        if (this.isComponent) {
            if ((!this.materialType.equalsIgnoreCase("gemstone") && this.materialCount.get() < 2) || (this.materialType.equalsIgnoreCase("gemstone") && this.materialCount.get() == 0)) {
                this.componentCountLabel.setForeground(AnsiTable.ansiGray);
            }
            else {
                this.componentCountLabel.setForeground(AnsiTable.ansiBlack);
            }
        }
        else if (this.materialCount.get() > 0) {
            this.componentCountLabel.setForeground(AnsiTable.ansiBlack);
            if (this.getTradeCount() > 0) {
                this.nameLabel.setFont(CraftingFrame.FONT_BOLD);
                this.componentCountLabel.setFont(CraftingFrame.FONT_BOLD);
            }
            else {
                this.nameLabel.setFont(CraftingFrame.FONT_PLAIN);
                this.componentCountLabel.setFont(CraftingFrame.FONT_PLAIN);
            }
        }
        else {
            this.componentCountLabel.setForeground(AnsiTable.ansiGray);
            this.componentCountLabel.setFont(CraftingFrame.FONT_PLAIN);
            this.nameLabel.setFont(CraftingFrame.FONT_PLAIN);
        }
    }
    
    private void initComponents() {
        this.typeLabel = new JLabel();
        this.componentCountLabel = new JLabel();
        this.nameLabel = new JLabel();
        this.setBackground(new Color(255, 255, 255));
        this.typeLabel.setFont(new Font("Arial", 0, 12));
        this.typeLabel.setText("A");
        this.typeLabel.setName("typeLabel");
        this.componentCountLabel.setFont(new Font("Arial", 0, 12));
        this.componentCountLabel.setText("00");
        this.componentCountLabel.setName("componentCountLabel");
        this.nameLabel.setFont(new Font("Arial", 0, 12));
        this.nameLabel.setText(" Ancient Elm");
        this.nameLabel.setName("nameLabel");
        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.typeLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.componentCountLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.nameLabel)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.typeLabel).addComponent(this.componentCountLabel).addComponent(this.nameLabel)));
    }
    
    public String getMaterialName() {
        return this.materialName;
    }
    
    public void setMaterialName(final String name) {
        this.materialName = name;
        this.nameLabel.setText(name);
        this.updateLabels();
    }
    
    public int getMaterialTier() {
        return this.materialTier;
    }
    
    public void setMaterialTier(final int tier) {
        this.materialTier = tier;
        this.updateLabels();
    }
    
    public int getMaterialTech() {
        return this.materialTech;
    }
    
    public void setMaterialTech(final int tech) {
        this.materialTech = tech;
        this.updateLabels();
    }
    
    public int getMaterialCount() {
        return this.materialCount.get();
    }
    
    public void setMaterialType(final String type) {
        if (type.equals("gemstone")) {
            this.isComponent = true;
        }
        else if (type.equals("jewels")) {
            this.isComponent = false;
        }
        this.materialType = type;
    }
    
    public String getMaterialType() {
        return this.materialType;
    }
    
    public void setMaterialCount(final int c) {
        this.materialCount.set(c);
        final StringBuilder buf = new StringBuilder();
        final Formatter formatter = new Formatter(buf);
        formatter.format("%02d", c);
        this.componentCountLabel.setText(buf.toString());
        this.updateLabels();
    }
    
    public String getMaterialCategory() {
        return this.materialCategory;
    }
    
    public void setMaterialCategory(final String category) {
        this.materialCategory = category;
        if (category.equals("") || this.materialType.equals("gemstone")) {
            this.typeLabel.setVisible(false);
        }
        else {
            this.typeLabel.setText(category);
        }
        this.updateLabels();
    }
    
    public int compareTo(final Object arg0) {
        if (arg0 instanceof ComponentJPanel) {
            final ComponentJPanel other = (ComponentJPanel)arg0;
            return this.getMaterialName().compareTo(other.getMaterialName());
        }
        return 0;
    }
    
    public int getTradeRatio() {
        Integer ratio = ComponentJPanel.tradeMap.get(this.materialType);
        if (ratio == null) {
            ratio = 0;
        }
        return ratio;
    }
    
    public int getTradeCount() {
        final int ratio = this.getTradeRatio();
        if (ratio > 0) {
            int trade = this.materialCount.get() % ratio;
            trade = (this.materialCount.get() - trade) / ratio;
            return trade;
        }
        return 0;
    }
    
    static {
        (tradeMap = new HashMap<String, Integer>()).put("layer", 3);
        ComponentJPanel.tradeMap.put("bolt", 4);
        ComponentJPanel.tradeMap.put("plank", 4);
        ComponentJPanel.tradeMap.put("ingot", 5);
        ComponentJPanel.tradeMap.put("gemstone", 12);
    }
}
