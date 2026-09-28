package com.webhtml.app;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class DialogHelper {

    // ============================================================
    // COLORS - iste vrednosti kao u HTML/CSS
    // ============================================================

    private static final int COLOR_POPUP = Color.parseColor("#1A1A1C");
    private static final int COLOR_BORDER = Color.parseColor("#7C874F");
    private static final int COLOR_ACCENT = Color.parseColor("#CBD868");

    private static final int COLOR_TITLE = Color.parseColor("#CBD868");
    private static final int COLOR_LABEL = Color.parseColor("#DADADA");
    private static final int COLOR_INPUT_TEXT = Color.parseColor("#9E9DA4");
    private static final int COLOR_MESSAGE = Color.parseColor("#BAB9BF");

    private static final int COLOR_BUTTON_TEXT = Color.parseColor("#555555");
    private static final int COLOR_ALERT_BUTTON_TEXT = Color.BLACK;

    private static final int COLOR_INPUT_BACKGROUND = Color.parseColor("#1A1A1A");
    private static final int COLOR_INPUT_BORDER = Color.parseColor("#606060");


    // ============================================================
    // UTILS
    // ============================================================

    private static int dp(Activity activity, float value) {
        return Math.round(
                value * activity.getResources()
                        .getDisplayMetrics().density
        );
    }


    /**
     * HTML koristi:
     *
     * transition: all .2s
     * :active {
     *     filter: brightness(1.25);
     * }
     *
     * Android nema CSS hover/active, pa ovde simuliramo
     * isti efekat tokom dodira.
     */
    private static void applyButtonTouchEffect(View button) {

        button.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    if (v.getBackground() != null) {
                        v.getBackground().setColorFilter(
                                Color.argb(0, 255, 255, 255),
                                PorterDuff.Mode.SRC_ATOP
                        );
                    }

                    // CSS brightness(1.25)
                    // #CBD868 -> približno #FEFF82
                    if (v.getBackground() != null) {
                        v.getBackground().setColorFilter(
                                Color.argb(0, 255, 255, 255),
                                PorterDuff.Mode.SRC_ATOP
                        );
                    }

                    v.animate()
                            .setDuration(0)
                            .start();

                    // Direktno menjamo boju backgrounda kroz filter.
                    if (v.getBackground() != null) {
                        v.getBackground().setColorFilter(
                                Color.argb(65, 255, 255, 255),
                                PorterDuff.Mode.SRC_ATOP
                        );
                    }

                    v.invalidate();
                    break;


                case MotionEvent.ACTION_UP:

                    if (v.getBackground() != null) {
                        v.getBackground().clearColorFilter();
                    }

                    v.invalidate();

                    v.performClick();
                    break;


                case MotionEvent.ACTION_CANCEL:

                    if (v.getBackground() != null) {
                        v.getBackground().clearColorFilter();
                    }

                    v.invalidate();
                    break;
            }

            return true;
        });
    }


    /**
     * Kreira background koji odgovara:
     *
     * background: #CBD868
     * border-radius: 10px
     */
    private static GradientDrawable createButtonBackground(Activity activity) {

        GradientDrawable drawable = new GradientDrawable();

        drawable.setColor(COLOR_ACCENT);
        drawable.setCornerRadius(dp(activity, 10));

        return drawable;
    }


    /**
     * Popup background:
     *
     * background: #1a1a1c;
     * border-radius: 10px;
     * box-shadow: 0 0 0 0.9px #7C874F;
     */
    private static GradientDrawable createPopupBackground(Activity activity) {

        GradientDrawable drawable = new GradientDrawable();

        drawable.setColor(COLOR_POPUP);
        drawable.setCornerRadius(dp(activity, 10));

        drawable.setStroke(
                Math.max(1, dp(activity, 0.9f)),
                COLOR_BORDER
        );

        return drawable;
    }


    /**
     * Input background:
     *
     * background: #1A1A1A;
     * border-radius: 10px;
     * box-shadow: 0 0 0 0.8px #606060;
     */
    private static GradientDrawable createInputBackground(Activity activity) {

        GradientDrawable drawable = new GradientDrawable();

        drawable.setColor(COLOR_INPUT_BACKGROUND);
        drawable.setCornerRadius(dp(activity, 10));

        drawable.setStroke(
                Math.max(1, dp(activity, 0.8f)),
                COLOR_INPUT_BORDER
        );

        return drawable;
    }


    // ============================================================
    // DOWNLOAD DIALOG
    // ============================================================

    public static void showNativeDownloadDialog(
            Activity activity,
            String suggestedFileName,
            String url,
            String mimetype,
            boolean isBlob,
            DownloadCallback callback
    ) {

        activity.runOnUiThread(() -> {

            float density =
                    activity.getResources()
                            .getDisplayMetrics()
                            .density;


            // ====================================================
            // POPUP
            //
            // HTML:
            //
            // width: calc(100% - 80px);
            // max-width: 360px;
            // padding: 14px;
            // ====================================================

            LinearLayout layout = new LinearLayout(activity);

            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER_HORIZONTAL);

            int popupPadding = dp(activity, 14);

            layout.setPadding(
                    popupPadding,
                    popupPadding,
                    popupPadding,
                    popupPadding
            );

            layout.setBackground(
                    createPopupBackground(activity)
            );


            // ====================================================
            // TITLE
            //
            // h3:
            // margin: 0 0 16px 0;
            // color: #CBD868;
            // font-size: 16px;
            // font-weight: 700;
            // text-align: center;
            // ====================================================

            TextView titleView = new TextView(activity);

            titleView.setText("Save File");
            titleView.setTextColor(COLOR_TITLE);
            titleView.setTextSize(16);
            titleView.setGravity(Gravity.CENTER);

            titleView.setTypeface(
                    Typeface.create(
                            "sans-serif",
                            Typeface.BOLD
                    )
            );

            titleView.setIncludeFontPadding(false);

            titleView.setPadding(
                    0,
                    0,
                    0,
                    dp(activity, 16)
            );

            layout.addView(
                    titleView,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );


            // ====================================================
            // LABEL
            //
            // margin-top: 10px;
            // margin-bottom: 1px;
            // margin-left: 6px;
            // font-size: 14px;
            // ====================================================

            TextView labelView = new TextView(activity);

            labelView.setText("File Name:");
            labelView.setTextColor(COLOR_LABEL);
            labelView.setTextSize(14);

            labelView.setIncludeFontPadding(false);

            labelView.setPadding(
                    dp(activity, 6),
                    dp(activity, 10),
                    0,
                    dp(activity, 1)
            );

            layout.addView(
                    labelView,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );


            // ====================================================
            // INPUT
            //
            // padding: 10px 8px;
            // margin: 4px 0 16px 0;
            // border-radius: 10px;
            // background: #1A1A1A;
            // color: #9E9DA4;
            // font-size: 14px;
            // ====================================================

            EditText input = new EditText(activity);

            input.setText(suggestedFileName);

            input.setTextSize(14);
            input.setTextColor(COLOR_INPUT_TEXT);

            input.setSingleLine(true);

            input.setIncludeFontPadding(false);

            input.setGravity(Gravity.CENTER_VERTICAL);

            input.setPadding(
                    dp(activity, 8),
                    dp(activity, 10),
                    dp(activity, 8),
                    dp(activity, 10)
            );

            input.setBackground(
                    createInputBackground(activity)
            );

            input.setSelectAllOnFocus(false);

            LinearLayout.LayoutParams inputParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            inputParams.setMargins(
                    0,
                    dp(activity, 4),
                    0,
                    dp(activity, 16)
            );

            layout.addView(input, inputParams);


            // ====================================================
            // BUTTON ROW
            //
            // display:flex;
            // gap:10px;
            // justify-content:center;
            // ====================================================

            LinearLayout buttonLayout =
                    new LinearLayout(activity);

            buttonLayout.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            buttonLayout.setGravity(
                    Gravity.CENTER_HORIZONTAL |
                    Gravity.CENTER_VERTICAL
            );


            // ====================================================
            // SAVE BUTTON
            //
            // flex: 1 1 auto;
            // min-width: 100px;
            // padding: 10px;
            // radius: 10px;
            // font-size: inherited/default ~14px;
            // font-weight: 600;
            // color: #555;
            // ====================================================

            TextView saveButton =
                    new TextView(activity);

            saveButton.setText("Save");

            saveButton.setTextColor(COLOR_BUTTON_TEXT);
            saveButton.setTextSize(14);

            saveButton.setGravity(Gravity.CENTER);

            saveButton.setIncludeFontPadding(false);

            saveButton.setTypeface(
                    Typeface.create(
                            "sans-serif",
                            Typeface.NORMAL
                    )
            );

            // CSS font-weight: 600
            saveButton.getPaint().setFakeBoldText(true);

            saveButton.setBackground(
                    createButtonBackground(activity)
            );

            saveButton.setPadding(
                    dp(activity, 10),
                    dp(activity, 10),
                    dp(activity, 10),
                    dp(activity, 10)
            );

            saveButton.setMinWidth(
                    dp(activity, 100)
            );

            saveButton.setClickable(true);


            // ====================================================
            // CLOSE BUTTON
            // ====================================================

            TextView closeButton =
                    new TextView(activity);

            closeButton.setText("Close");

            closeButton.setTextColor(COLOR_BUTTON_TEXT);
            closeButton.setTextSize(14);

            closeButton.setGravity(Gravity.CENTER);

            closeButton.setIncludeFontPadding(false);

            closeButton.setTypeface(
                    Typeface.create(
                            "sans-serif",
                            Typeface.NORMAL
                    )
            );

            // CSS font-weight: 600
            closeButton.getPaint().setFakeBoldText(true);

            closeButton.setBackground(
                    createButtonBackground(activity)
            );

            closeButton.setPadding(
                    dp(activity, 10),
                    dp(activity, 10),
                    dp(activity, 10),
                    dp(activity, 10)
            );

            closeButton.setMinWidth(
                    dp(activity, 100)
            );

            closeButton.setClickable(true);


            // ====================================================
            // BUTTON WIDTH
            //
            // HTML:
            //
            // flex: 1 1 auto;
            // gap: 10px;
            //
            // Zato oba dobijaju jednaku dostupnu širinu.
            // ====================================================

            LinearLayout.LayoutParams saveParams =
                    new LinearLayout.LayoutParams(
                            0,
                            dp(activity, 38),
                            1f
                    );

            saveParams.setMargins(
                    0,
                    0,
                    dp(activity, 10),
                    0
            );

            buttonLayout.addView(
                    saveButton,
                    saveParams
            );


            LinearLayout.LayoutParams closeParams =
                    new LinearLayout.LayoutParams(
                            0,
                            dp(activity, 38),
                            1f
                    );

            buttonLayout.addView(
                    closeButton,
                    closeParams
            );


            layout.addView(
                    buttonLayout,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );


            // ====================================================
            // DIALOG
            // ====================================================

            AlertDialog dialog =
                    new AlertDialog.Builder(activity)
                            .setView(layout)
                            .setCancelable(false)
                            .create();


            if (dialog.getWindow() != null) {

                dialog.getWindow()
                        .setBackgroundDrawableResource(
                                android.R.color.transparent
                        );
            }


            // ====================================================
            // BUTTON EFFECT
            // ====================================================

            applyButtonTouchEffect(saveButton);
            applyButtonTouchEffect(closeButton);


            // ====================================================
            // BUTTON ACTIONS
            // ====================================================

            closeButton.setOnClickListener(v ->
                    dialog.dismiss()
            );


            saveButton.setOnClickListener(v -> {

                String finalName =
                        input.getText()
                                .toString()
                                .trim();

                if (finalName.isEmpty()) {
                    finalName = suggestedFileName;
                }

                callback.onSave(finalName);

                dialog.dismiss();
            });


            // ====================================================
            // SHOW
            // ====================================================

            dialog.show();


            // ====================================================
            // DIM
            //
            // HTML background = #070707
            //
            // Android dimAmount 0.08 približno simulira
            // pozadinsko zatamnjenje koje sada imaš.
            // ====================================================

            if (dialog.getWindow() != null) {

                WindowManager.LayoutParams params =
                        dialog.getWindow().getAttributes();

                params.dimAmount = 0.08f;

                dialog.getWindow()
                        .setAttributes(params);
            }


            // ====================================================
            // WIDTH
            //
            // HTML:
            //
            // width: calc(100% - 80px);
            // max-width: 360px;
            //
            // ====================================================

            if (dialog.getWindow() != null) {

                WindowManager.LayoutParams lp =
                        dialog.getWindow().getAttributes();

                int screenWidth =
                        activity.getResources()
                                .getDisplayMetrics()
                                .widthPixels;

                int desiredWidth =
                        screenWidth - dp(activity, 80);

                int maxWidth =
                        dp(activity, 360);

                lp.width =
                        Math.min(
                                desiredWidth,
                                maxWidth
                        );

                lp.height =
                        WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow()
                        .setAttributes(lp);
            }
        });
    }


    // ============================================================
    // ALERT DIALOG
    // ============================================================

    public static void showCustomAlert(
            Activity activity,
            String message,
            JsResult result
    ) {

        activity.runOnUiThread(() -> {

            // ====================================================
            // POPUP
            //
            // width: calc(100% - 100px);
            // max-width: 420px;
            // padding: 12px;
            // ====================================================

            LinearLayout layout =
                    new LinearLayout(activity);

            layout.setOrientation(
                    LinearLayout.VERTICAL
            );

            layout.setGravity(
                    Gravity.CENTER_HORIZONTAL
            );

            int popupPadding =
                    dp(activity, 12);

            layout.setPadding(
                    popupPadding,
                    popupPadding,
                    popupPadding,
                    popupPadding
            );

            layout.setBackground(
                    createPopupBackground(activity)
            );


            // ====================================================
            // TITLE
            //
            // margin-top: 3px;
            // margin-bottom: 16px;
            // font-size: 16px;
            // color: #CBD868;
            // text-align: center;
            // ====================================================

            TextView titleView =
                    new TextView(activity);

            titleView.setText("Alert!");

            titleView.setTextColor(
                    COLOR_TITLE
            );

            titleView.setTextSize(16);

            titleView.setGravity(
                    Gravity.CENTER
            );

            titleView.setIncludeFontPadding(false);

            titleView.setTypeface(
                    Typeface.create(
                            "sans-serif",
                            Typeface.BOLD
                    )
            );

            titleView.setPadding(
                    0,
                    dp(activity, 3),
                    0,
                    dp(activity, 16)
            );

            layout.addView(
                    titleView,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );


            // ====================================================
            // MESSAGE
            //
            // p:
            //
            // margin: 20px 0 32px 6px;
            // font-size: 15px;
            // color: #BAB9BF;
            // line-height: 1.4;
            // ====================================================

            TextView messageView =
                    new TextView(activity);

            messageView.setText(message);

            messageView.setTextColor(
                    COLOR_MESSAGE
            );

            messageView.setTextSize(15);

            messageView.setGravity(
                    Gravity.START
            );

            messageView.setIncludeFontPadding(false);

            // Android lineSpacingExtra / lineSpacingMultiplier
            // približno odgovara CSS line-height: 1.4
            messageView.setLineSpacing(
                    0,
                    1.4f
            );

            messageView.setPadding(
                    dp(activity, 6),
                    dp(activity, 20),
                    0,
                    dp(activity, 32)
            );

            LinearLayout.LayoutParams messageParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            layout.addView(
                    messageView,
                    messageParams
            );


            // ====================================================
            // BUTTON ROW
            //
            // justify-content: flex-end;
            // gap: 10px;
            // margin-top: 12px;
            // ====================================================

            LinearLayout buttonLayout =
                    new LinearLayout(activity);

            buttonLayout.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            buttonLayout.setGravity(
                    Gravity.END |
                    Gravity.CENTER_VERTICAL
            );

            LinearLayout.LayoutParams buttonRowParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            buttonRowParams.setMargins(
                    0,
                    dp(activity, 12),
                    0,
                    0
            );


            // ====================================================
            // CLOSE BUTTON
            //
            // width: 90px;
            // padding: 10px;
            // font-size: 13.5px;
            // font-weight: 500;
            // color: #000;
            // ====================================================

            TextView closeButton =
                    new TextView(activity);

            closeButton.setText("Close");

            closeButton.setTextColor(
                    COLOR_ALERT_BUTTON_TEXT
            );

            closeButton.setTextSize(13.5f);

            closeButton.setGravity(
                    Gravity.CENTER
            );

            closeButton.setIncludeFontPadding(false);

            closeButton.setTypeface(
                    Typeface.create(
                            "sans-serif",
                            Typeface.NORMAL
                    )
            );

            // CSS font-weight: 500
            closeButton.getPaint()
                    .setFakeBoldText(true);

            closeButton.setPadding(
                    dp(activity, 10),
                    dp(activity, 10),
                    dp(activity, 10),
                    dp(activity, 10)
            );

            closeButton.setBackground(
                    createButtonBackground(activity)
            );

            closeButton.setClickable(true);


            LinearLayout.LayoutParams closeParams =
                    new LinearLayout.LayoutParams(
                            dp(activity, 90),
                            dp(activity, 38)
                    );

            buttonLayout.addView(
                    closeButton,
                    closeParams
            );


            layout.addView(
                    buttonLayout,
                    buttonRowParams
            );


            // ====================================================
            // DIALOG
            // ====================================================

            AlertDialog dialog =
                    new AlertDialog.Builder(activity)
                            .setView(layout)
                            .setCancelable(false)
                            .create();


            if (dialog.getWindow() != null) {

                dialog.getWindow()
                        .setBackgroundDrawableResource(
                                android.R.color.transparent
                        );
            }


            // ====================================================
            // BUTTON TOUCH
            // ====================================================

            applyButtonTouchEffect(closeButton);


            // ====================================================
            // CLOSE
            // ====================================================

            closeButton.setOnClickListener(v -> {

                result.confirm();

                dialog.dismiss();
            });


            // ====================================================
            // SHOW
            // ====================================================

            dialog.show();


            // ====================================================
            // DIM
            // ====================================================

            if (dialog.getWindow() != null) {

                WindowManager.LayoutParams params =
                        dialog.getWindow().getAttributes();

                params.dimAmount = 0.08f;

                dialog.getWindow()
                        .setAttributes(params);
            }


            // ====================================================
            // WIDTH
            //
            // HTML:
            //
            // width: calc(100% - 100px);
            // max-width: 420px;
            //
            // Height remains WRAP_CONTENT so the dialog can
            // grow vertically with longer messages.
            // ====================================================

            if (dialog.getWindow() != null) {

                WindowManager.LayoutParams lp =
                        dialog.getWindow().getAttributes();

                int screenWidth =
                        activity.getResources()
                                .getDisplayMetrics()
                                .widthPixels;

                int desiredWidth =
                        screenWidth - dp(activity, 100);

                int maxWidth =
                        dp(activity, 420);

                lp.width =
                        Math.min(
                                desiredWidth,
                                maxWidth
                        );

                lp.height =
                        WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow()
                        .setAttributes(lp);
            }
        });
    }


    // ============================================================
    // CALLBACK
    // ============================================================

    public interface DownloadCallback {

        void onSave(String fileName);
    }
}
