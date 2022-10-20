package com.example.jigsawpuzzle.conponent;

import com.example.jigsawpuzzle.ResourceTable;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

import java.util.List;

public class TestPageProvider extends PageSliderProvider {
    //数据实体类
    public static class DataItem{
        String mText;
        public DataItem(String txt) {
            mText = txt;
        }
    }
    // 数据源，每个页面对应list中的一项
    private List<DataItem> list;
    private Context mContext;

    public TestPageProvider(List<DataItem> list, Context context) {
        this.list = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        final DataItem data = list.get(i);
        Text label = new Text(null);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setLayoutConfig(
                new StackLayout.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT,
                        ComponentContainer.LayoutConfig.MATCH_PARENT
                ));
        label.setText(data.mText);
        label.setTextColor(Color.BLACK);
        label.setTextSize(50);
        label.setMarginsLeftAndRight(24, 24);
        label.setMarginsTopAndBottom(24, 24);
        ShapeElement element = new ShapeElement(mContext, ResourceTable.Graphic_background_page);
        label.setBackground(element);
        componentContainer.addComponent(label);
        return label;
    }
    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component) o);
    }
    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        //可添加具体处理逻辑
//        ...
        return true;
    }
}
