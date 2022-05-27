package com.hp.gre3000.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Method;

/**
 * Created by liumeng on 3/18/15.
 */
public final class UIUtils {
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    private static final float NUM = 0.5f;
    private static final float PADDING = 0.5f;

    private UIUtils() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void makeTheStatusbarTranslucent(Activity activity) {

        Window w = activity.getWindow();
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setTheStatusbarNotTranslucent(Activity activity) {

        WindowManager.LayoutParams attrs = activity.getWindow()
                .getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + NUM);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + NUM);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + NUM);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + NUM);
    }

    /**
     * 创建选择对话框 （确定 和 取消）
     *
     * @return
     */
    public static AlertDialog showSelectDialog(Context ctx, String title, String message,
                                               String confirmButtonString,
                                               DialogInterface.OnClickListener confirmOnClickListener,
                                               String cancelButtonString,
                                               DialogInterface.OnClickListener cancelOnClickListener
    ) {
        AlertDialog alertDialog = new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(confirmButtonString, confirmOnClickListener)
                .setNegativeButton(cancelButtonString, cancelOnClickListener)
                .create();
        alertDialog.show();
        return alertDialog;
    }

    public static int screenWith(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int screenheight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getStatusBarHeight(final Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        else
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * resources.getDisplayMetrics().density);
    }

    @SuppressWarnings("deprecation")
    public static void setBackgroundAndKeepPadding(View view, Drawable backgroundDrawable) {
        Rect drawablePadding = new Rect();
        backgroundDrawable.getPadding(drawablePadding);
        int top = view.getPaddingTop() + drawablePadding.top;
        int left = view.getPaddingLeft() + drawablePadding.left;
        int right = view.getPaddingRight() + drawablePadding.right;
        int bottom = view.getPaddingBottom() + drawablePadding.bottom;

        view.setBackgroundDrawable(backgroundDrawable);
        view.setPadding(left, top, right, bottom);
    }

    public static void setBackgroundAndKeepPadding(View view, int backgroundRes) {
        Drawable backgroundDrawable = view.getResources().getDrawable(backgroundRes);
        setBackgroundAndKeepPadding(view, backgroundDrawable);
    }

    public static boolean isGone(View view) {
        if (view != null) {
            return view.getVisibility() == View.GONE;
        }
        return false;
    }

    public static boolean isVisible(View view) {
        if (view != null) {
            return view.getVisibility() == View.VISIBLE;
        }
        return false;
    }

    public static void setGone(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public static void setVisible(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void setInvisible(View view) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void setWidth(View view, int width, int unit) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p != null) {
            p.width = getSize(view, width, unit);
            return;
        }
        view.setLayoutParams(generateLayoutParams(view, width, WRAP_CONTENT, unit));
    }

    public static void setDpWidth(View view, int width) {
        setWidth(view, width, TypedValue.COMPLEX_UNIT_DIP);
    }

    public static void setPxWidth(View view, int width) {
        setWidth(view, width, TypedValue.COMPLEX_UNIT_PX);
    }

    public static void setHeight(View view, int height, int unit) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p != null) {
            p.height = getSize(view, height, unit);
            return;
        }
        view.setLayoutParams(generateLayoutParams(view, WRAP_CONTENT, height, unit));
    }

    public static void setDpHeight(View view, int height) {
        setHeight(view, height, TypedValue.COMPLEX_UNIT_DIP);
    }

    public static void setPxHeight(View view, int height) {
        setHeight(view, height, TypedValue.COMPLEX_UNIT_PX);
    }

    private static int getSize(View view, int size, int unit) {
        if (size == MATCH_PARENT) {
            return size;
        }
        if (size == WRAP_CONTENT) {
            return size;
        }
        if (!isDpUnit(unit)) {
            return size;
        }
        return dip2px(view.getContext(), size);
    }

    public static void setLayoutInDpUnit(View view, int width, int height) {
        setLayout(view, width, height, TypedValue.COMPLEX_UNIT_DIP);
    }

    public static void setLayoutInPxUnit(View view, int width, int height) {
        setLayout(view, width, height, TypedValue.COMPLEX_UNIT_PX);
    }

    private static void setLayout(View view, int width, int height, int unit) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p != null) {
            p.width = getSize(view, width, unit);
            p.height = getSize(view, height, unit);
            return;
        }
        view.setLayoutParams(generateLayoutParams(view, width, height, unit));
    }

    private static boolean isDpUnit(int unit) {
        return TypedValue.COMPLEX_UNIT_DIP == unit;
    }

    private static ViewGroup.LayoutParams generateLayoutParams(View view, int width, int height, int unit) {
        if (view == null) {
            return null;
        }
        ViewParent parent = view.getParent();
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(getSize(view, width, unit), getSize(view, height, unit));
        if (parent instanceof ViewGroup) {
            try {
                View parentView = (View) view.getParent();
                Method generateLayoutParamsMethod = parentView.getClass().getDeclaredMethod("generateLayoutParams", ViewGroup.LayoutParams.class);
                generateLayoutParamsMethod.setAccessible(true);
                return (ViewGroup.LayoutParams) generateLayoutParamsMethod.invoke(parentView, lps);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lps;
    }

    public static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenWidthMinusOf(Context context, int dpValue) {
        return getScreenWidth(context) - dip2px(context, dpValue);
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenHeightMinusOf(Context context, int dpValue) {
        return getScreenHeight(context) - dip2px(context, dpValue);
    }

    public static void setMargin(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) view.getLayoutParams());
            if (left == Integer.MAX_VALUE) {
                left = layoutParams.leftMargin;
            } else {
                left = dip2px(view.getContext(), left);
            }
            if (top == Integer.MAX_VALUE) {
                top = layoutParams.topMargin;
            } else {
                top = dip2px(view.getContext(), top);
            }
            if (right == Integer.MAX_VALUE) {
                right = layoutParams.rightMargin;
            } else {
                right = dip2px(view.getContext(), right);
            }
            if (bottom == Integer.MAX_VALUE) {
                bottom = layoutParams.bottomMargin;
            } else {
                bottom = dip2px(view.getContext(), bottom);
            }
            layoutParams.setMargins(left, top, right, bottom);
            view.setLayoutParams(layoutParams);
        }
    }
}
