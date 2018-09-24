package com.powerranger.ginz.transfervoicetogoolge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class LanguageAdapter extends BaseAdapter {
    Context mContext;
    int myLayout;
    List<Language> listCountry;

    public LanguageAdapter(Context context, int myLayout, List<Language> listCountry) {
        mContext = context;
        this.myLayout = myLayout;
        this.listCountry = listCountry;
    }

    @Override
    public int getCount() {
        return listCountry != null ? listCountry.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);

        TextView  txtName = view.findViewById(R.id.text_language);
        ImageView imgFlag = view.findViewById(R.id.image_flag);

        txtName.setText(listCountry.get(i).getName());
        imgFlag.setImageResource(listCountry.get(i).getFlag());
        return view;
    }
}
