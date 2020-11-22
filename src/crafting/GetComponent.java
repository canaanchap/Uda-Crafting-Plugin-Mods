// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

import java.util.Iterator;
import java.util.HashMap;
import java.util.regex.Matcher;
import com.lsd.umc.util.AnsiTable;
import java.util.regex.Pattern;
import com.lsd.umc.script.ScriptInterface;

public class GetComponent
{
    private ScriptInterface script;
    private String retrieveContainer;
    private String retrieveMaterial;
    private STATE state;
    private String materialType;
    private final ComponentStorage storage;
    private static final Pattern getPattern;
    private final RetrieveQueue retrieveQueue;
    
    public GetComponent(final ComponentStorage storage) {
        this.retrieveContainer = null;
        this.retrieveMaterial = null;
        this.state = STATE.CLEAN;
        this.materialType = null;
        this.retrieveQueue = new RetrieveQueue();
        this.storage = storage;
    }
    
    public String getState() {
        return this.state.toString();
    }
    
    private void reset() {
        this.retrieveContainer = null;
        this.retrieveMaterial = null;
        this.retrieveQueue.clear();
        final STATE state = this.state;
        this.state = STATE.CLEAN;
        this.script.send("inventory");
    }
    
    public void matchText(final ScriptInterface script) {
        this.script = script;
        if (this.state == STATE.PROCESSING) {
            if (!this.retrieveQueue.isEmpty()) {
                final String material = this.retrieveQueue.getNext();
                final String container = this.storage.getLocation(this.materialType, material);
                if (container == null) {
                    script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT: " + AnsiTable.getCode("light red") + "Was not able to retrieve all materials.");
                    final STATE state = this.state;
                    this.state = STATE.COMPLETED;
                    script.send("inventory");
                    return;
                }
                if (container.equals("inventory")) {
                    return;
                }
                final int containerCount = this.storage.getContainerCount(container, this.materialType, material);
                final int retrieveCount = this.retrieveQueue.count(material);
                this.retrieveMaterial = material;
                this.retrieveContainer = container;
                if (containerCount <= retrieveCount) {
                    script.send("get all." + this.materialType + " " + this.retrieveMaterial + ", " + this.retrieveContainer);
                }
                else {
                    script.send("get " + this.materialType + " " + this.retrieveMaterial + ", " + this.retrieveContainer);
                }
                this.state = STATE.RETRIEVING;
            }
            else {
                final STATE state2 = this.state;
                this.state = STATE.COMPLETED;
                script.send("inventory");
            }
        }
        else if (this.state == STATE.RETRIEVING) {
            final String text = script.getText();
            if (text.startsWith("You get ")) {
                if (this.materialType.equals("component") && !text.contains(" < component > ") && !text.contains(" < gemstone >")) {
                    return;
                }
                if (this.materialType.equals("scraps") && !text.contains(" < scraps > ")) {
                    return;
                }
                int count = 1;
                final Matcher countMatcher = GetComponent.getPattern.matcher(text);
                if (countMatcher.find()) {
                    count = Integer.parseInt(countMatcher.group(1));
                }
                script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT:" + AnsiTable.getCode("white") + " Retrieved " + count + " " + this.retrieveMaterial);
                this.retrieveQueue.remove(this.retrieveMaterial, count);
                this.storage.remove(this.retrieveContainer, this.materialType, this.retrieveMaterial, count);
                this.storage.put("inventory", this.materialType, this.retrieveMaterial, count);
                this.storage.setFull(this.retrieveContainer, false);
                this.state = STATE.PROCESSING;
            }
            else if (text.startsWith("You see nothing by that name inside")) {
                script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT:" + AnsiTable.getCode("light red") + " Containers not synchronized, use #INSPECT.");
                this.retrieveQueue.clear();
                this.state = STATE.PROCESSING;
            }
            else if (text.startsWith("You see nothing by that name here.")) {
                script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT:" + AnsiTable.getCode("light red") + " Invalid container " + this.retrieveContainer);
                this.retrieveQueue.clear();
                this.state = STATE.PROCESSING;
            }
            else if (text.startsWith("Your inventory is full.")) {
                script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT:" + AnsiTable.getCode("light red") + " Full inventory.");
                this.state = STATE.PROCESSING;
                this.retrieveQueue.clear();
            }
        }
    }
    
    public String getComponent(final String args) {
        return this._getComponent("component", args);
    }
    
    public String getScrap(final String args) {
        return this._getComponent("scraps", args);
    }
    
    private String _getComponent(final String _materialType, final String args) {
        if (args.trim().equalsIgnoreCase("clear") || args.trim().equalsIgnoreCase("reset")) {
            this.reset();
            return "";
        }
        final String containerVariable = this.script.getVariable("SalvageContainers");
        if (containerVariable == null || containerVariable.equals("")) {
            this.script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT: " + AnsiTable.getCode("light red") + "Variable SalvageContainers must be set. Ex, #VARIABLE {SalvageContainers} {1-10.explorer,1-5.sack}" + "\u0001");
            return "";
        }
        if (this.state != STATE.CLEAN && this.state != STATE.COMPLETED) {
            this.script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT:" + AnsiTable.getCode("light red") + " Must wait till previous retrieval completes." + "\u0001");
            return "";
        }
        this.materialType = _materialType;
        this.retrieveQueue.clear();
        int targetCount = 0;
        String targetComponent = null;
        final String[] arr$;
        final String[] components = arr$ = args.trim().split(",");
        for (String component : arr$) {
            component = component.trim();
            if (!component.equals("")) {
                try {
                    targetComponent = CraftingData.find(component.split(" ", 2)[1].trim());
                    if (targetComponent == null) {
                        this.script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT:" + AnsiTable.getCode("light red") + " Unknown component (" + component.split(" ", 2)[1].trim() + ").");
                        this.script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT:" + AnsiTable.getCode("light red") + " Aborting retrieval." + "\u0001");
                        return "";
                    }
                    targetComponent = targetComponent.toLowerCase();
                    final int count = this.storage.getInventory(this.materialType, targetComponent);
                    if (count > 0) {
                        this.script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT: " + AnsiTable.getCode("white") + count + " " + targetComponent + " already in inventory." + "\u0001");
                    }
                    targetCount = Integer.parseInt(component.split(" ", 2)[0].trim());
                    targetCount -= count;
                    if (targetCount >= 1) {
                        for (int i = 0; i < targetCount; ++i) {
                            this.retrieveQueue.add(targetComponent);
                        }
                        this.script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT:" + AnsiTable.getCode("white") + " Added " + targetCount + " " + targetComponent + " to retrieval queue." + "\u0001");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    this.script.print(AnsiTable.getCode("yellow") + "#GETCOMPONENT:" + AnsiTable.getCode("light red") + " Invalid syntax - #GETCOMPONENT <# of> <component>");
                    return "";
                }
            }
        }
        this.retrieveContainer = null;
        this.retrieveMaterial = null;
        this.state = STATE.PROCESSING;
        this.script.parse("#CR");
        return "";
    }
    
    static {
        getPattern = Pattern.compile("You get \\((\\d+)\\) < (component|scraps|gemstone) > ");
    }
    
    private enum STATE
    {
        CLEAN, 
        PROCESSING, 
        RETRIEVING, 
        COMPLETED;
    }
    
    private class RetrieveQueue
    {
        private final HashMap<String, Integer> queueMap;
        
        private RetrieveQueue() {
            this.queueMap = new HashMap<String, Integer>();
        }
        
        public String getNext() {
            for (final String material : this.queueMap.keySet()) {
                final Integer count = this.queueMap.get(material);
                if (count != null && count > 0) {
                    return material;
                }
            }
            return null;
        }
        
        public boolean isEmpty() {
            for (final String material : this.queueMap.keySet()) {
                final Integer count = this.queueMap.get(material);
                if (count != null && count > 0) {
                    return false;
                }
            }
            return true;
        }
        
        public int count(final String material) {
            Integer count = this.queueMap.get(material);
            if (count == null) {
                count = 0;
            }
            return count;
        }
        
        public int add(final String material) {
            Integer count = this.queueMap.get(material);
            if (count == null) {
                count = 0;
            }
            ++count;
            this.queueMap.put(material, count);
            return count;
        }
        
        public int remove(final String material, final int decrement) {
            Integer count = this.queueMap.get(material);
            if (count == null) {
                count = 0;
            }
            count -= decrement;
            if (count < 0) {
                count = 0;
            }
            this.queueMap.put(material, count);
            return count;
        }
        
        public void clear() {
            this.queueMap.clear();
        }
    }
}
