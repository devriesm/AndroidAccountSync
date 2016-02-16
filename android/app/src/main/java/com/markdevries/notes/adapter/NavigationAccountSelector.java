package com.markdevries.notes.adapter;

import android.accounts.Account;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 2/15/16.
 */
public class NavigationAccountSelector extends BaseAdapter {

    private List<Account> mAccounts = new ArrayList<>();


    @Override
    public int getCount() {
        return mAccounts.size();
    }

    @Override
    public Account getItem(int position) {
        return mAccounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        TextView tv = (TextView) convertView;
        tv.setText(getItem(position).name);

        return convertView;
    }
}
