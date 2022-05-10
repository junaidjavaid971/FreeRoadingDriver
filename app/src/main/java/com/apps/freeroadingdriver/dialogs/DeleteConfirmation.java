package com.apps.freeroadingdriver.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.apps.freeroadingdriver.R;
import com.apps.freeroadingdriver.interfaces.DeleteConfirmationCallBack;

/**
 * Created by Harshil on 4/26/2017.
 */

public class DeleteConfirmation extends Dialog {
    int type;
    DeleteConfirmationCallBack deleteConfirmationCallBack;
    public DeleteConfirmation(@NonNull Context context, int type,  DeleteConfirmationCallBack deleteConfirmationCallBack) {
        super(context);
        this.type = type;
        this.deleteConfirmationCallBack = deleteConfirmationCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottom_sheet_delete);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView text = (TextView) findViewById(R.id.changeText);

        //type =1 for delete, type=2 for logout
        if (type==1){
//            text.setText(getContext().getResources().getString(R.string.are_you_sure_you_want_to_delete_this_card));
        }else if (type==2){
            text.setText(R.string.logout_text);
        }

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmationCallBack.deleteCard();
                dismiss();
            }
        });
    }
}
