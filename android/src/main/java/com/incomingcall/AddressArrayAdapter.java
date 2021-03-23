package com.incomingcall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;

public class AddressArrayAdapter extends ArrayAdapter<AddressList> {
    private final Context context;
    private final ArrayList<AddressList> values;

    public AddressArrayAdapter(Context context, ArrayList<AddressList> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.address_item, parent, false);
        TextView addressName = (TextView) rowView.findViewById(R.id.addressName);
        TextView addressInfo = (TextView) rowView.findViewById(R.id.addressInfo);
        addressName.setText(values.get(position).getName());
        addressInfo.setText(values.get(position).getAddress());

        if (this.values.size() -1 == position) {
            FrameLayout layout = (FrameLayout) rowView.findViewById(R.id.line_next_to);
            layout.setVisibility(View.GONE);
        }

        return rowView;
    }
}