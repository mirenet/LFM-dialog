package com.webhtml.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

public class DialogHelper {

    // ============================================================
    // DIALOG STIL
    // ============================================================

    // Glavna boja prozora
    private static final String DIALOG_BACKGROUND = "#191919";

    // Suptilan okvir oko prozora
    private static final String DIALOG_BORDER = "#353535";

    // Boja naslova
    private static final String TITLE_COLOR = "#C5C5C5";

    // Glavni tekst
    private static final String MAIN_TEXT_COLOR = "#C5C5C7";

    // Sekundarni tekst
    private static final String SECONDARY_TEXT_COLOR = "#A4A4A6";

    // Linija ispod inputa
    private static final String INPUT_LINE_COLOR = "#303032";

    // Plava boja dugmadi
    private static final String BUTTON_TEXT_COLOR = "#7890C5";

    // Pozadina dugmeta kada se pritisne
    private static final String BUTTON_PRESSED_COLOR = "#303033";


    // ============================================================
    // GEOMETRIJA
    // ============================================================

    /*
     * Ekran sa screenshota:
     *
     * 1080 x 2340 px
     *
     * Dialog je približno 890 px širok.
     *
     * 890 / 1080 = 0.824
     *
     * Zato koristimo oko 82.4% širine ekrana.
     */

    private static final float DIALOG_WIDTH_RATIO = 0.824f;


    /*
     * Ovo ostavljamo kao referentnu vrednost za uređaje
     * koji koriste density oko 3.
     */
    private static final float DIALOG_MARGIN_HORIZONTAL_DP = 32f;


    /*
     * Zaobljenje uglova.
     *
     * 14dp je približno izgled koji se vidi na screenshotu.
     */
    private static final float DIALOG_RADIUS_DP = 14f;


    // Debljina okvira
    private static final float DIALOG_BORDER_DP = 1f;


    // ============================================================
    // ALERT
    // ============================================================

    public static void showCustomAlert(
            Activity activity,
            String message,
            JsResult result) {

        activity.runOnUiThread(() -> {

            float density = activity.getResources()
                    .getDisplayMetrics()
                    .density;


            final FrameLayout overlayContainer =
                    new FrameLayout(activity);


            // ====================================================
            // ZATAMNJENJE POZADINE
            // ====================================================

            overlayContainer.setBackgroundColor(
                    Color.parseColor("#80000000")
            );

            overlayContainer.setClickable(true);
            overlayContainer.setFocusable(true);


            // ====================================================
            // GLAVNI DIALOG
            // ====================================================

            LinearLayout layout =
                    new LinearLayout(activity);

            layout.setOrientation(
                    LinearLayout.VERTICAL
            );


            int padHorizontal =
                    (int) (12 * density);


            layout.setPadding(
                    padHorizontal,
                    0,
                    padHorizontal,
                    0
            );


            // ====================================================
            // POZADINA + OKVIR
            // ====================================================

            GradientDrawable backgroundDrawable =
                    new GradientDrawable();


            backgroundDrawable.setColor(
                    Color.parseColor(
                            DIALOG_BACKGROUND
                    )
            );


            backgroundDrawable.setCornerRadius(
                    DIALOG_RADIUS_DP * density
            );


            backgroundDrawable.setStroke(
                    (int) (DIALOG_BORDER_DP * density),
                    Color.parseColor(
                            DIALOG_BORDER
                    )
            );


            layout.setBackground(
                    backgroundDrawable
            );


            // ====================================================
            // NASLOV
            // ====================================================

            TextView titleView =
                    new TextView(activity);


            titleView.setText(
                    "Alert!"
            );


            titleView.setTextColor(
                    Color.parseColor(
                            TITLE_COLOR
                    )
            );


            titleView.setTextSize(
                    16
            );


            titleView.setTypeface(
                    Typeface.create(
                            "sans-serif",
                            Typeface.BOLD
                    )
            );


            titleView.setGravity(
                    Gravity.CENTER
            );


            titleView.setIncludeFontPadding(
                    false
            );


            LinearLayout.LayoutParams titleParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            titleParams.setMargins(
                    0,
                    (int) (16 * density),
                    0,
                    (int) (14 * density)
            );


            layout.addView(
                    titleView,
                    titleParams
            );


            // ====================================================
            // PORUKA
            // ====================================================

            TextView messageView =
                    new TextView(activity);


            messageView.setText(
                    message
            );


            messageView.setTextColor(
                    Color.parseColor(
                            MAIN_TEXT_COLOR
                    )
            );


            messageView.setTextSize(
                    14
            );


            messageView.setLineSpacing(
                    0,
                    1.35f
            );


            messageView.setGravity(
                    Gravity.START
            );


            messageView.setIncludeFontPadding(
                    false
            );


            messageView.setPadding(
                    (int) (1 * density),
                    (int) (4 * density),
                    (int) (1 * density),
                    (int) (8 * density)
            );


            layout.addView(
                    messageView,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );


            // ====================================================
            // CLOSE DUGME
            // ====================================================

            TextView closeButton =
                    createStyledButton(
                            activity,
                            "Close",
                            BUTTON_TEXT_COLOR,
                            14f,
                            density
                    );


            LinearLayout buttonLayout =
                    new LinearLayout(activity);


            buttonLayout.setOrientation(
                    LinearLayout.HORIZONTAL
            );


            buttonLayout.setGravity(
                    Gravity.END |
                    Gravity.CENTER_VERTICAL
            );


            buttonLayout.setPadding(
                    0,
                    (int) (4 * density),
                    0,
                    (int) (8 * density)
            );


            buttonLayout.addView(
                    closeButton,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );


            layout.addView(
                    buttonLayout
            );


            // ====================================================
            // DIMENZIJE DIALOGA
            // ====================================================

            FrameLayout.LayoutParams params =
                    createDialogLayoutParams(
                            activity
                    );


            overlayContainer.addView(
                    layout,
                    params
            );


            // ====================================================
            // ROOT
            // ====================================================

            ViewGroup rootLayout =
                    activity.findViewById(
                            android.R.id.content
                    );


            rootLayout.addView(
                    overlayContainer,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    )
            );


            // ====================================================
            // POSTOJEĆA FUNKCIONALNOST
            // ====================================================

            closeButton.setOnClickListener(v -> {

                result.confirm();

                rootLayout.removeView(
                        overlayContainer
                );
            });
        });
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
            DownloadCallback callback) {

        activity.runOnUiThread(() -> {

            float density = activity.getResources()
                    .getDisplayMetrics()
                    .density;


            final FrameLayout overlayContainer =
                    new FrameLayout(activity);


            overlayContainer.setBackgroundColor(
                    Color.parseColor(
                            "#80000000"
                    )
            );


            overlayContainer.setClickable(true);
            overlayContainer.setFocusable(true);


            // ====================================================
            // GLAVNI DIALOG
            // ====================================================

            final LinearLayout layout =
                    new LinearLayout(activity);


            layout.setOrientation(
                    LinearLayout.VERTICAL
            );


            layout.setFocusable(true);

            layout.setFocusableInTouchMode(true);


            int padHorizontal =
                    (int) (12 * density);


            layout.setPadding(
                    padHorizontal,
                    0,
                    padHorizontal,
                    0
            );


            // ====================================================
            // POZADINA + OKVIR
            // ====================================================

            GradientDrawable backgroundDrawable =
                    new GradientDrawable();


            backgroundDrawable.setColor(
                    Color.parseColor(
                            DIALOG_BACKGROUND
                    )
            );


            backgroundDrawable.setCornerRadius(
                    DIALOG_RADIUS_DP * density
            );


            backgroundDrawable.setStroke(
                    (int) (DIALOG_BORDER_DP * density),
                    Color.parseColor(
                            DIALOG_BORDER
                    )
            );


            layout.setBackground(
                    backgroundDrawable
            );


            // ====================================================
            // NASLOV
            // ====================================================

            TextView titleView =
                    new TextView(activity);


            titleView.setText(
                    "Save File"
            );


            titleView.setTextColor(
                    Color.parseColor(
                            TITLE_COLOR
                    )
            );


            titleView.setTextSize(
                    16
            );


            titleView.setTypeface(
                    Typeface.create(
                            "sans-serif",
                            Typeface.BOLD
                    )
            );


            titleView.setGravity(
                    Gravity.CENTER
            );


            titleView.setIncludeFontPadding(
                    false
            );


            LinearLayout.LayoutParams titleParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            titleParams.setMargins(
                    0,
                    (int) (16 * density),
                    0,
                    (int) (13 * density)
            );


            layout.addView(
                    titleView,
                    titleParams
            );


            // ====================================================
            // FILE NAME LABEL
            // ====================================================

            TextView labelView =
                    new TextView(activity);


            labelView.setText(
                    "File Name:"
            );


            labelView.setTextColor(
                    Color.parseColor(
                            SECONDARY_TEXT_COLOR
                    )
            );


            labelView.setTextSize(
                    14
            );


            labelView.setIncludeFontPadding(
                    false
            );


            labelView.setPadding(
                    (int) (1 * density),
                    (int) (4 * density),
                    0,
                    (int) (4 * density)
            );


            layout.addView(
                    labelView
            );


            // ====================================================
            // INPUT
            // ====================================================

            final EditText input =
                    new EditText(activity);


            input.setText(
                    suggestedFileName
            );


            input.setTextSize(
                    14
            );


            input.setTextColor(
                    Color.parseColor(
                            MAIN_TEXT_COLOR
                    )
            );


            input.setSingleLine(
                    true
            );


            input.setIncludeFontPadding(
                    false
            );


            input.setPadding(
                    (int) (1 * density),
                    (int) (5 * density),
                    (int) (1 * density),
                    (int) (5 * density)
            );


            // ====================================================
            // CURSOR
            // ====================================================

            setCursorColorAndWidth(
                    input,
                    Color.parseColor(
                            "#8A8A8C"
                    ),
                    (int) (1.5f * density)
            );


            // ====================================================
            // TRANSPARENT INPUT
            // ====================================================

            input.setBackgroundColor(
                    Color.TRANSPARENT
            );


            LinearLayout.LayoutParams inputParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            layout.addView(
                    input,
                    inputParams
            );


            // ====================================================
            // DONJA LINIJA INPUTA
            // ====================================================

            View inputLine =
                    new View(activity);


            inputLine.setBackgroundColor(
                    Color.parseColor(
                            INPUT_LINE_COLOR
                    )
            );


            LinearLayout.LayoutParams inputLineParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            (int) (1 * density)
                    );


            inputLineParams.setMargins(
                    0,
                    0,
                    0,
                    (int) (12 * density)
            );


            layout.addView(
                    inputLine,
                    inputLineParams
            );


            // ====================================================
            // SAVE DUGME
            // ====================================================

            TextView saveButton =
                    createStyledButton(
                            activity,
                            "Save",
                            BUTTON_TEXT_COLOR,
                            14f,
                            density
                    );


            // ====================================================
            // CLOSE DUGME
            // ====================================================

            TextView closeButton =
                    createStyledButton(
                            activity,
                            "Close",
                            BUTTON_TEXT_COLOR,
                            14f,
                            density
                    );


            // ====================================================
            // BUTTON LAYOUT
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


            buttonLayout.setPadding(
                    0,
                    0,
                    0,
                    (int) (8 * density)
            );


            // ====================================================
            // SAVE PARAMS
            // ====================================================

            LinearLayout.LayoutParams saveParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            saveParams.weight = 1;


            saveParams.setMargins(
                    0,
                    0,
                    (int) (4 * density),
                    0
            );


            saveButton.setLayoutParams(
                    saveParams
            );


            // ====================================================
            // CLOSE PARAMS
            // ====================================================

            LinearLayout.LayoutParams closeParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            closeParams.weight = 1;


            closeParams.setMargins(
                    (int) (4 * density),
                    0,
                    0,
                    0
            );


            closeButton.setLayoutParams(
                    closeParams
            );


            // ====================================================
            // DODAVANJE DUGMADI
            // ====================================================

            buttonLayout.addView(
                    saveButton
            );


            buttonLayout.addView(
                    closeButton
            );


            layout.addView(
                    buttonLayout
            );


            input.clearFocus();


            // ====================================================
            // DIMENZIJE DIALOGA
            // ====================================================

            FrameLayout.LayoutParams params =
                    createDialogLayoutParams(
                            activity
                    );


            overlayContainer.addView(
                    layout,
                    params
            );


            // ====================================================
            // ROOT
            // ====================================================

            ViewGroup rootLayout =
                    activity.findViewById(
                            android.R.id.content
                    );


            rootLayout.addView(
                    overlayContainer,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    )
            );


            // ====================================================
            // CLOSE
            // ====================================================

            closeButton.setOnClickListener(
                    v -> rootLayout.removeView(
                            overlayContainer
                    )
            );


            // ====================================================
            // SAVE
            // ====================================================

            saveButton.setOnClickListener(v -> {

                String finalName =
                        input.getText()
                                .toString()
                                .trim();


                if (finalName.isEmpty()) {

                    finalName =
                            suggestedFileName;
                }


                callback.onSave(
                        finalName
                );


                rootLayout.removeView(
                        overlayContainer
                );
            });
        });
    }


    // ============================================================
    // DIMENZIJE DIALOGA
    // ============================================================

    private static FrameLayout.LayoutParams createDialogLayoutParams(
            Activity activity) {

        /*
         * Koristimo stvarnu širinu ekrana.
         *
         * Za 1080 px:
         *
         * 1080 x 0.824 = ~890 px
         *
         * što odgovara screenshotu.
         */

        int screenWidth =
                activity.getResources()
                        .getDisplayMetrics()
                        .widthPixels;


        int dialogWidth =
                (int) (
                        screenWidth *
                        DIALOG_WIDTH_RATIO
                );


        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(
                        dialogWidth,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );


        params.gravity =
                Gravity.CENTER;


        return params;
    }


    // ============================================================
    // STIL DUGMETA
    // ============================================================

    private static TextView createStyledButton(
            Activity activity,
            String text,
            String textColorHex,
            float textSizeSp,
            float density) {

        TextView button =
                new TextView(activity);


        // ========================================================
        // TEKST
        // ========================================================

        button.setText(
                text
        );


        button.setTextColor(
                Color.parseColor(
                        textColorHex
                )
        );


        button.setTextSize(
                textSizeSp
        );


        button.setTypeface(
                Typeface.create(
                        "sans-serif-medium",
                        Typeface.NORMAL
                )
        );


        button.setGravity(
                Gravity.CENTER
        );


        button.setIncludeFontPadding(
                false
        );


        button.getPaint()
                .setSubpixelText(
                        true
                );


        // ========================================================
        // NORMALNO STANJE
        // ========================================================

        button.setBackgroundColor(
                Color.TRANSPARENT
        );


        // ========================================================
        // PADDING
        // ========================================================

        int horizontalPadding =
                (int) (8 * density);


        int verticalPadding =
                (int) (10 * density);


        button.setPadding(
                horizontalPadding,
                verticalPadding,
                horizontalPadding,
                verticalPadding
        );


        // ========================================================
        // PRESSED EFEKAT
        // ========================================================

        button.setOnTouchListener(
                (v, event) -> {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            /*
                             * Kada se dugme pritisne,
                             * pojavljuje se zaobljena
                             * tamno-siva površina.
                             *
                             * To je izgled koji se vidi
                             * oko "Cancel" dugmeta na screenshotu.
                             */

                            GradientDrawable pressedBackground =
                                    new GradientDrawable();


                            pressedBackground.setColor(
                                    Color.parseColor(
                                            BUTTON_PRESSED_COLOR
                                    )
                            );


                            pressedBackground.setCornerRadius(
                                    32 * density
                            );


                            v.setBackground(
                                    pressedBackground
                            );


                            v.invalidate();

                            break;


                        case MotionEvent.ACTION_UP:

                        case MotionEvent.ACTION_CANCEL:

                            /*
                             * Vraćanje na potpuno
                             * transparentno stanje.
                             */

                            v.setBackgroundColor(
                                    Color.TRANSPARENT
                            );


                            v.invalidate();

                            break;
                    }


                    /*
                     * false je važno:
                     * postojeći click listener i dalje radi.
                     */

                    return false;
                }
        );


        return button;
    }


    // ============================================================
    // CURSOR
    // ============================================================

    private static void setCursorColorAndWidth(
            EditText editText,
            int color,
            int widthPx) {

        try {

            if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.Q) {

                ShapeDrawable cursorDrawable =
                        new ShapeDrawable(
                                new RectShape()
                        );


                cursorDrawable.setIntrinsicWidth(
                        widthPx
                );


                cursorDrawable.setBounds(
                        0,
                        0,
                        widthPx,
                        editText.getLineHeight()
                );


                cursorDrawable.getPaint()
                        .setColor(
                                color
                        );


                editText.setTextCursorDrawable(
                        cursorDrawable
                );

            } else {

                Field editorField =
                        TextView.class.getDeclaredField(
                                "mEditor"
                        );


                editorField.setAccessible(
                        true
                );


                Object editor =
                        editorField.get(
                                editText
                        );


                Field cursorField =
                        editor.getClass()
                                .getDeclaredField(
                                        "mCursorDrawable"
                                );


                cursorField.setAccessible(
                        true
                );


                Object drawables =
                        cursorField.get(
                                editor
                        );


                if (drawables instanceof
                        android.graphics.drawable.Drawable[]) {

                    GradientDrawable drawable =
                            new GradientDrawable();


                    drawable.setColor(
                            color
                    );


                    drawable.setSize(
                            widthPx,
                            editText.getLineHeight()
                    );


                    ((android.graphics.drawable.Drawable[])
                            drawables)[0] =
                            drawable;


                    ((android.graphics.drawable.Drawable[])
                            drawables)[1] =
                            drawable;
                }
            }

        } catch (Exception ignored) {
        }
    }


    // ============================================================
    // CALLBACK
    // ============================================================

    public interface DownloadCallback {

        void onSave(
                String fileName
        );
    }
}
