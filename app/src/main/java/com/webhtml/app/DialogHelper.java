package com.webhtml.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.reflect.Field;

public class DialogHelper {

    public static void showCustomAlert(Activity activity, String message, JsResult result) {
        activity.runOnUiThread(() -> {
            float density = activity.getResources().getDisplayMetrics().density;

            // Pozadinski zatamnjeni overlay unutar granica prozora
            final FrameLayout overlayContainer = new FrameLayout(activity);
            overlayContainer.setBackgroundColor(Color.parseColor("#80000000"));
            overlayContainer.setClickable(true);
            overlayContainer.setFocusable(true);

            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);

            int padAll = (int) (12 * density);
            layout.setPadding(padAll, padAll, padAll, padAll);

            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setColor(Color.parseColor("#1A1A1C"));
            backgroundDrawable.setCornerRadius(10 * density);
            backgroundDrawable.setStroke((int) (0.9f * density), Color.parseColor("#7C874F"));
            layout.setBackground(backgroundDrawable);

            TextView titleView = new TextView(activity);
            titleView.setText("Alert!");
            titleView.setTextColor(Color.parseColor("#CBD868"));
            titleView.setTextSize(16);
            titleView.setTypeface(null, Typeface.BOLD);
            titleView.setGravity(Gravity.CENTER);
            titleView.setPadding(0, (int) (3 * density), 0, (int) (16 * density));
            layout.addView(titleView);

            TextView messageView = new TextView(activity);
            messageView.setText(message);
            messageView.setTextColor(Color.parseColor("#BAB9BF"));
            messageView.setTextSize(15);
            messageView.setLineSpacing(0, 1.4f);
            messageView.setGravity(Gravity.START);
            messageView.setPadding((int) (6 * density), (int) (20 * density), 0, (int) (32 * density));
            layout.addView(messageView);

            TextView closeButton = createStyledButton(activity, "Close", "#000000", 13.5f, density);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    (int) (90 * density),
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            LinearLayout buttonLayout = new LinearLayout(activity);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            buttonLayout.setPadding(0, (int) (12 * density), 0, 0);
            buttonLayout.addView(closeButton, btnParams);
            layout.addView(buttonLayout);

            int marginHorizontal = (int) (50 * density);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.CENTER;
            params.leftMargin = marginHorizontal;
            params.rightMargin = marginHorizontal;

            overlayContainer.addView(layout, params);

            ViewGroup rootLayout = activity.findViewById(android.R.id.content);
            rootLayout.addView(overlayContainer, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            closeButton.setOnClickListener(v -> {
                result.confirm();
                rootLayout.removeView(overlayContainer);
            });
        });
    }

    public static void showNativeDownloadDialog(Activity activity, String suggestedFileName, String url, String mimetype, boolean isBlob, DownloadCallback callback) {
        activity.runOnUiThread(() -> {
            float density = activity.getResources().getDisplayMetrics().density;

            final FrameLayout overlayContainer = new FrameLayout(activity);
            overlayContainer.setBackgroundColor(Color.parseColor("#80000000"));
            overlayContainer.setClickable(true);
            overlayContainer.setFocusable(true);

            final LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);

            layout.setFocusable(true);
            layout.setFocusableInTouchMode(true);

            // Blago smanjen vertikalni padding (sa 16.5f na 15f) da se kompenzuje pomeranje
            int padVertical = (int) (15f * density);
            int padHorizontal = (int) (14 * density);
            layout.setPadding(padHorizontal, padVertical, padHorizontal, padVertical);

            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setColor(Color.parseColor("#1A1A1C"));
            backgroundDrawable.setCornerRadius(10 * density);
            backgroundDrawable.setStroke((int) (0.9f * density), Color.parseColor("#7C874F"));
            layout.setBackground(backgroundDrawable);

            TextView titleView = new TextView(activity);
            titleView.setText("Save File");
            titleView.setTextColor(Color.parseColor("#CBD868"));
            titleView.setTextSize(16);
            titleView.setTypeface(null, Typeface.BOLD);
            titleView.setGravity(Gravity.CENTER);
            titleView.setIncludeFontPadding(false);
            titleView.setPadding(0, 0, 0, (int) (11 * density));
            layout.addView(titleView);

            TextView labelView = new TextView(activity);
            labelView.setText("File Name:");
            labelView.setTextColor(Color.parseColor("#DADADA"));
            labelView.setTextSize(14);
            labelView.setIncludeFontPadding(false);
            labelView.setPadding((int) (6 * density), (int) (5 * density), 0, (int) (1 * density));
            layout.addView(labelView);

            final EditText input = new EditText(activity);
            input.setText(suggestedFileName);
            input.setTextSize(14);
            input.setTextColor(Color.parseColor("#9E9DA4"));
            input.setSingleLine(true);
            input.setPadding((int) (8 * density), (int) (10 * density), (int) (8 * density), (int) (10 * density));
            
            setCursorColorAndWidth(input, Color.parseColor("#888888"), (int) (1.5f * density));

            GradientDrawable inputBg = new GradientDrawable();
            inputBg.setColor(Color.parseColor("#1A1A1A"));
            inputBg.setCornerRadius(10 * density);
            inputBg.setStroke((int) (0.8f * density), Color.parseColor("#606060"));
            input.setBackground(inputBg);

            LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            // Smanjena donja margina input polja sa 11dp na 8dp (oslobođeno tačno 3px/dp)
            inputParams.setMargins(0, (int) (4 * density), 0, (int) (8 * density));
            layout.addView(input, inputParams);

            TextView saveButton = createStyledButton(activity, "Save", "#222222", 13.5f, density);
            TextView closeButton = createStyledButton(activity, "Close", "#222222", 13.5f, density);

            LinearLayout buttonLayout = new LinearLayout(activity);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            
            // Mali gornji padding od 3 piksela iznad dugmadi koji gura sve nagore
            buttonLayout.setPadding(0, (int) (3 * density), 0, 0);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            btnParams.weight = 1;
            btnParams.setMargins(0, 0, (int) (8 * density), 0);

            saveButton.setLayoutParams(btnParams);

            LinearLayout.LayoutParams closeBtnParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            closeBtnParams.weight = 1;
            closeButton.setLayoutParams(closeBtnParams);

            buttonLayout.addView(saveButton);
            buttonLayout.addView(closeButton);
            layout.addView(buttonLayout);

            input.clearFocus();

            int marginHorizontal = (int) (25 * density);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.CENTER;
            params.leftMargin = marginHorizontal;
            params.rightMargin = marginHorizontal;

            overlayContainer.addView(layout, params);

            ViewGroup rootLayout = activity.findViewById(android.R.id.content);
            rootLayout.addView(overlayContainer, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            closeButton.setOnClickListener(v -> rootLayout.removeView(overlayContainer));

            saveButton.setOnClickListener(v -> {
                String finalName = input.getText().toString().trim();
                if (finalName.isEmpty()) {
                    finalName = suggestedFileName;
                }
                callback.onSave(finalName);
                rootLayout.removeView(overlayContainer);
            });
        });
    }

    private static TextView createStyledButton(Activity activity, String text, String textColorHex, float textSizeSp, float density) {
        TextView button = new TextView(activity);
        button.setText(text);
        button.setTextColor(Color.parseColor(textColorHex));
        button.setTextSize(textSizeSp);
        button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        button.setGravity(Gravity.CENTER);
        button.setIncludeFontPadding(false);
        button.getPaint().setSubpixelText(true);

        int btnPad = (int) (10 * density);
        button.setPadding(btnPad, btnPad, btnPad, btnPad);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor("#CBD868"));
        bg.setCornerRadius(10 * density);
        button.setBackground(bg);

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.getBackground().setColorFilter(new LightingColorFilter(Color.WHITE, Color.argb(255, 40, 40, 20)));
                    v.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.getBackground().clearColorFilter();
                    v.invalidate();
                    break;
            }
            return false;
        });

        return button;
    }

    private static void setCursorColorAndWidth(EditText editText, int color, int widthPx) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ShapeDrawable cursorDrawable = new ShapeDrawable(new RectShape());
                cursorDrawable.setIntrinsicWidth(widthPx);
                cursorDrawable.setBounds(0, 0, widthPx, editText.getLineHeight());
                cursorDrawable.getPaint().setColor(color);
                editText.setTextCursorDrawable(cursorDrawable);
            } else {
                Field editorField = TextView.class.getDeclaredField("mEditor");
                editorField.setAccessible(true);
                Object editor = editorField.get(editText);
                Field cursorField = editor.getClass().getDeclaredField("mCursorDrawable");
                cursorField.setAccessible(true);
                Object drawables = cursorField.get(editor);
                if (drawables instanceof android.graphics.drawable.Drawable[]) {
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setColor(color);
                    drawable.setSize(widthPx, editText.getLineHeight());
                    ((android.graphics.drawable.Drawable[]) drawables)[0] = drawable;
                    ((android.graphics.drawable.Drawable[]) drawables)[1] = drawable;
                }
            }
        } catch (Exception ignored) {}
    }

    public interface DownloadCallback {
        void onSave(String fileName);
    }
}
