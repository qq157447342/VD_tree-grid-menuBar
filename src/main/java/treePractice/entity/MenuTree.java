package treePractice.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wumk124866 on 2018/6/13.
 */
public class MenuTree {

    private String id ;

    private String menuName;

    private Integer level;

    private String createTime;

    private Integer order;

    private MenuTree parentMenu;

    private List<MenuTree>  childrenList = new ArrayList<>();

    public MenuTree() {
    }

    public MenuTree(String id, String menuName, Integer level, String createTime, Integer order, MenuTree parentMenu) {
        this.id = id;
        this.menuName = menuName;
        this.level = level;
        this.createTime = createTime;
        this.order = order;
        this.parentMenu = parentMenu;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public MenuTree getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(MenuTree parentMenu) {
        this.parentMenu = parentMenu;
    }

    public List<MenuTree> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<MenuTree> childrenList) {
        this.childrenList = childrenList;
    }

    public boolean hasChildren(){
        return this.getChildrenList().size()==0?false:true;
    }
}
