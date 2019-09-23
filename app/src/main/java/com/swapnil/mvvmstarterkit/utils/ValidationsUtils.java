package com.swapnil.mvvmstarterkit.utils;

import android.content.Context;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.swapnil.mvvmstarterkit.R;


public class ValidationsUtils {
    public static boolean validateField(final Context context,
                                        final TextInputLayout textInputLayout, EditText editText,
                                        String type) {
        boolean isValid = false;
        String text = editText.getText().toString().trim();
        if (type.equals(Constants.EMAIL)) {
            if (text != null && text.isEmpty()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getResources().getString(R.string.err_enter_email));
            } else {
                if (!ValidationUtils.isEmailValid(text)) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getResources().getString(R.string.err_invalid_email));
                } else {
                    isValid = true;
                    textInputLayout.setErrorEnabled(false);
                }
            }
        }
        if (type.equals(Constants.PASSWORD)) {
            if (text != null && text.isEmpty()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getResources().getString(R.string.err_enter_password));
            } else {
                if (!ValidationUtils.isPasswordValid(text)) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getResources().getString(R.string.err_invalid_password));
                } else {
                    isValid = true;
                    textInputLayout.setErrorEnabled(false);
                }
            }
        }
        if (type.equals(Constants.CONFIRM_PASSWORD)) {
            if (text != null && text.isEmpty()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getResources().getString(R.string.err_enter_confirm_password));
            } else {
                if (!ValidationUtils.isPasswordValid(text)) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getResources().getString(R.string.err_invalid_confirm_password));
                } else {
                    isValid = true;
                    textInputLayout.setErrorEnabled(false);
                }
            }
        }

        if (type.equals(Constants.NAME)) {
            if (text != null && text.isEmpty()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getResources().getString(R.string.err_enter_name));
            } else {
                if (!ValidationUtils.isNameValid(text)) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getResources().getString(R.string.err_invalid_name));
                } else {
                    isValid = true;
                    textInputLayout.setErrorEnabled(false);
                }
            }
        }

        if (type.equals(Constants.DATE)) {
            if (text != null && text.isEmpty()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getResources().getString(R.string.err_enter_dob));
            } else {
                if (!ValidationUtils.isDateValid(text)) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getResources().getString(R.string.err_enter_dob));
                } else {
                    isValid = true;
                    textInputLayout.setErrorEnabled(false);
                }
            }
        }
        if (type.equals(Constants.AGE)) {
            if (text != null && text.isEmpty()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getResources().getString(R.string.err_enter_dob));
            } else {
                if (!ValidationUtils.isDateValid(text)) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getResources().getString(R.string.err_invalid_age));
                } else {
                    isValid = true;
                    textInputLayout.setErrorEnabled(false);
                }
            }
        }
        if (type.equals(Constants.GUARDIANS_AGE)) {
            if (text != null && text.isEmpty()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getResources().getString(R.string.err_enter_dob));
            } else {
                if (!ValidationUtils.isValidGuardiansAge(text)) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getResources().getString(R.string.err_invalid_age));
                } else {
                    isValid = true;
                    textInputLayout.setErrorEnabled(false);
                }
            }
        }

        if (type.equals(Constants.PHONE_NO)) {
            if (text != null && text.isEmpty()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getResources().getString(R.string.err_enter_phone_no));
            } else {
                if (!ValidationUtils.isPhoneNumberValid(text)) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getResources().getString(R.string.err_invalid_phone_no));
                } else {
                    isValid = true;
                    textInputLayout.setErrorEnabled(false);
                }
            }
        }
        if (type.equals(Constants.OTP)) {
            if (text != null && text.isEmpty()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getResources().getString(R.string.err_enter_otp));
            }else{
                isValid = true;
            }
        }

        return isValid;
    }
}


