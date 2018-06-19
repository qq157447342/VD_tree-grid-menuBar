package treePractice;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import sun.reflect.generics.tree.*;
import treePractice.entity.MenuTree;
import treePractice.entity.TestObject;
import treePractice.form.MenuGridForm;
import treePractice.util.DemoContentLayout;
import treePractice.util.MenuTreeContentLayout;
import treePractice.util.PageGrid;
import treePractice.util.WindowCustom;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    List<MenuTree> menuList = new ArrayList<>();
    List<MenuTree> menuListNoOrder = new ArrayList<>();
    List<MenuTree> allItem = new ArrayList<>();
    PageGrid<MenuTree> grid = new PageGrid<MenuTree>();
    //创建菜单树
    Tree<String> tree = new Tree();
    @Override
    protected void init(VaadinRequest vaadinRequest) {

        final VerticalLayout vlayout = new VerticalLayout();
        final HorizontalLayout hLayout = new HorizontalLayout();



        //设置树宽度
        tree.setWidth("300");
        //设置树的数据集
        TreeDataProvider<String> dataProviderPro = (TreeDataProvider<String>) tree.getDataProvider();
        TreeData<String> dataPro = dataProviderPro.getTreeData();
        //准备菜单的数据集
        menuList.addAll(getMenuTree());
        menuListNoOrder.addAll(menuList);
        //将准备好得数据集放入树的数据集中
        recursionSetData(dataPro,null,menuList);
        //获取当前树中的所有数据（包含所有后代）
        getAllItem(allItem,menuList);



        grid.addColumn(MenuTree::getMenuName).setId("menuName");
        grid.addColumn(MenuTree::getId).setId("id");
        grid.addColumn(MenuTree::getLevel).setId("level");
        grid.addColumn(MenuTree::getCreateTime).setId("createTime");
        grid.setHeightByRows(10);
        grid.setColumns("id","menuName","level","createTime");
        grid.setColumnOrder("id","menuName","level","createTime");
        grid.getColumn("id").setCaption("菜单id");
        grid.getColumn("menuName").setCaption("菜单名称");
        grid.getColumn("level").setCaption("菜单级别");
        grid.getColumn("createTime").setCaption("创建日期");
//        grid.getColumn("parentMenu").setCaption("父菜单");

        grid.setWidth("1300");
        MenuTree[]  allMenuTrees= new MenuTree[allItem.size()];
        MenuTreeContentLayout menuTreeContentLayout = new MenuTreeContentLayout(allItem);
        grid.setGridDataSource(menuTreeContentLayout);
        menuTreeContentLayout.refreshGridData(grid,menuTreeContentLayout.getTotalCount(grid),grid.getCurrentPageNumber(),grid.getNumberPerPage());
        grid.getEditor().setEnabled(true);
        for (Grid.Column<MenuTree, ?> c : grid.getColumns()) {
            c.setHidable(true);
        }
//        grid.setItems(allItem.toArray(allMenuTrees));
        grid.setSelectionMode(Grid.SelectionMode.MULTI);


        //建立表格上方的按钮菜单栏
        MenuBar menuBar = new MenuBar();
        MenuBar.MenuItem menuItem = menuBar.addItem("新增",VaadinIcons.ADD_DOCK,new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                Set<String> treeSelected = tree.getSelectedItems();
                MenuTree menuTree = allItem.get(allItem.size()-1);
                MenuGridForm menuGridForm = new MenuGridForm();
                //设置菜单id默认值，自动生成，无法更改
                String lastMenuId = menuListNoOrder.get(menuListNoOrder.size()-1).getId();
                String newMenuId = (Integer.valueOf(lastMenuId.substring(lastMenuId.length()-2)) + 1) < 10 ? "menu0" + (Integer.valueOf(lastMenuId.substring(lastMenuId.length()-2)) + 1): "menu" + (Integer.valueOf(lastMenuId.substring(lastMenuId.length()-2)) + 1);
                menuGridForm.getTextFieldId().setValue(newMenuId);
                menuGridForm.getTextFieldId().setReadOnly(true);
                //上级菜单为空，无法更改
                menuGridForm.getTextFieldParent().setReadOnly(true);
                //菜单级别为1，无法更改
                menuGridForm.getTextFieldLevel().setValue("1");
                menuGridForm.getTextFieldLevel().setReadOnly(true);
                //菜单创建时间默认为当天日期，自动生成无法更改
                Date dt=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                menuGridForm.getTextFieldCreateTime().setValue(sdf.format(dt));
                menuGridForm.getTextFieldCreateTime().setReadOnly(true);

//                GridLayout layout = menuGridForm.getGridLayout(menuTree);
                Window window = new Window("新增",menuGridForm.getGridLayout(menuTree));
                //是否可拖拽变大变小
                window.setResizable(false);
                window.setModal(true);
                window.center();
                addWindow(window);

                menuGridForm.getButtonSave().addClickListener(clickEvent -> {
                    String menuId = menuGridForm.getTextFieldId().getValue();
                    String menuName = menuGridForm.getTextFieldName().getValue();
                    String menuLevel = menuGridForm.getTextFieldLevel().getValue();
                    String menuCreateTime = menuGridForm.getTextFieldCreateTime().getValue();
                    String menuOrder = menuGridForm.getTextFieldOrder().getValue();
                    String menuParent = menuGridForm.getTextFieldParent().getValue();

                    if(menuName == null || menuName.equals("") || menuName.length()==0){
                        Notification notification = new Notification("【菜单名称】不能为空", Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.MIDDLE_CENTER);
                        notification.show(Page.getCurrent());
                        return;
                    }
                    for(int i = 0 ; i < allItem.size();i++){
                        if(menuName.equals(allItem.get(i).getMenuName())){
                            Notification notification = new Notification("菜单名称重复请重新输入", Notification.Type.ERROR_MESSAGE);
                            notification.setPosition(Position.MIDDLE_CENTER);
                            notification.show(Page.getCurrent());
                            return;
                        }
                    }

                    String positiveIntegerRegular = "^[0-9]*[1-9][0-9]*$";
                    if(!menuOrder.matches(positiveIntegerRegular)){
                        Notification notification = new Notification("【菜单顺序】只能输入正整数", Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.MIDDLE_CENTER);
                        notification.show(Page.getCurrent());
                        return;
                    };
                    MenuTree addMenu = new MenuTree();
                    addMenu.setId(menuId);
                    addMenu.setOrder(Integer.valueOf(menuOrder));
                    addMenu.setMenuName(menuName);
                    addMenu.setParentMenu(null);
                    addMenu.setCreateTime(menuCreateTime);
                    addMenu.setLevel(Integer.valueOf(menuLevel));

                    menuList.add(addMenu);
                    menuListNoOrder.add(addMenu);
                    Collections.sort(menuList, new Comparator<MenuTree>() {
                        @Override
                        public int compare(MenuTree o1, MenuTree o2) {
                            if(o1.getOrder() >= o2.getOrder()) {
                                return 1;
                            }
                            else {
                                return -1;
                            }
                        }
                    });
                    dataPro.clear();
                    recursionSetData(dataPro,null,menuList);
                    tree.setTreeData(dataPro);
                    allItem.add(addMenu);
                    Collections.sort(allItem, new Comparator<MenuTree>() {
                        @Override
                        public int compare(MenuTree o1, MenuTree o2) {
                            return o1.getMenuName().compareTo(o1.getMenuName());
                        }
                    });
                    MenuTreeContentLayout menuTreeContentLayout = new MenuTreeContentLayout(allItem);
                    grid.setGridDataSource(menuTreeContentLayout);
                    menuTreeContentLayout.refreshGridData(grid,menuTreeContentLayout.getTotalCount(grid),grid.getCurrentPageNumber(),grid.getNumberPerPage());
                    grid.refreshGrid();
//                    grid.setItems(allItem.toArray(new MenuTree[allItem.size()]));
                    removeWindow(window);
                });
                menuGridForm.getButtonCencel().addClickListener(clickEvent -> {
                    removeWindow(window);
                });
            }
        });
        menuBar.addItem("新增下级",VaadinIcons.EDIT,new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                Set<MenuTree> menuTreeSet = grid.getSelectedItems();
                if(menuTreeSet.size() != 1){
                    Notification notification = new Notification("请选中一条数据新增", Notification.Type.ERROR_MESSAGE);
                    notification.setPosition(Position.MIDDLE_CENTER);
                    notification.show(Page.getCurrent());
                    return;
                }
                MenuTree menuTree = grid.getSelectedItems().iterator().next();

                MenuGridForm menuGridForm = new MenuGridForm();
                //设置菜单id默认值，自动生成，无法更改
                String lastMenuId = menuTree.getId();

                String newMenuId = (menuTree.getChildrenList().size() + 1) < 10 ?lastMenuId + 0 + (menuTree.getChildrenList().size() + 1) :lastMenuId + (menuTree.getChildrenList().size() + 1);
                menuGridForm.getTextFieldId().setValue(newMenuId);
                menuGridForm.getTextFieldId().setReadOnly(true);
                //上级菜单被选中菜单，无法更改
                menuGridForm.getTextFieldParent().setValue(menuTree.getMenuName());
                menuGridForm.getTextFieldParent().setReadOnly(true);
                //菜单级别为选中菜单级别+1，无法更改
                menuGridForm.getTextFieldLevel().setValue((menuTree.getLevel() + 1) + "");
                menuGridForm.getTextFieldLevel().setReadOnly(true);
                //菜单创建时间默认为当天日期，自动生成无法更改
                Date dt=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                menuGridForm.getTextFieldCreateTime().setValue(sdf.format(dt));
                menuGridForm.getTextFieldCreateTime().setReadOnly(true);

//                GridLayout layout = menuGridForm.getGridLayout(menuTree);
//                Window window = new Window("新增下级",layout);
                Window window = new Window("新增下级",menuGridForm.getGridLayout(menuTree));
                window.setResizable(false);
                window.setModal(true);
                window.center();

                addWindow(window);

                menuGridForm.getButtonSave().addClickListener(clickEvent -> {
                    String menuId = menuGridForm.getTextFieldId().getValue();
                    String menuName = menuGridForm.getTextFieldName().getValue();
                    String menuLevel = menuGridForm.getTextFieldLevel().getValue();
                    String menuCreateTime = menuGridForm.getTextFieldCreateTime().getValue();
                    String menuOrder = menuGridForm.getTextFieldOrder().getValue();
                    String menuParent = menuGridForm.getTextFieldParent().getValue();

                    if(menuName == null || menuName.equals("") || menuName.length()==0){
                        Notification notification = new Notification("【菜单名称】不能为空", Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.MIDDLE_CENTER);
                        notification.show(Page.getCurrent());
                        return;
                    }
                    for(int i = 0 ; i < allItem.size();i++){
                        if(menuName.equals(allItem.get(i).getMenuName())){
                            Notification notification = new Notification("菜单名称重复请重新输入", Notification.Type.ERROR_MESSAGE);
                            notification.setPosition(Position.MIDDLE_CENTER);
                            notification.show(Page.getCurrent());
                            return;
                        }
                    }

                    String positiveIntegerRegular = "^[0-9]*[1-9][0-9]*$";
                    if(!menuOrder.matches(positiveIntegerRegular)){
                        Notification notification = new Notification("【菜单顺序】只能输入正整数", Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.MIDDLE_CENTER);
                        notification.show(Page.getCurrent());
                        return;
                    };
                    MenuTree addMenu = new MenuTree();
                    addMenu.setId(menuId);
                    addMenu.setOrder(Integer.valueOf(menuOrder));
                    addMenu.setMenuName(menuName);
                    addMenu.setParentMenu(null);
                    addMenu.setCreateTime(menuCreateTime);
                    addMenu.setLevel(Integer.valueOf(menuLevel));

                    if(menuTree.hasChildren()){
                        menuTree.getChildrenList().add(addMenu);
                    }else{
                        List<MenuTree> children = new ArrayList<>();
                        children.add(addMenu);
                        menuTree.setChildrenList(children);
                    }

                    menuList = addChild(menuList,addMenu);

                    Collections.sort(menuList, new Comparator<MenuTree>() {
                        @Override
                        public int compare(MenuTree o1, MenuTree o2) {
                            if(o1.getOrder() >= o2.getOrder()) {
                                return 1;
                            }
                            else {
                                return -1;
                            }
                        }
                    });
                    dataPro.clear();
                    recursionSetData(dataPro,null,menuList);
                    tree.setTreeData(dataPro);
                    allItem.add(addMenu);
                    Collections.sort(allItem, new Comparator<MenuTree>() {
                        @Override
                        public int compare(MenuTree o1, MenuTree o2) {
                            return o1.getMenuName().compareTo(o1.getMenuName());
                        }
                    });
                    MenuTreeContentLayout menuTreeContentLayout = new MenuTreeContentLayout(allItem);
                    grid.setGridDataSource(menuTreeContentLayout);
                    menuTreeContentLayout.refreshGridData(grid,menuTreeContentLayout.getTotalCount(grid),grid.getCurrentPageNumber(),grid.getNumberPerPage());
                    grid.refreshGrid();
//                    grid.setItems(allItem.toArray(new MenuTree[allItem.size()]));
                    removeWindow(window);
                });
                menuGridForm.getButtonCencel().addClickListener(clickEvent -> {
                    removeWindow(window);
                });


            }
        });
        menuBar.addItem("编辑",VaadinIcons.ADJUST,new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
//                GridLayout layout = new MenuGridForm().getGridLayout();

//                Window window = new Window("测试",layout);
//
//                window.center();
//                addWindow(window);
                System.out.println("++++++++++++++++++++++++++++++++++++");
            }
        });

        MenuBar.Command cmdDelete = selectItem -> {
            Set<MenuTree> menuTreeSet = grid.getSelectedItems();
            if(menuTreeSet.size() < 1){
                WindowCustom windowNotification = new WindowCustom(this,"notification","请选中一条数据删除");
                windowNotification.center();
                addWindow(windowNotification);
                return;
            }
            WindowCustom windowConfirm = new WindowCustom(this,"confirm","确定要删除吗");
            windowConfirm.getButtonYes().addClickListener(clickEvent -> {
               deleteMenuTree(new ArrayList(grid.getSelectedItems()));
                removeWindow(windowConfirm);
            });
            windowConfirm.center();
            addWindow(windowConfirm);
        };
        menuBar.addItem("删除",VaadinIcons.DEL_A,cmdDelete);
        MenuBar.MenuItem test = menuBar.addItem("");
        menuBar.setWidth(grid.getWidth() + "");

        vlayout.addComponents(menuBar,grid,grid.createPageNavigator());
        //重新建个垂直布局，把树状菜单往下方挤一点
        VerticalLayout verticalLayout = new VerticalLayout();
        Label labelSpace = new Label("");
        labelSpace.setHeight("40px");
        verticalLayout.addComponents(labelSpace);
        verticalLayout.addComponents(tree);
        hLayout.addComponents(verticalLayout,vlayout);
        setContent(hLayout);


        tree.addSelectionListener(selectionEvent -> {
            Optional<String> selectedItem =  selectionEvent.getFirstSelectedItem();
            if(!selectedItem.isPresent()){
                menuTreeContentLayout.setMenuTreeList(allItem);
                grid.setGridDataSource(menuTreeContentLayout);
                menuTreeContentLayout.refreshGridData(grid,menuTreeContentLayout.getTotalCount(grid),grid.getCurrentPageNumber(),grid.getNumberPerPage());
                grid.refreshGrid();
//                grid.setItems(allItem.toArray(allMenuTrees));
                return;
            }
            String selectedItemStr = selectedItem.get();
            MenuTree selectMenu = null;
            for(int i = 0 ; i < allItem.size();i++){
                if(selectedItemStr.equals(allItem.get(i).getMenuName())){
                    selectMenu = allItem.get(i);
                }
            }
            if(selectMenu.hasChildren()){
                menuTreeContentLayout.setMenuTreeList(selectMenu.getChildrenList());
                grid.setGridDataSource(menuTreeContentLayout);
                menuTreeContentLayout.refreshGridData(grid,menuTreeContentLayout.getTotalCount(grid),grid.getCurrentPageNumber(),grid.getNumberPerPage());
                grid.refreshGrid();
//                MenuTree[] menuTrees = new MenuTree[selectMenu.getChildrenList().size()];
//                grid.setItems( selectMenu.getChildrenList().toArray(menuTrees));
            }else{
                List<MenuTree> selectMenuArr= new ArrayList<>();
                selectMenuArr.add(selectMenu);
                menuTreeContentLayout.setMenuTreeList(selectMenuArr);
                grid.setGridDataSource(menuTreeContentLayout);
                menuTreeContentLayout.refreshGridData(grid,menuTreeContentLayout.getTotalCount(grid),grid.getCurrentPageNumber(),grid.getNumberPerPage());
                grid.refreshGrid();
//                grid.setItems(selectMenu);
            }

        });

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    public void deleteMenuTree(List<MenuTree> menuTreeList){
        List<MenuTree> treeList = removeChildren(menuList,menuTreeList);
        allItem.clear();
        getAllItem(allItem,treeList);
//        for(int i = 0 ; i < allItem.size();i++){
//            MenuTree menuTree = allItem.get(i);
//            for(int j = 0 ; j < menuTreeList.size();j++){
//                if(menuTree.getId().equals(menuTreeList.get(j).getId())   || menuTree.getId().contains(menuTreeList.get(j).getId()) ){
//                    allItem.remove(i);
//                    i = -1;
//                    break;
//                }
//            }
//        }
        //设置树的数据集
        TreeDataProvider<String> dataProviderPro = (TreeDataProvider<String>) tree.getDataProvider();
        TreeData<String> dataPro = dataProviderPro.getTreeData();
        dataPro.clear();
        recursionSetData(dataPro,null,menuList);
        tree.setTreeData(dataPro);

        MenuTreeContentLayout menuTreeContentLayout = new MenuTreeContentLayout(allItem);
        grid.setGridDataSource(menuTreeContentLayout);
        menuTreeContentLayout.refreshGridData(grid,menuTreeContentLayout.getTotalCount(grid),grid.getCurrentPageNumber(),grid.getNumberPerPage());
        grid.refreshGrid();
    }

    private List<MenuTree> removeChildren(List<MenuTree> menuTree,List<MenuTree> removeMenu){
        for(int i = 0 ; i < menuTree.size();i++){
            for(int j = 0 ; j < removeMenu.size();j++) {
                if(menuTree.get(i).getMenuName().equals(removeMenu.get(j).getMenuName())){
                    menuTree.remove(i);
                    i = -1;
                    break;
                }
                if(menuTree.get(i).hasChildren()){
                    removeChildren(menuTree.get(i).getChildrenList(),removeMenu);
                }
            }

        }

        return menuTree;
    }

    //人工生成一组随即数据
    public List<MenuTree> getMenuTree(){
        List<MenuTree> menuList = new ArrayList<>();
        menuList.add(new MenuTree("menu01","系统管理",1,"2018-06-13",1,null));
        menuList.add(new MenuTree("menu02","人事管理",1,"2018-06-13",2,null));
        menuList.add(new MenuTree("menu03","项目运营管理",1,"2018-06-13",3,null));
        menuList.add(new MenuTree("menu04","流程管理",1,"2018-06-13",4,null));
        Collections.sort(menuList, new Comparator<MenuTree>() {
            @Override
            public int compare(MenuTree o1, MenuTree o2) {
                if(o1.getOrder() >= o2.getOrder()) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
        for(int i = 0 ; i < menuList.size();i++){
            MenuTree parent = menuList.get(i);
            List<MenuTree> children = new ArrayList<>();
            for(int j = 1 ; j <= 3;j++){
                children.add(new MenuTree(parent.getId() + "0" +j,parent.getMenuName().substring(0,2) + "子菜单" + j,2,"2018-06-13",j,menuList.get(i)));
            }
            Collections.sort(children, new Comparator<MenuTree>() {
                @Override
                public int compare(MenuTree o1, MenuTree o2) {
                    if(o1.getOrder() >= o2.getOrder()) {
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
            });
            menuList.get(i).setChildrenList(children);
        }
        return menuList;
    }
    //新增子元素
    public List<MenuTree> addChild(List<MenuTree> list,MenuTree menuTree){
        for(int i = 0 ; i < list.size();i++){
            if(menuTree.getMenuName().equals(list.get(i).getMenuName())){
                list.get(i).setChildrenList(menuTree.getChildrenList());
                return list;
            }
            if(list.get(i).hasChildren()){
                addChild(list.get(i).getChildrenList(),menuTree);
            }
        }
        return list;
    }

    //根据树结构数据 给treeData赋值
    public TreeData recursionSetData(TreeData treeData, MenuTree parent, List<MenuTree> list) {
        for (int i = 0; i < list.size(); i++) {
            MenuTree menu = list.get(i);
            if(parent == null){
                treeData.addItem(null, menu.getMenuName());
            }else{
                treeData.addItem(parent.getMenuName(), menu.getMenuName());
            }

            if (menu.hasChildren()) {
                recursionSetData(treeData, menu, menu.getChildrenList());
            }
        }
        return treeData;
    }



    //获取树结构中所有的数据，返回allItem
    public List<MenuTree> getAllItem(List<MenuTree> allItem, List<MenuTree> menuTreeList) {
        for (int i = 0; i < menuTreeList.size(); i++) {
            allItem.add(menuTreeList.get(i));
            if (menuTreeList.get(i).getChildrenList().size() != 0) {
                getAllItem(allItem, menuTreeList.get(i).getChildrenList());
            }
        }
        return allItem;
    }
}
