package com.example.rishikumar.contactmanager;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;


/**
 * Created by rishi.kumar on 12/4/2017.
 */

class ContactAdapter extends ArrayAdapter<contactObject> implements Filterable {

    Context c;
    LayoutInflater inflater;
    ArrayList<contactObject> contacts;
    CustomFilter filter;
    ArrayList<contactObject> filterList;
    private SparseBooleanArray mSelectedItemsIds;


    public ContactAdapter(Context context, int resourceId, ArrayList<contactObject> contacts) {
        super(context, resourceId, contacts);

        mSelectedItemsIds = new SparseBooleanArray();
        this.c = context;
        this.contacts = contacts;
        this.filterList = contacts;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_info, null);
        }
        TextView nameTxt = (TextView) convertView.findViewById(R.id.name);
        TextView noTxt = (TextView) convertView.findViewById(R.id.no);
        //SET DATA TO THEM
        nameTxt.setText(contacts.get(pos).getName());
        noTxt.setText(contacts.get(pos).getNumber());

        return convertView;

    }


    @Override
    public void remove(contactObject object) {
        contacts.remove(object);
        notifyDataSetChanged();
    }


    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
        notifyDataSetChanged();
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();

    }



    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toUpperCase();
                ArrayList<contactObject> filters = new ArrayList<contactObject>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if ((filterList.get(i).getName().toUpperCase().contains(constraint))|| (filterList.get(i).getNumber().contains(constraint)) ) {
                        contactObject p = new contactObject(filterList.get(i).getName(), filterList.get(i).getNumber());
                        filters.add(p);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            contacts = (ArrayList<contactObject>) results.values;
            notifyDataSetChanged();

        }
    }
}
