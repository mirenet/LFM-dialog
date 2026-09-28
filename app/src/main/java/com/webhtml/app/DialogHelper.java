package com.webhtml.app;

import android.app.Activity;
import android.graphics.Color;
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
    // COLORS - iste vrednosti kao u HTML-u
    // ============================================================

    private static final int POPUP_BG = Color.parseColor("#1A1A1C");
    private static final int BORDER = Color.parseColor("#7C874F");
    private static final int ACCENT = Color.parseColor("#CBD868");

    private static final int TITLE_COLOR = Color.parseColor("#CBD868");
    private static final int LABEL_COLOR = Color.parseColor("#DADADA");
    private static final int MESSAGE_COLOR = Color.parseColor("#BAB9BF");

    private static final int INPUT_BG = Color.parseColor("#1A1A1A");
    private static final int INPUT_TEXT = Color.parseColor("#9E9DA4");
    private static final int INPUT_BORDER = Color.parseColor("#606060");

    private static final int BUTTON_TEXT = Color.parseColor("#555555");


    // ============================================================
    // DP
    // ============================================================

    private static int dp(Activity activity, float value) {
        return Math.round(
                value * activity.getResources()
                        .getDisplayMetrics()
                        .density
        );
    }


    // ============================================================
    // POPUP BACKGROUND
    // ============================================================

    private static GradientDrawable createPopupBackground(
            Activity activity
    ) {
        GradientDrawable drawable = new GradientDrawable();

        drawable.setColor(POPUP_BG);
        drawable.setCornerRadius(dp(activity, 10));

        drawable.setStroke(
                Math.max(1, dp(activity, 0.9f)),
                BORDER
        );

        return drawable;
    }


    // ============================================================
    // BUTTON BACKGROUND
    //
    // Normal:
    // #CBD868
    //
    // :active brightness(1.25):
    // približno #F0FF82
    // ============================================================

    private static StateListDrawable createButtonBackground(
            Activity activity
    ) {
        StateListDrawable states = new StateListDrawable();

        GradientDrawable pressed = new GradientDrawable();
        pressed.setColor(Color.parseColor("#F0FF82"));
        pressed.setCornerRadius(dp(activity, 10));

        GradientDrawable normal = new GradientDrawable();
        normal.setColor(ACCENT);
        normal.setCornerRadius(dp(activity, 10));

        states.addState(
                new int[]{android.R.attr.state_pressed},
                pressed
        );

        states.addState(
                new int[]{},
                normal
        );

        return states;
    }


    // ============================================================
    // INPUT BACKGROUND
    // ============================================================

    private static GradientDrawable createInputBackground(
            Activity activity
    ) {
        GradientDrawable drawable = new GradientDrawable();

        drawable.setColor(INPUT_BG);
        drawable.setCornerRadius(dp(activity, 10));

        drawable.setStroke(
                Math.max(1, dp(activity, 0.8f)),
                INPUT_BORDER
        );

        return drawable;
    }


    // ============================================================
    // BUTTON TOUCH
    //
    // StateListDrawable već radi visualni pressed state.
    //
    // Ovde samo obezbeđujemo da Android pravilno tretira
    // dodir i click.
    // ============================================================

    private static void applyButtonTouchEffect(View button) {

        button.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    v.setPressed(true);
                    return false;

                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    return false;

                case MotionEvent.ACTION_CANCEL:
                    v.setPressed(false);
                    return false;
            }

            return false;
        });
    }


    // ============================================================
    // ALERT
    // ============================================================

    public static void showCustomAlert(
            Activity activity,
            String message,
            JsResult result
    ) {

        activity.runOnUiThread(() -> {

            // ----------------------------------------------------
            // POPUP
            //
            // HTML:
            //
            // width: calc(100% - 100px)
            // max-width: 420px
            // padding: 12px
            // ----------------------------------------------------

            LinearLayout layout =
                    new LinearLayout(activity);

            layout.setOrientation(
                    LinearLayout.VERTICAL
            );

            layout.setGravity(
                    Gravity.CENTER_HORIZONTAL
            );

            int padding = dp(activity, 12);

            layout.setPadding(
                    padding,
                    padding,
                    padding,
                    padding
            );

            layout.setBackground(
                    createPopupBackground(activity)
            );


            // ----------------------------------------------------
            // TITLE
            //
            // HTML:
            //
            // font-size: 16px
            // font-weight: 700
            // margin-top: 3px
            // margin-bottom: 16px
            // ----------------------------------------------------

            TextView titleView =
                    new TextView(activity);

            titleView.setText("Alert!");
            titleView.setTextColor(TITLE_COLOR);

            titleView.setTextSize(16);

            titleView.setGravity(
                    Gravity.CENTER
            );

            titleView.setTypeface(
                    Typeface.create(
                            "sans-serif-bold",
                            Typeface.NORMAL
                    )
            );

            titleView.setIncludeFontPadding(false);

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


            // ----------------------------------------------------
            // MESSAGE
            //
            // HTML:
            //
            // margin: 20px 0 32px 6px
            // font-size: 15px
            // line-height: 1.4
            // ----------------------------------------------------

            TextView messageView =
                    new TextView(activity);

            messageView.setText(message);
            messageView.setTextColor(MESSAGE_COLOR);

            messageView.setTextSize(15);

            messageView.setGravity(
                    Gravity.START
            );

            messageView.setIncludeFontPadding(false);

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

            layout.addView(
                    messageView,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );


            // ----------------------------------------------------
            // BUTTON ROW
            //
            // HTML:
            //
            // margin-top: 12px
            // justify-content: flex-end
            // gap: 10px
            // ----------------------------------------------------

            LinearLayout buttonLayout =
                    new LinearLayout(activity);

            buttonLayout.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            buttonLayout.setGravity(
                    Gravity.END |
                    Gravity.CENTER_VERTICAL
            );

            LinearLayout.LayoutParams rowParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            rowParams.setMargins(
                    0,
                    dp(activity, 12),
                    0,
                    0
            );


            // ----------------------------------------------------
            // CLOSE BUTTON
            //
            // HTML:
            //
            // width: 90px
            // padding: 10px
            // font-size: 13.5px
            // font-weight: 500
            // color: #000
            // ----------------------------------------------------

            TextView closeButton =
                    new TextView(activity);

            closeButton.setText("Close");

            closeButton.setTextColor(Color.BLACK);

            closeButton.setTextSize(13.5f);

            closeButton.setGravity(
                    Gravity.CENTER
            );

            closeButton.setIncludeFontPadding(false);

            closeButton.setTypeface(
                    Typeface.create(
                            "sans-serif-medium",
                            Typeface.NORMAL
                    )
            );

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
            closeButton.setFocusable(true);


            LinearLayout.LayoutParams closeParams =
                    new LinearLayout.LayoutParams(
                            dp(activity, 90),
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            buttonLayout.addView(
                    closeButton,
                    closeParams
            );

            layout.addView(
                    buttonLayout,
                    rowParams
            );


            // ----------------------------------------------------
            // DIALOG
            // ----------------------------------------------------

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


            // ----------------------------------------------------
            // CLICK
            // ----------------------------------------------------

            closeButton.setOnClickListener(v -> {

                result.confirm();
                dialog.dismiss();

            });


            // ----------------------------------------------------
            // SHOW
            // ----------------------------------------------------

            dialog.show();


            // ----------------------------------------------------
            // DIM
            // ----------------------------------------------------

            if (dialog.getWindow() != null) {

                WindowManager.LayoutParams params =
                        dialog.getWindow().getAttributes();

                params.dimAmount = 0.08f;

                dialog.getWindow()
                        .setAttributes(params);
            }


            // ----------------------------------------------------
            // WIDTH
            //
            // HTML:
            //
            // width: calc(100% - 100px)
            // max-width: 420px
            //
            // HEIGHT:
            //
            // auto / wrap-content
            //
            // Zbog toga se alert širi kada je poruka duža.
            // ----------------------------------------------------

            if (dialog.getWindow() != null) {

                WindowManager.LayoutParams lp =
                        dialog.getWindow().getAttributes();

                int screenWidth =
                        activity.getResources()
                                .getDisplayMetrics()
                                .widthPixels;

                int requestedWidth =
                        screenWidth - dp(activity, 100);

                int maxWidth =
                        dp(activity, 420);

                lp.width = Math.min(
                        requestedWidth,
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
    // DOWNLOAD
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

            // ----------------------------------------------------
            // POPUP
            //
            // HTML:
            //
            // width: calc(100% - 80px)
            // max-width: 360px
            // padding: 14px
            // ----------------------------------------------------

            LinearLayout layout =
                    new LinearLayout(activity);

            layout.setOrientation(
                    LinearLayout.VERTICAL
            );

            layout.setGravity(
                    Gravity.CENTER_HORIZONTAL
            );

            int padding = dp(activity, 14);

            layout.setPadding(
                    padding,
                    padding,
                    padding,
                    padding
            );

            layout.setBackground(
                    createPopupBackground(activity)
            );


            // ----------------------------------------------------
            // TITLE
            // ----------------------------------------------------

            TextView titleView =
                    new TextView(activity);

            titleView.setText("Save File");

            titleView.setTextColor(TITLE_COLOR);

            titleView.setTextSize(16);

            titleView.setGravity(
                    Gravity.CENTER
            );

            titleView.setTypeface(
                    Typeface.create(
                            "sans-serif-bold",
                            Typeface.NORMAL
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


            // ----------------------------------------------------
            // LABEL
            //
            // margin-left: 6px
            // margin-top: 10px
            // margin-bottom: 1px
            // ----------------------------------------------------

            TextView labelView =
                    new TextView(activity);

            labelView.setText("File Name:");

            labelView.setTextColor(LABEL_COLOR);

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


            // ----------------------------------------------------
            // INPUT
            // ----------------------------------------------------

            EditText input =
                    new EditText(activity);

            input.setText(suggestedFileName);

            input.setTextSize(14);

            input.setTextColor(INPUT_TEXT);

            input.setSingleLine(true);

            input.setIncludeFontPadding(false);

            input.setGravity(
                    Gravity.CENTER_VERTICAL
            );

            input.setPadding(
                    dp(activity, 8),
                    dp(activity, 10),
                    dp(activity, 8),
                    dp(activity, 10)
            );

            input.setBackground(
                    createInputBackground(activity)
            );


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

            layout.addView(
                    input,
                    inputParams
            );


            // ----------------------------------------------------
            // BUTTON ROW
            //
            // gap: 10px
            // justify-content: center
            // ----------------------------------------------------

            LinearLayout buttonLayout =
                    new LinearLayout(activity);

            buttonLayout.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            buttonLayout.setGravity(
                    Gravity.CENTER_HORIZONTAL |
                    Gravity.CENTER_VERTICAL
            );


            // ----------------------------------------------------
            // SAVE BUTTON
            //
            // font-weight: 600
            // font-size: 14px
            // color: #555
            // padding: 10px
            // min-width: 100px
            // ----------------------------------------------------

            TextView saveButton =
                    new TextView(activity);

            saveButton.setText("Save");

            saveButton.setTextColor(BUTTON_TEXT);

            saveButton.setTextSize(14);

            saveButton.setGravity(
                    Gravity.CENTER
            );

            saveButton.setIncludeFontPadding(false);

            saveButton.setTypeface(
                    Typeface.create(
                            "sans-serif-medium",
                            Typeface.NORMAL
                    )
            );

            saveButton.setPadding(
                    dp(activity, 10),
                    dp(activity, 10),
                    dp(activity, 10),
                    dp(activity, 10)
            );

            saveButton.setBackground(
                    createButtonBackground(activity)
            );

            saveButton.setClickable(true);
            saveButton.setFocusable(true);


            // ----------------------------------------------------
            // CLOSE BUTTON
            // ----------------------------------------------------

            TextView closeButton =
                    new TextView(activity);

            closeButton.setText("Close");

            closeButton.setTextColor(BUTTON_TEXT);

            closeButton.setTextSize(14);

            closeButton.setGravity(
                    Gravity.CENTER
            );

            closeButton.setIncludeFontPadding(false);

            closeButton.setTypeface(
                    Typeface.create(
                            "sans-serif-medium",
                            Typeface.NORMAL
                    )
            );

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
            closeButton.setFocusable(true);


            // ----------------------------------------------------
            // BUTTON WIDTH
            //
            // HTML:
            //
            // flex: 1 1 auto
            // min-width: 100px
            // gap: 10px
            //
            // ----------------------------------------------------

            LinearLayout.LayoutParams saveParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                    );

            saveParams.setMargins(
                    0,
                    0,
                    dp(activity, 10),
                    0
            );

            saveButton.setMinWidth(
                    dp(activity, 100)
            );

            buttonLayout.addView(
                    saveButton,
                    saveParams
            );


            LinearLayout.LayoutParams closeParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                    );

            closeButton.setMinWidth(
                    dp(activity, 100)
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


            // ----------------------------------------------------
            // DIALOG
            // ----------------------------------------------------

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


            // ----------------------------------------------------
            // TOUCH EFFECT
            // ----------------------------------------------------

            applyButtonTouchEffect(saveButton);
            applyButtonTouchEffect(closeButton);


            // ----------------------------------------------------
            // CLOSE
            // ----------------------------------------------------

            closeButton.setOnClickListener(v ->
                    dialog.dismiss()
            );


            // ----------------------------------------------------
            // SAVE
            // ----------------------------------------------------

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


            // ----------------------------------------------------
            // SHOW
            // ----------------------------------------------------

            dialog.show();


            // ----------------------------------------------------
            // DIM
            // ----------------------------------------------------

            if (dialog.getWindow() != null) {

                WindowManager.LayoutParams params =
                        dialog.getWindow().getAttributes();

                params.dimAmount = 0.08f;

                dialog.getWindow()
                        .setAttributes(params);
            }


            // ----------------------------------------------------
            // WIDTH
            //
            // HTML:
            //
            // width: calc(100% - 80px)
            // max-width: 360px
            //
            // ----------------------------------------------------

            if (dialog.getWindow() != null) {

                WindowManager.LayoutParams lp =
                        dialog.getWindow().getAttributes();

                int screenWidth =
                        activity.getResources()
                                .getDisplayMetrics()
                                .widthPixels;

                int requestedWidth =
                        screenWidth - dp(activity, 80);

                int maxWidth =
                        dp(activity, 360);

                lp.width = Math.min(
                        requestedWidth,
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
