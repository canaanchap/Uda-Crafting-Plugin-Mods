// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

import java.util.Formatter;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;

public class RecipeObject implements Comparable
{
    private final ArrayList<Integer> tierList;
    private final ArrayList<String> categoryList;
    private String vendor;
    private String location;
    private LinkedList<MaterialObject> componentList;
    private final ArrayList<String> locationList;
    
    public RecipeObject() {
        this.tierList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
        this.categoryList = new ArrayList<String>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H"));
        this.componentList = new LinkedList<MaterialObject>();
        this.locationList = new ArrayList<String>(Arrays.asList("Light", "Head", "Neck", "About body", "Chest", "Arms", "Wrist", "Hands", "Weapon (Magic)", "Weapon (Finesse)", "Weapon (Combat)", "Legs", "Feet"));
    }
    
    public String getVendor() {
        return this.vendor;
    }
    
    public void setVendor(final String vendor) {
        this.vendor = vendor;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(final String location) {
        this.location = location;
    }
    
    public void addMaterial(final String type, final Integer count) {
        final MaterialObject material = new MaterialObject();
        material.setType(type);
        material.setCount(count);
        this.componentList.add(material);
    }
    
    public LinkedList<MaterialObject> getMaterials() {
        return this.componentList;
    }
    
    private boolean checkLocation(final HashMap<String, Boolean> filterMap) {
        boolean filter = false;
        boolean matched = false;
        for (final String filterLocation : this.locationList) {
            final Boolean value = filterMap.get(filterLocation);
            if (value != null && value) {
                filter = true;
                if (this.location.equals(filterLocation)) {
                    matched = true;
                    break;
                }
                continue;
            }
        }
        return !filter || matched;
    }
    
    private boolean checkTier(final HashMap<String, Boolean> filterMap, final ComponentJPanel material) {
        boolean filter = false;
        boolean matched = false;
        for (final Integer tier : this.tierList) {
            final Boolean check = filterMap.get(tier.toString());
            if (check != null) {
                filter = true;
                if (material.getMaterialTier() == tier) {
                    matched = true;
                    break;
                }
                continue;
            }
        }
        return !filter || matched;
    }
    
    private boolean checkTech(final HashMap<String, Boolean> filterMap, final ComponentJPanel material) {
        boolean matched = true;
        final Boolean artist = filterMap.get("Artist");
        if (artist != null && artist && material.isArtist()) {
            matched = false;
        }
        final Boolean expert = filterMap.get("Expert");
        if (expert != null && expert && material.isExpert()) {
            matched = false;
        }
        return matched;
    }
    
    private boolean checkCategory(final HashMap<String, Boolean> filterMap, final ComponentJPanel material) {
        boolean filter = false;
        boolean matched = false;
        for (final String category : this.categoryList) {
            final Boolean check = filterMap.get(category);
            if (check != null) {
                filter = true;
                if (material.getMaterialCategory().equalsIgnoreCase(category)) {
                    matched = true;
                    break;
                }
                continue;
            }
        }
        return !filter || matched;
    }
    
    public ArrayList<String> canMake(final HashMap<String, ComponentJPanel> componentsMap, final HashMap<String, Boolean> filterMap, final boolean showAll) {
        final ArrayList<String> usableList = new ArrayList<String>();
        if (this.componentList.size() == 0) {
            return null;
        }
        if (!this.checkLocation(filterMap)) {
            return usableList;
        }
        final LinkedList<ComponentJPanel> usablePrimary = new LinkedList<ComponentJPanel>();
        final MaterialObject primaryMaterial = this.componentList.get(0);
        final Iterator<String> componentIter = componentsMap.keySet().iterator();
        while (componentIter.hasNext()) {
            final ComponentJPanel component = componentsMap.get(componentIter.next());
            if (component.getMaterialType().equalsIgnoreCase(primaryMaterial.getMaterialType())) {
                if (component.getMaterialTier() == 0) {
                    continue;
                }
                if ((!showAll && component.getMaterialCount() <= primaryMaterial.getMaterialCount()) || !this.checkTier(filterMap, component) || !this.checkCategory(filterMap, component) || !this.checkTech(filterMap, component)) {
                    continue;
                }
                usablePrimary.add(component);
            }
        }
        final MaterialObject secondaryMaterial = this.componentList.get(1);
        for (final ComponentJPanel primaryComponent : usablePrimary) {
            final String[] gemstoneArray = CraftingData.getGemstones();
            final ArrayList<String> gemstones = new ArrayList<String>(Arrays.asList(gemstoneArray));
            gemstones.add(",Altering");
            for (String gemstone : gemstones) {
                int gemstoneCount = 1;
                if (gemstone.endsWith(",Altering")) {
                    gemstone = gemstone.split(",", 2)[0];
                    if (gemstone.length() > 0) {
                        gemstoneCount = 0;
                        final ComponentJPanel c = componentsMap.get(gemstone);
                        if (c != null) {
                            gemstoneCount = c.getMaterialCount();
                        }
                    }
                    if (!showAll && gemstoneCount == 0) {
                        continue;
                    }
                    final Iterator<String> secondaryIter = componentsMap.keySet().iterator();
                    while (secondaryIter.hasNext()) {
                        final ComponentJPanel secondaryComponent = componentsMap.get(secondaryIter.next());
                        if (secondaryComponent.getMaterialTier() == 0) {
                            continue;
                        }
                        if (!secondaryComponent.getMaterialType().equalsIgnoreCase(secondaryMaterial.getMaterialType())) {
                            continue;
                        }
                        final String combo = CraftingData.getCombo(primaryComponent.getMaterialCategory(), secondaryComponent.getMaterialCategory(), gemstone);
                        if (combo == null) {
                            continue;
                        }
                        boolean canUse = true;
                        if (primaryComponent.getMaterialName().equalsIgnoreCase(secondaryComponent.getMaterialName())) {
                            if (secondaryComponent.getMaterialCount() - 1 < primaryMaterial.getMaterialCount() + secondaryMaterial.getMaterialCount()) {
                                canUse = false;
                            }
                        }
                        else if (secondaryComponent.getMaterialCount() - 1 < secondaryMaterial.getMaterialCount()) {
                            canUse = false;
                        }
                        boolean filtered = false;
                        if (!filterMap.isEmpty()) {
                            for (final String filter : filterMap.keySet()) {
                                if (!filter.equals("Expert") && !filter.equals("Artist") && !this.isNumeric(filter) && !this.locationList.contains(filter)) {
                                    if (this.categoryList.contains(filter)) {
                                        continue;
                                    }
                                    boolean match = false;
                                    for (final String bonus : combo.split("/")) {
                                        if (bonus.equalsIgnoreCase(filter)) {
                                            match = true;
                                        }
                                    }
                                    if (!match) {
                                        filtered = true;
                                        break;
                                    }
                                    continue;
                                }
                            }
                        }
                        if (filtered || (!canUse && !showAll) || !this.checkTier(filterMap, secondaryComponent) || !this.checkCategory(filterMap, secondaryComponent)) {
                            continue;
                        }
                        final String primaryName = primaryComponent.getMaterialName();
                        String primaryTier = null;
                        if (primaryComponent.isExpert() && primaryComponent.isArtist()) {
                            primaryTier = "<font color=\"#00AA00\">" + primaryComponent.getMaterialCategory() + primaryComponent.getMaterialTier() + "</font>";
                        }
                        else if (primaryComponent.isExpert()) {
                            primaryTier = "<font color=\"#5555FF\">" + primaryComponent.getMaterialCategory() + primaryComponent.getMaterialTier() + "</font>";
                        }
                        else if (primaryComponent.isArtist()) {
                            primaryTier = "<font color=\"#FF7E00\">" + primaryComponent.getMaterialCategory() + primaryComponent.getMaterialTier() + "</font>";
                        }
                        else {
                            primaryTier = primaryComponent.getMaterialCategory() + primaryComponent.getMaterialTier();
                        }
                        final String secondaryName = secondaryComponent.getMaterialName();
                        String secondaryTier = null;
                        if (secondaryComponent.isExpert() && secondaryComponent.isArtist()) {
                            secondaryTier = "<font color=\"#00AA00\">" + secondaryComponent.getMaterialCategory() + secondaryComponent.getMaterialTier() + "</font>";
                        }
                        else if (secondaryComponent.isExpert()) {
                            secondaryTier = "<font color=\"#5555FF\">" + secondaryComponent.getMaterialCategory() + secondaryComponent.getMaterialTier() + "</font>";
                        }
                        else if (secondaryComponent.isArtist()) {
                            secondaryTier = "<font color=\"#FF7E00\">" + secondaryComponent.getMaterialCategory() + secondaryComponent.getMaterialTier() + "</font>";
                        }
                        else {
                            secondaryTier = secondaryComponent.getMaterialCategory() + secondaryComponent.getMaterialTier();
                        }
                        boolean bolded = true;
                        String primaryCount = null;
                        String secondaryCount = null;
                        if (secondaryComponent.getMaterialName().equalsIgnoreCase(primaryComponent.getMaterialName())) {
                            if (primaryComponent.getMaterialCount() > primaryMaterial.getMaterialCount() + secondaryMaterial.getMaterialCount()) {
                                final StringBuilder buffer = new StringBuilder();
                                final Formatter formatter = new Formatter(buffer);
                                formatter.format("%02d", primaryMaterial.getMaterialCount() + secondaryMaterial.getMaterialCount());
                                primaryCount = buffer.toString();
                            }
                            else if (primaryComponent.getMaterialCount() == primaryMaterial.getMaterialCount() + secondaryMaterial.getMaterialCount()) {
                                final StringBuilder buffer = new StringBuilder();
                                final Formatter formatter = new Formatter(buffer);
                                formatter.format("%02d", primaryMaterial.getMaterialCount() + secondaryMaterial.getMaterialCount());
                                primaryCount = "<font color=#AA0000>" + buffer.toString();
                            }
                            else {
                                final StringBuilder buffer = new StringBuilder();
                                final Formatter formatter = new Formatter(buffer);
                                formatter.format("%02d", primaryMaterial.getMaterialCount() + secondaryMaterial.getMaterialCount());
                                primaryCount = "<font color=#AAAAAA>" + buffer.toString();
                                bolded = false;
                            }
                        }
                        else {
                            if (primaryComponent.getMaterialCount() > primaryMaterial.getMaterialCount()) {
                                final StringBuilder buffer = new StringBuilder();
                                final Formatter formatter = new Formatter(buffer);
                                formatter.format("%02d", primaryMaterial.getMaterialCount());
                                primaryCount = buffer.toString();
                            }
                            else if (primaryComponent.getMaterialCount() == primaryMaterial.getMaterialCount()) {
                                final StringBuilder buffer = new StringBuilder();
                                final Formatter formatter = new Formatter(buffer);
                                formatter.format("%02d", primaryMaterial.getMaterialCount());
                                primaryCount = "<font color=#AA0000>" + buffer.toString();
                            }
                            else if (primaryComponent.getMaterialCount() < primaryMaterial.getMaterialCount()) {
                                final StringBuilder buffer = new StringBuilder();
                                final Formatter formatter = new Formatter(buffer);
                                formatter.format("%02d", primaryMaterial.getMaterialCount());
                                primaryCount = "<font color=#AAAAAA>" + buffer.toString();
                                bolded = false;
                            }
                            if (secondaryComponent.getMaterialCount() > secondaryMaterial.getMaterialCount()) {
                                final StringBuilder buffer = new StringBuilder();
                                final Formatter formatter = new Formatter(buffer);
                                formatter.format("%02d", secondaryMaterial.getMaterialCount());
                                secondaryCount = buffer.toString();
                            }
                            else if (secondaryComponent.getMaterialCount() == secondaryMaterial.getMaterialCount()) {
                                final StringBuilder buffer = new StringBuilder();
                                final Formatter formatter = new Formatter(buffer);
                                formatter.format("%02d", secondaryMaterial.getMaterialCount());
                                secondaryCount = "<font color=#AA0000>" + buffer.toString();
                            }
                            else if (secondaryComponent.getMaterialCount() < secondaryMaterial.getMaterialCount()) {
                                final StringBuilder buffer = new StringBuilder();
                                final Formatter formatter = new Formatter(buffer);
                                formatter.format("%02d", secondaryMaterial.getMaterialCount());
                                secondaryCount = "<font color=#AAAAAA>" + buffer.toString();
                                bolded = false;
                            }
                        }
                        String gemstoneText = capitalize(gemstone);
                        if (!gemstone.equals("")) {
                            gemstoneText = gemstoneText + " (" + gemstoneCount + ")";
                        }
                        if (bolded) {
                            if (secondaryCount != null) {
                                usableList.add(combo + "," + "<html><b>" + primaryTier + "</b> " + primaryCount + " " + primaryName + " (" + primaryComponent.getMaterialCount() + ") </html>," + "<html><b>" + secondaryTier + "</b> " + secondaryCount + " " + secondaryName + " (" + secondaryComponent.getMaterialCount() + ") </html>" + "," + gemstoneText);
                            }
                            else {
                                usableList.add(combo + "," + "<html><b>" + primaryTier + "</b> " + primaryCount + " " + primaryName + " (" + primaryComponent.getMaterialCount() + ") </html>," + "," + gemstoneText);
                            }
                        }
                        else {
                            if (bolded) {
                                continue;
                            }
                            if (secondaryCount != null) {
                                usableList.add(combo + "," + "<html>" + primaryTier + " " + primaryCount + " " + primaryName + " (" + primaryComponent.getMaterialCount() + ") </html>," + "<html>" + secondaryTier + " " + secondaryCount + " " + secondaryName + " (" + secondaryComponent.getMaterialCount() + ") </html>" + "," + gemstoneText);
                            }
                            else {
                                usableList.add(combo + "," + "<html>" + primaryTier + " " + primaryCount + " " + primaryName + " (" + primaryComponent.getMaterialCount() + ") </html>>," + "," + gemstoneText);
                            }
                        }
                    }
                }
            }
        }
        return usableList;
    }
    
    public int compareTo(final Object o) {
        if (!(o instanceof RecipeObject)) {
            return 0;
        }
        final int order = this.getVendor().compareTo(((RecipeObject)o).getVendor());
        if (order == 0) {
            return this.getLocation().compareTo(((RecipeObject)o).getLocation());
        }
        return order;
    }
    
    private static String capitalize(final String text) {
        if (text == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final int len = text.length();
        boolean upper = true;
        for (int pos = 0; pos < len; ++pos) {
            final String c = text.substring(pos, pos + 1);
            if (upper) {
                buf.append(c.toUpperCase());
                upper = false;
            }
            else {
                buf.append(c.toLowerCase());
            }
            if (c.equals(" ")) {
                upper = true;
            }
        }
        return buf.toString();
    }
    
    public boolean isNumeric(final String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
