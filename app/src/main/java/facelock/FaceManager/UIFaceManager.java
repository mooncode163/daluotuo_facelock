package com.daluotuo.facelock;


import android.app.Activity;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.moonma.common.Common;
import com.moonma.common.UIView;
import com.moonma.common.PopViewController;
import com.moonma.common.ItemInfo;
import com.moonma.common.UICellBase;
import com.moonma.FaceSDK.FaceInfo;
import com.moonma.FaceSDK.FaceDBCommon;
import com.daluotuo.facelock.UIFaceManagerCellItem;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: document your custom view class.
 * <p>
 * android tableview https://www.jianshu.com/p/8669c3ebd10b
 */
public class UIFaceManager extends UIView implements View.OnClickListener, UIFaceManagerCellItem.IUIFaceManagerCellItemDelegate {
    ImageButton btnClose;
    ImageButton btnRegister;
    public static String TAG = "UIFaceManager";

    ListView listView;
    BaseAdapter adapter;
    List<FaceInfo> listItem = new ArrayList<FaceInfo>();//实体类
    int oneCellNum = 4;
    boolean isEditDelete = false;

    int widthItem = 256;
    int heightItem = 341;

    public UIFaceManager(int layoutId, UIView parent) {
        super(layoutId, parent);

        btnClose = (ImageButton) findViewById(R.id.btn_facemanager_close);
        btnClose.setOnClickListener(this);

        btnRegister = (ImageButton) findViewById(R.id.btn_facemanager_register);
        btnRegister.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.list_facemanager);

        Size pixel = Common.GetScreenPixel();
        float v = pixel.getWidth() * 1.0f / widthItem;
        if ((v - (int) v) >= 0.5f) {
            oneCellNum++;
        }

        //oneCellNum = pixel.getWidth()/widthItem;

        //添加滚动出边界回弹效果
        //listView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        listItem = FaceDBCommon.main().GetAllFace();
        boolean isEmpty = false;
        if (listItem == null) {
            isEmpty = true;
        } else {
            if (listItem.size() == 0) {
                isEmpty = true;
            }
        }

        if (isEmpty) {
            for (int i = 0; i < 5; i++) {
                FaceInfo info = new FaceInfo();//给实体类赋值
                info.name = "小米" + i;
                listItem.add(info);
            }
        }


        final UIFaceManager pthis = this;
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                // TODO Auto-generated method stub

                int total = listItem.size();
                // totalItem = total;
                int numRows = total / oneCellNum;
                if (total % oneCellNum != 0) {
                    numRows++;
                }
                return numRows;//数目
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Activity ac = Common.getMainActivity();
                LayoutInflater inflater = ac.getLayoutInflater();
                View view;

                if (convertView == null) {
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    UICellBase uiCell = new UICellBase(-1);
                    for (int i = 0; i < pthis.oneCellNum; i++) {
                        UIFaceManagerCellItem item = new UIFaceManagerCellItem();
                        item.iDelegate = pthis;
                        // Size dp = com.moonma.common.Common.GetScreenDP();
                        Size pixel = Common.GetScreenPixel();
                        item.LoadLayoutRes(R.layout.uifacemanagercellitem, uiCell);
                        ViewGroup.LayoutParams lp = item.content.getLayoutParams();
                        lp.width = pixel.getWidth() / oneCellNum;//widthItem
                        //保持比例
                        lp.height = (int) (heightItem * 1.0f * lp.width / widthItem);//lheightItem

                        item.SetController(pthis.controller);
                        item.SetParent(uiCell);
                        item.index = pthis.oneCellNum * position + i;
                        item.Init();
                        uiCell.AddItem(item);
                        if (item.index < listItem.size()) {
                            item.Show(true);
                            FaceInfo info = listItem.get(item.index);
                            item.UpdateItem(info, pthis.isEditDelete);
                        } else {
                            item.Show(false);
                        }
                    }
                    uiCell.SetController(pthis.controller);
                    view = uiCell.content;
                    view.setTag(uiCell);
                    //   view = inflater.inflate(R.layout.uisettingcellitem, null);
                } else {
                    view = convertView;
                    Object objTag = view.getTag();
                    if (objTag instanceof UICellBase) {
                        UICellBase uiCell = (UICellBase) objTag;
                        for (int i = 0; i < uiCell.GetItemCount(); i++) {

                            UIFaceManagerCellItem item = (UIFaceManagerCellItem) uiCell.GetItem(i);
                            item.index = pthis.oneCellNum * position + i;
                            Log.d(TAG, "i=" + i + " position=" + position + " item.index=" + item.index);
                            if (item.index < listItem.size()) {
                                item.Show(true);
                                FaceInfo info = listItem.get(item.index);
                                item.UpdateItem(info, pthis.isEditDelete);
                            } else {
                                Log.d(TAG, "Hide i=" + i + " position=" + position + " item.index=" + item.index);
                                item.Show(false);
                            }
                        }
                    }

                    Log.i("info", "有缓存，不需要重新生成" + position);
                }

                return view;
            }

            @Override
            public long getItemId(int position) {//取在列表中与指定索引对应的行id
                return 0;
            }

            @Override
            public Object getItem(int position) {//获取数据集中与指定索引对应的数据项
                return null;
            }


        };
        listView.setAdapter(adapter);
    }

    void OnClickBtnClose() {
        PopViewController p = (PopViewController) this.controller;
        if (p != null) {
            p.Close();
        }
    }

    void OnClickBtnRegister() {
        RegisterViewController.main().Show(null, null);
        OnClickBtnClose();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_facemanager_close) {
            OnClickBtnClose();
        }
        if (view.getId() == R.id.btn_facetips_register) {
            OnClickBtnRegister();
        }
    }

    //UIFaceManagerCellItem
    @Override
    public void OnUIFaceManagerCellItemDidLongPress(UIFaceManagerCellItem ui) {
        isEditDelete = !isEditDelete;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnUIFaceManagerCellItemDidDelete(UIFaceManagerCellItem ui) {
        //adapter.notifyDataSetChanged();
        listItem.remove(ui.index);
        adapter.notifyDataSetChanged();
        //DB
        FaceDBCommon.main().DeleteFace(ui.infoFace);
        //listView.invalidate();
    }
    //UIFaceManagerCellItem
}
