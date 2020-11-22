// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.Clipboard;
import org.jdesktop.layout.GroupLayout;
import javax.swing.table.TableModel;
import java.awt.GridLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.TitledBorder;
import java.util.regex.Matcher;
import com.lsd.umc.util.AnsiTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javax.swing.JComponent;
import java.awt.Container;
import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import java.util.Iterator;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JTable;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.util.regex.Pattern;
import javax.swing.border.CompoundBorder;
import javax.swing.JPanel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.TreeMap;
import javax.swing.JLabel;
import java.util.HashMap;
import java.util.LinkedList;
import com.lsd.umc.script.ScriptInterface;
import java.awt.datatransfer.ClipboardOwner;
import javax.swing.JFrame;

public class CraftingFrame extends JFrame implements ClipboardOwner
{
    private ScriptInterface script;
    private final LinkedList<RecipeObject> recipeList;
    private final HashMap<String, Boolean> filterMap;
    private final HashMap<String, ComponentJPanel> tradeMap;
    private final HashMap<String, ComponentJPanel> componentsMap;
    private final HashMap<String, ComponentJPanel> scrapsMap;
    private final HashMap<String, JLabel> labelMap;
    private final TreeMap<ComponentJPanel, AtomicInteger> duplicateMap;
    private final LinkedList<String> gemstones;
    private final JPanel primaryPanel;
    private final CompoundBorder primaryPanelBorder;
    private final JPanel secondaryPanel;
    private final CompoundBorder secondaryPanelBorder;
    private final UpdateThread updateThread;
    private final RecipeTableModel recipeModel;
    private final Pattern secondaryPattern;
    private final Pattern recipePattern;
    public static final Font FONT_BOLD;
    public static final Font FONT_PLAIN;
    private Boolean canTrade;
    private JCheckBox aBox;
    private JCheckBox aboutBox;
    private JCheckBox agiBox;
    private JCheckBox armsBox;
    private JCheckBox artistBox;
    private JLabel artistLabel;
    private JCheckBox bBox;
    private JCheckBox cBox;
    private JCheckBox chestBox;
    private JPanel compPanel;
    private JPanel componentsTab;
    private JCheckBox conBox;
    private JCheckBox dBox;
    private JCheckBox damageBox;
    private JCheckBox dexBox;
    private JCheckBox eBox;
    private JCheckBox enduranceBox;
    private JCheckBox expertBox;
    private JLabel expertLabel;
    private JCheckBox fBox;
    private JCheckBox feetBox;
    private JPanel filterPanel;
    private JCheckBox gBox;
    private JCheckBox hBox;
    private JCheckBox handsBox;
    private JCheckBox headBox;
    private JCheckBox healthBox;
    private JCheckBox hitBox;
    private JCheckBox intBox;
    private JButton jButton1;
    private JCheckBox jCheckBox1;
    private JLabel jLabel1;
    private JScrollPane jScrollPane4;
    private JScrollPane jScrollPane5;
    private JScrollPane jScrollPane6;
    private JCheckBox legsBox;
    private JCheckBox lightBox;
    private JCheckBox manaBox;
    private JCheckBox neckBox;
    private JButton noneButton;
    private JCheckBox nrBox;
    private JCheckBox quickBox;
    private JPopupMenu recipePopup;
    private JScrollPane recipeScrollPanel;
    private JPanel recipeTab;
    private JTable recipeTable;
    private JButton retrieveButton;
    private JPanel scrapPanel;
    private JButton scrapsButton;
    private JPanel scrapsTab;
    private JToggleButton showAllButton;
    private JCheckBox speBox;
    private JCheckBox spiritBox;
    private JPopupMenu stacyPopup;
    private JPanel statsFour;
    private JPanel statsOne;
    private JPanel statsThree;
    private JPanel statsTwo;
    private JCheckBox strBox;
    private JTabbedPane tabs;
    private JCheckBox tier1Box;
    private JCheckBox tier2Box;
    private JCheckBox tier3Box;
    private JCheckBox tier4Box;
    private JButton tradeComponentsButton;
    private JPanel tradePanel;
    private JPanel tradeTab;
    private JPanel autoTradeTab;
    private JCheckBox weaponBox;
    private JCheckBox wilBox;
    private JCheckBox wisBox;
    private JCheckBox wristBox;
    
    public CraftingFrame(final ScriptInterface script) {
        this.recipeList = new LinkedList<RecipeObject>();
        this.filterMap = new HashMap<String, Boolean>();
        this.tradeMap = new HashMap<String, ComponentJPanel>();
        this.componentsMap = new HashMap<String, ComponentJPanel>();
        this.scrapsMap = new HashMap<String, ComponentJPanel>();
        this.labelMap = new HashMap<String, JLabel>();
        this.duplicateMap = new TreeMap<ComponentJPanel, AtomicInteger>();
        this.gemstones = new LinkedList<String>();
        this.primaryPanel = new JPanel();
        this.primaryPanelBorder = new CompoundBorder(BorderFactory.createTitledBorder("Primary Component"), new EmptyBorder(2, 10, 5, 10));
        this.secondaryPanel = new JPanel();
        this.secondaryPanelBorder = new CompoundBorder(BorderFactory.createTitledBorder("Trade Components"), new EmptyBorder(2, 10, 5, 10));
        this.recipeModel = new RecipeTableModel();
        this.secondaryPattern = Pattern.compile("^<html><b>(\\d+)</b> (.+)$");
        this.recipePattern = Pattern.compile(">\\s{0,1}(?:0){0,1}(\\d+)(?:</font>){0,1} ([^(]+?) \\(\\d+\\)\\s{0,1}</html>");
        this.canTrade = false;
        this.script = script;
        this.recipeModel.addColumn("");
        this.recipeModel.addColumn("Primary Component");
        this.recipeModel.addColumn("Secondary Component");
        this.recipeModel.addColumn("Gemstone");
        this.initComponents();
        this.buildComponentsPanel();
        this.buildTradePanel();
        this.buildScrapsPanel();
        this.populateRecipes();
        this.resetData();
        this.updateThread = new UpdateThread();
    }
    
    private void buildRecipePopup() {
        final JMenuItem menuItem = new JMenuItem("Retrieve Components");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                CraftingFrame.this.retrieveRecipeComponents();
            }
        });
        this.recipePopup.add(menuItem);
        this.recipeTable.addMouseListener(new MouseListener() {
            private void mouseEvent(final MouseEvent e) {
                CraftingFrame.this.recipePopup.show(e.getComponent(), e.getX(), e.getY());
            }
            
            public void mouseClicked(final MouseEvent e) {
            }
            
            public void mousePressed(final MouseEvent e) {
                if (e.isPopupTrigger()) {
                    this.mouseEvent(e);
                }
            }
            
            public void mouseReleased(final MouseEvent e) {
                if (e.isPopupTrigger()) {
                    this.mouseEvent(e);
                }
            }
            
            public void mouseEntered(final MouseEvent e) {
            }
            
            public void mouseExited(final MouseEvent e) {
            }
        });
    }
    
    private void buildStacyPopup() {
        final JMenuItem menuItem = new JMenuItem("Trade Scraps");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                CraftingFrame.this.script.parse("#TRADESCRAPS");
            }
        });
        this.stacyPopup.add(menuItem);
        this.scrapPanel.addMouseListener(new MouseListener() {
            public void mouseClicked(final MouseEvent e) {
            }
            
            public void mousePressed(final MouseEvent e) {
                if (e.isPopupTrigger()) {
                    CraftingFrame.this.stacyPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            
            public void mouseReleased(final MouseEvent e) {
                if (e.isPopupTrigger()) {
                    CraftingFrame.this.stacyPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            
            public void mouseEntered(final MouseEvent e) {
            }
            
            public void mouseExited(final MouseEvent e) {
            }
        });
    }
    
    public void start() {
        this.updateThread.start();
    }
    
    public void exit() {
        this.updateThread.exit();
        this.dispose();
    }
    
    public void clearTech() {
        for (final String item : this.componentsMap.keySet()) {
            final ComponentJPanel c = this.componentsMap.get(item);
            if (c != null) {
                c.setExpert(false);
                c.setArtist(false);
            }
        }
        for (final String item : this.scrapsMap.keySet()) {
            final ComponentJPanel c = this.scrapsMap.get(item);
            if (c != null) {
                c.setExpert(false);
                c.setArtist(false);
            }
        }
    }
    
    public void setArtist(final Integer tech) {
        for (final String item : this.componentsMap.keySet()) {
            final ComponentJPanel c = this.componentsMap.get(item);
            if (c != null && c.getMaterialTech() == tech) {
                c.setArtist(true);
                break;
            }
        }
        for (final String item : this.tradeMap.keySet()) {
            final ComponentJPanel c = this.tradeMap.get(item);
            if (c != null && c.getMaterialTech() == tech) {
                c.setArtist(true);
                break;
            }
        }
        for (final String item : this.scrapsMap.keySet()) {
            final ComponentJPanel c = this.scrapsMap.get(item);
            if (c != null && c.getMaterialTech() == tech) {
                c.setArtist(true);
                break;
            }
        }
    }
    
    public void setExpert(final Integer tech) {
        for (final String item : this.componentsMap.keySet()) {
            final ComponentJPanel c = this.componentsMap.get(item);
            if (c != null && c.getMaterialTech() == tech) {
                c.setExpert(true);
                break;
            }
        }
        for (final String item : this.tradeMap.keySet()) {
            final ComponentJPanel c = this.tradeMap.get(item);
            if (c != null && c.getMaterialTech() == tech) {
                c.setExpert(true);
                break;
            }
        }
        for (final String item : this.scrapsMap.keySet()) {
            final ComponentJPanel c = this.scrapsMap.get(item);
            if (c != null && c.getMaterialTech() == tech) {
                c.setExpert(true);
                break;
            }
        }
    }
    
    private void buildScrapsPanel() {
        this.scrapPanel.setLayout(new GridBagLayout());
        final GridBagConstraints bag = new GridBagConstraints();
        bag.fill = 2;
        bag.ipadx = 10;
        int x = 0;
        int y = 0;
        bag.gridy = y;
        JLabel jlabel = this.getJLabel("<html><b>Tier 1</b></html>");
        bag.gridx = 0;
        this.scrapPanel.add(jlabel, bag);
        this.labelMap.put("T1Scraps", jlabel);
        jlabel = this.getJLabel("<html><b>Tier 2</b></html>");
        bag.gridx = 1;
        this.scrapPanel.add(jlabel, bag);
        this.labelMap.put("T2Scraps", jlabel);
        jlabel = this.getJLabel("<html><b>Tier 3</b></html>");
        bag.gridx = 2;
        this.scrapPanel.add(jlabel, bag);
        this.labelMap.put("T3Scraps", jlabel);
        jlabel = this.getJLabel("<html><b>Tier 4</b></html>");
        bag.gridx = 3;
        this.scrapPanel.add(jlabel, bag);
        this.labelMap.put("T4Scraps", jlabel);
        bag.gridx = 4;
        jlabel = this.getJLabel("<html><b>Fragments</b></html>");
        this.labelMap.put("Fragments", jlabel);
        this.scrapPanel.add(jlabel, bag);
        ComponentJPanel c = new ComponentJPanel(this);
        c.setMaterialName("jewel fragments");
        c.setMaterialTech(0);
        c.setMaterialTier(0);
        c.setMaterialCategory("?");
        c.setMaterialType("jewels");
        c.setComponent(false);
        this.scrapsMap.put("jewel fragments", c);
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getMetal(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    c = new ComponentJPanel(this);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    c.setComponent(false);
                    this.scrapsMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.scrapPanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.scrapPanel.add(this.getNextGem(), bag);
        }
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getCloth(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    c = new ComponentJPanel(this);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    c.setComponent(false);
                    this.scrapsMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.scrapPanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.scrapPanel.add(this.getJLabel(" "), bag);
        }
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getLeather(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    c = new ComponentJPanel(this);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    c.setComponent(false);
                    this.scrapsMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.scrapPanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.scrapPanel.add(this.getJLabel(" "), bag);
        }
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getWood(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    c = new ComponentJPanel(this);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    c.setComponent(false);
                    this.scrapsMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.scrapPanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.scrapPanel.add(this.getNextGem(), bag);
        }
        bag.weightx = 1.0;
        bag.weighty = 1.0;
        bag.fill = 1;
        bag.gridwidth = 0;
        bag.gridheight = 0;
        ++y;
        bag.gridx = 0;
        bag.gridy = y;
        this.scrapPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        bag.gridy = 0;
        this.scrapPanel.add(this.getJLabel(" "), bag);
    }
    
    private void buildComponentsPanel() {
        this.compPanel.setLayout(new GridBagLayout());
        final GridBagConstraints bag = new GridBagConstraints();
        bag.fill = 2;
        bag.ipadx = 10;
        int x = 0;
        int y = 0;
        this.gemstones.clear();
        String gemTitle = "";
        for (final String gemstone : CraftingData.getGemstones()) {
            if (!gemTitle.equals(gemstone.split(",", 2)[1])) {
                if (!gemTitle.equals("")) {
                    this.gemstones.add(" ");
                }
                this.gemstones.add("<html><b>Gemstones " + gemstone.split(",", 2)[1]);
                this.gemstones.add(" ");
                gemTitle = gemstone.split(",", 2)[1];
            }
            this.gemstones.add(gemstone);
        }
        bag.gridy = y;
        JLabel jlabel = this.getJLabel("<html><b>Tier 1</b></html>");
        bag.gridx = 0;
        this.compPanel.add(jlabel, bag);
        this.labelMap.put("T1Comps", jlabel);
        jlabel = this.getJLabel("<html><b>Tier 2</b></html>");
        bag.gridx = 1;
        this.compPanel.add(jlabel, bag);
        this.labelMap.put("T2Comps", jlabel);
        jlabel = this.getJLabel("<html><b>Tier 3</b></html>");
        bag.gridx = 2;
        this.compPanel.add(jlabel, bag);
        this.labelMap.put("T3Comps", jlabel);
        jlabel = this.getJLabel("<html><b>Tier 4</b></html>");
        bag.gridx = 3;
        this.compPanel.add(jlabel, bag);
        this.labelMap.put("T4Comps", jlabel);
        bag.gridx = 4;
        this.compPanel.add(this.getNextGem(), bag);
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.compPanel.add(this.getNextGem(), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getMetal(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    final ComponentJPanel c = new ComponentJPanel(this);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    this.componentsMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.compPanel.add(this.getNextGem(), bag);
        }
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.compPanel.add(this.getNextGem(), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getCloth(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    final ComponentJPanel c = new ComponentJPanel(this);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    this.componentsMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.compPanel.add(this.getNextGem(), bag);
        }
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.compPanel.add(this.getNextGem(), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getLeather(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    final ComponentJPanel c = new ComponentJPanel(this);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    this.componentsMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.compPanel.add(this.getNextGem(), bag);
        }
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.compPanel.add(this.getNextGem(), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getWood(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    final ComponentJPanel c = new ComponentJPanel(this);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    this.componentsMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.compPanel.add(this.getNextGem(), bag);
        }
        bag.weightx = 1.0;
        bag.weighty = 1.0;
        bag.fill = 1;
        bag.gridwidth = 0;
        bag.gridheight = 0;
        ++y;
        bag.gridx = 0;
        bag.gridy = y;
        this.compPanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        bag.gridy = 0;
        this.compPanel.add(this.getJLabel(" "), bag);
    }
    
    private void buildTradePanel() {
        this.tradePanel.setLayout(new GridBagLayout());
        final GridBagConstraints bag = new GridBagConstraints();
        bag.fill = 2;
        bag.ipadx = 10;
        int x = 0;
        int y = 0;
        this.gemstones.clear();
        String gemTitle = "";
        for (final String gemstone : CraftingData.getGemstones()) {
            if (!gemTitle.equals(gemstone.split(",", 2)[1])) {
                if (!gemTitle.equals("")) {
                    this.gemstones.add(" ");
                }
                this.gemstones.add("<html><b>Gemstones " + gemstone.split(",", 2)[1]);
                this.gemstones.add(" ");
                gemTitle = gemstone.split(",", 2)[1];
            }
            this.gemstones.add(gemstone);
        }
        bag.gridy = y;
        JLabel jlabel = this.getJLabel("<html><b>Tier 1</b></html>");
        bag.gridx = 0;
        this.tradePanel.add(jlabel, bag);
        this.labelMap.put("T1Trades", jlabel);
        jlabel = this.getJLabel("<html><b>Tier 2</b></html>");
        bag.gridx = 1;
        this.tradePanel.add(jlabel, bag);
        this.labelMap.put("T2Trades", jlabel);
        jlabel = this.getJLabel("<html><b>Tier 3</b></html>");
        bag.gridx = 2;
        this.tradePanel.add(jlabel, bag);
        this.labelMap.put("T3Trades", jlabel);
        jlabel = this.getJLabel("<html><b>Tier 4</b></html>");
        bag.gridx = 3;
        this.tradePanel.add(jlabel, bag);
        this.labelMap.put("T4Trades", jlabel);
//        bag.gridx = 4;
//        this.tradePanel.add(this.getNextGem(), bag);
//        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.tradePanel.add(this.getJLabel(" "), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getMetal(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    final ComponentJPanel c = new ComponentJPanel(this, true);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    this.tradeMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.tradePanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.tradePanel.add(this.getNextGem(), bag);
        }
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.tradePanel.add(this.getJLabel(" "), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getCloth(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    final ComponentJPanel c = new ComponentJPanel(this, true);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    this.tradeMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.tradePanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.tradePanel.add(this.getNextGem(), bag);
        }
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.tradePanel.add(this.getJLabel(" "), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getLeather(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    final ComponentJPanel c = new ComponentJPanel(this, true);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    this.tradeMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.tradePanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.tradePanel.add(this.getNextGem(), bag);
        }
        ++y;
        bag.gridy = y;
        bag.gridx = 0;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 1;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 2;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 3;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 4;
        this.tradePanel.add(this.getJLabel(" "), bag);
        for (int cat = 65; cat <= 72; ++cat) {
            ++y;
            x = 0;
            for (int t = 1; t <= 4; ++t) {
                final String material = CraftingData.getWood(Character.toString((char)cat), t);
                if (" ".equals(material)) {
                    bag.gridy = y;
                    bag.gridx = x;
                    this.compPanel.add(this.getJLabel(" "), bag);
                    ++x;
                }
                else {
                    final ComponentJPanel c = new ComponentJPanel(this, true);
                    c.setMaterialName(material);
                    c.setMaterialTech(CraftingData.getTech(material));
                    c.setMaterialTier(CraftingData.getTier(material));
                    c.setMaterialCategory(CraftingData.getCategory(material));
                    c.setMaterialType(CraftingData.getType(material));
                    this.tradeMap.put(material.toLowerCase(), c);
                    bag.gridy = y;
                    bag.gridx = x;
                    this.tradePanel.add(c, bag);
                    ++x;
                }
            }
            bag.gridx = x;
            this.tradePanel.add(this.getNextGem(), bag);
        }
        bag.weightx = 1.0;
        bag.weighty = 1.0;
        bag.fill = 1;
        bag.gridwidth = 0;
        bag.gridheight = 0;
        ++y;
        bag.gridx = 0;
        bag.gridy = y;
        this.tradePanel.add(this.getJLabel(" "), bag);
        bag.gridx = 5;
        bag.gridy = 0;
        final JPanel gerahfPanel = new JPanel();
        gerahfPanel.setLayout(new BorderLayout());
        gerahfPanel.setBackground(Color.white);
        BoxLayout layout = new BoxLayout(this.primaryPanel, 1);
        this.primaryPanel.setLayout(layout);
        this.primaryPanel.setBackground(Color.white);
        this.primaryPanel.setBorder(this.primaryPanelBorder);
        gerahfPanel.add(this.primaryPanel, "North");
        layout = new BoxLayout(this.secondaryPanel, 1);
        this.secondaryPanel.setLayout(layout);
        this.secondaryPanel.setBackground(Color.white);
        this.secondaryPanel.setBorder(this.secondaryPanelBorder);
        gerahfPanel.add(this.secondaryPanel, "Center");
        gerahfPanel.add(this.tradeComponentsButton, "South");
        this.tradePanel.add(gerahfPanel, bag);
    }
    
    private JComponent getNextGem() {
        if (!this.gemstones.isEmpty()) {
            final String gem = this.gemstones.removeFirst();
            if (gem != null) {
                if (!gem.startsWith("<html>") && !gem.equals(" ")) {
                    final ComponentJPanel c = new ComponentJPanel(this);
                    c.setMaterialName(gem.split(",", 2)[0]);
                    c.setMaterialCategory(gem.split(",", 2)[1]);
                    c.setMaterialTech(0);
                    c.setMaterialTier(0);
                    c.setMaterialCategory("");
                    c.setMaterialType("gemstone");
                    this.componentsMap.put(c.getMaterialName(), c);
                    this.tradeMap.put(c.getMaterialName(), c);
//                    script.print("Material Name: " +c.getMaterialName());
//                    script.print("Material Cat: " +c.getMaterialCategory());
                    return c;
                }
                return this.getJLabel(gem);
            }
        }
        return this.getJLabel(" ");
    }
    
    private void populateRecipes() {
        RecipeObject recipe = new RecipeObject();
        recipe.setVendor("Hagglish");
        recipe.setLocation("Arms");
        recipe.addMaterial("layer", 7);
        recipe.addMaterial("ingot", 4);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Hagglish");
        recipe.setLocation("Legs");
        recipe.addMaterial("layer", 7);
        recipe.addMaterial("ingot", 4);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Hagglish");
        recipe.setLocation("Hands");
        recipe.addMaterial("bolt", 4);
        recipe.addMaterial("layer", 3);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Hagglish");
        recipe.setLocation("Feet");
        recipe.addMaterial("layer", 4);
        recipe.addMaterial("bolt", 3);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Eregalt");
        recipe.setLocation("Head");
        recipe.addMaterial("ingot", 7);
        recipe.addMaterial("bolt", 4);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Eregalt");
        recipe.setLocation("Chest");
        recipe.addMaterial("ingot", 9);
        recipe.addMaterial("layer", 6);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Tessa");
        recipe.setLocation("About body");
        recipe.addMaterial("layer", 7);
        recipe.addMaterial("layer", 4);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Tessa");
        recipe.setLocation("Neck");
        recipe.addMaterial("plank", 4);
        recipe.addMaterial("plank", 3);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Tessa");
        recipe.setLocation("Wrist");
        recipe.addMaterial("plank", 4);
        recipe.addMaterial("layer", 3);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Faja");
        recipe.setLocation("About body");
        recipe.addMaterial("bolt", 7);
        recipe.addMaterial("bolt", 4);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Faja");
        recipe.setLocation("Chest");
        recipe.addMaterial("bolt", 9);
        recipe.addMaterial("layer", 6);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Faja");
        recipe.setLocation("Light");
        recipe.addMaterial("bolt", 14);
        recipe.addMaterial("plank", 7);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Badgit");
        recipe.setLocation("Weapon (Magic)");
        recipe.addMaterial("plank", 9);
        recipe.addMaterial("layer", 6);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Badgit");
        recipe.setLocation("Weapon (Finesse)");
        recipe.addMaterial("plank", 9);
        recipe.addMaterial("ingot", 6);
        this.recipeList.add(recipe);
        recipe = new RecipeObject();
        recipe.setVendor("Badgit");
        recipe.setLocation("Weapon (Combat)");
        recipe.addMaterial("ingot", 9);
        recipe.addMaterial("plank", 6);
        this.recipeList.add(recipe);
        Collections.sort(this.recipeList);
    }
    
    public final void updateMaterials() {
        this.updateThread.doUpdate.set(true);
    }
    
    private void _updateMaterials() {
        int T1Comps = 0;
        int T2Comps = 0;
        int T3Comps = 0;
        int T4Comps = 0;
        int T1Scraps = 0;
        int T2Scraps = 0;
        int T3Scraps = 0;
        int T4Scraps = 0;
        int totalComps = 0;
        int totalScraps = 0;
        int totalFragments = 0;
        int totalGems = 0;
        int totalExpert = 0;
        int totalArtist = 0;
        int gems4 = 0;
        int gems5 = 0;
        int gemsAltering = 0;
        for (final String item : this.componentsMap.keySet()) {
            final ComponentJPanel c = this.componentsMap.get(item);
            if (c.isExpert()) {
                ++totalExpert;
            }
            if (c.isArtist()) {
                ++totalArtist;
            }
            if (c.getMaterialType().equals("gemstone")) {
                totalGems += c.getMaterialCount();
                final String type = CraftingData.getGemstoneType(c.getMaterialName());
                if (type == null) {
                    continue;
                }
                if (type.equals("30/4")) {
                    gems4 += c.getMaterialCount();
                }
                else if (type.equals("60/8")) {
                    gems5 += c.getMaterialCount();
                }
                else {
                    if (!type.equalsIgnoreCase("Altering")) {
                        continue;
                    }
                    gemsAltering += c.getMaterialCount();
                }
            }
            else {
                if (c.getMaterialTier() == 1) {
                    T1Comps += c.getMaterialCount();
                }
                else if (c.getMaterialTier() == 2) {
                    T2Comps += c.getMaterialCount();
                }
                else if (c.getMaterialTier() == 3) {
                    T3Comps += c.getMaterialCount();
                }
                else if (c.getMaterialTier() == 4) {
                    T4Comps += c.getMaterialCount();
                }
                totalComps += c.getMaterialCount();
            }
        }
        this.canTrade = false;
        for (final String item : this.scrapsMap.keySet()) {
            final ComponentJPanel c = this.scrapsMap.get(item);
            if (c.getMaterialType().equals("jewels")) {
                totalFragments += c.getMaterialCount();
            }
            else {
                if (c.getTradeCount() > 0) {
                    this.canTrade = true;
                }
                if (c.getMaterialTier() == 1) {
                    T1Scraps += c.getMaterialCount();
                }
                else if (c.getMaterialTier() == 2) {
                    T2Scraps += c.getMaterialCount();
                }
                else if (c.getMaterialTier() == 3) {
                    T3Scraps += c.getMaterialCount();
                }
                else if (c.getMaterialTier() == 4) {
                    T4Scraps += c.getMaterialCount();
                }
                totalScraps += c.getMaterialCount();
            }
        }
        for (final Component c2 : this.compPanel.getComponents()) {
            if (c2 instanceof JLabel) {
                final JLabel label = (JLabel)c2;
                if (label.getText().contains("30/4")) {
                    label.setText("<html><b>Gemstones 30/4</b> (" + gems4 + ")</html>");
                }
                else if (label.getText().contains("60/8")) {
                    label.setText("<html><b>Gemstones 60/8</b> (" + gems5 + ")</html>");
                }
                else if (label.getText().contains("Altering")) {
                    label.setText("<html><b>Gemstones Altering</b> (" + gemsAltering + ")</html>");
                }
            }
        }
        int nextLevel = 0;
        if (totalExpert < 155) {
            nextLevel = 155;
        }
        if (totalExpert < 140) {
            nextLevel = 140;
        }
        if (totalExpert < 125) {
            nextLevel = 125;
        }
        if (totalExpert < 110) {
            nextLevel = 110;
        }
        if (totalExpert < 95) {
            nextLevel = 95;
        }
        if (totalExpert < 80) {
            nextLevel = 80;
        }
        if (totalExpert < 65) {
            nextLevel = 65;
        }
        if (totalExpert < 50) {
            nextLevel = 50;
        }
        if (totalExpert < 35) {
            nextLevel = 35;
        }
        if (totalExpert < 20) {
            nextLevel = 20;
        }
        if (totalExpert < 8) {
            nextLevel = 8;
        }
        this.expertLabel.setText("[Expert: " + totalExpert + "/" + nextLevel);
        nextLevel = 0;
        if (totalArtist < 126) {
            nextLevel = 126;
        }
        if (totalArtist < 108) {
            nextLevel = 108;
        }
        if (totalArtist < 99) {
            nextLevel = 99;
        }
        if (totalArtist < 90) {
            nextLevel = 90;
        }
        if (totalArtist < 72) {
            nextLevel = 72;
        }
        if (totalArtist < 63) {
            nextLevel = 63;
        }
        if (totalArtist < 54) {
            nextLevel = 54;
        }
        if (totalArtist < 36) {
            nextLevel = 36;
        }
        if (totalArtist < 27) {
            nextLevel = 27;
        }
        if (totalArtist < 18) {
            nextLevel = 18;
        }
        if (totalArtist < 6) {
            nextLevel = 6;
        }
        this.artistLabel.setText("Artist: " + totalArtist + "/" + nextLevel + "]");
        if (this.canTrade) {
            this.tabs.setTitleAt(1, "<html><b>Scraps (" + totalScraps + ")</html>");
        }
        else {
            this.tabs.setTitleAt(1, "<html>Scraps (" + totalScraps + ")");
        }
        JLabel label2 = this.labelMap.get("T1Scraps");
        label2.setText("<html><b>Tier 1</b> (" + T1Scraps + ")");
        label2 = this.labelMap.get("T2Scraps");
        label2.setText("<html><b>Tier 2</b> (" + T2Scraps + ")");
        label2 = this.labelMap.get("T3Scraps");
        label2.setText("<html><b>Tier 3</b> (" + T3Scraps + ")");
        label2 = this.labelMap.get("T4Scraps");
        label2.setText("<html><b>Tier 4</b> (" + T4Scraps + ")");
        label2 = this.labelMap.get("Fragments");
        label2.setText("<html><b>Fragments </b> (" + totalFragments + ")");
        this.tabs.setTitleAt(2, "<html>Components (" + totalComps + ")");
        label2 = this.labelMap.get("T1Comps");
        label2.setText("<html><b>Tier 1</b> (" + T1Comps + ")");
        label2 = this.labelMap.get("T2Comps");
        label2.setText("<html><b>Tier 2</b> (" + T2Comps + ")");
        label2 = this.labelMap.get("T3Comps");
        label2.setText("<html><b>Tier 3</b> (" + T3Comps + ")");
        label2 = this.labelMap.get("T4Comps");
        label2.setText("<html><b>Tier 4</b> (" + T4Comps + ")");
        label2 = this.labelMap.get("T1Trades");
        label2.setText("<html><b>Tier 1</b> (" + T1Comps + ")");
        label2 = this.labelMap.get("T2Trades");
        label2.setText("<html><b>Tier 2</b> (" + T2Comps + ")");
        label2 = this.labelMap.get("T3Trades");
        label2.setText("<html><b>Tier 3</b> (" + T3Comps + ")");
        label2 = this.labelMap.get("T4Trades");
        label2.setText("<html><b>Tier 4</b> (" + T4Comps + ")");
        final long start = System.currentTimeMillis();
        this.filterMap.clear();
        if (this.tier1Box.isSelected()) {
            this.filterMap.put("1", true);
        }
        if (this.tier2Box.isSelected()) {
            this.filterMap.put("2", true);
        }
        if (this.tier3Box.isSelected()) {
            this.filterMap.put("3", true);
        }
        if (this.tier4Box.isSelected()) {
            this.filterMap.put("4", true);
        }
        if (this.aBox.isSelected()) {
            this.filterMap.put("A", true);
        }
        if (this.bBox.isSelected()) {
            this.filterMap.put("B", true);
        }
        if (this.cBox.isSelected()) {
            this.filterMap.put("C", true);
        }
        if (this.dBox.isSelected()) {
            this.filterMap.put("D", true);
        }
        if (this.eBox.isSelected()) {
            this.filterMap.put("E", true);
        }
        if (this.fBox.isSelected()) {
            this.filterMap.put("F", true);
        }
        if (this.gBox.isSelected()) {
            this.filterMap.put("G", true);
        }
        if (this.hBox.isSelected()) {
            this.filterMap.put("H", true);
        }
        if (this.lightBox.isSelected()) {
            this.filterMap.put("Light", true);
        }
        if (this.headBox.isSelected()) {
            this.filterMap.put("Head", true);
        }
        if (this.neckBox.isSelected()) {
            this.filterMap.put("Neck", true);
        }
        if (this.aboutBox.isSelected()) {
            this.filterMap.put("About body", true);
        }
        if (this.chestBox.isSelected()) {
            this.filterMap.put("Chest", true);
        }
        if (this.armsBox.isSelected()) {
            this.filterMap.put("Arms", true);
        }
        if (this.wristBox.isSelected()) {
            this.filterMap.put("Wrist", true);
        }
        if (this.handsBox.isSelected()) {
            this.filterMap.put("Hands", true);
        }
        if (this.weaponBox.isSelected()) {
            this.filterMap.put("Weapon (Magic)", true);
            this.filterMap.put("Weapon (Finesse)", true);
            this.filterMap.put("Weapon (Combat)", true);
        }
        if (this.legsBox.isSelected()) {
            this.filterMap.put("Legs", true);
        }
        if (this.feetBox.isSelected()) {
            this.filterMap.put("Feet", true);
        }
        if (this.strBox.isSelected()) {
            this.filterMap.put("Str", true);
        }
        if (this.conBox.isSelected()) {
            this.filterMap.put("Con", true);
        }
        if (this.dexBox.isSelected()) {
            this.filterMap.put("Dex", true);
        }
        if (this.agiBox.isSelected()) {
            this.filterMap.put("Agi", true);
        }
        if (this.intBox.isSelected()) {
            this.filterMap.put("Int", true);
        }
        if (this.wisBox.isSelected()) {
            this.filterMap.put("Wis", true);
        }
        if (this.wilBox.isSelected()) {
            this.filterMap.put("Wil", true);
        }
        if (this.speBox.isSelected()) {
            this.filterMap.put("Spe", true);
        }
        if (this.hitBox.isSelected()) {
            this.filterMap.put("Hit", true);
        }
        if (this.damageBox.isSelected()) {
            this.filterMap.put("Damage", true);
        }
        if (this.quickBox.isSelected()) {
            this.filterMap.put("Quick", true);
        }
        if (this.nrBox.isSelected()) {
            this.filterMap.put("NR", true);
        }
        if (this.healthBox.isSelected()) {
            this.filterMap.put("Health", true);
        }
        if (this.manaBox.isSelected()) {
            this.filterMap.put("Mana", true);
        }
        if (this.spiritBox.isSelected()) {
            this.filterMap.put("Spirit", true);
        }
        if (this.enduranceBox.isSelected()) {
            this.filterMap.put("Endurance", true);
        }
        if (this.artistBox.isSelected()) {
            this.filterMap.put("Artist", true);
        }
        if (this.expertBox.isSelected()) {
            this.filterMap.put("Expert", true);
        }
        boolean craft = false;
        int recipeCount = 0;
        this.recipeModel.getDataVector().removeAllElements();
        for (final RecipeObject recipe : this.recipeList) {
            final ArrayList<String> usable = recipe.canMake(this.componentsMap, this.filterMap, this.showAllButton.isSelected());
            Collections.sort(usable);
            if (usable != null && usable.size() > 0) {
                craft = true;
                if (this.recipeModel.getRowCount() > 0) {
                    this.recipeModel.addRow(new String[] { "" });
                }
                final String primary = "<html><b>" + recipe.getMaterials().get(0).getMaterialCount() + " " + recipe.getMaterials().get(0).getMaterialType();
                final String secondary = "<html><b>" + recipe.getMaterials().get(1).getMaterialCount() + " " + recipe.getMaterials().get(1).getMaterialType();
                this.recipeModel.addRow(new String[] { "<html><b>" + recipe.getLocation() + " - " + recipe.getVendor(), primary, secondary, "" });
                for (final String r : usable) {
                    this.recipeModel.addRow(r.split(","));
                    ++recipeCount;
                }
            }
        }
        if (!craft) {
            this.recipeModel.addRow(new String[] { "Not enough components." });
            this.tabs.setTitleAt(0, "<html>Recipes (0)");
        }
        else {
            this.tabs.setTitleAt(0, "<html>Recipes (" + recipeCount + ")");
            this.recipeTable.setRowSelectionInterval(0, 0);
        }
    }
    
    public void incrementScraps(String name, final int count) {
        name = name.toLowerCase().trim();
        final ComponentJPanel c = this.scrapsMap.get(name);
        if (c != null) {
            c.setMaterialCount(c.getMaterialCount() + count);
        }
        this.updateMaterials();
    }
    
    public void incrementComponent(String name, final int count) {
        name = name.toLowerCase().trim();
        ComponentJPanel c = this.componentsMap.get(name);
        if (c != null) {
            c.setMaterialCount(c.getMaterialCount() + count);
        }
        c = this.tradeMap.get(name);
        if (c != null) {
            c.setMaterialCount(c.getMaterialCount() + count);
        }
        this.updateMaterials();
    }
    
    private void retrieveRecipeComponents() {
        final int index = this.recipeTable.getSelectedRow();
        final Object primaryObject = this.recipeModel.getValueAt(index, 1);
        final Object secondaryObject = this.recipeModel.getValueAt(index, 2);
        final Object gemstoneObject = this.recipeModel.getValueAt(index, 3);
        int primaryCount = 0;
        int secondaryCount = 0;
        String primaryComponent = null;
        String secondaryComponent = null;
        String gemstoneComponent = null;
        if (primaryObject instanceof String) {
            final Matcher matcher = this.recipePattern.matcher((CharSequence)primaryObject);
            if (matcher.find()) {
                try {
                    primaryCount = Integer.parseInt(matcher.group(1).trim());
                }
                catch (Exception ex) {}
                primaryComponent = CraftingData.find(matcher.group(2).trim().replace("*", ""));
            }
        }
        if (secondaryObject instanceof String) {
            final Matcher matcher = this.recipePattern.matcher((CharSequence)secondaryObject);
            if (matcher.find()) {
                try {
                    secondaryCount = Integer.parseInt(matcher.group(1).trim());
                }
                catch (Exception ex2) {}
                secondaryComponent = CraftingData.find(matcher.group(2).trim().replace("*", ""));
            }
        }
        if (gemstoneObject instanceof String) {
            String gemstone = (String)gemstoneObject;
            if (gemstone.length() > 0) {
                gemstone = gemstone.split(" \\(", 2)[0];
                gemstoneComponent = CraftingData.find(gemstone);
            }
        }
        if (primaryCount == 0 || primaryComponent == null) {
            return;
        }
        final StringBuilder cmd = new StringBuilder();
        cmd.append("#GETCOMPONENTS ");
        cmd.append(primaryCount);
        cmd.append(" ");
        cmd.append(primaryComponent);
        if (secondaryCount > 0 && secondaryComponent != null) {
            cmd.append(", ");
            cmd.append(secondaryCount);
            cmd.append(" ");
            cmd.append(secondaryComponent);
        }
        if (gemstoneComponent != null) {
            cmd.append(", 1 ");
            cmd.append(gemstoneComponent);
        }
        this.script.print(AnsiTable.getCode("yellow") + "CRAFTING: " + AnsiTable.getCode("white") + cmd.toString() + "\u0001");
        this.script.parse(cmd.toString());
    }
    
    public final void resetData() {
        for (final ComponentJPanel c : this.componentsMap.values()) {
            if (c != null) {
                c.setMaterialCount(0);
                c.setExpert(false);
                c.setArtist(false);
            }
        }
        for (final ComponentJPanel c : this.tradeMap.values()) {
            if (c != null) {
                c.setMaterialCount(0);
                c.setExpert(false);
                c.setArtist(false);
            }
        }
        for (final ComponentJPanel c : this.scrapsMap.values()) {
            if (c != null) {
                c.setMaterialCount(0);
                c.setExpert(false);
                c.setArtist(false);
            }
        }
    }
    
    public HashMap<String, Integer> getTradingComponents() {
        final HashMap<String, Integer> trading = new HashMap<String, Integer>();
        for (final Component c : this.primaryPanel.getComponents()) {
            if (c instanceof JLabel) {
                trading.put(((JLabel)c).getText(), -1);
            }
        }
        for (final Component c : this.secondaryPanel.getComponents()) {
            if (c instanceof JLabel) {
                final String text = ((JLabel)c).getText();
                final Matcher matcher = this.secondaryPattern.matcher(text);
                if (matcher.find()) {
                    int count = 0;
                    try {
                        count = Integer.parseInt(matcher.group(1));
                    }
                    catch (Exception ex) {}
                    if (matcher.group(2) != null && count > 0 && matcher.group(2) != null) {
                        trading.put(matcher.group(2), count);
                    }
                }
            }
        }
        return trading;
    }
    
    public HashMap<String, Integer> getTradingScraps() {
        final HashMap<String, Integer> trading = new HashMap<String, Integer>();
        final Iterator<String> iter = this.scrapsMap.keySet().iterator();
        while (iter.hasNext()) {
            final ComponentJPanel c = this.scrapsMap.get(iter.next());
            if (c != null && c.getTradeCount() > 0) {
                int count = c.getTradeCount();
                if (count <= 0) {
                    continue;
                }
                if (count > 2) {
                    count = 2;
                }
                trading.put(c.getMaterialName().toLowerCase(), count * c.getTradeRatio());
            }
        }
        return trading;
    }
    
    public void leftClick(final ComponentJPanel item) {
        if (this.primaryPanel.getComponentCount() < 1 || item.getMaterialCount() < 1 || item.getMaterialName().startsWith("[")) {
            script.print("Returning " +item.getMaterialName());
            return;
        }
        AtomicInteger state = this.duplicateMap.get(item);
        if (state == null) {
            state = new AtomicInteger(1);
            item.setBackground(AnsiTable.ansiGreen);
            this.duplicateMap.put(item, state);
        }
        else if (state.get() == 1 && item.getMaterialCount() > 1) {
            state.set(2);
            item.setBackground(AnsiTable.ansiLightGreen);
        }
        else if (state.get() > 0) {
            this.duplicateMap.remove(item);
            item.setBackground(AnsiTable.ansiWhite);
        }
        this.secondaryPanel.removeAll();
        for (final ComponentJPanel duplicateItem : this.duplicateMap.keySet()) {
            final AtomicInteger count = this.duplicateMap.get(duplicateItem);
            if (count != null && count.get() > 0) {
                this.secondaryPanel.add(this.getJLabel("<html><b>" + count.get() + "</b> " + duplicateItem.getMaterialName()));
            }
        }
        this.tradePanel.validate();
        this.secondaryPanel.repaint();
        if (this.secondaryPanel.getComponentCount() > 0) {
            this.tradeComponentsButton.setEnabled(true);
        }
        else {
            this.tradeComponentsButton.setEnabled(false);
        }
        final Integer[] values = this.getValues(this.duplicateMap);
        if (values != null && values.length == 2) {
            ((TitledBorder)this.primaryPanelBorder.getOutsideBorder()).setTitle("Primary Component (" + values[0] + ")");
            ((TitledBorder)this.secondaryPanelBorder.getOutsideBorder()).setTitle("Trade Components (" + values[1] + ")");
            this.secondaryPanel.repaint();
            this.primaryPanel.repaint();
        }
    }
    
    private Integer[] getValues(final TreeMap<ComponentJPanel, AtomicInteger> map) {
        int primary = 0;
        int secondary = 0;
        for (final ComponentJPanel comp : map.keySet()) {
            final AtomicInteger count = map.get(comp);
            if (count.get() == 0) {
                switch (comp.getMaterialTier()) {
                    case 1: {
                        primary = 8;
                        continue;
                    }
                    case 2: {
                        primary = 16;
                        continue;
                    }
                    case 3: {
                        primary = 30;
                        continue;
                    }
                    case 4: {
                        primary = 48;
                        continue;
                    }
                    default: {
                        primary = 0;
                        continue;
                    }
                }
            }
            else {
                if (count.get() != 1 && count.get() != 2) {
                    continue;
                }
                if (comp.getMaterialType().equals("layer")) {
                    secondary += count.get() * (comp.getMaterialTier() * 3);
                }
                else if (comp.getMaterialType().equals("bolt") || comp.getMaterialType().equals("plank")) {
                    secondary += count.get() * (comp.getMaterialTier() * 4);
                }
                else {
                    if (!comp.getMaterialType().equals("ingot")) {
                        continue;
                    }
                    secondary += count.get() * (comp.getMaterialTier() * 5);
                }
            }
        }
        return new Integer[] { primary, secondary };
    }
    
    public void rightClick(final ComponentJPanel item) {
        if (item.getMaterialCount() < 1 || item.getMaterialName().startsWith("[")) {
            return;
        }
        final AtomicInteger current = this.duplicateMap.get(item);
        for (final ComponentJPanel panel : this.duplicateMap.keySet()) {
            panel.setBackground(AnsiTable.ansiWhite);
        }
        this.duplicateMap.clear();
        this.primaryPanel.removeAll();
        this.secondaryPanel.removeAll();
        if (current == null || current.get() > 0) {
            item.setBackground(AnsiTable.ansiLightBlue);
            this.duplicateMap.put(item, new AtomicInteger(0));
            this.primaryPanel.add(this.getJLabel(item.getMaterialName()));
        }
        this.tradePanel.validate();
        this.primaryPanel.repaint();
        this.secondaryPanel.repaint();
        final Integer[] values = this.getValues(this.duplicateMap);
        if (values != null && values.length == 2) {
            ((TitledBorder)this.primaryPanelBorder.getOutsideBorder()).setTitle("Primary Component (" + values[0] + ")");
            ((TitledBorder)this.secondaryPanelBorder.getOutsideBorder()).setTitle("Trade Components (" + values[1] + ")");
            this.primaryPanel.repaint();
            this.secondaryPanel.repaint();
        }
        this.tradeComponentsButton.setEnabled(false);
    }
    
    private JLabel getJLabel(final String text) {
        final JLabel label = new JLabel(text);
        label.setFont(CraftingFrame.FONT_PLAIN);
        return label;
    }
    
    private void initComponents() {
        this.tradeComponentsButton = new JButton();
        this.recipePopup = new JPopupMenu();
        this.stacyPopup = new JPopupMenu();
        this.tabs = new JTabbedPane();
        this.recipeTab = new JPanel();
        this.filterPanel = new JPanel();
        this.statsFour = new JPanel();
        this.tier1Box = new JCheckBox();
        this.tier2Box = new JCheckBox();
        this.tier3Box = new JCheckBox();
        this.tier4Box = new JCheckBox();
        this.aBox = new JCheckBox();
        this.bBox = new JCheckBox();
        this.cBox = new JCheckBox();
        this.dBox = new JCheckBox();
        this.eBox = new JCheckBox();
        this.fBox = new JCheckBox();
        this.gBox = new JCheckBox();
        this.hBox = new JCheckBox();
        this.statsOne = new JPanel();
        this.lightBox = new JCheckBox();
        this.headBox = new JCheckBox();
        this.neckBox = new JCheckBox();
        this.aboutBox = new JCheckBox();
        this.chestBox = new JCheckBox();
        this.armsBox = new JCheckBox();
        this.wristBox = new JCheckBox();
        this.handsBox = new JCheckBox();
        this.weaponBox = new JCheckBox();
        this.legsBox = new JCheckBox();
        this.feetBox = new JCheckBox();
        this.statsTwo = new JPanel();
        this.strBox = new JCheckBox();
        this.conBox = new JCheckBox();
        this.dexBox = new JCheckBox();
        this.agiBox = new JCheckBox();
        this.intBox = new JCheckBox();
        this.wisBox = new JCheckBox();
        this.wilBox = new JCheckBox();
        this.speBox = new JCheckBox();
        this.quickBox = new JCheckBox();
        this.nrBox = new JCheckBox();
        this.hitBox = new JCheckBox();
        this.damageBox = new JCheckBox();
        this.statsThree = new JPanel();
        this.healthBox = new JCheckBox();
        this.manaBox = new JCheckBox();
        this.spiritBox = new JCheckBox();
        this.enduranceBox = new JCheckBox();
        this.artistBox = new JCheckBox();
        this.expertBox = new JCheckBox();
        this.noneButton = new JButton();
        this.showAllButton = new JToggleButton();
        this.recipeScrollPanel = new JScrollPane();
        this.recipeTable = new JTable();
        this.scrapsTab = new JPanel();
        this.jScrollPane5 = new JScrollPane();
        this.scrapPanel = new JPanel();
        this.componentsTab = new JPanel();
        this.jScrollPane4 = new JScrollPane();
        this.compPanel = new JPanel();
        this.tradeTab = new JPanel();
        this.autoTradeTab = new JPanel();
        this.jScrollPane6 = new JScrollPane();
        this.tradePanel = new JPanel();
        this.jCheckBox1 = new JCheckBox();
        this.jButton1 = new JButton();
        this.expertLabel = new JLabel();
        this.artistLabel = new JLabel();
        this.jLabel1 = new JLabel();
        this.retrieveButton = new JButton();
        this.scrapsButton = new JButton();
        this.tradeComponentsButton.setFont(new Font("Arial", 0, 12));
        this.tradeComponentsButton.setText("Trade with Gerahf");
        this.tradeComponentsButton.setFocusable(false);
        this.tradeComponentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.tradeComponentsButtonActionPerformed(evt);
            }
        });
        this.setTitle("Salvage Materials");
        this.setName("CraftingFrame");
        this.tabs.setBackground(new Color(255, 255, 255));
        this.tabs.setFont(new Font("Arial", 0, 12));
        this.tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent evt) {
                CraftingFrame.this.tabsStateChanged(evt);
            }
        });
        this.recipeTab.setBackground(new Color(255, 255, 255));
        this.recipeTab.setFont(new Font("Arial", 0, 12));
        this.filterPanel.setBackground(new Color(255, 255, 255));
        this.filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));
        this.filterPanel.setLayout(new GridLayout(4, 0));
        this.statsFour.setBackground(new Color(255, 255, 255));
        this.tier1Box.setBackground(new Color(255, 255, 255));
        this.tier1Box.setFont(new Font("Arial", 0, 12));
        this.tier1Box.setText("Tier 1");
        this.tier1Box.setFocusable(false);
        this.tier1Box.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.tier1BoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.tier1Box);
        this.tier2Box.setBackground(new Color(255, 255, 255));
        this.tier2Box.setFont(new Font("Arial", 0, 12));
        this.tier2Box.setText("Tier 2");
        this.tier2Box.setFocusable(false);
        this.tier2Box.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.tier2BoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.tier2Box);
        this.tier3Box.setBackground(new Color(255, 255, 255));
        this.tier3Box.setFont(new Font("Arial", 0, 12));
        this.tier3Box.setText("Tier 3");
        this.tier3Box.setFocusable(false);
        this.tier3Box.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.tier3BoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.tier3Box);
        this.tier4Box.setBackground(new Color(255, 255, 255));
        this.tier4Box.setFont(new Font("Arial", 0, 12));
        this.tier4Box.setText("Tier 4");
        this.tier4Box.setFocusable(false);
        this.tier4Box.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.tier4BoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.tier4Box);
        this.aBox.setBackground(new Color(255, 255, 255));
        this.aBox.setFont(new Font("Arial", 0, 12));
        this.aBox.setText("A");
        this.aBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.aBoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.aBox);
        this.bBox.setBackground(new Color(255, 255, 255));
        this.bBox.setFont(new Font("Arial", 0, 12));
        this.bBox.setText("B");
        this.bBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.bBoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.bBox);
        this.cBox.setBackground(new Color(255, 255, 255));
        this.cBox.setFont(new Font("Arial", 0, 12));
        this.cBox.setText("C");
        this.cBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.cBoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.cBox);
        this.dBox.setBackground(new Color(255, 255, 255));
        this.dBox.setFont(new Font("Arial", 0, 12));
        this.dBox.setText("D");
        this.dBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.dBoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.dBox);
        this.eBox.setBackground(new Color(255, 255, 255));
        this.eBox.setFont(new Font("Arial", 0, 12));
        this.eBox.setText("E");
        this.eBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.eBoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.eBox);
        this.fBox.setBackground(new Color(255, 255, 255));
        this.fBox.setFont(new Font("Arial", 0, 12));
        this.fBox.setText("F");
        this.fBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.fBoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.fBox);
        this.gBox.setBackground(new Color(255, 255, 255));
        this.gBox.setFont(new Font("Arial", 0, 12));
        this.gBox.setText("G");
        this.gBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.gBoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.gBox);
        this.hBox.setBackground(new Color(255, 255, 255));
        this.hBox.setFont(new Font("Arial", 0, 12));
        this.hBox.setText("H");
        this.hBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.hBoxActionPerformed(evt);
            }
        });
        this.statsFour.add(this.hBox);
        this.filterPanel.add(this.statsFour);
        this.statsOne.setBackground(new Color(255, 255, 255));
        this.lightBox.setBackground(new Color(255, 255, 255));
        this.lightBox.setFont(new Font("Arial", 0, 12));
        this.lightBox.setText("Light");
        this.lightBox.setFocusable(false);
        this.lightBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.lightBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.lightBox);
        this.headBox.setBackground(new Color(255, 255, 255));
        this.headBox.setFont(new Font("Arial", 0, 12));
        this.headBox.setText("Head");
        this.headBox.setFocusable(false);
        this.headBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.headBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.headBox);
        this.neckBox.setBackground(new Color(255, 255, 255));
        this.neckBox.setFont(new Font("Arial", 0, 12));
        this.neckBox.setText("Neck");
        this.neckBox.setFocusable(false);
        this.neckBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.neckBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.neckBox);
        this.aboutBox.setBackground(new Color(255, 255, 255));
        this.aboutBox.setFont(new Font("Arial", 0, 12));
        this.aboutBox.setText("About body");
        this.aboutBox.setFocusable(false);
        this.aboutBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.aboutBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.aboutBox);
        this.chestBox.setBackground(new Color(255, 255, 255));
        this.chestBox.setFont(new Font("Arial", 0, 12));
        this.chestBox.setText("Chest");
        this.chestBox.setFocusable(false);
        this.chestBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.chestBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.chestBox);
        this.armsBox.setBackground(new Color(255, 255, 255));
        this.armsBox.setFont(new Font("Arial", 0, 12));
        this.armsBox.setText("Arms");
        this.armsBox.setFocusable(false);
        this.armsBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.armsBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.armsBox);
        this.wristBox.setBackground(new Color(255, 255, 255));
        this.wristBox.setFont(new Font("Arial", 0, 12));
        this.wristBox.setText("Wrist");
        this.wristBox.setFocusable(false);
        this.wristBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.wristBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.wristBox);
        this.handsBox.setBackground(new Color(255, 255, 255));
        this.handsBox.setFont(new Font("Arial", 0, 12));
        this.handsBox.setText("Hands");
        this.handsBox.setFocusable(false);
        this.handsBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.handsBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.handsBox);
        this.weaponBox.setBackground(new Color(255, 255, 255));
        this.weaponBox.setFont(new Font("Arial", 0, 12));
        this.weaponBox.setText("Weapon");
        this.weaponBox.setFocusable(false);
        this.weaponBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.weaponBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.weaponBox);
        this.legsBox.setBackground(new Color(255, 255, 255));
        this.legsBox.setFont(new Font("Arial", 0, 12));
        this.legsBox.setText("Legs");
        this.legsBox.setFocusable(false);
        this.legsBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.legsBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.legsBox);
        this.feetBox.setBackground(new Color(255, 255, 255));
        this.feetBox.setFont(new Font("Arial", 0, 12));
        this.feetBox.setText("Feet");
        this.feetBox.setFocusable(false);
        this.feetBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.feetBoxActionPerformed(evt);
            }
        });
        this.statsOne.add(this.feetBox);
        this.filterPanel.add(this.statsOne);
        this.statsTwo.setBackground(new Color(255, 255, 255));
        this.strBox.setBackground(new Color(255, 255, 255));
        this.strBox.setFont(new Font("Arial", 0, 12));
        this.strBox.setText("Str");
        this.strBox.setFocusable(false);
        this.strBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.strBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.strBox);
        this.conBox.setBackground(new Color(255, 255, 255));
        this.conBox.setFont(new Font("Arial", 0, 12));
        this.conBox.setText("Con");
        this.conBox.setFocusable(false);
        this.conBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.conBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.conBox);
        this.dexBox.setBackground(new Color(255, 255, 255));
        this.dexBox.setFont(new Font("Arial", 0, 12));
        this.dexBox.setText("Dex");
        this.dexBox.setFocusable(false);
        this.dexBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.dexBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.dexBox);
        this.agiBox.setBackground(new Color(255, 255, 255));
        this.agiBox.setFont(new Font("Arial", 0, 12));
        this.agiBox.setText("Agi");
        this.agiBox.setFocusable(false);
        this.agiBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.agiBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.agiBox);
        this.intBox.setBackground(new Color(255, 255, 255));
        this.intBox.setFont(new Font("Arial", 0, 12));
        this.intBox.setText("Int");
        this.intBox.setFocusable(false);
        this.intBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.intBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.intBox);
        this.wisBox.setBackground(new Color(255, 255, 255));
        this.wisBox.setFont(new Font("Arial", 0, 12));
        this.wisBox.setText("Wis");
        this.wisBox.setFocusable(false);
        this.wisBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.wisBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.wisBox);
        this.wilBox.setBackground(new Color(255, 255, 255));
        this.wilBox.setFont(new Font("Arial", 0, 12));
        this.wilBox.setText("Wil");
        this.wilBox.setFocusable(false);
        this.wilBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.wilBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.wilBox);
        this.speBox.setBackground(new Color(255, 255, 255));
        this.speBox.setFont(new Font("Arial", 0, 12));
        this.speBox.setText("Spe");
        this.speBox.setFocusable(false);
        this.speBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.speBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.speBox);
        this.quickBox.setBackground(new Color(255, 255, 255));
        this.quickBox.setFont(new Font("Arial", 0, 12));
        this.quickBox.setText("Quick");
        this.quickBox.setFocusable(false);
        this.quickBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.quickBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.quickBox);
        this.nrBox.setBackground(new Color(255, 255, 255));
        this.nrBox.setFont(new Font("Arial", 0, 12));
        this.nrBox.setText("NR");
        this.nrBox.setFocusable(false);
        this.nrBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.nrBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.nrBox);
        this.hitBox.setBackground(new Color(255, 255, 255));
        this.hitBox.setFont(new Font("Arial", 0, 12));
        this.hitBox.setText("Hit");
        this.hitBox.setFocusable(false);
        this.hitBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.hitBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.hitBox);
        this.damageBox.setBackground(new Color(255, 255, 255));
        this.damageBox.setFont(new Font("Arial", 0, 12));
        this.damageBox.setText("Damage");
        this.damageBox.setFocusable(false);
        this.damageBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.damageBoxActionPerformed(evt);
            }
        });
        this.statsTwo.add(this.damageBox);
        this.filterPanel.add(this.statsTwo);
        this.statsThree.setBackground(new Color(255, 255, 255));
        this.healthBox.setBackground(new Color(255, 255, 255));
        this.healthBox.setFont(new Font("Arial", 0, 12));
        this.healthBox.setText("Health");
        this.healthBox.setFocusable(false);
        this.healthBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.healthBoxActionPerformed(evt);
            }
        });
        this.statsThree.add(this.healthBox);
        this.manaBox.setBackground(new Color(255, 255, 255));
        this.manaBox.setFont(new Font("Arial", 0, 12));
        this.manaBox.setText("Mana");
        this.manaBox.setFocusable(false);
        this.manaBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.manaBoxActionPerformed(evt);
            }
        });
        this.statsThree.add(this.manaBox);
        this.spiritBox.setBackground(new Color(255, 255, 255));
        this.spiritBox.setFont(new Font("Arial", 0, 12));
        this.spiritBox.setText("Spirit");
        this.spiritBox.setFocusable(false);
        this.spiritBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.spiritBoxActionPerformed(evt);
            }
        });
        this.statsThree.add(this.spiritBox);
        this.enduranceBox.setBackground(new Color(255, 255, 255));
        this.enduranceBox.setFont(new Font("Arial", 0, 12));
        this.enduranceBox.setText("Endurance");
        this.enduranceBox.setFocusable(false);
        this.enduranceBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.enduranceBoxActionPerformed(evt);
            }
        });
        this.statsThree.add(this.enduranceBox);
        this.artistBox.setBackground(new Color(255, 255, 255));
        this.artistBox.setFont(new Font("Arial", 0, 12));
        this.artistBox.setText("Artistry");
        this.artistBox.setFocusable(false);
        this.artistBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.artistBoxActionPerformed(evt);
            }
        });
        this.statsThree.add(this.artistBox);
        this.expertBox.setBackground(new Color(255, 255, 255));
        this.expertBox.setFont(new Font("Arial", 0, 12));
        this.expertBox.setText("Expertise");
        this.expertBox.setFocusable(false);
        this.expertBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.expertBoxActionPerformed(evt);
            }
        });
        this.statsThree.add(this.expertBox);
        this.noneButton.setFont(new Font("Arial", 0, 12));
        this.noneButton.setText("Reset");
        this.noneButton.setFocusable(false);
        this.noneButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.noneButtonActionPerformed(evt);
            }
        });
        this.statsThree.add(this.noneButton);
        this.showAllButton.setFont(new Font("Arial", 0, 12));
        this.showAllButton.setText("Show All");
        this.showAllButton.setFocusable(false);
        this.showAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.showAllButtonActionPerformed(evt);
            }
        });
        this.statsThree.add(this.showAllButton);
        this.filterPanel.add(this.statsThree);
        this.recipeScrollPanel.setBackground(new Color(255, 255, 255));
        this.recipeTable.setFont(new Font("Arial", 0, 12));
        this.recipeTable.setModel(this.recipeModel);
        this.recipeTable.setSelectionMode(0);
        this.recipeTable.setShowHorizontalLines(false);
        this.recipeTable.setShowVerticalLines(false);
        this.recipeScrollPanel.setViewportView(this.recipeTable);
        final GroupLayout recipeTabLayout = new GroupLayout((Container)this.recipeTab);
        this.recipeTab.setLayout((LayoutManager)recipeTabLayout);
        recipeTabLayout.setHorizontalGroup((GroupLayout.Group)recipeTabLayout.createParallelGroup(1).add(2, (GroupLayout.Group)recipeTabLayout.createSequentialGroup().addContainerGap().add((GroupLayout.Group)recipeTabLayout.createParallelGroup(2).add(1, (Component)this.recipeScrollPanel).add(1, (Component)this.filterPanel, -1, -1, 32767)).addContainerGap()));
        recipeTabLayout.setVerticalGroup((GroupLayout.Group)recipeTabLayout.createParallelGroup(1).add((GroupLayout.Group)recipeTabLayout.createSequentialGroup().add((Component)this.filterPanel, -2, -1, -2).addPreferredGap(0).add((Component)this.recipeScrollPanel, -1, 421, 32767).addContainerGap()));
        this.tabs.addTab("Recipes", this.recipeTab);
        this.scrapsTab.setBackground(new Color(255, 255, 255));
        this.scrapsTab.setFont(new Font("Arial", 0, 12));
        this.scrapsTab.setLayout(new BorderLayout());
        this.jScrollPane5.setBackground(new Color(255, 255, 255));
        this.scrapPanel.setBackground(new Color(255, 255, 255));
        this.scrapPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        final GroupLayout scrapPanelLayout = new GroupLayout((Container)this.scrapPanel);
        this.scrapPanel.setLayout((LayoutManager)scrapPanelLayout);
        scrapPanelLayout.setHorizontalGroup((GroupLayout.Group)scrapPanelLayout.createParallelGroup(1).add(0, 1976, 32767));
        scrapPanelLayout.setVerticalGroup((GroupLayout.Group)scrapPanelLayout.createParallelGroup(1).add(0, 585, 32767));
        this.jScrollPane5.setViewportView(this.scrapPanel);
        this.scrapsTab.add(this.jScrollPane5, "Center");
        this.tabs.addTab("Scraps", this.scrapsTab);
        this.componentsTab.setBackground(new Color(255, 255, 255));
        this.componentsTab.setFont(new Font("Arial", 0, 12));
        this.componentsTab.setLayout(new BorderLayout());
        this.jScrollPane4.setBackground(new Color(255, 255, 255));
        this.compPanel.setBackground(new Color(255, 255, 255));
        this.compPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        final GroupLayout compPanelLayout = new GroupLayout((Container)this.compPanel);
        this.compPanel.setLayout((LayoutManager)compPanelLayout);
        compPanelLayout.setHorizontalGroup((GroupLayout.Group)compPanelLayout.createParallelGroup(1).add(0, 1976, 32767));
        compPanelLayout.setVerticalGroup((GroupLayout.Group)compPanelLayout.createParallelGroup(1).add(0, 585, 32767));
        this.jScrollPane4.setViewportView(this.compPanel);
        this.componentsTab.add(this.jScrollPane4, "Center");
        this.tabs.addTab("Components", this.componentsTab);
        this.tradeTab.setFont(new Font("Arial", 0, 12));
        this.tradeTab.setLayout(new BorderLayout());
        this.jScrollPane6.setBackground(new Color(255, 255, 255));
        this.tradePanel.setBackground(new Color(255, 255, 255));
        this.tradePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        final GroupLayout tradePanelLayout = new GroupLayout((Container)this.tradePanel);
        this.tradePanel.setLayout((LayoutManager)tradePanelLayout);
        tradePanelLayout.setHorizontalGroup((GroupLayout.Group)tradePanelLayout.createParallelGroup(1).add(0, 1976, 32767));
        tradePanelLayout.setVerticalGroup((GroupLayout.Group)tradePanelLayout.createParallelGroup(1).add(0, 585, 32767));
        this.jScrollPane6.setViewportView(this.tradePanel);
        this.tradeTab.add(this.jScrollPane6, "Center");
        this.tabs.addTab("<html>Gerahf&nbsp;</html>", this.tradeTab);
        this.tabs.addTab("Auto Trading", this.tradeTab);
        this.jCheckBox1.setFont(new Font("Arial", 0, 12));
        this.jCheckBox1.setText("Always on top");
        this.jCheckBox1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.jCheckBox1ActionPerformed(evt);
            }
        });
        this.jButton1.setFont(new Font("Arial", 0, 12));
        this.jButton1.setText("Inspect Salvage");
        this.jButton1.setFocusable(false);
        this.jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.jButton1ActionPerformed(evt);
            }
        });
        this.expertLabel.setFont(new Font("Arial", 1, 12));
        this.expertLabel.setText("[Expert: 0");
        this.artistLabel.setFont(new Font("Arial", 1, 12));
        this.artistLabel.setText("Artist: 0]");
        this.jLabel1.setFont(new Font("Arial", 1, 12));
        this.jLabel1.setText("Tech Points");
        this.retrieveButton.setFont(new Font("Arial", 0, 12));
        this.retrieveButton.setText("Retrieve Recipe Components");
        this.retrieveButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.retrieveButtonActionPerformed(evt);
            }
        });
        this.scrapsButton.setFont(new Font("Arial", 0, 12));
        this.scrapsButton.setText("Trade with Stacy");
        this.scrapsButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                CraftingFrame.this.scrapsButtonActionPerformed(evt);
            }
        });
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout((LayoutManager)layout);
        layout.setHorizontalGroup((GroupLayout.Group)layout.createParallelGroup(1).add((GroupLayout.Group)layout.createSequentialGroup().addContainerGap().add((GroupLayout.Group)layout.createParallelGroup(1).add(2, (Component)this.tabs, -1, 746, 32767).add((GroupLayout.Group)layout.createSequentialGroup().add((Component)this.jButton1).addPreferredGap(0).add((Component)this.retrieveButton).addPreferredGap(0).add((Component)this.scrapsButton).addPreferredGap(0, -1, 32767).add((Component)this.jLabel1).addPreferredGap(0).add((Component)this.expertLabel).addPreferredGap(0).add((Component)this.artistLabel).addPreferredGap(1).add((Component)this.jCheckBox1))).add(6, 6, 6)));
        layout.setVerticalGroup((GroupLayout.Group)layout.createParallelGroup(1).add(2, (GroupLayout.Group)layout.createSequentialGroup().addContainerGap().add((Component)this.tabs).addPreferredGap(0).add((GroupLayout.Group)layout.createParallelGroup(3).add((Component)this.jCheckBox1, -2, 23, -2).add((Component)this.jButton1).add((Component)this.retrieveButton).add((Component)this.scrapsButton).add((Component)this.jLabel1).add((Component)this.expertLabel).add((Component)this.artistLabel)).addContainerGap()));
        this.pack();
    }
    
    private void jButton1ActionPerformed(final ActionEvent evt) {
        this.script.parse("#INSPECT");
    }
    
    private void jCheckBox1ActionPerformed(final ActionEvent evt) {
        this.setAlwaysOnTop(this.jCheckBox1.isSelected());
    }
    
    private void tradeComponentsButtonActionPerformed(final ActionEvent evt) {
        this.script.parse("#TRADECOMPONENTS");
    }
    
    private void strBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void conBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void dexBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void agiBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void intBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void wisBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void wilBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void speBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void quickBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void nrBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void hitBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void damageBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void healthBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void manaBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void spiritBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void enduranceBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void noneButtonActionPerformed(final ActionEvent evt) {
        for (final Component c : this.statsTwo.getComponents()) {
            if (c instanceof JCheckBox) {
                ((JCheckBox)c).setSelected(false);
            }
        }
        for (final Component c : this.statsOne.getComponents()) {
            if (c instanceof JCheckBox) {
                ((JCheckBox)c).setSelected(false);
            }
        }
        for (final Component c : this.statsThree.getComponents()) {
            if (c instanceof JCheckBox) {
                ((JCheckBox)c).setSelected(false);
            }
        }
        for (final Component c : this.statsFour.getComponents()) {
            if (c instanceof JCheckBox) {
                ((JCheckBox)c).setSelected(false);
            }
        }
        this.showAllButton.setSelected(false);
        this.updateMaterials();
    }
    
    private void showAllButtonActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void lightBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void headBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void neckBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void aboutBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void chestBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void armsBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void wristBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void handsBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void weaponBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void legsBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void feetBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void tier1BoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void tier2BoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void tier3BoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void tabsStateChanged(final ChangeEvent evt) {
        final int index = this.tabs.getSelectedIndex();
        if (index == 0) {
            this.retrieveButton.setVisible(true);
            this.scrapsButton.setVisible(false);
        }
        else if (index == 1) {
            this.retrieveButton.setVisible(false);
            this.scrapsButton.setVisible(true);
        }
        else {
            this.retrieveButton.setVisible(false);
            this.scrapsButton.setVisible(false);
        }
    }
    
    private void retrieveButtonActionPerformed(final ActionEvent evt) {
        this.retrieveRecipeComponents();
    }
    
    private void scrapsButtonActionPerformed(final ActionEvent evt) {
        this.script.parse("#TRADESCRAPS");
    }
    
    private void aBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void bBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void cBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void dBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void eBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void fBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void gBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void hBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void artistBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void tier4BoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    private void expertBoxActionPerformed(final ActionEvent evt) {
        this.updateMaterials();
    }
    
    public void lostOwnership(final Clipboard clipboard, final Transferable contents) {
    }
    
    static {
        FONT_BOLD = new Font("Arial", 1, 12);
        FONT_PLAIN = new Font("Arial", 0, 12);
    }
    
    private class UpdateThread extends Thread
    {
        private boolean exit;
        public final AtomicBoolean doUpdate;
        
        public UpdateThread() {
            this.exit = false;
            this.doUpdate = new AtomicBoolean(true);
        }
        
        public void exit() {
            this.exit = true;
        }
        
        @Override
        public void run() {
            while (!this.exit) {
                if (this.doUpdate.get()) {
                    CraftingFrame.this._updateMaterials();
                    this.doUpdate.set(false);
                }
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(CraftingFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private class RecipeTableModel extends DefaultTableModel
    {
        @Override
        public boolean isCellEditable(final int row, final int col) {
            return false;
        }
    }
}
