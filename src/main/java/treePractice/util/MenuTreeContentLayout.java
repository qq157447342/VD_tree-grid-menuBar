package treePractice.util;

import com.vaadin.ui.Grid;
import treePractice.entity.MenuTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wumk124866 on 2018/6/15.
 */
public class MenuTreeContentLayout implements PageGrid.GridDataSource {


    List<MenuTree> menuTreeList = new ArrayList<>();

    public List<MenuTree> getMenuTreeList() {
        return menuTreeList;
    }

    public void setMenuTreeList(List<MenuTree> menuTreeList) {
        this.menuTreeList = menuTreeList;
    }

    public MenuTreeContentLayout(List<MenuTree> menuTreeList) {
        this.menuTreeList = menuTreeList;
    }

    @Override
    public int getTotalCount(Grid grid){

        return menuTreeList.size();
    }

    @Override
    public void refreshGridData(Grid grid, int totalSize, int currentPageNumber, int numberPerPage){

        List<MenuTree> list1 = getGridLists(totalSize,currentPageNumber,numberPerPage);
        //System.out.println(list1.size()+"每页实际显示的条数");
        grid.setItems(list1);
    }

    public List<MenuTree> getGridLists(int totalSize,int currentPageNumber,int numberPerPage){
        //System.out.println("总数，当前页数，每页条数分别为："+totalSize+"==="+currentPageNumber+"--------"+numberPerPage);
        List<MenuTree> menuTrees = new ArrayList<>();
        if(currentPageNumber*numberPerPage >= totalSize){
            menuTrees.addAll(menuTreeList.subList((currentPageNumber-1)*numberPerPage,totalSize));
        }else{
            menuTrees.addAll(menuTreeList.subList((currentPageNumber-1)*numberPerPage,currentPageNumber*numberPerPage));
        }

        return menuTrees;
    }
}
