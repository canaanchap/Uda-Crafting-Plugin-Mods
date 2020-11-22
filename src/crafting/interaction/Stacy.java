// 
// Decompiled by Procyon v0.5.36
// 

package crafting.interaction;

import java.util.Iterator;
import java.util.Map;
import java.util.Collection;
import crafting.TradeInventory;
import com.lsd.umc.util.AnsiTable;
import java.util.LinkedList;
import java.util.HashMap;
import crafting.CraftingFrame;
import com.lsd.umc.script.ScriptInterface;

public class Stacy
{
    private final ScriptInterface script;
    private final CraftingFrame frame;
    private HashMap<String, Integer> tradeMap;
    private final HashMap<String, Integer> inventoryMap;
    private final HashMap<String, Integer> finalMap;
    private final String prompt = "\u001b[2m\u001b[7m\u001b[2m";
    private STATE state;
    private final LinkedList<String> retrieveList;
    
    public Stacy(final ScriptInterface script, final CraftingFrame frame) {
        this.inventoryMap = new HashMap<String, Integer>();
        this.finalMap = new HashMap<String, Integer>();
        this.state = STATE.INIT;
        this.retrieveList = new LinkedList<String>();
        this.script = script;
        this.frame = frame;
    }
    
    public boolean matchText() {
        final String text = this.script.getText();
        final String event = this.script.getEvent();
        if (this.state == STATE.INIT) {
            this.tradeMap = this.frame.getTradingScraps();
            if (this.tradeMap.isEmpty()) {
                this.script.print(AnsiTable.getCode("yellow") + "#TRADESCRAPS: " + AnsiTable.getCode("white") + "No scraps to trade." + "\u0001");
                return true;
            }
            for (final String item : this.tradeMap.keySet()) {
                final int count = this.tradeMap.get(item);
                this.script.print(AnsiTable.getCode("yellow") + "#TRADESCRAPS: " + AnsiTable.getCode("white") + "Trading " + count + " " + item + " scraps." + "\u0001");
            }
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
                    this.state = STATE.RETRIEVING_SCRAPS;
                    return false;
                }
                this.finalMap.clear();
                this.finalMap.putAll(this.tradeMap);
                this.script.send("follow stacy");
                this.state = STATE.FOLLOW_STACY;
                return false;
            }
            else {
                TradeInventory.matchInventoryScraps(this.script, this.inventoryMap);
            }
        }
        if (this.state == STATE.RETRIEVING_SCRAPS && !this.retrieveList.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            while (!this.retrieveList.isEmpty()) {
                final String scraps = this.retrieveList.removeFirst();
                sb.append(scraps);
                if (!this.retrieveList.isEmpty()) {
                    sb.append(",");
                }
            }
            this.script.parse("#GETSCRAPS " + sb.toString());
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
                    this.state = STATE.RETRIEVING_SCRAPS;
                }
                return false;
            }
        }
        if (this.state == STATE.RETRIEVING_INVENTORY) {
            if (event.startsWith("\u001b[2m\u001b[7m\u001b[2m")) {
                this.finalMap.clear();
                for (final String item : this.inventoryMap.keySet()) {
                    final Integer itemCount = this.inventoryMap.get(item);
                    final Integer tradeCount = this.tradeMap.get(item);
                    if (tradeCount != null && itemCount != null && itemCount >= tradeCount) {
                        this.finalMap.put(item, tradeCount);
                    }
                }
                if (this.finalMap.size() > 0) {
                    this.script.send("follow stacy");
                    this.state = STATE.FOLLOW_STACY;
                    return false;
                }
                this.script.print(AnsiTable.getCode("yellow") + "#TRADESCRAPS: " + AnsiTable.getCode("white") + "No scraps to trade." + "\u0001");
                return true;
            }
            else {
                TradeInventory.matchInventoryScraps(this.script, this.inventoryMap);
            }
        }
        if (this.state == STATE.FOLLOW_STACY) {
            if (text.equals("There's no one here by that name.")) {
                this.script.print(AnsiTable.getCode("yellow") + "#TRADESCRAPS: " + AnsiTable.getCode("white") + "Stacy not found." + "\u0001");
                return true;
            }
            if (text.equals("    'begin':   I've handed you the scraps that I'd like to exchange.")) {
                for (final String item : this.finalMap.keySet()) {
                    for (int count = this.finalMap.get(item), i = 0; i < count; ++i) {
                        this.script.send("give scraps " + item + ", stacy");
                    }
                }
                this.script.send("answer begin");
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
        RETRIEVING_SCRAPS, 
        RETRIEVING_INVENTORY, 
        INSPECT, 
        FOLLOW_STACY, 
        COMPLETED;
    }
}
