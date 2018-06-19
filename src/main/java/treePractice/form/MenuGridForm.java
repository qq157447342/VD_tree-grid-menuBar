package treePractice.form;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import treePractice.MyUI;
import treePractice.entity.MenuTree;

import javax.swing.text.Style;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MenuGridForm {
     Label labelId = new Label("菜单id");
     TextField textFieldId = new TextField();
     Label labelName = new Label("菜单名字");
     TextField textFieldName = new TextField();
     Label labelLevel = new Label("菜单级别");
     TextField textFieldLevel = new TextField();
     Label labelCreateTime = new Label("创建时间");
     TextField textFieldCreateTime = new TextField();
     Label labelOrder = new Label("菜单顺序");
     TextField textFieldOrder = new TextField();
     Label labelParent = new Label("上级菜单");
     TextField textFieldParent = new TextField();
     Button buttonSave = new Button("保存");
     Button buttonCencel = new Button("取消");
    VerticalLayout verticalLayout = new VerticalLayout();
    VerticalLayout verticalLayoutButton = new VerticalLayout();
    VerticalLayout layout =  new VerticalLayout();




    public VerticalLayout getGridLayout(MenuTree menuTree){
//        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
//        horizontalLayout1.addComponents(labelId,labelName,labelLevel,labelCreateTime);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        textFieldId.setCaption("菜单id");
        textFieldName.setCaption("菜单名字");
        textFieldLevel.setCaption("菜单级别");
        textFieldCreateTime.setCaption("创建时间");
        horizontalLayout1.addComponents(textFieldId,textFieldName,textFieldLevel,textFieldCreateTime);
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        textFieldOrder.setCaption("菜单顺序");
        textFieldParent.setCaption("上级菜单");
        horizontalLayout2.addComponents(textFieldOrder,textFieldParent);

        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        buttonSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        buttonCencel.setClickShortcut(KeyCode.ESCAPE);
        buttonSave.setClickShortcut(KeyCode.ENTER);
        horizontalLayout3.addComponents(buttonSave,buttonCencel);

//        GridLayout layout = new GridLayout(8,15);
//        GridLayout layoutButton = new GridLayout(8,1);
//
//        layout.setMargin(true);
//        layout.setWidth("900px");
//
//        labelId.setHeight("30px");
//        layout.addComponent(labelId,0,0,1,0);
//        layout.addComponent(textFieldId,0,1,1,1);
//
//        labelName.setHeight("30px");
//        layout.addComponent(labelName,2,0,3,0);
//        layout.addComponent(textFieldName,2,1,3,1);
//
//        labelLevel.setHeight("30px");
//        layout.addComponent(labelLevel,4,0,5,0);
//        layout.addComponent(textFieldLevel,4,1,5,1);
//
//        labelCreateTime.setHeight("30px");
//        layout.addComponent(labelCreateTime,6,0,7,0);
//        layout.addComponent(textFieldCreateTime,6,1,7,1);
//
//        labelOrder.setHeight("30px");
//        layout.addComponent(labelOrder,0,2,1,2);
//        layout.addComponent(textFieldOrder,0,3,1,3);
//
//        labelParent.setHeight("30px");
//        layout.addComponent(labelParent,2,2,3,2);
//        layout.addComponent(textFieldParent,2,3,3,3);
//
////        Label labelSpaceSave = new Label("");
////        labelSpaceSave.setHeight("40");
//////        labelSpaceSave.setWidth("800px");
////        layoutButton.addComponent(labelSpaceSave,4,0,5,0);
//////        buttonSave.addStyleName();
////        layoutButton.addComponent(buttonSave,6,0,7,0);
//
////        Label labelSpaceCencel = new Label("");
////        labelSpaceCencel.setHeight("40px");
////        layout.addComponent(labelSpaceCencel,1,0,1,0);
////        layout.addComponent(buttonCencel,1,0,1,0);
        verticalLayout.addComponents(horizontalLayout1,horizontalLayout2);
//        verticalLayout.setHeight("200px");
        verticalLayoutButton.addComponents(horizontalLayout3);
        verticalLayoutButton.setComponentAlignment(horizontalLayout3,Alignment.BOTTOM_RIGHT);
        verticalLayoutButton.setMargin(false);
        verticalLayoutButton.addStyleName("wumingkai");
//        verticalLayout.setHeight("100px");
        layout.addComponents(verticalLayout,verticalLayoutButton);
//        verticalLayout.setHeight("150px");
        return layout;
    }


    public Label getLabelId() {
        return labelId;
    }

    public void setLabelId(Label labelId) {
        this.labelId = labelId;
    }

    public TextField getTextFieldId() {
        return textFieldId;
    }

    public void setTextFieldId(TextField textFieldId) {
        this.textFieldId = textFieldId;
    }

    public Label getLabelName() {
        return labelName;
    }

    public void setLabelName(Label labelName) {
        this.labelName = labelName;
    }

    public TextField getTextFieldName() {
        return textFieldName;
    }

    public void setTextFieldName(TextField textFieldName) {
        this.textFieldName = textFieldName;
    }

    public Label getLabelLevel() {
        return labelLevel;
    }

    public void setLabelLevel(Label labelLevel) {
        this.labelLevel = labelLevel;
    }

    public TextField getTextFieldLevel() {
        return textFieldLevel;
    }

    public void setTextFieldLevel(TextField textFieldLevel) {
        this.textFieldLevel = textFieldLevel;
    }

    public Label getLabelCreateTime() {
        return labelCreateTime;
    }

    public void setLabelCreateTime(Label labelCreateTime) {
        this.labelCreateTime = labelCreateTime;
    }

    public TextField getTextFieldCreateTime() {
        return textFieldCreateTime;
    }

    public void setTextFieldCreateTime(TextField textFieldCreateTime) {
        this.textFieldCreateTime = textFieldCreateTime;
    }

    public Label getLabelOrder() {
        return labelOrder;
    }

    public void setLabelOrder(Label labelOrder) {
        this.labelOrder = labelOrder;
    }

    public TextField getTextFieldOrder() {
        return textFieldOrder;
    }

    public void setTextFieldOrder(TextField textFieldOrder) {
        this.textFieldOrder = textFieldOrder;
    }

    public Label getLabelParent() {
        return labelParent;
    }

    public void setLabelParent(Label labelParent) {
        this.labelParent = labelParent;
    }

    public TextField getTextFieldParent() {
        return textFieldParent;
    }

    public void setTextFieldParent(TextField textFieldParent) {
        this.textFieldParent = textFieldParent;
    }

    public Button getButtonSave() {
        return buttonSave;
    }

    public void setButtonSave(Button buttonSave) {
        this.buttonSave = buttonSave;
    }

    public Button getButtonCencel() {
        return buttonCencel;
    }

    public void setButtonCencel(Button buttonCencel) {
        this.buttonCencel = buttonCencel;
    }
}
