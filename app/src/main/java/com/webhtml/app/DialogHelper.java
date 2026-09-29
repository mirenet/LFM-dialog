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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import java.lang.reflect.Field;

public class DialogHelper {

    public static void showCustomAlert(Activity activity, String message, JsResult result) {
        activity.runOnUiThread(() -> {
            float density = activity.getResources().getDisplayMetrics().density;

            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);

            int padHorizontal = (int) (16 * density);
            int padTop = (int) (14 * density);
            int padBottom = (int) (12 * density);

            layout.setPadding(padHorizontal, padTop, padHorizontal, padBottom);

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
                    (int) (38 * density)
            );

            LinearLayout buttonLayout = new LinearLayout(activity);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            buttonLayout.setPadding(0, (int) (12 * density), 0, 0);
            buttonLayout.addView(closeButton, btnParams);
            layout.addView(buttonLayout);

            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setView(layout)
                    .setCancelable(false)
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                // Uklonjena sistemska animacija otvaranja za trenutni prikaz (instant)
                dialog.getWindow().setWindowAnimations(0);
            }

            closeButton.setOnClickListener(v -> {
                result.confirm();
                dialog.dismiss();
            });

            dialog.show();

            // Inicijalna širina vraćena na originalnu vrednost
            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.dimAmount = 0.5f; 
                dialog.getWindow().setAttributes(params);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
                lp.width = screenWidth - (int) (100 * density);
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
            }

            // Real-time praćenje promene veličine sa zaštitom od prevelikog skupljanja i pucanja
            View appDecorView = activity.getWindow().getDecorView();
            View.OnLayoutChangeListener resizeListener = new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (dialog.isShowing() && dialog.getWindow() != null) {
                        int newWidth = right - left;
                        if (newWidth > 100) {
                            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                            int targetWidth = newWidth - (int) (100 * density);
                            // Osiguravamo da širina nikad ne padne ispod bezbednog minimuma niti predje trenutni okvir
                            int safeWidth = Math.max((int) (180 * density), Math.min(newWidth - (int) (20 * density), targetWidth));
                            if (lp.width != safeWidth) {
                                lp.width = safeWidth;
                                dialog.getWindow().setAttributes(lp);
                            }
                        }
                    }
                }
            };

            appDecorView.addOnLayoutChangeListener(resizeListener);

            dialog.setOnDismissListener(dialogInterface -> {
                appDecorView.removeOnLayoutChangeListener(resizeListener);
            });
        });
    }

    public static void showNativeDownloadDialog(Activity activity, String suggestedFileName, String url, String mimetype, boolean isBlob, DownloadCallback callback) {
        activity.runOnUiThread(() -> {
            float density = activity.getResources().getDisplayMetrics().density;

            final LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);

            // Omogućavamo glavnom layout-u da preuzme fokus kada se klikne van input polja
            layout.setFocusable(true);
            layout.setFocusableInTouchMode(true);

            int padHorizontal = (int) (14 * density);
            int padTop = (int) (14 * density);
            int padBottom = (int) (14 * density);

            layout.setPadding(padHorizontal, padTop, padHorizontal, padBottom);

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
            titleView.setPadding(0, 0, 0, (int) (16 * density));
            layout.addView(titleView);

            TextView labelView = new TextView(activity);
            labelView.setText("File Name:");
            labelView.setTextColor(Color.parseColor("#DADADA"));
            labelView.setTextSize(14);
            labelView.setPadding((int) (6 * density), (int) (10 * density), 0, (int) (1 * density));
            layout.addView(labelView);

            final EditText input = new EditText(activity);
            input.setText(suggestedFileName);
            input.setTextSize(14);
            input.setTextColor(Color.parseColor("#9E9DA4"));
            input.setSingleLine(true);
            input.setPadding((int) (8 * density), (int) (10 * density), (int) (8 * density), (int) (10 * density));
            
            // Tanak sivi kursor
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
            inputParams.setMargins(0, (int) (4 * density), 0, (int) (16 * density));
            layout.addView(input, inputParams);

            TextView saveButton = createStyledButton(activity, "Save", "#222222", 14f, density);
            TextView closeButton = createStyledButton(activity, "Close", "#222222", 14f, density);

            LinearLayout buttonLayout = new LinearLayout(activity);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            buttonLayout.setPadding(0, 0, 0, 0);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int) (38 * density)
            );
            btnParams.weight = 1;
            btnParams.setMargins(0, 0, (int) (8 * density), 0);

            saveButton.setLayoutParams(btnParams);
            closeButton.setLayoutParams(btnParams);

            buttonLayout.addView(saveButton);
            buttonLayout.addView(closeButton);
            layout.addView(buttonLayout);

            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setView(layout)
                    .setCancelable(false)
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                // Uklonjena sistemska animacija za trenutni (instant) prikaz prozora
                dialog.getWindow().setWindowAnimations(0);
            }

            closeButton.setOnClickListener(v -> dialog.dismiss());

            saveButton.setOnClickListener(v -> {
                String finalName = input.getText().toString().trim();
                if (finalName.isEmpty()) {
                    finalName = suggestedFileName;
                }
                callback.onSave(finalName);
                dialog.dismiss();
            });

            dialog.show();

            // Čim se prozor prikaže, skidamo početni fokus sa input polja da kursor ne svetli sam od sebe
            input.clearFocus();

            // Pouzdano gubljenje fokusa i skrivanje kursora/tastature pri kliku bilo gde van input polja
            if (dialog.getWindow() != null) {
                View decorView = dialog.getWindow().getDecorView();
                decorView.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        View focusedView = activity.getCurrentFocus();
                        if (focusedView instanceof EditText) {
                            int[] location = new int[2];
                            focusedView.getLocationOnScreen(location);
                            float x = event.getRawX();
                            float y = event.getRawY();
                            // Ako je kliknuto van granica EditText-a
                            if (x < location[0] || x > (location[0] + focusedView.getWidth()) ||
                                    y < location[1] || y > (location[1] + focusedView.getHeight())) {
                                focusedView.clearFocus();
                                layout.requestFocus(); // Glavni layout preuzima fokus i gasi kursor
                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                                }
                            }
                        }
                    }
                    return false;
                });
            }

            // Inicijalna širina vraćena na originalnu vrednost
            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.dimAmount = 0.5f;
                dialog.getWindow().setAttributes(params);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
                lp.width = screenWidth - (int) (80 * density);
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
            }

            // Real-time praćenje promene veličine sa zaštitom za download dijalog
            View appDecorView = activity.getWindow().getDecorView();
            View.OnLayoutChangeListener resizeListener = new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (dialog.isShowing() && dialog.getWindow() != null) {
                        int newWidth = right - left;
                        if (newWidth > 100) {
                            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                            int targetWidth = newWidth - (int) (80 * density);
                            int safeWidth = Math.max((int) (200 * density), Math.min(newWidth - (int) (20 * density), targetWidth));
                            if (lp.width != safeWidth) {
                                lp.width = safeWidth;
                                dialog.getWindow().setAttributes(lp);
                            }
                        }
                    }
                }
            };

            appDecorView.addOnLayoutChangeListener(resizeListener);

            dialog.setOnDismissListener(dialogInterface -> {
                appDecorView.removeOnLayoutChangeListener(resizeListener);
            });
        });
    }

    /**
     * Kreira dugme sa originalnim LightingColorFilter hover efektom
     */
    private static TextView createStyledButton(Activity activity, String text, String textColorHex, float textSizeSp, float density) {
        TextView button = new TextView(activity);
        button.setText(text);
        button.setTextColor(Color.parseColor(textColorHex));
        button.setTextSize(textSizeSp);
        button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        button.setGravity(Gravity.CENTER);
        button.setIncludeFontPadding(false);
        button.getPaint().setSubpixelText(true);

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

    /**
     * Postavlja tanak, elegantan sivi kursor u EditText polje
     */
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
