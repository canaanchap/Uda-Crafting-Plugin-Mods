// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.Clipboard;
import java.util.regex.Matcher;
import com.lsd.umc.util.AnsiTable;
import crafting.interaction.Gerahf;
import crafting.interaction.Stacy;
import crafting.interaction.Inspection;
import java.awt.Component;
import javax.swing.JMenuItem;
import java.util.regex.Pattern;
import com.lsd.umc.script.ScriptInterface;

public class Crafting
{
    private ScriptInterface script;
    private final Pattern witherMultiplePattern;
    private final Pattern receiveMultiplePattern;
    private final Pattern receivePattern;
    private final Pattern givePattern;
    private final Pattern giveMultiplePattern;
    private final Pattern getPattern;
    private final Pattern getMultiplePattern;
    private final Pattern scrollGemTradePattern;
    private final Pattern scrollGemCreatePattern;
    private CraftingFrame frame;
    private Object interaction;
    private final ComponentStorage storage;
    private final GetComponent objectGetComponent;
    private final PutComponent objectPutComponent;
    
    public Crafting() {
        this.witherMultiplePattern = Pattern.compile("^\\((\\d+)\\) < salvage > .+ you carry withers into dust\\.$");
        this.receiveMultiplePattern = Pattern.compile("^\u001b\\[0m\u001b\\[37m(?:[A-Z].+? gives you|From out of nowhere,) \u001b\\[1;37m\\(\u001b\\[1;33m(\\d+)\u001b\\[1;37m\\)\u001b\\[0m\u001b\\[37m < (scraps|component|gemstone) > (.+?)(?:\\.| appear in your inventory\\.)\u001b\\[0m");
        this.receivePattern = Pattern.compile("^\u001b\\[0m\u001b\\[37m(?:[A-Z].+? gives you|From out of nowhere,) \u001b\\[1;37m< (scraps|component|gemstone) > (.+?)\u001b\\[0m\u001b\\[37m(?:\\.| appears in your inventory\\.)\u001b\\[0m");
        this.givePattern = Pattern.compile("^\u001b\\[0m\u001b\\[37mYou give \u001b\\[1;37m< (scraps|component|gemstone) > (.+?)\u001b\\[0m\u001b\\[37m to .+\\.\u001b\\[0m$");
        this.giveMultiplePattern = Pattern.compile("^\u001b\\[0m\u001b\\[37mYou give \u001b\\[1;37m\\(\u001b\\[1;33m(\\d+)\u001b\\[1;37m\\)\u001b\\[0m\u001b\\[37m < (scraps|component|gemstone) > (.+?) to .+\\.\u001b\\[0m$");
        this.scrollGemTradePattern = Pattern.compile("");
        this.scrollGemCreatePattern = Pattern.compile("");
        this.getPattern = Pattern.compile("^You (get|drop) < (scraps|component|gemstone) > (.+?)\\.$");
        this.getMultiplePattern = Pattern.compile("^\u001b\\[0m\u001b\\[37mYou (get|drop) \u001b\\[1;37m\\(\u001b\\[1;33m(\\d+)\u001b\\[1;37m\\)\u001b\\[0m\u001b\\[37m < (scraps|component|gemstone) > (.+?)\\.\u001b\\[0m$");
        this.interaction = null;
        this.storage = new ComponentStorage();
        this.objectGetComponent = new GetComponent(this.storage);
        this.objectPutComponent = new PutComponent(this.storage);
    }
    
    public void UnloadEvent() {
        if (this.frame != null) {
            this.frame.exit();
        }
    }
    
    public void init(final ScriptInterface script) {
        this.script = script;
        (this.frame = new CraftingFrame(script)).start();
        final String className = this.getClass().getName();
        script.registerCommand("tradescraps", className, "commandTradeScraps");
        script.registerCommand("tradecomponents", className, "commandTradeComponents");
        script.registerCommand("inspect", className, "commandInspect");
        script.registerCommand("materials", className, "showFrame");
        script.registerMenu("Salvage", new JMenuItem("Materials"), className, "showFrame");
        script.setVariable("UMC_SALVAGE", "0");
        script.registerCommand("getcomponents", className, "getComponent");
        script.registerCommand("getscraps", className, "getScrap");
        script.registerCommand("putcomponents", className, "putComponent");
        script.registerCommand("putscraps", className, "putScrap");
    }
    
    public String putComponent(final String args) {
        this.storage.calculateFull();
        return this.objectPutComponent.putComponent(args);
    }
    
    public String putScrap(final String args) {
        this.storage.calculateFull();
        return this.objectPutComponent.putScrap(args);
    }
    
    public String getComponent(final String args) {
        return this.objectGetComponent.getComponent(args);
    }
    
    public String getScrap(final String args) {
        return this.objectGetComponent.getScrap(args);
    }
    
    public String showFrame(final String args) {
        if (this.frame.isVisible()) {
            this.frame.toFront();
        }
        else if (!this.frame.isVisible()) {
            this.script.restoreContext((Component)this.frame);
            this.frame.setVisible(true);
        }
        return "";
    }
    
    public void IncomingEvent(final ScriptInterface script) {
        final String text = script.getText();
        final String event = script.getEvent();
        if (this.interaction != null) {
            if (this.interaction instanceof Inspection) {
                final Inspection inspection = (Inspection)this.interaction;
                if (inspection.matchText()) {
                    this.interaction = null;
                }
            }
            else if (this.interaction instanceof Stacy) {
                final Stacy stacy = (Stacy)this.interaction;
                if (stacy.matchText()) {
                    this.interaction = null;
                }
            }
            else if (this.interaction instanceof Gerahf) {
                final Gerahf gerahf = (Gerahf)this.interaction;
                if (gerahf.matchText()) {
                    this.interaction = null;
                }
            }
        }
        this.objectGetComponent.matchText(script);
        this.objectPutComponent.matchText(script);
        if (text.contains(" gives you ") || text.startsWith("From out of nowhere, ")) {
            int count = 0;
            String item = null;
            String type = null;
            final Matcher matcher = this.receivePattern.matcher(event);
            if (matcher.find()) {
                count = 1;
                type = matcher.group(1);
                item = matcher.group(2);
            }
            else {
                final Matcher multipleMatcher = this.receiveMultiplePattern.matcher(event);
                if (multipleMatcher.find()) {
                    count = Integer.parseInt(multipleMatcher.group(1));
                    type = multipleMatcher.group(2);
                    item = multipleMatcher.group(3);
                }
            }
            if (count > 0) {
                final String material = CraftingData.find(CraftingData.getShortName(item));
                if (material != null) {
                    if (type.equals("scraps")) {
                        script.print(AnsiTable.getCode("yellow") + "Crafting: " + AnsiTable.getCode("white") + "You received " + count + " " + material + " scrap(s).");
                        this.frame.incrementScraps(material, count);
                        this.storage.put("inventory", type, material, count);
                    }
                    else {
                        script.print(AnsiTable.getCode("yellow") + "Crafting: " + AnsiTable.getCode("white") + "You received " + count + " " + material + " " + type + "(s).");
                        this.frame.incrementComponent(material, count);
                        this.storage.put("inventory", "component", material, count);
                    }
                }
            }
        }
        if (text.endsWith("into dust.")) {
            int witherCount = 0;
            final Matcher matcher2 = this.witherMultiplePattern.matcher(text);
            if (matcher2.find()) {
                witherCount = Integer.parseInt(matcher2.group(1));
                int salvageCount = script.getIntegerVariable("UMC_SALVAGE");
                salvageCount -= witherCount;
                if (salvageCount < 0) {
                    salvageCount = 0;
                }
                script.setVariable("UMC_SALVAGE", Integer.toString(salvageCount));
            }
        }
        if (text.startsWith("You depleate your supply of")) {
            int count = 0;
            String item = null;
            String type = null;
            final Matcher gemTradeMatcher = this.scrollGemTradePattern.matcher(event);
            if (gemTradeMatcher.find()){
                count = Integer.parseInt(gemTradeMatcher.group(3));
                type = gemTradeMatcher.group(1);
                item = gemTradeMatcher.group(2);
            }
        }
        if (text.startsWith("You successfully created")) {
            int count = 0;
            String item = null;
            String type = null;
            final Matcher gemCreateMatcher = this.scrollGemCreatePattern.matcher(event);
            if (gemCreateMatcher.find()){
                count = 1;
                type = gemCreateMatcher.group(1);
                item = gemCreateMatcher.group(2);
            }
        }
        if (text.startsWith("You give")) {
            int count = 0;
            String item = null;
            String type = null;
            final Matcher giveMatcher = this.givePattern.matcher(event);
            if (giveMatcher.find()) {
                count = 1;
                type = giveMatcher.group(1);
                item = giveMatcher.group(2);
            }
            else {
                final Matcher giveMultipleMatcher = this.giveMultiplePattern.matcher(event);
                if (giveMultipleMatcher.find()) {
                    count = Integer.parseInt(giveMultipleMatcher.group(1));
                    type = giveMultipleMatcher.group(2);
                    item = giveMultipleMatcher.group(3);
                }
            }
            if (count > 0) {
                final String material = CraftingData.find(CraftingData.getShortName(item));
                if (material != null) {
                    if (type.equals("scraps")) {
                        script.print(AnsiTable.getCode("yellow") + "Crafting: " + AnsiTable.getCode("white") + "You gave " + count + " " + material + " scrap(s).");
                        this.frame.incrementScraps(material, -1 * count);
                        this.storage.remove("inventory", type, material, count);
                    }
                    else {
                        script.print(AnsiTable.getCode("yellow") + "Crafting: " + AnsiTable.getCode("white") + "You gave " + count + " " + material + " " + type + "(s).");
                        this.frame.incrementComponent(material, -1 * count);
                        this.storage.remove("inventory", type, material, count);
                    }
                }
            }
        }
        if (text.startsWith("You get ") || text.startsWith("You drop ")) {
            int count = 0;
            String action = null;
            String type = null;
            String item2 = null;
            final Matcher getMatcher = this.getPattern.matcher(text);
            if (getMatcher.find()) {
                count = 1;
                action = getMatcher.group(1);
                type = getMatcher.group(2);
                item2 = getMatcher.group(3);
            }
            else {
                final Matcher getMultipleMatcher = this.getMultiplePattern.matcher(event);
                if (getMultipleMatcher.find()) {
                    action = getMultipleMatcher.group(1);
                    count = Integer.parseInt(getMultipleMatcher.group(2));
                    type = getMultipleMatcher.group(3);
                    item2 = getMultipleMatcher.group(4);
                }
            }
            if (count > 0) {
                if (action.equals("get") && !item2.contains(" from ")) {
                    final String material2 = CraftingData.find(CraftingData.getShortName(item2));
                    if (material2 != null) {
                        if (type.equals("scraps")) {
                            script.print(AnsiTable.getCode("yellow") + "Crafting: " + AnsiTable.getCode("white") + "You get " + count + " " + material2 + " scrap(s).");
                            this.frame.incrementScraps(material2, count);
                            this.storage.put("inventory", type, material2, count);
                        }
                        else {
                            script.print(AnsiTable.getCode("yellow") + "Crafting: " + AnsiTable.getCode("white") + "You get " + count + " " + material2 + " " + type + "(s).");
                            this.frame.incrementComponent(material2, count);
                            this.storage.put("inventory", type, material2, count);
                        }
                    }
                }
                else if (action.equals("drop")) {
                    final String material2 = CraftingData.find(CraftingData.getShortName(item2));
                    if (material2 != null) {
                        if (type.equals("scraps")) {
                            script.print(AnsiTable.getCode("yellow") + "Crafting: " + AnsiTable.getCode("white") + "You drop " + count + " " + material2 + " scrap(s).");
                            this.frame.incrementScraps(material2, -1 * count);
                            this.storage.remove("inventory", type, material2, count);
                        }
                        else {
                            script.print(AnsiTable.getCode("yellow") + "Crafting: " + AnsiTable.getCode("white") + "You drop " + count + " " + material2 + " " + type + "(s).");
                            this.frame.incrementComponent(material2, -1 * count);
                            this.storage.remove("inventory", type, material2, count);
                        }
                    }
                }
            }
        }
    }
    
    public String commandInspect(final String arg) {
        final String var = this.script.getVariable("SalvageContainers");
        if (var == null || var.equals("")) {
            this.script.print("");
            this.script.print("#INSPECT requires the following variable to be defined:");
            this.script.print(AnsiTable.getCode("yellow") + "  SalvageContainers" + AnsiTable.getCode("gray"));
            this.script.print("");
            this.script.print("The purpose of this alias is to examine any containers");
            this.script.print("in your inventory which may contain crafting materials.");
            this.script.print("");
            this.script.print("Ex. #VARIABLE {SalvageContainers} {1-10.explorer,1-5.sack}");
            this.script.print("\u0001");
            return "";
        }
        if (arg.equalsIgnoreCase("clear") || arg.equalsIgnoreCase("reset")) {
            this.interaction = null;
        }
        if (this.interaction != null) {
            this.script.print(AnsiTable.getCode("yellow") + "#INSPECT: " + AnsiTable.getCode("light red") + "Already processing an interaction." + "\u0001");
            return "";
        }
        this.interaction = new Inspection(this.script, this.frame, this.storage, true);
        this.script.parse("#CR");
        return "";
    }
    
    public String commandTradeComponents(final String arg) {
        if (!arg.equals("clear") && !arg.equals("reset") && this.interaction != null) {
            this.script.print(AnsiTable.getCode("yellow") + "#TRADECOMPONENTS: " + AnsiTable.getCode("light red") + "Already processing an interaction." + "\u0001");
            return "";
        }
        this.interaction = new Gerahf(this.script, this.frame);
        this.script.parse("#CR");
        return "";
    }
    
    public String commandTradeScraps(final String arg) {
        if (!arg.equals("clear") && !arg.equals("reset") && this.interaction != null) {
            this.script.print(AnsiTable.getCode("yellow") + "#TRADESCRAPS: " + AnsiTable.getCode("light red") + "Already processing an interaction." + "\u0001");
            return "";
        }
        this.interaction = new Stacy(this.script, this.frame);
        this.script.parse("#CR");
        return "";
    }
    
    public void lostOwnership(final Clipboard clipboard, final Transferable contents) {
    }
    
    public static String formatNum(final int num) {
        if (num < 10) {
            return " " + num;
        }
        return Integer.toString(num);
    }
    
    private enum State
    {
        EMPTY, 
        INSPECT_WAITFORPROMPT, 
        INSPECT_CAPTURE, 
        TRADE_FOLLOW, 
        TRADE_INVENTORY, 
        TRADE_WAITFORPROMPT, 
        TRADE_RETRIEVE, 
        TRADE_RETRIEVE_WAIT, 
        TRADE_FINISHED;
    }
}
