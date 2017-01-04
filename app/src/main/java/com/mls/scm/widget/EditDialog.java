package com.mls.scm.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import com.yundian.yundianchong.R;
import com.yundian.yundianchong.application.AppContext;


/**
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to
 * create and use your own look & feel.
 *
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 *
 * <a href="http://my.oschina.net/arthor" target="_blank" rel="nofollow">@author</a> antoine vianey
 */
public class EditDialog extends Dialog {
    public static EditDialogListener editDialogListener;
    private EditDialog dialog;
    Builder b;
    private Context mContext;

    public EditDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public EditDialog(Context context) {
        super(context);
        this.mContext = context;

    }

    public EditDialog getDialog(String title, String content) {
      b= new Builder(mContext);
        b.setMessage(title, content);
        dialog = b.create();
        dialog.show();
        return dialog;
    }

    public Builder getBuilder() {
        return b;
    }


    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {
        private TextView txtName;
        private TextView txtContent;
        private EditText editContent;
        private Context context;
        private String title, content;

        public Builder(Context context) {
            this.context = context;
        }

        public void setMessage(String title, String content) {
            this.title = title;
            this.content = content;
        }
        public void setEidInputType(int inputType) {
            editContent.setInputType(inputType);
        }
        /**
         * Create the custom dialog
         */
        @SuppressLint("Override")
        public EditDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final EditDialog dialog = new EditDialog(context,
                    R.style.Dialog);
            dialog.setCanceledOnTouchOutside(false);
            View layout = inflater.inflate(R.layout.view_nick_dialog, null);
            txtName = (TextView) layout.findViewById(R.id.txt_name);
            txtContent = (TextView) layout.findViewById(R.id.txt_content);
            editContent = (EditText) layout.findViewById(R.id.edt_content);
            txtName.setText(title);
            txtContent.setText(content);

            layout.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editDialogListener != null) {
                        editDialogListener.cancel();
                    }
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.txt_sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String editContent = Builder.this.editContent.getText().toString().trim();
                    if (TextUtils.isEmpty(editContent)) {
                        AppContext.showToast("请输入");
                        return;
                    }
                    if (editDialogListener != null) {
                        editDialogListener.sure(editContent);

                    }
                    dialog.dismiss();
                }
            });
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            return dialog;
        }

    }

    public interface EditDialogListener {
        void sure(String editContent);

        void cancel();
    }


    public EditDialogListener getClearDataListener() {
        return editDialogListener;
    }

    public void seteditDialogListener(EditDialogListener editDialogListener) {
        this.editDialogListener = editDialogListener;
    }
}