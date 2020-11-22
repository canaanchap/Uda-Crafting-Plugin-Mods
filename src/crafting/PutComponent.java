// 
// Decompiled by Procyon v0.5.36
// 
package crafting;

import java.util.regex.Matcher;
import com.lsd.umc.util.AnsiTable;
import java.util.regex.Pattern;
import java.util.LinkedList;
import com.lsd.umc.script.ScriptInterface;

public class PutComponent
{
    private ScriptInterface script;
    private LinkedList<String> containerList;
    private STATE state;
    private String materialType;
    private String currentContainer;
    private final String prompt = "\u001b[2m\u001b[7m\u001b[2m";
    private final Pattern itemPattern;
    private final Pattern countPattern;
    private final ComponentStorage storage;
    
    public PutComponent(final ComponentStorage storage) {
        this.containerList = new LinkedList<String>();
        this.state = STATE.CLEAN;
        this.materialType = null;
        this.currentContainer = null;
        this.itemPattern = Pattern.compile("< (scraps|component|gemstone) > (.+?) inside .+");
        this.countPattern = Pattern.compile("^You put \\((\\d+)\\) < ");
        this.storage = storage;
    }
    
    public String getState() {
        return this.state.toString();
    }
    
    public void matchText(final ScriptInterface script) {
        this.script = script;
        if (this.state == STATE.WAITING || this.state == STATE.WAITFORPROMPT) {
            final String text = script.getText();
            if (text.startsWith("The item you are trying to put away is not in your inventory.")) {
                script.print(AnsiTable.getCode("yellow") + "#PUTCOMPONENT:" + AnsiTable.getCode("white") + " Completed.");
                this.state = STATE.WAITFORPROMPT;
                this.containerList.clear();
            }
            else if (text.startsWith("You put")) {
                this.state = STATE.WAITFORPROMPT;
                if (this.currentContainer != null && text.startsWith("You put")) {
                    final Matcher typeMatch = this.itemPattern.matcher(text);
                    if (typeMatch.find()) {
                        String type = "component";
                        if (typeMatch.group(1).equals("scraps")) {
                            type = "scraps";
                        }
                        int count = 1;
                        final Matcher countMatch = this.countPattern.matcher(text);
                        if (countMatch.find()) {
                            count = Integer.parseInt(countMatch.group(1));
                        }
                        final String shortName = CraftingData.getShortName(typeMatch.group(2));
                        script.print(AnsiTable.getCode("yellow") + "#PUTCOMPONENT: " + AnsiTable.getCode("white") + "Indexed " + count + " " + shortName + " in " + this.currentContainer);
                        this.storage.put(this.currentContainer, type, shortName, count);
                        this.storage.remove("inventory", type, shortName, count);
                    }
                }
            }
            else if (text.startsWith("You notice that ") && text.endsWith(" is full.")) {
                this.storage.setFull(this.currentContainer, true);
                this.state = STATE.WAITFORPROMPT;
            }
            if (this.state == STATE.WAITFORPROMPT && script.getEvent().startsWith("\u001b[2m\u001b[7m\u001b[2m")) {
                final STATE state = this.state;
                this.state = STATE.PROCESSING;
            }
        }
        if (this.state == STATE.PROCESSING) {
            if (this.containerList.isEmpty()) {
                final STATE state2 = this.state;
                this.state = STATE.COMPLETED;
                script.send("inventory");
                return;
            }
            if (script.getStringState("prompt").equals("true")) {
                while (!this.containerList.isEmpty()) {
                    final String container = this.containerList.removeFirst();
                    if (container != null) {
                        if (this.storage.isFull(container)) {
                            continue;
                        }
                        this.currentContainer = container;
                        script.send("put all." + this.materialType + ", " + container);
                        this.state = STATE.WAITING;
                        break;
                    }
                }
            }
        }
    }
    
    public String putComponent(final String args) {
        return this._putComponent("component");
    }
    
    public String putScrap(final String args) {
        return this._putComponent("scraps");
    }
    
    private String _putComponent(final String _materialType) {
        final String containerVariable = this.script.getVariable("SalvageContainers");
        if (containerVariable == null || containerVariable.equals("")) {
            this.script.print(AnsiTable.getCode("yellow") + "#PUTCOMPONENT: " + AnsiTable.getCode("light red") + "Variable SalvageContainers must be set. Ex, #VARIABLE {SalvageContainers} {1-10.explorer,1-5.sack}" + "\u0001");
            return "";
        }
        if (this.state != STATE.CLEAN && this.state != STATE.COMPLETED) {
            this.script.print(AnsiTable.getCode("yellow") + "#PUTCOMPONENT: " + AnsiTable.getCode("light red") + "Must wait till previous request completes." + "\u0001");
            return "";
        }
        this.materialType = _materialType;
        this.containerList.clear();
        final Pattern pattern = Pattern.compile("^(\\d+)-(\\d+)\\.(.+)$");
        final String[] arr$;
        final String[] containers = arr$ = containerVariable.split(",");
        for (final String container : arr$) {
            final Matcher match = pattern.matcher(container.trim());
            if (match.find()) {
                try {
                    final int low = Integer.parseInt(match.group(1));
                    final int high = Integer.parseInt(match.group(2));
                    final String containerName = match.group(3);
                    for (int i = low; i <= high; ++i) {
                        this.containerList.add(i + "." + containerName);
                    }
                }
                catch (Exception e) {
                    this.script.print(AnsiTable.getCode("light red") + e.getMessage());
                }
            }
        }
        this.state = STATE.PROCESSING;
        this.script.parse("#CR");
        return "";
    }
    
    private enum STATE
    {
        CLEAN, 
        PROCESSING, 
        WAITING, 
        WAITFORPROMPT, 
        COMPLETED;
    }
}
