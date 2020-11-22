// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;

public class ComponentStorage
{
    private final HashMap<String, HashMap<String, AtomicInteger>> scrapsContainerMap;
    private final HashMap<String, HashMap<String, AtomicInteger>> componentContainerMap;
    private final LinkedList<String> fullContainerList;
    
    public ComponentStorage() {
        this.scrapsContainerMap = new HashMap<String, HashMap<String, AtomicInteger>>();
        this.componentContainerMap = new HashMap<String, HashMap<String, AtomicInteger>>();
        this.fullContainerList = new LinkedList<String>();
    }
    
    public void reset() {
        for (final HashMap<String, AtomicInteger> container : this.scrapsContainerMap.values()) {
            container.clear();
        }
        for (final HashMap<String, AtomicInteger> container : this.componentContainerMap.values()) {
            container.clear();
        }
        this.scrapsContainerMap.clear();
        this.componentContainerMap.clear();
    }
    
    public void calculateFull() {
        final HashMap<String, AtomicInteger> countMap = new HashMap<String, AtomicInteger>();
        this.countContainers(countMap, this.scrapsContainerMap);
        this.countContainers(countMap, this.componentContainerMap);
        for (final String container : countMap.keySet()) {
            final AtomicInteger count = countMap.get(container);
            if (container.toLowerCase().endsWith("rucksack")) {
                if (count.get() < 32) {
                    continue;
                }
                this.setFull(container, true);
            }
            else if (container.toLowerCase().endsWith("explorer")) {
                if (count.get() < 28) {
                    continue;
                }
                this.setFull(container, true);
            }
            else {
                if (!container.toLowerCase().endsWith("satchel") || count.get() < 28) {
                    continue;
                }
                this.setFull(container, true);
            }
        }
    }
    
    private void countContainers(final HashMap<String, AtomicInteger> countMap, final HashMap<String, HashMap<String, AtomicInteger>> materialContainer) {
        for (final String container : materialContainer.keySet()) {
            final HashMap<String, AtomicInteger> materialMap = materialContainer.get(container);
            for (final String material : materialMap.keySet()) {
                final AtomicInteger materialCount = materialMap.get(material);
                AtomicInteger containerCount = countMap.get(container);
                if (containerCount == null) {
                    containerCount = new AtomicInteger(0);
                    countMap.put(container, containerCount);
                }
                containerCount.addAndGet(materialCount.get());
            }
        }
    }
    
    public boolean isFull(final String container) {
        return this.fullContainerList.contains(container);
    }
    
    public void setFull(final String container, final boolean value) {
        if (value) {
            if (!this.fullContainerList.contains(container)) {
                this.fullContainerList.add(container);
            }
        }
        else {
            this.fullContainerList.remove(container);
        }
    }
    
    public int put(final String container, final String type, String material, final Integer count) {
        material = material.toLowerCase();
        HashMap<String, AtomicInteger> materialMap = null;
        if (type.equalsIgnoreCase("scraps")) {
            materialMap = this.scrapsContainerMap.get(container);
            if (materialMap == null) {
                materialMap = new HashMap<String, AtomicInteger>();
                this.scrapsContainerMap.put(container, materialMap);
            }
        }
        else {
            materialMap = this.componentContainerMap.get(container);
            if (materialMap == null) {
                materialMap = new HashMap<String, AtomicInteger>();
                this.componentContainerMap.put(container, materialMap);
            }
        }
        AtomicInteger materialCount = materialMap.get(material);
        if (materialCount == null) {
            materialCount = new AtomicInteger(0);
            materialMap.put(material, materialCount);
        }
        return materialCount.addAndGet(count);
    }
    
    public int remove(final String container, final String type, String material, int count) {
        material = material.toLowerCase();
        if (count > 0) {
            count *= -1;
        }
        HashMap<String, AtomicInteger> materialMap = null;
        if (type.equalsIgnoreCase("scraps")) {
            materialMap = this.scrapsContainerMap.get(container);
            if (materialMap == null) {
                materialMap = new HashMap<String, AtomicInteger>();
                this.scrapsContainerMap.put(container, materialMap);
            }
        }
        else {
            materialMap = this.componentContainerMap.get(container);
            if (materialMap == null) {
                materialMap = new HashMap<String, AtomicInteger>();
                this.componentContainerMap.put(container, materialMap);
            }
        }
        AtomicInteger materialCount = materialMap.get(material);
        if (materialCount == null) {
            materialCount = new AtomicInteger(0);
            materialMap.put(material, materialCount);
        }
        if (materialCount.addAndGet(count) < 0) {
            materialCount.set(0);
        }
        return materialCount.get();
    }
    
    public int getInventory(final String type, String material) {
        material = material.toLowerCase();
        HashMap<String, HashMap<String, AtomicInteger>> containerMap = null;
        if (type.equalsIgnoreCase("scraps")) {
            containerMap = this.scrapsContainerMap;
        }
        else {
            containerMap = this.componentContainerMap;
        }
        final HashMap<String, AtomicInteger> materialMap = containerMap.get("inventory");
        if (materialMap != null) {
            final AtomicInteger count = materialMap.get(material);
            if (count != null && count.get() > 0) {
                return count.get();
            }
        }
        return 0;
    }
    
    public int getContainerCount(final String container, final String materialType, final String material) {
        HashMap<String, HashMap<String, AtomicInteger>> containerMap;
        if (materialType.equalsIgnoreCase("scraps")) {
            containerMap = this.scrapsContainerMap;
        }
        else {
            containerMap = this.componentContainerMap;
        }
        final HashMap<String, AtomicInteger> materialMap = containerMap.get(container);
        final AtomicInteger count = materialMap.get(material);
        if (count != null && count.get() > 0) {
            return count.get();
        }
        return 0;
    }
    
    public String getLocation(final String type, String material) {
        material = material.toLowerCase();
        HashMap<String, HashMap<String, AtomicInteger>> containerMap = null;
        if (type.equalsIgnoreCase("scraps")) {
            containerMap = this.scrapsContainerMap;
        }
        else {
            containerMap = this.componentContainerMap;
        }
        if (containerMap != null) {
            for (final String container : containerMap.keySet()) {
                if (!container.equals("inventory")) {
                    final HashMap<String, AtomicInteger> materialMap = containerMap.get(container);
                    final AtomicInteger count = materialMap.get(material);
                    if (count != null && count.get() > 0) {
                        return container;
                    }
                    continue;
                }
            }
        }
        return null;
    }
}
