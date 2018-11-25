package com.mamikos.mamiagent.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
<<<<<<< HEAD
<<<<<<< V1167
 * Created by root on 10/20/16.
=======
 * Created by root on 11/25/16.
>>>>>>> Prepare New Interface OwnerProfile
=======
 * Created by root on 11/25/16.
>>>>>>> applozic
 *
 */

public class NumberTextWatcher implements TextWatcher
{
    private static final String TAG = NumberTextWatcher.class.getSimpleName();
    private final DecimalFormat df;
    private final DecimalFormat dfnd;
    private final EditText et;
    private boolean hasFractionalPart;
    private int trailingZeroCount;

    /*#,###*/
    public NumberTextWatcher(EditText editText, String pattern) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.UK);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormatSymbols dfSymbols = new DecimalFormatSymbols(Locale.UK);
        dfSymbols.setDecimalSeparator(',');
        dfSymbols.setGroupingSeparator('.');
        df = new DecimalFormat(pattern, dfSymbols);
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###", otherSymbols);
        this.et = editText;
        hasFractionalPart = false;
    }

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        if (s != null && !s.toString().isEmpty()) {
            try {
                int inilen, endlen;
                inilen = et.getText().length();
                //String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("Rp ","");
                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                Number n = df.parse(v);
                int cp = et.getSelectionStart();
                if (hasFractionalPart) {
                    StringBuilder trailingZeros = new StringBuilder();
                    while (trailingZeroCount-- > 0)
                        trailingZeros.append('0');
                    et.setText(df.format(n) + trailingZeros.toString());
                } else
                {
                    et.setText(dfnd.format(n));
                }
                /*String v1 = et.getText().toString();
                v1.replace(',', '.');*/
                //BigDecimal bd = new BigDecimal(et.getText().toString());
                //et.setText("Rp ".concat(et.getText().toString()));
                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel < et.getText().length())
                {
                    et.setSelection(sel);
                } else if (trailingZeroCount > -1)
                {
                    et.setSelection(et.getText().length());
                    //et.setSelection(et.getText().length() - 3);
                } /*else
                {
                    et.setSelection(et.getText().length());
                }*/
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
        }

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        int index = s.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
        trailingZeroCount = 0;
        if (index > -1)
        {
            for (index++; index < s.length(); index++)
            {
                if (s.charAt(index) == '0')
                {
                    trailingZeroCount++;
                }
                else
                {
                    trailingZeroCount = 0;
                }
            }
            hasFractionalPart = true;
        } else
        {
            hasFractionalPart = false;
        }
    }
}