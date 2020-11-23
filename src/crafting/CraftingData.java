/*
 * Decompiled with CFR 0.150.
 */
package crafting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class CraftingData {
    private static final HashMap<String, String> typeMap = new HashMap();
    private static final HashMap<String, String> comboMap = new HashMap();
    private static final String[] woodArray = new String[]{"A,Thick Oak,3,Thorned Acacia,35,Irongrowth,67,Dragonbone,99", "B,Smoky Birch,6,Scarlet Bole,38,Whip-wing Beech,70,Windweald Timber,102", "C,Swirled Teak,11,Fine Rosewood,43,Blooming Laurel,75,Elysian Pearwood,107", "D,Applewood,14,Giant Willowreed,46,Sedgewood,78,Swampskein Reed,110", "E,Plain Beech,19,Solid Mahogany,51,Dowsing Oak,83,Unburning Oak,115", "F,Golden Yew,22,Scented Cedar,54,Fragrant Acanthus,86,Murmuring Ashbranch,118", "G,Pinewood,27,Ancient Elm,59,Whiteweald,91,Thrice-chanted Yew,123", "H,Dark Hickory,30,Stained Cypress,62,Twisted Hemlock,94,Wyrding Willow,126"};
    private static final String[] leatherArray = new String[]{"A,Toughened Leather,2,Gnarled Bruteskin,34,Blood-grain Leather,66, ,0", "B,Oiled Calfskin,7,Ermine Pelt,39,Moon-brushed Vellum,71,Wyrmwing Lamina,103", "C,Softened Hide,10,Supple Gromhide,42,Lustrous Silver Pelt,74,Ether Membrane,106", "D,Buckskin,15,Copper-grain Leather,47,Phytoderm Husk,79,Chimeric Vellum,111", "E,Rough Shagreen,18,Cuirboulli,50,Ruby-dusted Leather,82, ,0", "F,Embossed Leather,23,Scented Whitehide,55,Spored Lamella,87,Demon-slough,119", "G,Soft Suede,26,Blush Vellum,58,Sanded Scales,90, ,0", "H,Sand-baked Hide,31,Charred Scales,63,Cinderhide,95,Hoarfrost Scrap,127"};
    private static final String[] clothArray = new String[]{"A,Ramie Fiber,1,Hunter's Fleece,33,Royal Satin,65,Scornshroud,97", "B,Gypsy Linen,8,Glossy Angora,40,Dewstrand,72,Sidhe Spinnings,104", "C,Ashcloth,9,Dark Satin,41,Spindrift Felt,73,Windseal Weave,105", "D,Ryven Felt,16,Bucksheet,48,Sable Silk,80,Tidal Fiber,112", "E,Mhaldryn Wool,17,Sandflax,49,Pearlsheet,81,Woven Phlogiston,113", "F,Crushed Velvet,24,Argent Mesh,56,Sovereign Silk,88,Undercroft Wrappings,120", "G,Light Taffeta,25,Lustrous Silk,57,Angelic Gossamer,89,Dawnsveil,121", "H,Cashmere,32,Rich Velour,64,Rich Damask,96,Webweave,128"};
    private static final String[] metalArray = new String[]{"A,Wrought Iron,4,Tempered Steel,36,Golem Shard,68, ,0", "B,Brass,5,Pure Electrum,37,Mirrorsteel,69, ,0", "C,Bronze,12,Palladium,44,Fey Mithril,76, ,0", "D,Coarse Gold,13,Damascus Steel,45,Galvanic Alloy,77,Igneous Exolith,109", "E,Titanium,20,Adamantine,52,Limpid Iron,84,Living Steel,116", "F,Tin,21,Cold Iron,53,Sangrolith,85, ,0", "G,Rough Silver,28,Onicite,60,Cerulean Ferrum,92, ,0", "H,Dull Platinum,29,Obdurium,61,Siren-Steel,93,Meteor Iron,125"};
    private static final String[] gemstoneArray = new String[]{"blended tourmaline,30/4", "deep emerald,30/4", "delicate tanzanite,30/4", "faded amethyst,30/4", "jade inlay,30/4", "pale iolite,30/4", "pale ruby,30/4", "pink spinel,30/4", "pure aquamarine,30/4", "pure topaz,30/4", "sardonyx inlay,30/4", "soft citrine,30/4", "bloodstone inlay,60/8", "champagne diamond,60/8", "demantoid garnet,60/8", "fire opal,60/8", "moonstone inlay,60/8", "rich alexandrite,60/8", "star sapphire,60/8", "swirled ametrine,60/8", "amber droplet,Altering", "blue zircon,Altering", "jasper crescent,Altering", "lapis lazuli,Altering", "polished coral,Altering", "smoky quartz,Altering", "smooth chrysoberyl,Altering", "twisted agate,Altering", "god-flame,Beast","fallen star,Beast"};
    private static final LinkedList<String[]> materialArrays = new LinkedList();

    public static String getGemstoneType(String name) {
        for (String item : gemstoneArray) {
            String[] data = item.split(",", 2);
            if (data == null || data.length != 2 || data[0] == null || data[1] == null || !name.equalsIgnoreCase(data[0])) continue;
            return data[1];
        }
        return null;
    }

    public static String getCombo(String A, String B, String C) {
        C = C == null || C.length() < 1 ? "" : C.toLowerCase();
        if (A == null || A.length() < 1 || B == null || B.length() < 1) {
            return null;
        }
        if (A.toCharArray()[0] <= B.toCharArray()[0]) {
            return comboMap.get(A + B + C);
        }
        return comboMap.get(B + A + C);
    }

    public static String[] getWood() {
        return woodArray;
    }

    public static String[] getGemstones() {
        return gemstoneArray;
    }

    public static String getWood(String category, Integer tier) {
        for (String item : woodArray) {
            String[] dataArray = item.split(",");
            if (!category.equalsIgnoreCase(dataArray[0])) continue;
            if (tier == 1) {
                return dataArray[1];
            }
            if (tier == 2) {
                return dataArray[3];
            }
            if (tier == 3) {
                return dataArray[5];
            }
            if (tier != 4) continue;
            return dataArray[7];
        }
        return null;
    }

    public static String getLeather(String category, Integer tier) {
        for (String item : leatherArray) {
            String[] dataArray = item.split(",");
            if (!category.equalsIgnoreCase(dataArray[0])) continue;
            if (tier == 1) {
                return dataArray[1];
            }
            if (tier == 2) {
                return dataArray[3];
            }
            if (tier == 3) {
                return dataArray[5];
            }
            if (tier != 4) continue;
            return dataArray[7];
        }
        return null;
    }

    public static String getCloth(String category, Integer tier) {
        for (String item : clothArray) {
            String[] dataArray = item.split(",");
            if (!category.equalsIgnoreCase(dataArray[0])) continue;
            if (tier == 1) {
                return dataArray[1];
            }
            if (tier == 2) {
                return dataArray[3];
            }
            if (tier == 3) {
                return dataArray[5];
            }
            if (tier != 4) continue;
            return dataArray[7];
        }
        return null;
    }

    public static String getMetal(String category, Integer tier) {
        for (String item : metalArray) {
            String[] dataArray = item.split(",");
            if (!category.equalsIgnoreCase(dataArray[0])) continue;
            if (tier == 1) {
                return dataArray[1];
            }
            if (tier == 2) {
                return dataArray[3];
            }
            if (tier == 3) {
                return dataArray[5];
            }
            if (tier != 4) continue;
            return dataArray[7];
        }
        return null;
    }

    public static Integer getTier(String name) {
        String[] dataArray;
        for (String data : woodArray) {
            dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return 1;
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return 2;
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return 3;
            }
            if (!name.equalsIgnoreCase(dataArray[7])) continue;
            return 4;
        }
        for (String data : leatherArray) {
            dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return 1;
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return 2;
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return 3;
            }
            if (!name.equalsIgnoreCase(dataArray[7])) continue;
            return 4;
        }
        for (String data : clothArray) {
            dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return 1;
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return 2;
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return 3;
            }
            if (!name.equalsIgnoreCase(dataArray[7])) continue;
            return 4;
        }
        for (String data : metalArray) {
            dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return 1;
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return 2;
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return 3;
            }
            if (!name.equalsIgnoreCase(dataArray[7])) continue;
            return 4;
        }
        return 0;
    }

    public static Integer getTech(String name) {
        String[] dataArray;
        for (String data : woodArray) {
            dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return Integer.parseInt(dataArray[2]);
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return Integer.parseInt(dataArray[4]);
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return Integer.parseInt(dataArray[6]);
            }
            if (!name.equalsIgnoreCase(dataArray[7])) continue;
            return Integer.parseInt(dataArray[8]);
        }
        for (String data : leatherArray) {
            dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return Integer.parseInt(dataArray[2]);
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return Integer.parseInt(dataArray[4]);
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return Integer.parseInt(dataArray[6]);
            }
            if (!name.equalsIgnoreCase(dataArray[7])) continue;
            return Integer.parseInt(dataArray[8]);
        }
        for (String data : clothArray) {
            dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return Integer.parseInt(dataArray[2]);
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return Integer.parseInt(dataArray[4]);
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return Integer.parseInt(dataArray[6]);
            }
            if (!name.equalsIgnoreCase(dataArray[7])) continue;
            return Integer.parseInt(dataArray[8]);
        }
        for (String data : metalArray) {
            dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return Integer.parseInt(dataArray[2]);
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return Integer.parseInt(dataArray[4]);
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return Integer.parseInt(dataArray[6]);
            }
            if (!name.equalsIgnoreCase(dataArray[7])) continue;
            return Integer.parseInt(dataArray[8]);
        }
        return 0;
    }

    public static String getCategory(String name) {
        String[] dataArray;
        for (String data : woodArray) {
            dataArray = data.split(",");
            if (!name.equalsIgnoreCase(dataArray[1]) && !name.equalsIgnoreCase(dataArray[3]) && !name.equalsIgnoreCase(dataArray[5]) && !name.equalsIgnoreCase(dataArray[7])) continue;
            return dataArray[0];
        }
        for (String data : leatherArray) {
            dataArray = data.split(",");
            if (!name.equalsIgnoreCase(dataArray[1]) && !name.equalsIgnoreCase(dataArray[3]) && !name.equalsIgnoreCase(dataArray[5]) && !name.equalsIgnoreCase(dataArray[7])) continue;
            return dataArray[0];
        }
        for (String data : clothArray) {
            dataArray = data.split(",");
            if (!name.equalsIgnoreCase(dataArray[1]) && !name.equalsIgnoreCase(dataArray[3]) && !name.equalsIgnoreCase(dataArray[5]) && !name.equalsIgnoreCase(dataArray[7])) continue;
            return dataArray[0];
        }
        for (String data : metalArray) {
            dataArray = data.split(",");
            if (!name.equalsIgnoreCase(dataArray[1]) && !name.equalsIgnoreCase(dataArray[3]) && !name.equalsIgnoreCase(dataArray[5]) && !name.equalsIgnoreCase(dataArray[7])) continue;
            return dataArray[0];
        }
        return "?";
    }

    public static String getType(String name) {
        String[] dataArray;
        for (String data : woodArray) {
            dataArray = data.split(",");
            if (!name.equalsIgnoreCase(dataArray[1]) && !name.equalsIgnoreCase(dataArray[3]) && !name.equalsIgnoreCase(dataArray[5]) && !name.equalsIgnoreCase(dataArray[7])) continue;
            return "plank";
        }
        for (String data : leatherArray) {
            dataArray = data.split(",");
            if (!name.equalsIgnoreCase(dataArray[1]) && !name.equalsIgnoreCase(dataArray[3]) && !name.equalsIgnoreCase(dataArray[5]) && !name.equalsIgnoreCase(dataArray[7])) continue;
            return "layer";
        }
        for (String data : clothArray) {
            dataArray = data.split(",");
            if (!name.equalsIgnoreCase(dataArray[1]) && !name.equalsIgnoreCase(dataArray[3]) && !name.equalsIgnoreCase(dataArray[5]) && !name.equalsIgnoreCase(dataArray[7])) continue;
            return "bolt";
        }
        for (String data : metalArray) {
            dataArray = data.split(",");
            if (!name.equalsIgnoreCase(dataArray[1]) && !name.equalsIgnoreCase(dataArray[3]) && !name.equalsIgnoreCase(dataArray[5]) && !name.equalsIgnoreCase(dataArray[7])) continue;
            return "ingot";
        }
        return "gemstone";
    }

    public static String getTypeFromLongName(String name) {
        for (String key : typeMap.keySet()) {
            if (!name.startsWith(key)) continue;
            return typeMap.get(key);
        }
        return "gemstone";
    }

    public static String getShortName(String name) {
        for (String key : typeMap.keySet()) {
            if (!name.startsWith(key)) continue;
            return name.substring(key.length());
        }
        if (CraftingData.getTypeFromLongName(name).equals("gemstone")) {
            return name.replaceFirst("(?:a |an )", "");
        }
        return name;
    }

    public static String find(String name) {
        if ((name = name.toLowerCase()).startsWith("jewel") || name.startsWith("fragment")) {
            return "jewel fragments";
        }
        Iterator i$ = materialArrays.iterator();
        while (i$.hasNext()) {
            String[] materials;
            for (String materialArray : materials = (String[])i$.next()) {
                String[] material = materialArray.split(",");
                if (CraftingData._matches(material[1], name)) {
                    return material[1];
                }
                if (CraftingData._matches(material[3], name)) {
                    return material[3];
                }
                if (CraftingData._matches(material[5], name)) {
                    return material[5];
                }
                if (!CraftingData._matches(material[7], name)) continue;
                return material[7];
            }
        }
        for (String gemstone : gemstoneArray) {
            String[] material = gemstone.split(",", 2);
            if (!CraftingData._matches(material[0], name.replaceFirst("^(?:a |an )", ""))) continue;
            return material[0];
        }
        return null;
    }

    private static boolean _matches(String material, String name) {
        boolean matched = true;
        material = material.toLowerCase().trim();
        name = name.toLowerCase().trim();
        String[] nameTokens = name.split(" ");
        String[] materialTokens = material.split(" ");
        for (int nameIndex = 0; nameIndex < nameTokens.length; ++nameIndex) {
            boolean nameMatch = false;
            for (int materialIndex = 0; materialIndex < materialTokens.length; ++materialIndex) {
                if (!materialTokens[materialIndex].equals(nameTokens[nameIndex])) continue;
                nameMatch = true;
                break;
            }
            if (nameMatch) continue;
            matched = false;
        }
        return matched;
    }

    static {
        materialArrays.add(woodArray);
        materialArrays.add(leatherArray);
        materialArrays.add(clothArray);
        materialArrays.add(metalArray);
        typeMap.put("a shred of ", "layer");
        typeMap.put("a layer of ", "layer");
        typeMap.put("a cut of ", "bolt");
        typeMap.put("a bolt of ", "bolt");
        typeMap.put("a stalk of ", "plank");
        typeMap.put("a plank of ", "plank");
        typeMap.put("a nugget of ", "ingot");
        typeMap.put("an ingot of ", "ingot");
        typeMap.put("a handful of ", "jewels");
        comboMap.put("AA", "Str/Spe");
        comboMap.put("AB", "Str/Dex/NR");
        comboMap.put("AC", "Dex/Agi");
        comboMap.put("AD", "Con/Dex/Health");
        comboMap.put("AE", "Str/Wil/Endurance");
        comboMap.put("AF", "Int/Wis/Wil");
        comboMap.put("AG", "Con/Int/Spirit");
        comboMap.put("AH", "Agi/Wil/Damage");
        comboMap.put("BB", "Quick/Hit");
        comboMap.put("BC", "Dex/Agi/Spe");
        comboMap.put("BD", "Int/Spe/Mana");
        comboMap.put("BE", "Con/Dex/Health");
        comboMap.put("BF", "Dex/Agi/Spe");
        comboMap.put("BG", "Quick/NR/Damage");
        comboMap.put("BH", "Spe/NR");
        comboMap.put("CC", "Agi/Health");
        comboMap.put("CD", "Dex/Agi/Spe");
        comboMap.put("CE", "Con/Wis/Spe");
        comboMap.put("CF", "Int/Spe/Mana");
        comboMap.put("CG", "Agi/Wil/Damage");
        comboMap.put("CH", "Quick/NR/Damage");
        comboMap.put("DD", "Str/Con/Wil");
        comboMap.put("DE", "Str/Dex/NR");
        comboMap.put("DF", "Hit/Mana/Spirit");
        comboMap.put("DG", "Dex/Wil");
        comboMap.put("DH", "Str/Wil/Endurance");
        comboMap.put("EE", "Int/NR");
        comboMap.put("EF", "Con/Wil");
        comboMap.put("EG", "Hit/Mana/Spirit");
        comboMap.put("EH", "Str/Con/Wil");
        comboMap.put("FF", "Int/Wis");
        comboMap.put("FG", "Int/Wis/Wil");
        comboMap.put("FH", "Con/Int/Spirit");
        comboMap.put("GG", "Con/Wis");
        comboMap.put("GH", "Con/Wis/Spe");
        comboMap.put("HH", "Int/Wis/Wil");
        comboMap.put("ADtwisted agate", "Damage/Hit");
        comboMap.put("ADsmooth chrysoberyl", "Damage/Hit");
        comboMap.put("AEsmoky quartz", "Wil/Health");
        comboMap.put("AEtwisted agate", "Wil/Health");
        comboMap.put("AFblue zircon", "Int/Hit");
        comboMap.put("AFpolished coral", "Int/Hit");
        comboMap.put("BCjasper crescent", "Dex/Hit");
        comboMap.put("BCsmooth chrysoberyl", "Dex/Hit");
        comboMap.put("BDsmoky quartz", "Spe/Damage");
        comboMap.put("BDlapis lazuli", "Spe/Damage");
        comboMap.put("CHamber droplet", "Wis/Damage");
        comboMap.put("CHlapis lazuli", "Wis/Damage");
        comboMap.put("DEjasper crescent", "Con/Hit");
        comboMap.put("DEblue zircon", "Con/Hit");
        comboMap.put("GHpolished coral", "Damage/Health");
        comboMap.put("GHamber droplet", "Damage/Health");
    }
}