package treePractice.util;

import com.sun.org.apache.bcel.internal.generic.Select;
import com.vaadin.ui.*;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.NumberRenderer;
import treePractice.entity.TestObject;

import java.util.ArrayList;
import java.util.List;

public class DemoContentLayout extends VerticalLayout implements PageGrid.GridDataSource{

	@Override
	public int getTotalCount(Grid grid){
		return 100;
	}

	@Override
		public void refreshGridData(Grid grid, int totalSize, int currentPageNumber, int numberPerPage){

        List<TestObject> list1 = getGridLists(totalSize,currentPageNumber,numberPerPage);
        //System.out.println(list1.size()+"每页实际显示的条数");
        grid.setItems(list1);
	}

	public List<TestObject> getGridLists(int totalSize,int currentPageNumber,int numberPerPage){
	    //System.out.println("总数，当前页数，每页条数分别为："+totalSize+"==="+currentPageNumber+"--------"+numberPerPage);

	    List<TestObject> list1 = new ArrayList<>();

	    for (int j = 0;j < 100;j++){

            TestObject testObject1 = new TestObject();

            testObject1.setFoo("foo");
            testObject1.setBar(j+1);
            testObject1.setKm(1.0);
            list1.add(testObject1);
        }

        if(currentPageNumber*numberPerPage >= totalSize){
            list1 = list1.subList((currentPageNumber-1)*numberPerPage,totalSize);
        }else{
            list1 = list1.subList((currentPageNumber-1)*numberPerPage,currentPageNumber*numberPerPage);
        }

	    return list1;
    }
}
