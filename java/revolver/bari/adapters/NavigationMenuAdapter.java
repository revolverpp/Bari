package revolver.bari.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import revolver.bari.R;

@SuppressLint("ViewHolder")
public class NavigationMenuAdapter extends ArrayAdapter<String> {

    private Item[] items;
    private LayoutInflater mLayoutInflater;
    private int selected;

    public NavigationMenuAdapter(Context ctx, Item[] items) {
        super(ctx, android.R.layout.simple_list_item_1, new String[items.length]);
        this.items = items;
        this.mLayoutInflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.custom_navi_item, null);

        if (selected == position)
            v.setBackgroundColor(Color.parseColor("#cccccc"));

        TextView text = (TextView) v.findViewById(R.id.custom_navi_item_text);
        ImageView image = (ImageView) v.findViewById(R.id.custom_navi_item_image);
        View divider = v.findViewById(R.id.custom_navi_item_divider);

        text.setText(items[position].text);
        image.setImageResource(items[position].res);
        if (items[position].divider)
            divider.setVisibility(View.VISIBLE);

        return v;
    }

    public void setSelected(int index) {
        selected = index;
    }

}
