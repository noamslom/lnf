package il.co.noamsl.lostnfound.webService.serverInternal;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class LostTableList {
    @ElementList(inline = true, required = false)
    private List<LostTable> lostTables;

    public LostTableList() {

    }

    public List<LostTable> getLostTables() {
        return lostTables;
    }

    public void setLostTables(List<LostTable> lostTables) {
        this.lostTables = lostTables;
    }
}
