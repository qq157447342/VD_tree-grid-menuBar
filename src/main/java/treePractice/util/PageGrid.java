package treePractice.util;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

public class PageGrid<T> extends Grid<T>{

    private String PER_PAGE = "每页:";

    private String NUMBER_PAGE = "页数:";

    // -----------------------------------------------------------------------
    /**
     * <pre>
     *  每页：[ 1 ] 条/总共：12条                      <<  <  页数：[ 2 ] / 3  >  >>
     * </pre>
     */

    // 每页文字标签
    private Label lblNumberPerPage = null;

    // 每页显示记录条数下拉框
    private ComboBox cmbNumberPerPage;

    // 总记录数标签
    private Label lblTotalSize;

    // 第一页按钮
    private Button imgFirstPage = null;

    // 下一页按钮
    private Button imgNextPage = null;

    // 页数标签
    private Label lblPageNumber = null;

    // 当前页数输入框
    private ComboBox cmbCurrentPage = null;

    // 页数输入框与总页数标签之间的分割线
    private Label lblWhiteSpaceSeparator = null;

    // 总页数标签
    private Label lblTotalPage = null;

    // 上一页按钮
    private Button imgPreviousPage = null;

    // 最后一页按钮
    private Button imgLastPage = null;

    // 每页显示条数
    private int numberPerPage = 10;

    // 当前页码：如果小于等于0，则默认显示第一页；如果大于总页数，则显示最后一页
    private int currentPageNumber = 1;

    // 总记录条数
    private int totalSize = 0;

    //用来存放可选择的页码
    List<String> currentPageList = new ArrayList<String>();

    public PageGrid() {
        this(null, 10);
    }

    public PageGrid(int numberPerPage) {
        this(null, numberPerPage);
    }

    public PageGrid(String caption, int numberPerPage) {
        super(caption);
        setNumberPerPage(numberPerPage);
        setTableShowRows();
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    private void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getNumberPerPage() {
        return numberPerPage;
    }

    private void setNumberPerPage(int numberPerPage) {
        if (numberPerPage > 0) {
            this.numberPerPage = numberPerPage;
        }
    }

    public int getTotalSize() {
        if (totalSize < 0) {
            totalSize = 0;
        }

        return totalSize;
    }

    private void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * 获取最大页码数。
     *
     * @return
     */
    public int getMaxPageNumber() {
        int size = getTotalSize();
        int total = gridDataSource.getTotalCount(this);
        if(size == 0){
            size = total;
        }
        int maxPageNumber = (int) Math.ceil(size / (double) numberPerPage);
        if (maxPageNumber < 1) {
            maxPageNumber = 1;
        }

        return maxPageNumber;
    }

    /**
     * 设置表格（实际显示）行数。
     */
    private void setTableShowRows() {

        int showRows = getNumberPerPage();
        setNumberPerPage(showRows);
    }

    private void checkCurrentPageNumber() {
        // 当前页码小于等于0时，则显示第一页

        if (getCurrentPageNumber() <= 0) {
            setCurrentPageNumber(1);
            return;
        }

        // 当前页码超出最多页码时，则显示最后一页
        int maxPageNumber = getMaxPageNumber();
        if (getCurrentPageNumber() > maxPageNumber) {
            setCurrentPageNumber(maxPageNumber);
            return;
        }
    }

    public void repaintPageNavigator() {

        // 重新设置页数选择控件值
        currentPageList = getCurrentPageList();

        this.getCurrentPageNumberComboBox().setItems(currentPageList);
        this.getCurrentPageNumberComboBox().setValue(getCurrentPageNumber());
        if(lblTotalSize != null){
            this.lblTotalSize.setValue("&nbsp;条/共" + gridDataSource.getTotalCount(this) + "条");
        }
    }

    public List<String> getCurrentPageList(){
        if (getMaxPageNumber() == 1) {
            this.getFirstPageImage().setEnabled(false);
            this.getPreviousPageImage().setEnabled(false);
            this.getNextPageImage().setEnabled(false);
            this.getLastPageImage().setEnabled(false);
        } else {
            this.getFirstPageImage().setEnabled(getCurrentPageNumber() > 1);
            this.getPreviousPageImage().setEnabled(getCurrentPageNumber() > 1);
            this.getNextPageImage().setEnabled(getCurrentPageNumber() < getMaxPageNumber());
            this.getLastPageImage().setEnabled(getCurrentPageNumber() < getMaxPageNumber());
        }
        List<String> pageLists = new ArrayList<String>();
        int maxPageNumber = getMaxPageNumber();
        if (maxPageNumber > 0) {
            for (int i = 1; i <= maxPageNumber; i++) {
                pageLists.add(String.valueOf(i));
            }
        } else {
            pageLists.add(String.valueOf(0));
        }

        //重新加载总页数
        getLabelTotalPage();

        return pageLists;
    }


    //提供的两个接口
    public interface GridDataSource {

        public int getTotalCount(Grid grid);

        public void refreshGridData(Grid grid, int totalSize, int currentPageNumber, int numberPerPage);

    }

    private GridDataSource gridDataSource = null;

    public void setGridDataSource(GridDataSource gridDataSource) {
        this.gridDataSource = gridDataSource;
    }

    /**
     * 重新加载表格的四种情况：</br>
     *
     * <pre>
     * 1）初始化表格时，默认显示第一页；
     * 2）改变表格每页显示条数；
     * 3）点击第一页、上一页、下一页、最后一页按钮；
     * 4）手动更新当前页数。
     * </pre>
     */
    public void refreshGrid(boolean setSelect) {

        if (gridDataSource == null) {
            return;
        }

        setTotalSize(gridDataSource.getTotalCount(this));

        checkCurrentPageNumber();

//       System.out.println("总条数："+getTotalSize()+",当前页面数："+getCurrentPageNumber()+",每页条数："+getNumberPerPage());

        gridDataSource.getTotalCount(this);
        gridDataSource.refreshGridData(this, getTotalSize(), getCurrentPageNumber(), getNumberPerPage());

        setTableShowRows();


        repaintPageNavigator();
    }

    public void refreshGrid() {
        refreshGrid(false);
    }



    // -----------------------------创建分页导航控件----------------------------------


    /**
     * <pre>
     * 每页：_1_ 条/总共：12条                                      << <  页数：_2_ / 3  > >>
     * </pre>
     */
    public HorizontalLayout createPageNavigator() {
        HorizontalLayout pageNavigator = new HorizontalLayout();
        pageNavigator.setWidth("100%");

        HorizontalLayout leftPartial = getLeftLayout();
        HorizontalLayout rightPartial = getRightLayout();
        pageNavigator.addComponent(leftPartial);
        pageNavigator.addComponent(rightPartial);
        pageNavigator.setComponentAlignment(rightPartial, Alignment.MIDDLE_CENTER);
        pageNavigator.setExpandRatio(leftPartial, 1);

        return pageNavigator;
    }

    private HorizontalLayout getLeftLayout() {
        HorizontalLayout leftLayout = new HorizontalLayout();

        leftLayout.addComponent(getLabelPerPage());
        leftLayout.addComponent(getComoBoxNumberPerPage());
        leftLayout.addComponent(getLabelTotalSize());
        leftLayout.setComponentAlignment(getLabelPerPage(), Alignment.MIDDLE_LEFT);
        leftLayout.setComponentAlignment(getComoBoxNumberPerPage(), Alignment.MIDDLE_LEFT);
        leftLayout.setComponentAlignment(getLabelTotalSize(), Alignment.MIDDLE_LEFT);

        return leftLayout;
    }

    private Label getLabelPerPage() {
        if (lblNumberPerPage == null) {
            lblNumberPerPage = new Label(PER_PAGE + "&nbsp;", ContentMode.HTML);
            lblNumberPerPage.addStyleName("pagetable-text");
        }

        return lblNumberPerPage;
    }

    //获取下拉框每页数据条数选择
    private ComboBox getComoBoxNumberPerPage() {
        if (cmbNumberPerPage == null) {
            cmbNumberPerPage = new ComboBox();

            Object args[] = {"5","10","20","50"};

            cmbNumberPerPage.setItems(args);
            cmbNumberPerPage.setEmptySelectionAllowed(false);
            cmbNumberPerPage.setTextInputAllowed(false);
            cmbNumberPerPage.setValue(10);

            cmbNumberPerPage.setHeight("25px");
            cmbNumberPerPage.setWidth("65px");
            cmbNumberPerPage.addStyleName("small-combo");
            cmbNumberPerPage.addValueChangeListener(valueChangeEvent -> {

                int numberPerPage = Integer.valueOf(String.valueOf(valueChangeEvent.getValue()));
                setNumberPerPage(numberPerPage);
                setCurrentPageNumber(1);
                this.refreshGrid();
            });

        }

        return cmbNumberPerPage;
    }

    private Label getLabelTotalSize() {
        if (lblTotalSize == null) {
            lblTotalSize = new Label("&nbsp;条/共" + gridDataSource.getTotalCount(this) + "条", ContentMode.HTML);
            lblTotalSize.addStyleName("pagetable-text");
        }

        return lblTotalSize;
    }


    private HorizontalLayout getRightLayout() {
        HorizontalLayout rightLayout = new HorizontalLayout();
        rightLayout.setWidth(null);

        rightLayout.addComponent(getFirstPageImage());
        rightLayout.addComponent(getPreviousPageImage());
        rightLayout.addComponent(getLabelPageNumber());
        rightLayout.addComponent(getCurrentPageNumberComboBox());

        rightLayout.addComponent(getLabelTotalPage());
        rightLayout.addComponent(getNextPageImage());
        rightLayout.addComponent(getLastPageImage());

        rightLayout.setComponentAlignment(getFirstPageImage(), Alignment.MIDDLE_LEFT);
        rightLayout.setComponentAlignment(getPreviousPageImage(), Alignment.MIDDLE_LEFT);
        rightLayout.setComponentAlignment(getLabelPageNumber(), Alignment.MIDDLE_LEFT);
        rightLayout.setComponentAlignment(getCurrentPageNumberComboBox(), Alignment.MIDDLE_LEFT);
        rightLayout.setComponentAlignment(getLabelTotalPage(),Alignment.MIDDLE_LEFT);
        rightLayout.setComponentAlignment(getNextPageImage(), Alignment.MIDDLE_LEFT);
        rightLayout.setComponentAlignment(getLastPageImage(), Alignment.MIDDLE_LEFT);

        return rightLayout;
    }

    private Button getFirstPageImage() {
        if (imgFirstPage == null) {
            imgFirstPage = new Button("首页");
            imgFirstPage.addClickListener(clickEvent -> {
                //第一页
                this.setCurrentPageNumber(1);
                this.refreshGrid();
            });
            imgFirstPage.setHeight("25px");
            imgFirstPage.setWidth("53px");
            imgFirstPage.setStyleName("small-button");
        }

        return imgFirstPage;
    }

    private Button getPreviousPageImage() {
        if (imgPreviousPage == null) {
            imgPreviousPage = new Button("上一页");
            imgPreviousPage.addClickListener(clickEvent -> {
                //前一页
                setCurrentPageNumber(getCurrentPageNumber() - 1);
                this.refreshGrid();
            });

            imgPreviousPage.setHeight("25px");
            imgPreviousPage.setWidth("69px");
            imgPreviousPage.setStyleName("small-button");
        }

        return imgPreviousPage;
    }

    private Label getLabelPageNumber() {
        if (lblPageNumber == null) {
            lblPageNumber = new Label(NUMBER_PAGE + "&nbsp;", ContentMode.HTML);
            lblPageNumber.setWidth(null);
            lblPageNumber.addStyleName("pagetable-text");
        }

        return lblPageNumber;
    }

    private ComboBox getCurrentPageNumberComboBox() {
        if (cmbCurrentPage == null) {
            cmbCurrentPage = new ComboBox();

            cmbCurrentPage.setEmptySelectionAllowed(false);
            cmbCurrentPage.setHeight("25px");
            cmbCurrentPage.setWidth("65px");
            cmbCurrentPage.setItems(this.getCurrentPageList());
            cmbCurrentPage.setValue(getCurrentPageNumber());
            cmbCurrentPage.addStyleName("small-combo");
            cmbCurrentPage.addValueChangeListener(valueChangeEvent -> {
                int currentPageNumber = getCurrentPageNumber();
                //判读输入的数据是否是数字
                int currentPageNumberChange = isNumeric( valueChangeEvent.getValue());

                if (currentPageNumber != currentPageNumberChange) {
                    if(currentPageNumberChange < 1 || currentPageNumberChange > getMaxPageNumber()){
                        currentPageNumberChange = currentPageNumber;
                    }
                    this.setCurrentPageNumber(currentPageNumberChange);
                    this.refreshGrid();
                }
            });
        }

        return cmbCurrentPage;
    }

    public static int isNumeric(Object str) {
        int changeValue = 1;
        try {
            changeValue = Integer.valueOf(String.valueOf(str));
        } catch (Exception e) {
            return 0;//异常 说明是null或者字符串
        }
        return changeValue;
    }

    private Label getLabelTotalPage() {
        int maxPage = this.getMaxPageNumber();
        if (lblTotalPage == null) {
            lblTotalPage = new Label("&nbsp;页/共" + maxPage + "页", ContentMode.HTML);
        }

        lblTotalPage.setValue("&nbsp;页/共" + maxPage + "页");
        lblTotalPage.addStyleName("pagetable-text");
        return lblTotalPage;
    }

    private Button getNextPageImage() {
        if (imgNextPage == null) {
            imgNextPage = new Button("下一页");
            imgNextPage.setStyleName("next-page");
            imgNextPage.addClickListener(clickEvent -> {
                //下一页
                setCurrentPageNumber(getCurrentPageNumber() + 1);
                this.refreshGrid();
            });

            imgNextPage.setHeight("25px");
            imgNextPage.setWidth("69px");
            imgNextPage.setStyleName("small-button");
        }

        return imgNextPage;
    }

    private Button getLastPageImage() {
        if (imgLastPage == null) {
            imgLastPage = new Button("尾页");
            imgLastPage.addClickListener(clickEvent -> {
                //尾页
                this.setCurrentPageNumber(this.getMaxPageNumber());
                this.refreshGrid();
            });

            imgLastPage.setHeight("25px");
            imgLastPage.setWidth("55px");
            imgLastPage.setStyleName("small-button");
        }

        return imgLastPage;
    }

}


