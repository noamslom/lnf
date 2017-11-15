package il.co.noamsl.lostnfound.item;

import il.co.noamsl.lostnfound.eitan.FoundTable;
import il.co.noamsl.lostnfound.eitan.LostTable;
import il.co.noamsl.lostnfound.serverInterface.fake.FakeImage;

/**
 * Created by noams on 04/08/2017.
 */

public class LfItemImpl implements LfItem {
    private WSLfItem wsLfItem;
    private NoamImage fakeImage;
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

    public void setPicture(String picture) {
        wsLfItem.setPicture(picture);
    }

    @Override
    public NoamImage getMainImage() {
        if (fakeImage == null) {
            fakeImage = new FakeImage();
        }
        return fakeImage; //// FIXME: 14/11/2017  server need to fix
    }

    @Override
    public Integer getId() {
        return wsLfItem.getRecordid();
    }

    @Override
    public boolean isAFound() {
        return wsLfItem instanceof FoundTable;
    }

    @Override
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

    @Override
    public String toString() {
        return wsLfItem.toString();
    }

    public LfItemImpl(int id, String name, String description, String location, Integer owner, String picture, boolean relevant, boolean isAFound) {
        if (isAFound) {
            wsLfItem = new FoundTable(name, description, location, owner, picture,id,relevant) ;
            // FIXME: 14/11/2017 change from null , picture shouldnt be string in server
        }
        else {
            wsLfItem = new LostTable(name, description, location, owner, null,id,relevant);
        }
    }



    @Override
    public FoundTable toFoundTable() {
        //// FIXME: 14/11/2017 change picture from string and dont put null
        return (FoundTable) wsLfItem;

    }
    @Override
    public LostTable toLostTable(){
        return (LostTable) wsLfItem;
    }
}
