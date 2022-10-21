package com.example.jigsawpuzzle.slice;

import com.example.jigsawpuzzle.ResourceTable;
import com.example.jigsawpuzzle.conponent.TestPageProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import com.example.jigsawpuzzle.ResourceTable;

import java.util.ArrayList;

public class SelectSlice extends AbilitySlice implements Component.ClickedListener {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0,"SelectSlice: ");
    private Button btn_back, btn_play;
    private int diff = 4;
    private String jigsawName = "dog";
    private PageSlider pageSlider;
    private RadioContainer container;

    private int[] images = {
            ResourceTable.Media_dog,
            ResourceTable.Media_doraemon,
            ResourceTable.Media_ultraman,
            ResourceTable.Media_hellokitty,
            ResourceTable.Media_mickey,
            ResourceTable.Media_minion,
            ResourceTable.Media_snoopy,
            ResourceTable.Media_snow,
            ResourceTable.Media_spongebob,
            ResourceTable.Media_winnie
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_select_slice);

        // 初始化组件
        initCom();

        // 添加响应事件
        addListener();
    }

    /**
     * 添加响应事件
     */
    private void addListener(){
        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int itemPos, float itemPosOffset, int itemPosPixles) {
            }
            @Override
            public void onPageSlideStateChanged(int state) {
            }
            @Override
            public void onPageChosen(int itemPos) {
                HiLog.info(label, "onPageChosen-itemPos=" + itemPos);
                if(itemPos == 0) jigsawName = "dog";
                else if(itemPos == 1) jigsawName = "doraemon";
                else if(itemPos == 2) jigsawName = "ultraman";
                else if(itemPos == 3) jigsawName = "hellokitty";
                else if(itemPos == 4) jigsawName = "mickey";
                else if(itemPos == 5) jigsawName = "minion";
                else if(itemPos == 6) jigsawName = "snoopy";
                else if(itemPos == 7) jigsawName = "snow";
                else if(itemPos == 8) jigsawName = "spongebob";
                else if(itemPos == 9) jigsawName = "winnie";
            }
        });


    }

    /**
     * 初始化组件
     */
    private void initCom() {
        btn_play = findComponentById(ResourceTable.Id_btn_play);
        btn_back = findComponentById(ResourceTable.Id_btn_back);
        btn_play.setClickedListener(this);
        btn_back.setClickedListener(this);

        initPageSlider();

        container = (RadioContainer) findComponentById(ResourceTable.Id_radio_container);
        container.mark(0);
        int count = container.getChildCount();
        for (int i = 0; i < count; i++){
            ((RadioButton) container.getComponentAt(i)).setButtonElement(createStateElement());
        }
        container.setMarkChangedListener(new RadioContainer.CheckedStateChangedListener() {
            @Override
            public void onCheckedChanged(RadioContainer radioContainer, int index) {
                HiLog.info(label, "index="+index);
                if(index == 0){
                    // easy
                    diff = 4;
                }else if(index == 1){
                    diff = 9;
                }
            }
        });
    }

    /**
     * 初始化pageSilder
     */
    private void initPageSlider() {
        pageSlider = (PageSlider) findComponentById(ResourceTable.Id_page_slider);
        pageSlider.setProvider(new TestPageProvider(getData(), this));
        pageSlider.setReboundEffect(true);
    }

    /**
     * 添加pageSlider页面
     * @return
     */
    private ArrayList<TestPageProvider.DataItem> getData() {
        ArrayList<TestPageProvider.DataItem> dataItems = new ArrayList<>();
        int len = images.length;
        for(int i = 0; i<len; i++){
            dataItems.add(new TestPageProvider.DataItem(images[i]));
        }
//        dataItems.add(new TestPageProvider.DataItem(ResourceTable.Media_dog));
//        dataItems.add(new TestPageProvider.DataItem(ResourceTable.Media_doraemon));
//        dataItems.add(new TestPageProvider.DataItem(ResourceTable.Media_ultraman));
        return dataItems;
    }


    /**
     * 获取radioButton的样式
     * @return
     */
    private StateElement createStateElement() {
        ShapeElement elementButtonOn = new ShapeElement();
        elementButtonOn.setRgbColor(RgbPalette.RED);
        elementButtonOn.setShape(ShapeElement.OVAL);

        ShapeElement elementButtonOff = new ShapeElement();
        elementButtonOff.setRgbColor(RgbPalette.WHITE);
        elementButtonOff.setShape(ShapeElement.OVAL);

        StateElement checkElement = new StateElement();
        checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_CHECKED}, elementButtonOn);
        checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, elementButtonOff);
        return checkElement;
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        if (component == btn_back) {
            AbilitySlice slice = new MainAbilitySlice();
            Intent intent = new Intent();
            present(slice, intent);
        } else if (component == btn_play) {
            if (diff == 4) {
                AbilitySlice slice = new PlayFourSlice();
                Intent intent = new Intent();
                intent.setParam("jigsawName", jigsawName);
                present(slice, intent);
            } else if(diff == 9){
                AbilitySlice slice = new PlayNineSlice();
                Intent intent = new Intent();
                intent.setParam("jigsawName", jigsawName);
                present(slice, intent);
            }
        }
    }
}
