package com.example.seemeetcan;

import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.select.NodeFilter;

import java.util.ArrayList;

public class ProfileViewAdapter extends BaseAdapter implements Filterable {
    private Profile myProfile;
    private ArrayList<Profile> profileList = new ArrayList<>();
    private ArrayList<Profile> filteredList = new ArrayList<>();
    private ProfileFilter filter = new ProfileFilter();

    public ProfileViewAdapter(Profile myProfile) {
        super();
        this.myProfile = myProfile;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addItem(Profile profile) {
        profileList.add(profile);
    }

    public void setItems(ArrayList<Profile> profiles) {
        this.profileList = profiles;
        this.filteredList = profiles;
    }

    public void addItems(ArrayList<Profile> profiles) {
        profileList.addAll(profiles);
    }

    public void setMyProfile(Profile myProfile) {
        this.myProfile = myProfile;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_profile, viewGroup, false);
        }

        ImageView ivPicture = (ImageView) view.findViewById(R.id.iv_picture);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        CheckBox chbFavor = (CheckBox) view.findViewById(R.id.ckb_favor);

        Profile profile = filteredList.get(i);

        tvName.setText(profile.getName());

        chbFavor.setChecked(false);
        for (int id : myProfile.getFavoriteIds()) {
            if (id == profile.getId()) {
                chbFavor.setChecked(true);
                break;
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ProfileFilter();
        }
        return filter;
    }

    private class ProfileFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.values = profileList;
                results.count = profileList.size();
            } else {
                ArrayList<Profile> list = new ArrayList<>();

                for (Profile p : profileList) {
                    if (p.getName().toUpperCase().contains(charSequence.toString().toUpperCase()) ||
                            p.getMbti().toUpperCase().contains(charSequence.toString().toUpperCase())) {
                        list.add(p);
                    }
                }

                results.values = list;
                results.count = list.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredList = (ArrayList<Profile>) filterResults.values;

            if (filterResults.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
