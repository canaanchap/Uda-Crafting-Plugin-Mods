// 
// Decompiled by Procyon v0.5.36
// 

package crafting.interaction;

import com.lsd.umc.util.AnsiTable;
import java.util.regex.Matcher;
import crafting.CraftingData;
import java.util.LinkedList;
import crafting.ComponentStorage;
import java.util.regex.Pattern;
import crafting.CraftingFrame;
import com.lsd.umc.script.ScriptInterface;

public class Inspection
{
    private final ScriptInterface script;
    private final CraftingFrame frame;
    private boolean inspectTech;
    private Pattern techPattern;
    private final Pattern itemPattern;
    private final Pattern countPattern;
    private final String prompt = "\u001b[2m\u001b[7m\u001b[2m";
    private final ComponentStorage storage;
    private final LinkedList<String> containerList;
    private String currentContainer;
    private STATE state;
    
    public Inspection(final ScriptInterface script, final CraftingFrame frame, final ComponentStorage storage, final boolean inspectTech) {
        this.inspectTech = false;
        this.techPattern = Pattern.compile("^\\s+([\\d+,]+) - (?:Expertise|Artistry): ");
        this.itemPattern = Pattern.compile("^[ \\[\\]\\d]{9}< (scraps|component|gemstone) > (.+)$");
        this.countPattern = Pattern.compile("^  \\[\\s+(\\d+) \\]");
        this.containerList = new LinkedList<String>();
        this.currentContainer = "";
        this.state = STATE.INIT;
        this.script = script;
        this.frame = frame;
        this.inspectTech = inspectTech;
        this.storage = storage;
        this.state = STATE.INIT;
    }
    
    public boolean matchText() {
        final String text = this.script.getText();
        if (this.state == STATE.INIT) {
            this.generateList();
            this.storage.reset();
            this.frame.resetData();
            if (this.inspectTech) {
                this.script.send("quest action, craft");
                this.state = STATE.CAPTURE_TECH;
            }
            else {
                this.script.parse("#CR");
                this.state = STATE.CAPTURE_WAITFORPROMPT;
            }
        }
        if (this.state == STATE.CAPTURE_TECH) {
//            if (text.startsWith(" [ Quest Actions: " + this.script.getName() + " ]: Crafting")) {
            if (text.startsWith(" [ Quest Actions: " + this.script.getText() + " ]: Crafting")) {
                this.frame.clearTech();
            }
            else if (text.startsWith(" [ Total Quest Actions ]:")) {
                this.state = STATE.CAPTURE_WAITFORPROMPT;
            }
            else if (text.startsWith("< Page Scrolling Paused")) {
                this.script.parse("#CR");
            }
            else {
                final Matcher techMatcher = this.techPattern.matcher(text);
                if (techMatcher.find()) {
                    try {
                        Integer tech = Integer.parseInt(techMatcher.group(1).replace(",", ""));
                        if (tech > 1000) {
                            tech -= 1000;
                            this.frame.setArtist(tech);
                        }
                        else {
                            this.frame.setExpert(tech);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if (this.state == STATE.CAPTURE_CONTAINERS || this.state == STATE.CAPTURE_WAITFORPROMPT || this.state == STATE.CAPTURE_WAITFORPROMPT_COMPLETED) {
            if (this.state == STATE.CAPTURE_CONTAINERS) {
                if (text.equals("Inventory:")) {
                    this.currentContainer = "inventory";
                    this.state = STATE.CAPTURE_WAITFORPROMPT_COMPLETED;
                }
                else if (text.startsWith("Contents of ")) {
                    this.state = STATE.CAPTURE_WAITFORPROMPT;
                }
            }
            else if (this.state == STATE.CAPTURE_WAITFORPROMPT && this.script.getEvent().startsWith("\u001b[2m\u001b[7m\u001b[2m")) {
                this.state = STATE.CAPTURE_CONTAINERS;
                this.inspectSalvage();
            }
            else if (this.state == STATE.CAPTURE_WAITFORPROMPT_COMPLETED && this.script.getEvent().startsWith("\u001b[2m\u001b[7m\u001b[2m")) {
                this.state = STATE.COMPLETED;
                this.frame.updateMaterials();
                return true;
            }
            Matcher matcher = this.itemPattern.matcher(text);
            if (matcher.find()) {
                final String type = matcher.group(1);
                final String name = matcher.group(2);
                final String shortName = CraftingData.getShortName(name);
                Integer count = 1;
                matcher = this.countPattern.matcher(text);
                if (matcher.find()) {
                    try {
                        count = Integer.parseInt(matcher.group(1));
                    }
                    catch (Exception ex) {}
                }
                if (type.equals("scraps")) {
                    this.frame.incrementScraps(shortName, count);
                    this.storage.put(this.currentContainer, "scraps", shortName, count);
                }
                else if (type.equals("component") || type.equals("gemstone")) {
                    this.frame.incrementComponent(shortName, count);
                    this.storage.put(this.currentContainer, "component", shortName, count);
                }
            }
        }
        return false;
    }
    
    private void inspectSalvage() {
        if (this.containerList.isEmpty()) {
            this.script.send("inventory");
        }
        else {
            this.currentContainer = this.containerList.pop();
            this.script.send("look in " + this.currentContainer);
        }
    }
    
    private void generateList() {
        this.containerList.clear();
        final String containerVariable = this.script.getVariable("SalvageContainers");
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
                        this.script.print("Added " + i + "." + containerName);
                    }
                }
                catch (Exception e) {
                    this.script.print(AnsiTable.getCode("light red") + e.getMessage());
                }
            }
        }
    }
    
    private enum STATE
    {
        INIT, 
        CAPTURE_TECH, 
        CAPTURE_CONTAINERS, 
        CAPTURE_WAITFORPROMPT, 
        CAPTURE_WAITFORPROMPT_COMPLETED, 
        COMPLETED;
    }
}
