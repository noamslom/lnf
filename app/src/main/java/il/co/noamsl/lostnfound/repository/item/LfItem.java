package il.co.noamsl.lostnfound.repository.item;

import android.content.Context;
import android.graphics.drawable.Drawable;

import il.co.noamsl.lostnfound.Util;
import il.co.noamsl.lostnfound.repository.cache.Cacheable;
import il.co.noamsl.lostnfound.webService.serverInternal.FoundTable;
import il.co.noamsl.lostnfound.webService.serverInternal.LostTable;


public class LfItem implements Cacheable {
    private WSLfItem wsLfItem;
    private Drawable drawablePicture = null;

    public LfItem(WSLfItem wsLfItem) {
        this.wsLfItem = wsLfItem;
    }

    public LfItem(Integer id, String name, String description, String location, Integer owner, String picture, boolean relevant, boolean isAFound) {
        if (isAFound) {
            wsLfItem = new FoundTable(name, description, location, owner, picture, id, relevant);
        } else {
            wsLfItem = new LostTable(name, description, location, owner, picture, id, relevant);
        }
    }

    public String getName() {
        return wsLfItem.getName();
    }

    public void setName(String name) {
        wsLfItem.setName(name);
    }

    public String getDescription() {
        return wsLfItem.getDescription();
    }

    public void setDescription(String description) {
        wsLfItem.setDescription(description);
    }

    public Integer getOwner() {
        return wsLfItem.getOwner();
    }

    public void setOwner(Integer owner) {
        wsLfItem.setOwner(owner);
    }



    public String getPicture() {
        return wsLfItem.getPicture();
    }

    public Integer getId() {
        return wsLfItem.getRecordid();
    }

    public boolean isAFound() {
        return wsLfItem instanceof FoundTable;
    }

    public boolean isALost() {
        return wsLfItem instanceof LostTable;
    }

    public void setRecordid(Integer recordid) {
        wsLfItem.setRecordid(recordid);
    }

    public String getLocation() {
        return wsLfItem.getLocation();
    }

    public void setLocation(String location) {
        wsLfItem.setLocation(location);
    }

    public Boolean getRelevant() {
        return wsLfItem.getRelevant();
    }

    public void setRelevant(Boolean relevant) {
        wsLfItem.setRelevant(relevant);
    }

    public String toString() {
        return wsLfItem.toString();
    }

    public FoundTable toFoundTable() {
        return (FoundTable) wsLfItem;

    }

    public LostTable toLostTable() {
        return (LostTable) wsLfItem;
    }

    @Override
    public String getCacheId() {
        return String.valueOf(getId());
    }

    public Drawable getDrawablePicture(Context context) {
        if (drawablePicture != null)
            return drawablePicture;

        final int compressionRation = 10;
        return Util.base64ToDrawable(context.getResources(), getPicture(), compressionRation);

    }
}
