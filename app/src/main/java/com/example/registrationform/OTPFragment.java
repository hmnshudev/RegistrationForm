package com.example.registrationform;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class OTPFragment extends DialogFragment {

    private EditText et1, et2, et3, et4, et5, et6;
    private StringBuilder otp = new StringBuilder();
    private SQLiteHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.otp_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        db = new SQLiteHelper(getContext());
        textListeners();
    }

    private void initViews(View view) {
        et1 = view.findViewById(R.id.et1);
        et2 = view.findViewById(R.id.et2);
        et3 = view.findViewById(R.id.et3);
        et4 = view.findViewById(R.id.et4);
        et5 = view.findViewById(R.id.et5);
        et6 = view.findViewById(R.id.et6);
    }

    private void textListeners() {
        et1.addTextChangedListener(new Generic(et1));
        et2.addTextChangedListener(new Generic(et2));
        et3.addTextChangedListener(new Generic(et3));
        et4.addTextChangedListener(new Generic(et4));
        et5.addTextChangedListener(new Generic(et5));
        et6.addTextChangedListener(new Generic(et6));
    }

    private class Generic implements TextWatcher {

        private View view;

        Generic(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.et1:
                    if (text.length() == 1) {
                        et2.requestFocus();
                        otp.append(et1.getText());
                    } else if (text.length() == 0) {
                        otp.deleteCharAt(0);
                    }
                    break;
                case R.id.et2:
                    if (text.length() == 1) {
                        et3.requestFocus();
                        otp.append(et2.getText());
                    } else if (text.length() == 0) {
                        et1.requestFocus();
                        otp.deleteCharAt(1);
                    }
                    break;
                case R.id.et3:
                    if (text.length() == 1) {
                        et4.requestFocus();
                        otp.append(et3.getText());
                    } else if (text.length() == 0) {
                        et2.requestFocus();
                        otp.deleteCharAt(2);
                    }
                    break;
                case R.id.et4:
                    if (text.length() == 1) {
                        et5.requestFocus();
                        otp.append(et4.getText());
                    } else if (text.length() == 0) {
                        et3.requestFocus();
                        otp.deleteCharAt(3);
                    }
                    break;
                case R.id.et5:
                    if (text.length() == 1) {
                        et6.requestFocus();
                        otp.append(et5.getText());
                    } else if (text.length() == 0) {
                        et4.requestFocus();
                        otp.deleteCharAt(4);
                    }
                    break;
                case R.id.et6:
                    if (text.length() == 1) {
                        et6.requestFocus();
                        otp.append(et6.getText());
                        if ((otp.toString()).equals("241096")) {
                            db.deleteAll();
                            Toast.makeText(getContext(), "Table Deleted", Toast.LENGTH_SHORT).show();
                            dismiss();
                            Objects.requireNonNull(getActivity()).finish();

                        } else {
                            Toast.makeText(getContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    } else if (text.length() == 0) {
                        et5.requestFocus();
                        otp.deleteCharAt(5);
                    }
                    break;
            }
        }
    }
}
