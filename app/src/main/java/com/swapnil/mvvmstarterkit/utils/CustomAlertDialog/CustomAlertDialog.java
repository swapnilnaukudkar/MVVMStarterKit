package com.swapnil.mvvmstarterkit.utils.CustomAlertDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.swapnil.mvvmstarterkit.R;


/**
 * Created by Joiner on 04/06/18.
 */

public class CustomAlertDialog {

    private String title, message, positiveButtonText, negativeButtonText;
    private Activity activity;
    private int image;
    private Animation animation;
    private OnClickListener positiveButtonListener, negativeButtonListener;
    private int positiveButtonColor,
            negativeButtonColor,
            backgroundColor,
            positiveButtonTextColor,
            negativeButtonTextColor,
            titleColor,
            messageColor;
    private boolean cancelable;
    private Dialog dialog;

    public CustomAlertDialog(Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.positiveButtonText = builder.positiveButtonText;
        this.negativeButtonText = builder.negativeButtonText;
        this.activity = builder.activity;
        this.image = builder.image;
        this.animation = builder.animation;
        this.positiveButtonListener = builder.positiveButtonListener;
        this.negativeButtonListener = builder.negativeButtonListener;
        this.positiveButtonColor = builder.positiveButtonColor;
        this.negativeButtonColor = builder.negativeButtonColor;
        this.backgroundColor = builder.backgroundColor;
        this.positiveButtonTextColor = builder.positiveButtonTextColor;
        this.negativeButtonTextColor = builder.negativeButtonTextColor;
        this.titleColor = builder.titleColor;
        this.messageColor = builder.messageColor;
        this.cancelable = builder.cancelable;

    }

    public static class Builder {
        private String title, message, positiveButtonText, negativeButtonText;
        private Activity activity;
        private int image;
        private Animation animation;
        private OnClickListener positiveButtonListener, negativeButtonListener;
        private int positiveButtonColor,
                negativeButtonColor,
                backgroundColor,
                positiveButtonTextColor,
                negativeButtonTextColor,
                titleColor,
                messageColor;
        private boolean cancelable = true;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener positiveButtonListener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonListener = positiveButtonListener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener negativeButtonListener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonListener = negativeButtonListener;
            return this;
        }

        public Builder setImage(int image) {
            this.image = image;
            return this;
        }

        public Builder setAnimation(Animation animation) {
            this.animation = animation;
            return this;
        }

        public Builder setPositiveButtonColor(int positiveButtonColor) {
            this.positiveButtonColor = positiveButtonColor;
            return this;
        }

        public Builder setNegativeButtonColor(int negativeButtonColor) {
            this.negativeButtonColor = negativeButtonColor;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setPositiveButtonTextColor(int positiveButtonTextColor) {
            this.positiveButtonTextColor = positiveButtonTextColor;
            return this;
        }

        public Builder setNegativeButtonTextColor(int negativeButtonTextColor) {
            this.negativeButtonTextColor = negativeButtonTextColor;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setMessageColor(int messageColor) {
            this.messageColor = messageColor;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        @SuppressLint("RestrictedApi")
        public CustomAlertDialog build() {
            TextView tvMessage, tvTitle;
            ImageView ivImage;
            AppCompatButton btNegative, btPositive;
            View viewSeparator, viewButtonSeparator;
            final Dialog dialog;
            LinearLayout llButtons;
            CardView cvBackgroundAlertdialog;

            if (animation == Animation.POP) {
                dialog = new Dialog(activity, R.style.PopTheme);
            } else if (animation == Animation.SIDE) {
                dialog = new Dialog(activity, R.style.SideTheme);
            } else if (animation == Animation.SLIDE) {
                dialog = new Dialog(activity, R.style.SlideTheme);
            } else if (animation == Animation.ZOOM) {
                dialog = new Dialog(activity, R.style.ZoomTheme);
            } else {
                dialog = new Dialog(activity);
            }

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(cancelable);
            dialog.setContentView(R.layout.dialog_alert);

            viewSeparator = dialog.findViewById(R.id.v_separator);
            tvTitle = dialog.findViewById(R.id.tv_title);
            tvMessage = dialog.findViewById(R.id.tv_message);
            ivImage = dialog.findViewById(R.id.iv_image);
            btNegative = dialog.findViewById(R.id.bt_negative);
            btPositive = dialog.findViewById(R.id.bt_positive);
            viewButtonSeparator = dialog.findViewById(R.id.view_button_separator);
            cvBackgroundAlertdialog = dialog.findViewById(R.id.cv_background_alertdialog); // referencia para CardView
            llButtons = dialog.findViewById(R.id.ll_buttons);

            if (message != null) {
                if (!message.equals("")) {
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText(message);
                    if (messageColor != 0) {
                        tvMessage.setTextColor(AppCompatResources.getColorStateList(activity, messageColor));
                    }
                } else {
                    tvMessage.setVisibility(View.GONE);
                }

            } else {
                tvMessage.setVisibility(View.GONE);
            }

            if (title != null) {
                if (!title.equals("")) {
                    viewSeparator.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText(title);
                    if (titleColor != 0) {
                        tvTitle.setTextColor(AppCompatResources.getColorStateList(activity, titleColor));
                        viewSeparator.setBackgroundResource(titleColor);
                    }
                } else {
                    viewSeparator.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.GONE);
                }
            } else {
                viewSeparator.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
            }

            if (positiveButtonText != null || negativeButtonText != null) {

                llButtons.setVisibility(View.VISIBLE);

                if (positiveButtonText != null) {

                    if (!positiveButtonText.equals("")) {

                        btPositive.setVisibility(View.VISIBLE);
                        btPositive.setText(positiveButtonText);

                        if (positiveButtonColor != 0) {
                            btPositive.setSupportBackgroundTintList(AppCompatResources.getColorStateList(activity, positiveButtonColor));
                        }

                        if (positiveButtonTextColor != 0) {
                            btPositive.setTextColor(AppCompatResources.getColorStateList(activity, positiveButtonTextColor));
                        }

                        if (positiveButtonListener != null) {
                            btPositive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    positiveButtonListener.onClick();
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            btPositive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    } else {
                        btPositive.setVisibility(View.GONE);
                        viewButtonSeparator.setVisibility(View.GONE);
                    }

                } else {
                    btPositive.setVisibility(View.GONE);
                    viewButtonSeparator.setVisibility(View.GONE);
                }


                if (negativeButtonText != null) {

                    if (!negativeButtonText.equals("")) {

                        btNegative.setVisibility(View.VISIBLE);
                        btNegative.setText(negativeButtonText);

                        if (negativeButtonColor != 0) {
                            btNegative.setSupportBackgroundTintList(AppCompatResources.getColorStateList(activity, negativeButtonColor));
                        }

                        if (negativeButtonTextColor != 0) {
                            btNegative.setTextColor(AppCompatResources.getColorStateList(activity, negativeButtonTextColor));
                        }

                        if (negativeButtonListener != null) {

                            btNegative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    negativeButtonListener.onClick();
                                    dialog.dismiss();
                                }
                            });
                        } else {

                            btNegative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    } else {
                        btNegative.setVisibility(View.GONE);
                        viewButtonSeparator.setVisibility(View.GONE);
                    }

                } else {
                    btNegative.setVisibility(View.GONE);
                    viewButtonSeparator.setVisibility(View.GONE);
                }

            } else {

                llButtons.setVisibility(View.GONE);

            }


            if (image != 0) {

                ivImage.setImageResource(image);
                ivImage.setVisibility(View.VISIBLE);

            } else {
                ivImage.setVisibility(View.GONE);
            }


            if (backgroundColor != 0) {
                cvBackgroundAlertdialog.setCardBackgroundColor(AppCompatResources.getColorStateList(activity, backgroundColor));
            }

            if (activity!=null && !activity.isFinishing()) {
                dialog.show();
            }



            return new CustomAlertDialog(this);

        }

    }
}
