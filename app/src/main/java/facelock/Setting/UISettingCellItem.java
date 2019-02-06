package com.daluotuo.facelock;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.moonma.common.UIView;
import com.moonma.common.PopViewController;

import com.moonma.FaceSDK.FaceSDKBase;
import com.moonma.FaceSDK.IFaceSDKBaseListener;
import com.moonma.FaceSDK.FaceSDKCommon;
import com.moonma.FaceSDK.FaceDBCommon;
import com.moonma.FaceSDK.IFaceDBBaseListener;
import com.daluotuo.facelock.RegisterViewController;
import com.moonma.common.UICellItemBase;

/**
 * TODO: document your custom view class.
 * <p>
 * android tableview https://www.jianshu.com/p/8669c3ebd10b
 */
public class UISettingCellItem extends UICellItemBase implements View.OnClickListener {
    ImageButton btnClose;
    IUISettingCellItemDelegate iDelegate;

    public interface IUISettingCellItemDelegate {
        public void OnUISettingCellItemDidClick(UISettingCellItem ui);
    }

    public UISettingCellItem(int layoutId, ViewGroup parent) {
        LoadLayoutRes(layoutId, parent);
        this.content.setOnClickListener(this);
        TextView tv = (TextView) findViewById(R.id.setting_item_title);//找到Textviewname
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (iDelegate != null) {
            iDelegate.OnUISettingCellItemDidClick(this);
        }

    }
}
