package com.example.jigsawpuzzle.slice;

import com.example.jigsawpuzzle.game.Blank;
import com.example.jigsawpuzzle.ResourceTable;
import com.example.jigsawpuzzle.game.Chip;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Point;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import javax.sql.CommonDataSource;
import java.util.*;


public class PlayFourSlice extends AbilitySlice {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0,"MY_TAG");
    private Image chip1, chip2, chip3, chip4, tip;
    //    private Image blank11, blank12, blank21, blank22;
    private List<Blank> BlankList = new LinkedList<>();
    private int[] imgIds = {ResourceTable.Media_dog411, ResourceTable.Media_dog412, ResourceTable.Media_dog421, ResourceTable.Media_dog422};
    private int[] chipIds = {ResourceTable.Id_chip1, ResourceTable.Id_chip2, ResourceTable.Id_chip3, ResourceTable.Id_chip4};
    private int tipId = ResourceTable.Media_dog;
    private Map<Integer, Chip> chipMap = new HashMap<>();
    private int chipCnt = 4;
    private List<Image> imageList = new LinkedList<>();
    private float[] chipX = {760, 387, 417, 74};
    private float[] chipY = {1283, 1172, 1555, 1294};
    private Button btn_begin, btn_tip;
    private String jigsawName = "dog";
    private TickTimer tickTimer;
    long startTime = 0, passTime = 0;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_playfour_slice);
        jigsawName = intent.getStringParam("jigsawName");

        Image image = findComponentById(ResourceTable.Id_chip1);
        HiLog.info(label, "chip1Pos="+image.getContentPosition()[0]);


        // 初始化组件
        initComponent();

//         设置chip图片
        setChip();

//         添加响应事件
        addListener();

    }

    /**
     * 添加响应事件函数
     */
    private void addListener() {
        btn_tip.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                HiLog.info(label, "actioncode="+touchEvent.getAction());
                if(touchEvent.getAction() == 1){ // 按下
                    tip.setVisibility(Component.VISIBLE);
                }else if(touchEvent.getAction() == 2){  // 放下
                    tip.setVisibility(Component.INVISIBLE);
                }
                return false;
            }
        });
        btn_begin.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 设置时钟
                passTime = 0;
                startTime = System.currentTimeMillis();
                tickTimer.setBaseTime(startTime - passTime);
                tickTimer.start();
                tickTimer.setVisibility(Component.VISIBLE);

                // 设置图片可见
                for (Chip chip: chipMap.values()){
                    chip.getImage().setVisibility(Component.VISIBLE);
                }
            }
        });

        // 为chip添加响应事件
        for (Chip chip: chipMap.values()){
            chip.getImage().setDraggedListener(Component.DRAG_HORIZONTAL_VERTICAL, new Component.DraggedListener(){
                private Point point;
                //拖拽位置
                private Point componentPo;
                @Override
                public void onDragDown(Component component, DragInfo dragInfo) {

                }

                @Override
                public void onDragStart(Component component, DragInfo dragInfo) {
                    point = dragInfo.startPoint;
                    //获取组件在父组件中的起始位置
                    componentPo = new Point(component.getContentPositionX(), component.getContentPositionY());

                    HiLog.info(label, "up");
                    Chip curChip = chipMap.get(component.getId());
                    HiLog.info(label, "chipRightX="+curChip.getRightX() + ", chipRightY="+ curChip.getRightY());
                    if (curChip.isInSpace()) {
//                            Log.d(TAG, "onTouch->"+chip.getCurX()+" "+chip.getCurY());
                        for (Blank blank : BlankList) {
                            if (blank.getX() == curChip.getCurX() && blank.getY() == curChip.getCurY()) {
                                blank.setOccupy(false);
                                break;
                            }
                        }
                        curChip.outOfSpace();
                    }
                }

                @Override
                public void onDragUpdate(Component component, DragInfo dragInfo) {
                    //计算每一次更新时的偏移量。draginfo中的偏移量是相对于上一次拖拽时的偏移量，在方向发	生冲突时，会产生位置动态刷新问题
                    float xOffset = dragInfo.updatePoint.getPointX() - point.getPointX();
                    float yOffset = dragInfo.updatePoint.getPointY() - point.getPointY();
                    //setContentPosition为组件在父组件中的位置
                    component.setContentPosition(componentPo.getPointX() + xOffset, componentPo.getPointY() + yOffset);
                    componentPo = new Point(component.getContentPositionX(), component.getContentPositionY());
                }

                @Override
                public void onDragEnd(Component component, DragInfo dragInfo) {
                    HiLog.info(label, "down x=" + component.getContentPositionX() + " y="+ component.getContentPositionY());
                    Chip curChip = chipMap.get(component.getId());
                    for(Blank blank: BlankList){
                        if(inPosition(component, blank.getImage()) && !blank.isOccupy()){
                            component.setContentPositionX(blank.getImage().getContentPositionX());
                            component.setContentPositionY(blank.getImage().getContentPositionY());

                            // 设置当前chip的所在位置
                            curChip.setCurX(blank.getX());
                            curChip.setCurY(blank.getY());

                            blank.setOccupy(true);
                            HiLog.info(label, "ChipId="+component.getId() + ", X=" + blank.getX() + ", Y=" + blank.getY());

                            // 判断是否完成拼图
                            isBINGO();

                        }
                    }
                }

                @Override
                public void onDragCancel(Component component, DragInfo dragInfo) {

                }
            });
        }

    }
    /**
     * 重新挑战游戏
     */
    private void rePlay() {
        for (Blank blank: BlankList){
            blank.setOccupy(false);
        }
        tickTimer.setVisibility(Component.INVISIBLE);

        // 重新设置chip的位置
        relocateChipPosition();

        // 重新设置chip的图片
        setChip();
    }


    /**
     * 获取正确拼图数量
     *
     * @return
     */
    private int getBINGOChipCnt() {
        int cnt = 0;
        for (Chip chip : chipMap.values()) {
            if (chip.isRightPosition()) cnt++;
        }
        return cnt;
    }


    /**
     * 获取拼图完成时间
     * @return
     */
    private String getPlayTime(){
        String[] timeList = tickTimer.getText().split(":");
        return timeList[0] + "'" + timeList[1] + "''" + timeList[2];
    }

    /**
     * 判断是否成功完成拼图 并完成成功响应
     */
    private boolean isBINGO() {
        HiLog.info(label, "bingoCnt=" + getBINGOChipCnt());

        if (getBINGOChipCnt() == chipCnt) {
            // 停止计时器
            passTime = System.currentTimeMillis() - startTime + passTime;
            tickTimer.setBaseTime(System.currentTimeMillis() - passTime);
            tickTimer.stop();

            // 创建弹窗
            CommonDialog cd = new CommonDialog(this);
            cd.setTitleText("拼图完成");
            cd.setContentText("恭喜，您的成绩是: " + getPlayTime());
            cd.setAutoClosable(true);
            cd.setButton(0, "选择图片难度", null);
            cd.setButton(1, "重新挑战", new IDialog.ClickedListener() {
                @Override
                public void onClick(IDialog iDialog, int i) {
                    rePlay();
                    cd.destroy();
                }
            });
            cd.setButton(2, "确定", new IDialog.ClickedListener() {
                @Override
                public void onClick(IDialog iDialog, int i) {
                    cd.destroy();
                }
            });
            cd.show();
        }
        return false;
    }

    /**
     * 初始化组件
     */
    void initComponent() {

        if(jigsawName.equals("dog")) {
            imgIds[0] = ResourceTable.Media_dog411;
            imgIds[1] = ResourceTable.Media_dog412;
            imgIds[2] = ResourceTable.Media_dog421;
            imgIds[3] = ResourceTable.Media_dog422;
            tipId = ResourceTable.Media_dog;
        }else if(jigsawName.equals("ultraman")) {
            imgIds[0] = ResourceTable.Media_ultraman411;
            imgIds[1] = ResourceTable.Media_ultraman412;
            imgIds[2] = ResourceTable.Media_ultraman421;
            imgIds[3] = ResourceTable.Media_ultraman422;
            tipId = ResourceTable.Media_ultraman;
        }else if(jigsawName.equals("doraemon")){
            imgIds[0] = ResourceTable.Media_doraemon411;
            imgIds[1] = ResourceTable.Media_doraemon412;
            imgIds[2] = ResourceTable.Media_doraemon421;
            imgIds[3] = ResourceTable.Media_doraemon422;
            tipId = ResourceTable.Media_doraemon;
        }else{
            imgIds[0] = ResourceTable.Media_dog411;
            imgIds[1] = ResourceTable.Media_dog412;
            imgIds[2] = ResourceTable.Media_dog421;
            imgIds[3] = ResourceTable.Media_dog422;
            tipId = ResourceTable.Media_dog;
        }

        btn_begin = findComponentById(ResourceTable.Id_btn_begin);
        btn_tip =findComponentById(ResourceTable.Id_btn_tip);
        tip = findComponentById(ResourceTable.Id_tip);
        tip.setVisibility(Component.INVISIBLE);

        // 初始化定时器
        tickTimer = findComponentById(ResourceTable.Id_ticktimer);
        tickTimer.setCountDown(false);
        tickTimer.setFormat("mm:ss:SSSS");
        tickTimer.setVisibility(Component.INVISIBLE);


        // 初始化chipImage
        imageList.clear();
        for(int chipid: chipIds){
            imageList.add(findComponentById(chipid));
        }
        relocateChipPosition();

        // 初始化空白
        BlankList.clear();
        BlankList.add(new Blank(findComponentById(ResourceTable.Id_blank11), 1, 1));
        BlankList.add(new Blank(findComponentById(ResourceTable.Id_blank12), 1, 2));
        BlankList.add(new Blank(findComponentById(ResourceTable.Id_blank21), 2, 1));
        BlankList.add(new Blank(findComponentById(ResourceTable.Id_blank22), 2, 2));
    }

    /**
     * 初始化Chip图片并设置映射关系
     */
    void setChip(){
        // 设置单参考图片==============
        tip.setImageAndDecodeBounds(tipId);

        // 打乱chipView顺序
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
            int a = Math.abs(random.nextInt()) % chipCnt;
            int b = Math.abs(random.nextInt()) % chipCnt;
            int temp = chipIds[a];
            chipIds[a] = chipIds[b];
            chipIds[b] = temp;
        }

        // 设置chip背景图片
        int k = 0;
        chipMap.clear();
        for(int i = 1; i<= 2; i++){
            for(int j = 1; j<= 2; j++, k++){
                // 获取组件
                Image chipView = findComponentById(chipIds[k]);
                chipView.setPixelMap(imgIds[k]);
                // 添加映射关系
                chipMap.put(chipIds[k], new Chip(i, j, chipView, chipIds[k]));
            }
        }

        // 设置图片不可见
        for (Chip chip: chipMap.values()){
            chip.getImage().setVisibility(Component.INVISIBLE);
        }
    }

    /**
     * 判断chip是否在合适的位置
     * @param chip
     * @param blank
     * @return
     */
    private boolean inPosition(Component chip, Component blank) {
        int len = 100;
        float chipX = chip.getContentPositionX(), chipY = chip.getContentPositionY();
        float blankX = blank.getContentPositionX(), blankY = blank.getContentPositionY();
        return chipX <= blankX + len && chipX >= blankX - len && chipY <= blankY + len && chipY >= blankY - len;
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
    /**
     * 重新定位chip的位置
     */
    private void relocateChipPosition() {
        for(int i = 0; i<chipCnt; i++){
            imageList.get(i).setContentPositionX(chipX[i]);
            imageList.get(i).setContentPositionY(chipY[i]);
        }
    }


}

