// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

import java.util.regex.Matcher;
import java.util.Iterator;
import com.lsd.umc.util.AnsiTable;
import java.util.LinkedList;
import java.util.HashMap;
import com.lsd.umc.script.ScriptInterface;
import java.util.regex.Pattern;

public class TradeInventory
{
    private static final Pattern itemPattern;
    private static final Pattern countPattern;
    
    public static LinkedList<String> getDelta(final ScriptInterface script, final HashMap<String, Integer> tradeMap, final HashMap<String, Integer> inventoryMap) {
        final LinkedList<String> returnList = new LinkedList<String>();
        for (final String item : tradeMap.keySet()) {
            Integer count = tradeMap.get(item);
            if (count == -1) {
                count = 1;
            }
            Integer inventoryCount = inventoryMap.get(item.toLowerCase());
            if (inventoryCount == null) {
                inventoryCount = 0;
            }
            int delta = count;
            if (delta > 0) {
                returnList.add(delta + " " + item);
            }
            else if (delta < 0) {
                delta = 0;
            }
            script.print(AnsiTable.getCode("yellow") + "#TRADE: " + AnsiTable.getCode("white") + "Retrieving " + delta + " " + item + "." + "\u0001");
        }
        return returnList;
    }
    
    public static void matchInventoryScraps(final ScriptInterface script, final HashMap<String, Integer> inventoryMap) {
        matchInventory(script, "scraps", inventoryMap);
    }
    
    public static void matchInventoryComponents(final ScriptInterface script, final HashMap<String, Integer> inventoryMap) {
        matchInventory(script, "component", inventoryMap);
    }
    
    private static void matchInventory(final ScriptInterface script, final String matchType, final HashMap<String, Integer> inventoryMap) {
        final String text = script.getText();
        Matcher matcher = TradeInventory.itemPattern.matcher(text);
        if (matcher.find()) {
            final String type = matcher.group(1);
            final String name = matcher.group(2);
            final String shortName = CraftingData.getShortName(name).toLowerCase();
            Integer count = 1;
            matcher = TradeInventory.countPattern.matcher(text);
            if (matcher.find()) {
                try {
                    count = Integer.parseInt(matcher.group(1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (type.equals(matchType)) {
                script.print(AnsiTable.getCode("yellow") + "#TRADE: " + AnsiTable.getCode("white") + "Inventory has " + count + " " + shortName + "." + "\u0001");
                inventoryMap.put(shortName, count);
            }
        }
    }
    
    static {
        itemPattern = Pattern.compile("^[ \\[\\]\\d]{9}< (scraps|component|gemstone) > (.+)$");
        countPattern = Pattern.compile("^  \\[\\s+(\\d+) \\]");
    }
}
