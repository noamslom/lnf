package il.co.noamsl.lostnfound.eitan;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class FoundTableList {
    @ElementList(inline = true, required = false)
    private List<FoundTable> foundTables;

    public FoundTableList() {

    }

    public List<FoundTable> getFoundTables() {
        return foundTables;
    }

    public void setFoundTables(List<FoundTable> foundTables) {
        this.foundTables = foundTables;
    }
}
