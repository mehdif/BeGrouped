package smartcity.begrouped.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.POIManager;
import smartcity.begrouped.model.POI;


public class AddDestinationFragment extends Fragment {

    private ListView maListViewPerso;
    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data
    private SimpleAdapter mSchedule;
    private EditText search_query;
    private static final String TAG_NAME = "First day";
    private static final String TAG_TYPE = "hang out";
    private static final String TAG_TEMPS = "HH::MM";

    public AddDestinationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_destination, container, false);
        maListViewPerso = (ListView) rootView.findViewById(R.id.listView1);
        listItem = new ArrayList<HashMap<String, String>>();
        maListViewPerso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

            }
        });
        mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichageitem,
                new String[] {"img", TAG_NAME,TAG_TYPE,TAG_TEMPS}, new int[] {R.id.img, R.id.titre, R.id.description, R.id.superviseur});
        maListViewPerso.setAdapter(mSchedule);
        search_query = (EditText) rootView.findViewById(R.id.search_input);

        search_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    Toast.makeText(getActivity(), "Looking for : "+search_query.getText(), Toast.LENGTH_LONG).show();

                    // Anes : Call your search function here : performSearch();
                    LinkedList<POI> listPOI= POIManager.searchPOIByNameByTask(search_query.getText().toString());
                    listItem.clear();
                    for(int i=0;i<listPOI.size();i++) {
                        POI poi= listPOI.get(i);
                        Log.v("group", poi.toString());
                        map = new HashMap<String, String>();
                        // recuperer les données du superviseur
                        map.put(TAG_NAME,poi.getName());
                        map.put(TAG_TYPE,poi.getType());
                        map.put(TAG_TEMPS,"");
                        map.put("img", String.valueOf(R.drawable.ic_action_view_as_grid));//Ici l icone qui va s'afficher
                        listItem.add(map);
                    }
                    mSchedule.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
