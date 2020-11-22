// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashMap;

public class CraftingData
{
    private static final HashMap<String, String> typeMap;
    private static final HashMap<String, String> comboMap;
    private static final String[] woodArray;
    private static final String[] leatherArray;
    private static final String[] clothArray;
    private static final String[] metalArray;
    private static final String[] gemstoneArray;
    private static final LinkedList<String[]> materialArrays;
    
    public static String getGemstoneType(final String name) {
        for (final String item : CraftingData.gemstoneArray) {
            final String[] data = item.split(",", 2);
            if (data != null && data.length == 2 && data[0] != null && data[1] != null && name.equalsIgnoreCase(data[0])) {
                return data[1];
            }
        }
        return null;
    }
    
    public static String getCombo(final String A, final String B, String C) {
        if (C == null || C.length() < 1) {
            C = "";
        }
        else {
            C = C.toLowerCase();
        }
        if (A == null || A.length() < 1 || B == null || B.length() < 1) {
            return null;
        }
        if (A.toCharArray()[0] <= B.toCharArray()[0]) {
            return CraftingData.comboMap.get(A + B + C);
        }
        return CraftingData.comboMap.get(B + A + C);
    }
    
    public static String[] getWood() {
        return CraftingData.woodArray;
    }
    
    public static String[] getGemstones() {
        return CraftingData.gemstoneArray;
    }
    
    public static String getWood(final String category, final Integer tier) {
        for (final String item : CraftingData.woodArray) {
            final String[] dataArray = item.split(",");
            if (category.equalsIgnoreCase(dataArray[0])) {
                if (tier == 1) {
                    return dataArray[1];
                }
                if (tier == 2) {
                    return dataArray[3];
                }
                if (tier == 3) {
                    return dataArray[5];
                }
                if (tier == 4) {
                    return dataArray[7];
                }
            }
        }
        return null;
    }
    
    public static String getLeather(final String category, final Integer tier) {
        for (final String item : CraftingData.leatherArray) {
            final String[] dataArray = item.split(",");
            if (category.equalsIgnoreCase(dataArray[0])) {
                if (tier == 1) {
                    return dataArray[1];
                }
                if (tier == 2) {
                    return dataArray[3];
                }
                if (tier == 3) {
                    return dataArray[5];
                }
                if (tier == 4) {
                    return dataArray[7];
                }
            }
        }
        return null;
    }
    
    public static String getCloth(final String category, final Integer tier) {
        for (final String item : CraftingData.clothArray) {
            final String[] dataArray = item.split(",");
            if (category.equalsIgnoreCase(dataArray[0])) {
                if (tier == 1) {
                    return dataArray[1];
                }
                if (tier == 2) {
                    return dataArray[3];
                }
                if (tier == 3) {
                    return dataArray[5];
                }
                if (tier == 4) {
                    return dataArray[7];
                }
            }
        }
        return null;
    }
    
    public static String getMetal(final String category, final Integer tier) {
        for (final String item : CraftingData.metalArray) {
            final String[] dataArray = item.split(",");
            if (category.equalsIgnoreCase(dataArray[0])) {
                if (tier == 1) {
                    return dataArray[1];
                }
                if (tier == 2) {
                    return dataArray[3];
                }
                if (tier == 3) {
                    return dataArray[5];
                }
                if (tier == 4) {
                    return dataArray[7];
                }
            }
        }
        return null;
    }
    
    public static Integer getTier(final String name) {
        for (final String data : CraftingData.woodArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return 1;
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return 2;
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return 3;
            }
            if (name.equalsIgnoreCase(dataArray[7])) {
                return 4;
            }
        }
        for (final String data : CraftingData.leatherArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return 1;
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return 2;
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return 3;
            }
            if (name.equalsIgnoreCase(dataArray[7])) {
                return 4;
            }
        }
        for (final String data : CraftingData.clothArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return 1;
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return 2;
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return 3;
            }
            if (name.equalsIgnoreCase(dataArray[7])) {
                return 4;
            }
        }
        for (final String data : CraftingData.metalArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return 1;
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return 2;
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return 3;
            }
            if (name.equalsIgnoreCase(dataArray[7])) {
                return 4;
            }
        }
        return 0;
    }
    
    public static Integer getTech(final String name) {
        for (final String data : CraftingData.woodArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return Integer.parseInt(dataArray[2]);
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return Integer.parseInt(dataArray[4]);
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return Integer.parseInt(dataArray[6]);
            }
            if (name.equalsIgnoreCase(dataArray[7])) {
                return Integer.parseInt(dataArray[8]);
            }
        }
        for (final String data : CraftingData.leatherArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return Integer.parseInt(dataArray[2]);
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return Integer.parseInt(dataArray[4]);
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return Integer.parseInt(dataArray[6]);
            }
            if (name.equalsIgnoreCase(dataArray[7])) {
                return Integer.parseInt(dataArray[8]);
            }
        }
        for (final String data : CraftingData.clothArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return Integer.parseInt(dataArray[2]);
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return Integer.parseInt(dataArray[4]);
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return Integer.parseInt(dataArray[6]);
            }
            if (name.equalsIgnoreCase(dataArray[7])) {
                return Integer.parseInt(dataArray[8]);
            }
        }
        for (final String data : CraftingData.metalArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1])) {
                return Integer.parseInt(dataArray[2]);
            }
            if (name.equalsIgnoreCase(dataArray[3])) {
                return Integer.parseInt(dataArray[4]);
            }
            if (name.equalsIgnoreCase(dataArray[5])) {
                return Integer.parseInt(dataArray[6]);
            }
            if (name.equalsIgnoreCase(dataArray[7])) {
                return Integer.parseInt(dataArray[8]);
            }
        }
        return 0;
    }
    
    public static String getCategory(final String name) {
        for (final String data : CraftingData.woodArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1]) || name.equalsIgnoreCase(dataArray[3]) || name.equalsIgnoreCase(dataArray[5]) || name.equalsIgnoreCase(dataArray[7])) {
                return dataArray[0];
            }
        }
        for (final String data : CraftingData.leatherArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1]) || name.equalsIgnoreCase(dataArray[3]) || name.equalsIgnoreCase(dataArray[5]) || name.equalsIgnoreCase(dataArray[7])) {
                return dataArray[0];
            }
        }
        for (final String data : CraftingData.clothArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1]) || name.equalsIgnoreCase(dataArray[3]) || name.equalsIgnoreCase(dataArray[5]) || name.equalsIgnoreCase(dataArray[7])) {
                return dataArray[0];
            }
        }
        for (final String data : CraftingData.metalArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1]) || name.equalsIgnoreCase(dataArray[3]) || name.equalsIgnoreCase(dataArray[5]) || name.equalsIgnoreCase(dataArray[7])) {
                return dataArray[0];
            }
        }
        return "?";
    }
    
    public static String getType(final String name) {
        for (final String data : CraftingData.woodArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1]) || name.equalsIgnoreCase(dataArray[3]) || name.equalsIgnoreCase(dataArray[5]) || name.equalsIgnoreCase(dataArray[7])) {
                return "plank";
            }
        }
        for (final String data : CraftingData.leatherArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1]) || name.equalsIgnoreCase(dataArray[3]) || name.equalsIgnoreCase(dataArray[5]) || name.equalsIgnoreCase(dataArray[7])) {
                return "layer";
            }
        }
        for (final String data : CraftingData.clothArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1]) || name.equalsIgnoreCase(dataArray[3]) || name.equalsIgnoreCase(dataArray[5]) || name.equalsIgnoreCase(dataArray[7])) {
                return "bolt";
            }
        }
        for (final String data : CraftingData.metalArray) {
            final String[] dataArray = data.split(",");
            if (name.equalsIgnoreCase(dataArray[1]) || name.equalsIgnoreCase(dataArray[3]) || name.equalsIgnoreCase(dataArray[5]) || name.equalsIgnoreCase(dataArray[7])) {
                return "ingot";
            }
        }
        return "gemstone";
    }
    
    public static String getTypeFromLongName(final String name) {
        for (final String key : CraftingData.typeMap.keySet()) {
            if (name.startsWith(key)) {
                return CraftingData.typeMap.get(key);
            }
        }
        return "gemstone";
    }
    
    public static String getShortName(final String name) {
        for (final String key : CraftingData.typeMap.keySet()) {
            if (name.startsWith(key)) {
                return name.substring(key.length());
            }
        }
        if (getTypeFromLongName(name).equals("gemstone")) {
            return name.replaceFirst("(?:a |an )", "");
        }
        return name;
    }
    
    public static String find(String name) {
        name = name.toLowerCase();
        if (name.startsWith("jewel") || name.startsWith("fragment")) {
            return "jewel fragments";
        }
        for (final String[] arr$ : CraftingData.materialArrays) {
            final String[] materials = arr$;
            for (final String materialArray : arr$) {
                final String[] material = materialArray.split(",");
                if (_matches(material[1], name)) {
                    return material[1];
                }
                if (_matches(material[3], name)) {
                    return material[3];
                }
                if (_matches(material[5], name)) {
                    return material[5];
                }
                if (_matches(material[7], name)) {
                    return material[7];
                }
            }
        }
        for (final String gemstone : CraftingData.gemstoneArray) {
            // This might be it setting array length to 2 instead of 3 on gems.
            final String[] material2 = gemstone.split(",", 3);
            if (_matches(material2[0], name.replaceFirst("^(?:a |an )", ""))) {
                return material2[0];
            }
        }
        return null;
    }
    
    private static boolean _matches(String material, String name) {
        boolean matched = true;
        material = material.toLowerCase().trim();
        name = name.toLowerCase().trim();
        final String[] nameTokens = name.split(" ");
        final String[] materialTokens = material.split(" ");
        for (int nameIndex = 0; nameIndex < nameTokens.length; ++nameIndex) {
            boolean nameMatch = false;
            for (int materialIndex = 0; materialIndex < materialTokens.length; ++materialIndex) {
                if (materialTokens[materialIndex].equals(nameTokens[nameIndex])) {
                    nameMatch = true;
                    break;
                }
            }
            if (!nameMatch) {
                matched = false;
            }
        }
        return matched;
    }
    
    static {
        typeMap = new HashMap<String, String>();
        comboMap = new HashMap<String, String>();
        woodArray = new String[] { "A,Thick Oak,3,Thorned Acacia,35,Irongrowth,67,Dragonbone,99", "B,Smoky Birch,6,Scarlet Bole,38,Whip-wing Beech,70,Windweald Timber,102", "C,Swirled Teak,11,Fine Rosewood,43,Blooming Laurel,75,Elysian Pearwood,107", "D,Applewood,14,Giant Willowreed,46,Sedgewood,78,Swampskein Reed,110", "E,Plain Beech,19,Solid Mahogany,51,Dowsing Oak,83,Unburning Oak,115", "F,Golden Yew,22,Scented Cedar,54,Fragrant Acanthus,86,Murmuring Ashbranch,118", "G,Pinewood,27,Ancient Elm,59,Whiteweald,91,Thrice-chanted Yew,123", "H,Dark Hickory,30,Stained Cypress,62,Twisted Hemlock,94,Wyrding Willow,126" };
        leatherArray = new String[] { "A,Toughened Leather,2,Gnarled Bruteskin,34,Blood-grain Leather,66, ,0", "B,Oiled Calfskin,7,Ermine Pelt,39,Moon-brushed Vellum,71,Wyrmwing Lamina,103", "C,Softened Hide,10,Supple Gromhide,42,Lustrous Silver Pelt,74,Ether Membrane,106", "D,Buckskin,15,Copper-grain Leather,47,Phytoderm Husk,79,Chimeric Vellum,111", "E,Rough Shagreen,18,Cuirboulli,50,Ruby-dusted Leather,82, ,0", "F,Embossed Leather,23,Scented Whitehide,55,Spored Lamella,87,Demon-slough,119", "G,Soft Suede,26,Blush Vellum,58,Sanded Scales,90, ,0", "H,Sand-baked Hide,31,Charred Scales,63,Cinderhide,95,Hoarfrost Scrap,127" };
        clothArray = new String[] { "A,Ramie Fiber,1,Hunter's Fleece,33,Royal Satin,65,Scornshroud,97", "B,Gypsy Linen,8,Glossy Angora,40,Dewstrand,72,Sidhe Spinnings,104", "C,Ashcloth,9,Dark Satin,41,Spindrift Felt,73,Windseal Weave,105", "D,Ryven Felt,16,Bucksheet,48,Sable Silk,80,Tidal Fiber,112", "E,Mhaldryn Wool,17,Sandflax,49,Pearlsheet,81,Woven Phlogiston,113", "F,Crushed Velvet,24,Argent Mesh,56,Sovereign Silk,88,Undercroft Wrappings,120", "G,Light Taffeta,25,Lustrous Silk,57,Angelic Gossamer,89,Dawnsveil,121", "H,Cashmere,32,Rich Velour,64,Rich Damask,96,Webweave,128" };
        metalArray = new String[] { "A,Wrought Iron,4,Tempered Steel,36,Golem Shard,68, ,0", "B,Brass,5,Pure Electrum,37,Mirrorsteel,69, ,0", "C,Bronze,12,Palladium,44,Fey Mithril,76, ,0", "D,Coarse Gold,13,Damascus Steel,45,Galvanic Alloy,77,Igneous Exolith,109", "E,Titanium,20,Adamantine,52,Limpid Iron,84,Living Steel,116", "F,Tin,21,Cold Iron,53,Sangrolith,85, ,0", "G,Rough Silver,28,Onicite,60,Cerulean Ferrum,92, ,0", "H,Dull Platinum,29,Obdurium,61,Siren-Steel,93,Meteor Iron,125" };
        gemstoneArray = new String[] { "blended tourmaline,30/4", "deep emerald,30/4", "delicate tanzanite,30/4", "faded amethyst,30/4", "jade inlay,30/4", "pale iolite,30/4", "pale ruby,30/4", "pink spinel,30/4", "pure aquamarine,30/4", "pure topaz,30/4", "sardonyx inlay,30/4", "soft citrine,30/4", "bloodstone inlay,60/8", "champagne diamond,60/8", "demantoid garnet,60/8", "fire opal,60/8", "moonstone inlay,60/8", "rich alexandrite,60/8", "star sapphire,60/8", "swirled ametrine,60/8","amber droplet,Altering", "blue zircon,Altering", "jasper crescent,Altering", "lapis lazuli,Altering", "polished coral,Altering", "smoky quartz,Altering", "smooth chrysoberyl,Altering", "twisted agate,Altering" };
        (materialArrays = new LinkedList<String[]>()).add(CraftingData.woodArray);
        CraftingData.materialArrays.add(CraftingData.leatherArray);
        CraftingData.materialArrays.add(CraftingData.clothArray);
        CraftingData.materialArrays.add(CraftingData.metalArray);
        CraftingData.materialArrays.add(CraftingData.gemstoneArray);
        CraftingData.typeMap.put("a shred of ", "layer");
        CraftingData.typeMap.put("a layer of ", "layer");
        CraftingData.typeMap.put("a cut of ", "bolt");
        CraftingData.typeMap.put("a bolt of ", "bolt");
        CraftingData.typeMap.put("a stalk of ", "plank");
        CraftingData.typeMap.put("a plank of ", "plank");
        CraftingData.typeMap.put("a nugget of ", "ingot");
        CraftingData.typeMap.put("an ingot of ", "ingot");
        CraftingData.typeMap.put("a handful of ", "jewels");
        CraftingData.comboMap.put("AA", "Str/Spe");
        CraftingData.comboMap.put("AB", "Str/Dex/NR");
        CraftingData.comboMap.put("AC", "Dex/Agi");
        CraftingData.comboMap.put("AD", "Con/Dex/Health");
        CraftingData.comboMap.put("AE", "Str/Wil/Endurance");
        CraftingData.comboMap.put("AF", "Int/Wis/Wil");
        CraftingData.comboMap.put("AG", "Con/Int/Spirit");
        CraftingData.comboMap.put("AH", "Agi/Wil/Damage");
        CraftingData.comboMap.put("BB", "Quick/Hit");
        CraftingData.comboMap.put("BC", "Dex/Agi/Spe");
        CraftingData.comboMap.put("BD", "Int/Spe/Mana");
        CraftingData.comboMap.put("BE", "Con/Dex/Health");
        CraftingData.comboMap.put("BF", "Dex/Agi/Spe");
        CraftingData.comboMap.put("BG", "Quick/NR/Damage");
        CraftingData.comboMap.put("BH", "Spe/NR");
        CraftingData.comboMap.put("CC", "Agi/Health");
        CraftingData.comboMap.put("CD", "Dex/Agi/Spe");
        CraftingData.comboMap.put("CE", "Con/Wis/Spe");
        CraftingData.comboMap.put("CF", "Int/Spe/Mana");
        CraftingData.comboMap.put("CG", "Agi/Wil/Damage");
        CraftingData.comboMap.put("CH", "Quick/NR/Damage");
        CraftingData.comboMap.put("DD", "Str/Con/Wil");
        CraftingData.comboMap.put("DE", "Str/Dex/NR");
        CraftingData.comboMap.put("DF", "Hit/Mana/Spirit");
        CraftingData.comboMap.put("DG", "Dex/Wil");
        CraftingData.comboMap.put("DH", "Str/Wil/Endurance");
        CraftingData.comboMap.put("EE", "Int/NR");
        CraftingData.comboMap.put("EF", "Con/Wil");
        CraftingData.comboMap.put("EG", "Hit/Mana/Spirit");
        CraftingData.comboMap.put("EH", "Str/Con/Wil");
        CraftingData.comboMap.put("FF", "Int/Wis");
        CraftingData.comboMap.put("FG", "Int/Wis/Wil");
        CraftingData.comboMap.put("FH", "Con/Int/Spirit");
        CraftingData.comboMap.put("GG", "Con/Wis");
        CraftingData.comboMap.put("GH", "Con/Wis/Spe");
        CraftingData.comboMap.put("HH", "Int/Wis/Wil");
        CraftingData.comboMap.put("ADtwisted agate", "Damage/Hit");
        CraftingData.comboMap.put("ADsmooth chrysoberyl", "Damage/Hit");
        CraftingData.comboMap.put("AEsmoky quartz", "Wil/Health");
        CraftingData.comboMap.put("AEtwisted agate", "Wil/Health");
        CraftingData.comboMap.put("AFblue zircon", "Int/Hit");
        CraftingData.comboMap.put("AFpolished coral", "Int/Hit");
        CraftingData.comboMap.put("BCjasper crescent", "Dex/Hit");
        CraftingData.comboMap.put("BCsmooth chrysoberyl", "Dex/Hit");
        CraftingData.comboMap.put("BDsmoky quartz", "Spe/Damage");
        CraftingData.comboMap.put("BDlapis lazuli", "Spe/Damage");
        CraftingData.comboMap.put("CHamber droplet", "Wis/Damage");
        CraftingData.comboMap.put("CHlapis lazuli", "Wis/Damage");
        CraftingData.comboMap.put("DEjasper crescent", "Con/Hit");
        CraftingData.comboMap.put("DEblue zircon", "Con/Hit");
        CraftingData.comboMap.put("GHpolished coral", "Damage/Health");
        CraftingData.comboMap.put("GHamber droplet", "Damage/Health");
    }
}
