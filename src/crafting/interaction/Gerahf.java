// 
// Decompiled by Procyon v0.5.36
// 

package crafting.interaction;

import java.util.Iterator;
import com.lsd.umc.util.AnsiTable;
import java.util.Collection;
import crafting.TradeInventory;
import java.util.LinkedList;
import java.util.HashMap;
import crafting.CraftingFrame;
import com.lsd.umc.script.ScriptInterface;

public class Gerahf
{
    private final ScriptInterface script;
    private final CraftingFrame frame;
    private HashMap<String, Integer> tradeMap;
    private final HashMap<String, Integer> inventoryMap;
    private final String prompt = "\u001b[2m\u001b[7m\u001b[2m";
    private STATE state;
    private final LinkedList<String> retrieveList;
    
    public Gerahf(final ScriptInterface script, final CraftingFrame frame) {
        this.inventoryMap = new HashMap<String, Integer>();
        this.state = STATE.INIT;
        this.retrieveList = new LinkedList<String>();
        this.script = script;
        this.frame = frame;
    }
    
    public boolean matchText() {
        final String text = this.script.getText();
        final String event = this.script.getEvent();
        if (this.state == STATE.INIT) {
            this.tradeMap = this.frame.getTradingComponents();
            this.state = STATE.COUNT_INVENTORY;
            this.script.send("inventory");
        }
        if (this.state == STATE.COUNT_INVENTORY && text.startsWith("Inventory:")) {
            this.state = STATE.COUNT_CAPTURE_INVENTORY;
            this.inventoryMap.clear();
        }
        if (this.state == STATE.COUNT_CAPTURE_INVENTORY) {
            if (event.startsWith("\u001b[2m\u001b[7m\u001b[2m")) {
                this.retrieveList.clear();
                this.retrieveList.addAll(TradeInventory.getDelta(this.script, this.tradeMap, this.inventoryMap));
                if (!this.retrieveList.isEmpty()) {
                    this.state = STATE.RETRIEVING_COMPONENTS;
                    return false;
                }
                this.script.send("follow gerahf");
                this.state = STATE.FOLLOW_GERAHF;
                return false;
            }
            else {
                TradeInventory.matchInventoryComponents(this.script, this.inventoryMap);
            }
        }
        if (this.state == STATE.RETRIEVING_COMPONENTS && !this.retrieveList.isEmpty()) {
            final String components = this.retrieveList.removeFirst();
            this.script.parse("#GETCOMPONENTS " + components);
            this.state = STATE.RETRIEVING;
            return false;
        }
        if (this.state == STATE.RETRIEVING) {
            if (text.startsWith("Your inventory is full.")) {
                this.retrieveList.clear();
            }
            else if (text.startsWith("Inventory:")) {
                if (this.retrieveList.isEmpty()) {
                    this.state = STATE.RETRIEVING_INVENTORY;
                    this.inventoryMap.clear();
                }
                else {
                    this.state = STATE.RETRIEVING_COMPONENTS;
                }
                return false;
            }
        }
        if (this.state == STATE.RETRIEVING_INVENTORY) {
            if (event.startsWith("\u001b[2m\u001b[7m\u001b[2m")) {
                boolean success = true;
                for (final String tradeItem : this.tradeMap.keySet()) {
                    Integer inventoryCount = this.inventoryMap.get(tradeItem.toLowerCase());
                    if (inventoryCount == null) {
                        inventoryCount = 0;
                    }
                    Integer tradeCount = this.tradeMap.get(tradeItem);
                    if (tradeCount == -1) {
                        tradeCount = 1;
                    }
                    if (inventoryCount < tradeCount) {
                        success = false;
                        this.script.print(AnsiTable.getCode("yellow") + "#TRADECOMPONENTS: " + AnsiTable.getCode("light red") + "Not enough " + tradeItem + "." + "\u0001");
                    }
                }
                if (success) {
                    this.script.send("follow gerahf");
                    this.state = STATE.FOLLOW_GERAHF;
                    return false;
                }
                return true;
            }
            else {
                TradeInventory.matchInventoryComponents(this.script, this.inventoryMap);
            }
        }
        if (this.state == STATE.FOLLOW_GERAHF) {
            if (text.equals("There's no one here by that name.")) {
                this.script.print(AnsiTable.getCode("yellow") + "#TRADECOMPONENTS: " + AnsiTable.getCode("white") + "Gerahf not found." + "\u0001");
                return true;
            }
            if (text.startsWith("    'here':    I'm looking for more of this component") || text.startsWith("    'cancel':  On second thought, I may not need any more of that.")) {
                for (final String item : this.tradeMap.keySet()) {
                    final int count = this.tradeMap.get(item);
                    if (count == -1) {
                        this.script.send("give component " + item + ", gerahf");
                        break;
                    }
                }
                this.script.send("answer here");
                this.state = STATE.WAIT_GERAHF;
                return false;
            }
        }
        if (this.state == STATE.WAIT_GERAHF) {
            if (text.startsWith("Commodity trader Gerahf says, 'No luck, I'm afraid")) {
                return true;
            }
            if (text.startsWith("   'offer':   How about those goods in exchange for your component?")) {
                for (final String item : this.tradeMap.keySet()) {
                    final int count = this.tradeMap.get(item);
                    if (count > 0) {
                        for (int i = 0; i < count; ++i) {
                            this.script.send("give component " + item + ", gerahf");
                        }
                    }
                }
                this.script.send("answer offer");
                return true;
            }
        }
        return false;
    }
    
    private enum STATE
    {
        INIT, 
        COUNT_CAPTURE_INVENTORY, 
        COUNT_INVENTORY, 
        RETRIEVING, 
        RETRIEVING_COMPONENTS, 
        RETRIEVING_INVENTORY, 
        INSPECT, 
        FOLLOW_GERAHF, 
        WAIT_GERAHF, 
        COMPLETED;
    }
}
