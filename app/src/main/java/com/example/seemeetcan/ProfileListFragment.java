package com.example.seemeetcan;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

public class ProfileListFragment extends Fragment {
    private View view;
    private SearchView searchView;
    private ArrayList<Profile> profiles;
    private ListView listView;
    private Adapter adapter;
    private ProfileViewAdapter profileViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profiles = new ArrayList<>();
        MainActivity activity = (MainActivity) getActivity();
        profileViewAdapter = new ProfileViewAdapter(activity.getMyProfile());

    }

    @Override
    public void onResume() {
        super.onResume();
        new RequestProfileList().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_list, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_profile);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                profileViewAdapter.getFilter().filter(s);
                return false;
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_filter:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RequestProfileList extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result;
                JSONObject jsonObject;
                MainActivity activity = (MainActivity) getActivity();
                RequestHttpURLConnection con = new RequestHttpURLConnection("list");
                String json;
                jsonObject = new JSONObject();
                jsonObject.accumulate("Id", activity.getMyProfile().getId());
                json = jsonObject.toString();

                result = con.request(json);
                jsonObject = new JSONObject(result);
                profiles.removeAll(profiles);

                if (!jsonObject.isNull("Users")) {
                    JSONArray arr = jsonObject.getJSONArray("Users");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject tmp = (JSONObject) arr.get(i);
                        Profile p = new Profile(tmp);
                        profiles.add(p);
                    }
                }
            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Collections.sort(profiles);
            profileViewAdapter.setItems(profiles);
            listView.setAdapter(profileViewAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MainActivity activity = (MainActivity) getActivity();
                    Profile selectedProfile = (Profile) adapterView.getAdapter().getItem(i);
                    Intent detailIntent = new Intent(activity.getApplication(), DetailActivity.class);
                    detailIntent.putExtra("my_profile", activity.getMyProfile());
                    detailIntent.putExtra("profile", selectedProfile);
                    startActivity(detailIntent);
                }
            });
        }
    }
}