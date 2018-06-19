package treePractice.util;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import treePractice.MyUI;

/**
 * Created by wumk124866 on 2018/6/15.
 */
public class WindowCustom extends Window {
    Label label = new Label();
    Button buttonYes = new Button("确认");
    Button buttonNo = new Button();
    VerticalLayout verticalLayout = new VerticalLayout();
    //通知消息窗口
    final static String WINDOW_NOTIFICATION = "notification";
    //确认窗口
    final static String WINDOW_CONFIRM = "confirm";
    static boolean selectResult = false;

    public  WindowCustom(MyUI myUI ,String op,String message) {
        this.setWidth("320px");
        this.setHeight("180px");
        label.setValue(message);
        label.setHeight("50px");
        verticalLayout.addComponents(label);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        if(op.equals(WINDOW_CONFIRM)){
            buttonNo.setCaption("取消");
            buttonYes.addStyleName(ValoTheme.BUTTON_SMALL);
            buttonNo.addStyleName(ValoTheme.BUTTON_SMALL);
//        buttonNo.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//        buttonYes.addStyleName(ValoTheme.BUTTON_DANGER);
            horizontalLayout.addComponents(buttonYes);
            horizontalLayout.addComponents(buttonNo);
            horizontalLayout.setSizeFull();
            horizontalLayout.setComponentAlignment(buttonYes,Alignment.BOTTOM_CENTER);
            horizontalLayout.setComponentAlignment(buttonNo,Alignment.BOTTOM_CENTER);
            verticalLayout.addComponents(horizontalLayout);
        }else if(op.equals(WINDOW_NOTIFICATION)){
            buttonNo.setCaption("确认");
            verticalLayout.addComponents(buttonNo);
            verticalLayout.setComponentAlignment(buttonNo,Alignment.BOTTOM_CENTER);

        }
        verticalLayout.setComponentAlignment(label,Alignment.TOP_CENTER);
//        verticalLayout.setComponentAlignment(horizontalLayout,Alignment.BOTTOM_CENTER);
        this.setCaption("提示");
        this.setContent(verticalLayout);
        this.setResizable(false);
        this.setModal(true);
//        buttonYes.addClickListener(clickEvent -> {
//            selectResult = true;
//            myUI.removeWindow(this);
//        });
        buttonNo.addClickListener(clickEvent -> {
            selectResult = false;
            myUI.removeWindow(this);
        });
    }


    public Button getButtonYes() {
        return buttonYes;
    }

    public void setButtonYes(Button buttonYes) {
        this.buttonYes = buttonYes;
    }

    public Button getButtonNo() {
        return buttonNo;
    }

    public void setButtonNo(Button buttonNo) {
        this.buttonNo = buttonNo;
    }
}
