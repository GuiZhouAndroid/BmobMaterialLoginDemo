package com.yj.system.ui.Wave;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * created by on 2021/10/22
 * 描述：自定义TextView 重写isFocused()函数，让他放回true也就是一直获取了
 * 焦点效果自然也就出来了，如果这都不能解决那肯定就不是焦点问题了。
 * 那就要找到问题，在想办法解决
 * @author ZSAndroid
 * @create 2021-10-22-8:05
 */
public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView
{
    public MarqueeTextView(Context context)
    {
        this(context, null);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        setFocusable(true);
        setFocusableInTouchMode(true);

        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
    {
        if (focused)
        {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focused)
    {
        if (focused)
        {
            super.onWindowFocusChanged(focused);
        }
    }

    @Override
    public boolean isFocused()
    {
        return true;
    }
}
