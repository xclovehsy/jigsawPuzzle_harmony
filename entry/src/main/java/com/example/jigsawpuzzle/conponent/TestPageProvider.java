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
    public static class DataItem {
        int mimage;

        public DataItem(int image) {
            mimage = image;
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

        Image image = new Image(mContext);
        image.setLayoutConfig(
                new StackLayout.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT,
                        ComponentContainer.LayoutConfig.MATCH_PARENT
                ));
        image.setMarginsLeftAndRight(24, 24);
        image.setMarginsTopAndBottom(24, 24);
        image.setImageAndDecodeBounds(data.mimage);
        image.setScaleMode(Image.ScaleMode.STRETCH);
        ShapeElement element = new ShapeElement(mContext, ResourceTable.Graphic_background_page);
        image.setBackground(element);

        componentContainer.addComponent(image);
        return image;
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
