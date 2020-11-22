// 
// Decompiled by Procyon v0.5.36
// 

package crafting;

public class MaterialObject
{
    private String type;
    private int count;
    private boolean available;
    
    public String getMaterialType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public int getMaterialCount() {
        return this.count;
    }
    
    public void setCount(final int count) {
        this.count = count;
    }
    
    public boolean isAvailable() {
        return this.available;
    }
    
    public void setAvailable(final boolean available) {
        this.available = available;
    }
}
