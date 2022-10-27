package com.example.jigsawpuzzle.slice;

import com.example.jigsawpuzzle.game.Blank;
import com.example.jigsawpuzzle.ResourceTable;
import com.example.jigsawpuzzle.game.Block;
import com.example.jigsawpuzzle.game.Chip;
import com.example.jigsawpuzzle.game.MyImage;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Point;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import java.util.*;


public class HuarongRoadNine extends AbilitySlice {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0, "MY_TAG");
    private Image img_tip;
    private int[] imgIds = {ResourceTable.Media_doraemon911, ResourceTable.Media_doraemon912, ResourceTable.Media_doraemon913, ResourceTable.Media_doraemon921, ResourceTable.Media_doraemon922, ResourceTable.Media_doraemon923, ResourceTable.Media_doraemon931, ResourceTable.Media_doraemon932, ResourceTable.Media_doraemon933};
    private int[] blockIds = {ResourceTable.Id_blank11, ResourceTable.Id_blank12, ResourceTable.Id_blank13, ResourceTable.Id_blank21, ResourceTable.Id_blank22, ResourceTable.Id_blank23, ResourceTable.Id_blank31, ResourceTable.Id_blank32, ResourceTable.Id_blank33};
    private int tipId = ResourceTable.Media_doraemon;
    private Map<Integer, Block> BlockMap = new HashMap<>();
    private int BlockCnt = 9;
    private List<MyImage> myimageList = new ArrayList<>();
    //    private List<Image> blockList = new LinkedList<>();
    private Button btn_begin, btn_end;
    private String jigsawName = "doraemon";
    private TickTimer tickTimer;
    long startTime = 0, passTime = 0;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_huarongnine_slice);
//        jigsawName = intent.getStringParam("jigsawName");


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

        btn_begin.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 设置时钟
                passTime = 0;
                startTime = System.currentTimeMillis();
                tickTimer.setBaseTime(startTime - passTime);
                tickTimer.start();
                tickTimer.setVisibility(Component.VISIBLE);

                // 设置可滑动
                for (Block block : BlockMap.values()) {
                    block.getView().setClickable(true);
                }
            }
        });

        for (Block block : BlockMap.values()) {
            block.getView().setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    Block myBlock = BlockMap.get(component.getId());

                    blockSwap(myBlock);

                }
            });
        }

        btn_end.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                isBINGO();
            }
        });


//        // 为chip添加响应事件
//        for (Chip chip: chipMap.values()){
//            chip.getImage().setDraggedListener(Component.DRAG_HORIZONTAL_VERTICAL, new Component.DraggedListener(){
//                private Point point;
//                //拖拽位置
//                private Point componentPo;
//                @Override
//                public void onDragDown(Component component, DragInfo dragInfo) {
//
//                }
//
//                @Override
//                public void onDragStart(Component component, DragInfo dragInfo) {
//                    point = dragInfo.startPoint;
//                    //获取组件在父组件中的起始位置
//                    componentPo = new Point(component.getContentPositionX(), component.getContentPositionY());
//
//                    HiLog.info(label, "up");
//                    Chip curChip = chipMap.get(component.getId());
//                    HiLog.info(label, "chipRightX="+curChip.getRightX() + ", chipRightY="+ curChip.getRightY());
//                    if (curChip.isInSpace()) {
////                            Log.d(TAG, "onTouch->"+chip.getCurX()+" "+chip.getCurY());
//                        for (Blank blank : BlankList) {
//                            if (blank.getX() == curChip.getCurX() && blank.getY() == curChip.getCurY()) {
//                                blank.setOccupy(false);
//                                break;
//                            }
//                        }
//                        curChip.outOfSpace();
//                    }
//                }
//
//                @Override
//                public void onDragUpdate(Component component, DragInfo dragInfo) {
//                    //计算每一次更新时的偏移量。draginfo中的偏移量是相对于上一次拖拽时的偏移量，在方向发	生冲突时，会产生位置动态刷新问题
//                    float xOffset = dragInfo.updatePoint.getPointX() - point.getPointX();
//                    float yOffset = dragInfo.updatePoint.getPointY() - point.getPointY();
//                    //setContentPosition为组件在父组件中的位置
//                    component.setContentPosition(componentPo.getPointX() + xOffset, componentPo.getPointY() + yOffset);
//                    componentPo = new Point(component.getContentPositionX(), component.getContentPositionY());
//                }
//
//                @Override
//                public void onDragEnd(Component component, DragInfo dragInfo) {
//                    HiLog.info(label, "down x=" + component.getContentPositionX() + " y="+ component.getContentPositionY());
//                    Chip curChip = chipMap.get(component.getId());
//                    for(Blank blank: BlankList){
//                        if(inPosition(component, blank.getImage()) && !blank.isOccupy()){
//                            component.setContentPositionX(blank.getImage().getContentPositionX());
//                            component.setContentPositionY(blank.getImage().getContentPositionY());
//
//                            // 设置当前chip的所在位置
//                            curChip.setCurX(blank.getX());
//                            curChip.setCurY(blank.getY());
//
//                            blank.setOccupy(true);
//                            HiLog.info(label, "ChipId="+component.getId() + ", X=" + blank.getX() + ", Y=" + blank.getY());
//
//                            // 判断是否完成拼图
//                            isBINGO();
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onDragCancel(Component component, DragInfo dragInfo) {
//
//                }
//            });
//        }

    }

    /**
     *
     */
    private void blockSwap(Block block) {
        int x = block.getX();
        int y = block.getY();

        for (Block block1 : BlockMap.values()) {
            int tx = block1.getX(), ty = block1.getY();
            if (Math.abs(tx + ty - x - y) == 1 && block1.isBlank()) {
                // 交换两个图片的image
                MyImage timg = block.getImage();
                block.setImage(block1.getImage());
                block1.setImage(timg);

                block.setBlank(true);
                block1.setBlank(false);

                block.RenewImage();
                block1.RenewImage();
            }
        }
    }

    /**
     * 重新挑战游戏
     */
    private void rePlay() {

        tickTimer.setVisibility(Component.INVISIBLE);

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
        for (Block block : BlockMap.values()) {
            if (block.isRightPosition()) cnt++;
        }
        return cnt;
    }


    /**
     * 获取拼图完成时间
     *
     * @return
     */
    private String getPlayTime() {
        String[] timeList = tickTimer.getText().split(":");
        return timeList[0] + "'" + timeList[1] + "''" + timeList[2];
    }

    /**
     * 判断是否成功完成拼图 并完成成功响应
     */
    private boolean isBINGO() {
        HiLog.info(label, "bingoCnt=" + getBINGOChipCnt());

        if (getBINGOChipCnt() == BlockCnt) {
            // 停止计时器
            passTime = System.currentTimeMillis() - startTime + passTime;
            tickTimer.setBaseTime(System.currentTimeMillis() - passTime);
            tickTimer.stop();

            // 创建弹窗
            CommonDialog cd = new CommonDialog(this);
            cd.setTitleText("拼图完成");
            cd.setContentText("恭喜，您的成绩是: " + getPlayTime());
            cd.setAutoClosable(true);
            cd.setButton(0, "选择图片难度", new IDialog.ClickedListener() {
                @Override
                public void onClick(IDialog iDialog, int i) {
                    AbilitySlice slice = new SelectSlice();
                    Intent intent = new Intent();
                    present(slice, intent);
                    cd.destroy();
                }
            });
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
        }else{
            // 创建弹窗
            CommonDialog cd = new CommonDialog(this);
            cd.setTitleText("拼图未完成");
            cd.setContentText("您未将所有拼图放到正确的位置！");
            cd.setAutoClosable(true);
            cd.setButton(0, "放弃挑战", new IDialog.ClickedListener() {
                @Override
                public void onClick(IDialog iDialog, int i) {
                    AbilitySlice slice = new SelectSlice();
                    Intent intent = new Intent();
                    present(slice, intent);
                    cd.destroy();
                }
            });
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
        HiLog.info(label, "nineSlice_jigsawName=" + jigsawName);

        if (jigsawName.equals("dog")) {
            imgIds[0] = ResourceTable.Media_dog911;
            imgIds[1] = ResourceTable.Media_dog912;
            imgIds[2] = ResourceTable.Media_dog913;
            imgIds[3] = ResourceTable.Media_dog921;
            imgIds[4] = ResourceTable.Media_dog922;
            imgIds[5] = ResourceTable.Media_dog923;
            imgIds[6] = ResourceTable.Media_dog931;
            imgIds[7] = ResourceTable.Media_dog932;
            imgIds[8] = ResourceTable.Media_dog933;
            tipId = ResourceTable.Media_dog;
        } else if (jigsawName.equals("ultraman")) {
            imgIds[0] = ResourceTable.Media_ultraman911;
            imgIds[1] = ResourceTable.Media_ultraman912;
            imgIds[2] = ResourceTable.Media_ultraman913;
            imgIds[3] = ResourceTable.Media_ultraman921;
            imgIds[4] = ResourceTable.Media_ultraman922;
            imgIds[5] = ResourceTable.Media_ultraman923;
            imgIds[6] = ResourceTable.Media_ultraman931;
            imgIds[7] = ResourceTable.Media_ultraman932;
            imgIds[8] = ResourceTable.Media_ultraman933;
            tipId = ResourceTable.Media_ultraman;
        } else if (jigsawName.equals("doraemon")) {
            imgIds[0] = ResourceTable.Media_doraemon911;
            imgIds[1] = ResourceTable.Media_doraemon912;
            imgIds[2] = ResourceTable.Media_doraemon913;
            imgIds[3] = ResourceTable.Media_doraemon921;
            imgIds[4] = ResourceTable.Media_doraemon922;
            imgIds[5] = ResourceTable.Media_doraemon923;
            imgIds[6] = ResourceTable.Media_doraemon931;
            imgIds[7] = ResourceTable.Media_doraemon932;
            imgIds[8] = ResourceTable.Media_doraemon933;
            tipId = ResourceTable.Media_doraemon;
        } else if (jigsawName.equals("hellokitty")) {
            imgIds[0] = ResourceTable.Media_hellokitty911;
            imgIds[1] = ResourceTable.Media_hellokitty912;
            imgIds[2] = ResourceTable.Media_hellokitty913;
            imgIds[3] = ResourceTable.Media_hellokitty921;
            imgIds[4] = ResourceTable.Media_hellokitty922;
            imgIds[5] = ResourceTable.Media_hellokitty923;
            imgIds[6] = ResourceTable.Media_hellokitty931;
            imgIds[7] = ResourceTable.Media_hellokitty932;
            imgIds[8] = ResourceTable.Media_hellokitty933;
            tipId = ResourceTable.Media_hellokitty;

        } else if (jigsawName.equals("mickey")) {
            imgIds[0] = ResourceTable.Media_mickey911;
            imgIds[1] = ResourceTable.Media_mickey912;
            imgIds[2] = ResourceTable.Media_mickey913;
            imgIds[3] = ResourceTable.Media_mickey921;
            imgIds[4] = ResourceTable.Media_mickey922;
            imgIds[5] = ResourceTable.Media_mickey923;
            imgIds[6] = ResourceTable.Media_mickey931;
            imgIds[7] = ResourceTable.Media_mickey932;
            imgIds[8] = ResourceTable.Media_mickey933;
            tipId = ResourceTable.Media_mickey;

        } else if (jigsawName.equals("minion")) {
            imgIds[0] = ResourceTable.Media_minion911;
            imgIds[1] = ResourceTable.Media_minion912;
            imgIds[2] = ResourceTable.Media_minion913;
            imgIds[3] = ResourceTable.Media_minion921;
            imgIds[4] = ResourceTable.Media_minion922;
            imgIds[5] = ResourceTable.Media_minion923;
            imgIds[6] = ResourceTable.Media_minion931;
            imgIds[7] = ResourceTable.Media_minion932;
            imgIds[8] = ResourceTable.Media_minion933;
            tipId = ResourceTable.Media_minion;

        } else if (jigsawName.equals("snoopy")) {
            imgIds[0] = ResourceTable.Media_snoopy911;
            imgIds[1] = ResourceTable.Media_snoopy912;
            imgIds[2] = ResourceTable.Media_snoopy913;
            imgIds[3] = ResourceTable.Media_snoopy921;
            imgIds[4] = ResourceTable.Media_snoopy922;
            imgIds[5] = ResourceTable.Media_snoopy923;
            imgIds[6] = ResourceTable.Media_snoopy931;
            imgIds[7] = ResourceTable.Media_snoopy932;
            imgIds[8] = ResourceTable.Media_snoopy933;
            tipId = ResourceTable.Media_snoopy;

        } else if (jigsawName.equals("snow")) {
            imgIds[0] = ResourceTable.Media_snow911;
            imgIds[1] = ResourceTable.Media_snow912;
            imgIds[2] = ResourceTable.Media_snow913;
            imgIds[3] = ResourceTable.Media_snow921;
            imgIds[4] = ResourceTable.Media_snow922;
            imgIds[5] = ResourceTable.Media_snow923;
            imgIds[6] = ResourceTable.Media_snow931;
            imgIds[7] = ResourceTable.Media_snow932;
            imgIds[8] = ResourceTable.Media_snow933;
            tipId = ResourceTable.Media_snow;

        } else if (jigsawName.equals("spongebob")) {
            imgIds[0] = ResourceTable.Media_spongebob911;
            imgIds[1] = ResourceTable.Media_spongebob912;
            imgIds[2] = ResourceTable.Media_spongebob913;
            imgIds[3] = ResourceTable.Media_spongebob921;
            imgIds[4] = ResourceTable.Media_spongebob922;
            imgIds[5] = ResourceTable.Media_spongebob923;
            imgIds[6] = ResourceTable.Media_spongebob931;
            imgIds[7] = ResourceTable.Media_spongebob932;
            imgIds[8] = ResourceTable.Media_spongebob933;
            tipId = ResourceTable.Media_spongebob;

        } else if (jigsawName.equals("winnie")) {
            imgIds[0] = ResourceTable.Media_winnie911;
            imgIds[1] = ResourceTable.Media_winnie912;
            imgIds[2] = ResourceTable.Media_winnie913;
            imgIds[3] = ResourceTable.Media_winnie921;
            imgIds[4] = ResourceTable.Media_winnie922;
            imgIds[5] = ResourceTable.Media_winnie923;
            imgIds[6] = ResourceTable.Media_winnie931;
            imgIds[7] = ResourceTable.Media_winnie932;
            imgIds[8] = ResourceTable.Media_winnie933;
            tipId = ResourceTable.Media_winnie;

        } else {
            imgIds[0] = ResourceTable.Media_doraemon911;
            imgIds[1] = ResourceTable.Media_doraemon912;
            imgIds[2] = ResourceTable.Media_doraemon913;
            imgIds[3] = ResourceTable.Media_doraemon921;
            imgIds[4] = ResourceTable.Media_doraemon922;
            imgIds[5] = ResourceTable.Media_doraemon923;
            imgIds[6] = ResourceTable.Media_doraemon931;
            imgIds[7] = ResourceTable.Media_doraemon932;
            imgIds[8] = ResourceTable.Media_doraemon933;
            tipId = ResourceTable.Media_doraemon;
        }

        btn_begin = findComponentById(ResourceTable.Id_btn_begin);
        btn_end = findComponentById(ResourceTable.Id_btn_end);
        img_tip = findComponentById(ResourceTable.Id_img_tip);

        // 初始化定时器
        tickTimer = findComponentById(ResourceTable.Id_ticktimer);
        tickTimer.setCountDown(false);
        tickTimer.setFormat("mm:ss:SSSS");
        tickTimer.setVisibility(Component.INVISIBLE);

        myimageList.clear();
        int k = 0;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++, k++) {
                if (k == BlockCnt - 1) {
                    break;
                }
                myimageList.add(new MyImage(i, j, imgIds[k]));
            }
        }
    }

    /**
     * 初始化Chip图片并设置映射关系
     */
    void setChip() {
        // 设置单参考图片==============
        img_tip.setImageAndDecodeBounds(tipId);

        // 打乱
//        Collections.shuffle(myimageList);

        // 设置chip背景图片


        int k = 0;
        BlockMap.clear();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++, k++) {
                if (k == BlockCnt - 1) {
                    break;
                }
                // 获取组件
                Image blockView = findComponentById(blockIds[k]);
                blockView.setPixelMap(myimageList.get(k).getImageId());
                // 添加映射关系
                BlockMap.put(blockIds[k], new Block(i, j, myimageList.get(k), blockView, false));
            }
        }

//        添加最后一个空白
        Image blockView = findComponentById(blockIds[BlockCnt - 1]);
        blockView.setPixelMap(ResourceTable.Media_question);
        // 添加映射关系
        BlockMap.put(blockIds[BlockCnt - 1], new Block(3, 3, new MyImage(3, 3, ResourceTable.Media_question), blockView, true));


        // 设置图片不可点
        for (Block block : BlockMap.values()) {
            block.getView().setClickable(false);
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

}

