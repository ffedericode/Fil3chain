package cs.scrs.miner.models;

/**
 * Created by Christian on 11/07/2016.
 */
public class WidgetModel {

    String type;
    String name;
    Integer page=0;

    public WidgetModel() {
    }

    public WidgetModel(String type, String name, Integer page) {
        this.type = type;
        this.name = name;
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
