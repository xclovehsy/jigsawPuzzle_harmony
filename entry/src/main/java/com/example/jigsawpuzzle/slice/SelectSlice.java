package com.example.jigsawpuzzle.slice;

import com.example.jigsawpuzzle.ResourceTable;
import com.example.jigsawpuzzle.conponent.TestPageProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.agp.window.dialog.CommonDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT; // 注意引入

import java.util.ArrayList;

public class SelectSlice extends AbilitySlice implements Component.ClickedListener {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0,"SelectSlice: ");
    private Button btn_back, btn_play;
    private int diff = 3, diffNewVal, model = 0;  // model=0 jig  model=1 huarong
    private int jigsawId = ResourceTable.Media_dog;
    private PageSlider pageSlider;
    private Button selectDiffBtn = null, selectModelBtn = null;
    private Picker diffPicker;
    private int pm_px, pg_px;
    private boolean isShowNum = true;
    private Text diffText, modelText;


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

        pm_px=AttrHelper.vp2px(getContext().getResourceManager().getDeviceCapability().width,this);
        pg_px=AttrHelper.vp2px(getContext().getResourceManager().getDeviceCapability().height,this);

        // 初始化组件
        initCom();

        // 添加响应事件
        addListener();
    }

    /**
     * 添加响应事件
     */
    private void addListener(){

        // 图片选择框
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
//                if(itemPos == 0) jigsawName = "dog";
//                else if(itemPos == 1) jigsawName = "doraemon";
//                else if(itemPos == 2) jigsawName = "ultraman";
//                else if(itemPos == 3) jigsawName = "hellokitty";
//                else if(itemPos == 4) jigsawName = "mickey";
//                else if(itemPos == 5) jigsawName = "minion";
//                else if(itemPos == 6) jigsawName = "snoopy";
//                else if(itemPos == 7) jigsawName = "snow";
//                else if(itemPos == 8) jigsawName = "spongebob";
//                else if(itemPos == 9) jigsawName = "winnie";
                jigsawId = images[itemPos];
            }
        });

        // 选择游戏难度tDialog
        selectDiffBtn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                CommonDialog cd = new CommonDialog(getContext());
                cd.setCornerRadius(15);
                DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_diff_dialog, null, false);
                diffPicker = dl.findComponentById(ResourceTable.Id_diff_picker);

                diffPicker.setMinValue(0); // 设置选择器中的最小值
                diffPicker.setMaxValue(6); // 设置选择器中的最大值
                diffPicker.setWheelModeEnabled(true);
                diffPicker.setValue(1);

                diffPicker.setFormatter(i -> {
                    String value;
                    switch (i) {
                        case 0:
                            value = "2×2";
                            break;
                        case 1:
                            value = "3×3";
                            break;
                        case 2:
                            value = "4×4";
                            break;
                        case 3:
                            value = "5×5";
                            break;
                        case 4:
                            value = "6×6";
                            break;
                        case 5:
                            value = "7×7";
                            break;
                        case 6:
                            value = "8×8";
                            break;
                        default:
                            value = "" + i;
                    }
                    return value;
                });

                diffPicker.setValueChangedListener((picker1, oldVal, newVal) -> {
                    // oldVal:上一次选择的值； newVal：最新选择的值
                    HiLog.info(label, "picker_newVal=" + newVal);
                    diffNewVal = newVal +2;
                });


                Button btn_cancel = dl.findComponentById(ResourceTable.Id_diff_cancel);
                btn_cancel.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        cd.destroy();
                    }
                });

                Button btn_ok = dl.findComponentById(ResourceTable.Id_diff_ok);
                btn_ok.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        String str = diffNewVal+"×"+diffNewVal;
                        diffText.setText(str);
                        diff = diffNewVal;
                        cd.destroy();
                    }
                });
                cd.setSize(800, MATCH_CONTENT);
                cd.setContentCustomComponent(dl);
                cd.show();
            }
        });

        // 模式选择dialog
        selectModelBtn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                CommonDialog cd = new CommonDialog(getContext());
                cd.setCornerRadius(15);
                DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_model_dialog, null, false);
                Button jigbtn = dl.findComponentById(ResourceTable.Id_jig_btn);
                jigbtn.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        modelText.setText("经典拼图");
                        model = 0;
                        cd.destroy();
                    }
                });

                Button huabtn = dl.findComponentById(ResourceTable.Id_huarong_btn);
                huabtn.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        modelText.setText("华容道");
                        model = 1;
                        cd.destroy();
                    }
                });

                Switch showNuwSwitch = dl.findComponentById(ResourceTable.Id_shownum_switch);
                ShapeElement elementThumbOn = new ShapeElement();
                elementThumbOn.setShape(ShapeElement.OVAL);
                elementThumbOn.setRgbColor(RgbColor.fromArgbInt(0xFF1E90FF));
                elementThumbOn.setCornerRadius(50);
                // 关闭状态下滑块的样式
                ShapeElement elementThumbOff = new ShapeElement();
                elementThumbOff.setShape(ShapeElement.OVAL);
                elementThumbOff.setRgbColor(RgbColor.fromArgbInt(0xFFFFFFFF));
                elementThumbOff.setCornerRadius(50);
                // 开启状态下轨迹样式
                ShapeElement elementTrackOn = new ShapeElement();
                elementTrackOn.setShape(ShapeElement.RECTANGLE);
                elementTrackOn.setRgbColor(RgbColor.fromArgbInt(0xFF87CEFA));
                elementTrackOn.setCornerRadius(50);
                // 关闭状态下轨迹样式
                ShapeElement elementTrackOff = new ShapeElement();
                elementTrackOff.setShape(ShapeElement.RECTANGLE);
                elementTrackOff.setRgbColor(RgbColor.fromArgbInt(0xFF808080));
                elementTrackOff.setCornerRadius(50);
                showNuwSwitch.setTrackElement(trackElementInit(elementTrackOn, elementTrackOff));
                showNuwSwitch.setThumbElement(thumbElementInit(elementThumbOn, elementThumbOff));
                showNuwSwitch.setChecked(true);
                showNuwSwitch.setCheckedStateChangedListener(new AbsButton.CheckedStateChangedListener() {
                    // 回调处理Switch状态改变事件
                    @Override
                    public void onCheckedChanged(AbsButton button, boolean isChecked) {
                        isShowNum = isChecked;
                    }
                });

                cd.setSize(800, MATCH_CONTENT);
                cd.setContentCustomComponent(dl);
                cd.show();
            }
        });

    }

    private StateElement trackElementInit(ShapeElement on, ShapeElement off){
        StateElement trackElement = new StateElement();
        trackElement.addState(new int[]{ComponentState.COMPONENT_STATE_CHECKED}, on);
        trackElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, off);
        return trackElement;
    }
    private StateElement thumbElementInit(ShapeElement on, ShapeElement off) {
        StateElement thumbElement = new StateElement();
        thumbElement.addState(new int[]{ComponentState.COMPONENT_STATE_CHECKED}, on);
        thumbElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, off);
        return thumbElement;
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

        selectDiffBtn = findComponentById(ResourceTable.Id_selectDiffBtn);
        selectModelBtn = findComponentById(ResourceTable.Id_selectModelBtn);
        diffText = findComponentById(ResourceTable.Id_diffText);
        modelText = findComponentById(ResourceTable.Id_modelText);
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
            if(model == 0){   // 经典拼图
                AbilitySlice slice = new jigsawSlice();
                Intent intent = new Intent();
                intent.setParam("jigsawId", jigsawId);
                intent.setParam("diff", diff);
                intent.setParam("isShowNum", isShowNum);
                present(slice, intent);

            }else if(model == 1){   // 华融道
                AbilitySlice slice = new HuarongRoadNine();
                Intent intent = new Intent();
                intent.setParam("jigsawId", jigsawId);
                intent.setParam("diff", diff);
                intent.setParam("isShowNum", isShowNum);
                present(slice, intent);
            }

        }
    }
}
