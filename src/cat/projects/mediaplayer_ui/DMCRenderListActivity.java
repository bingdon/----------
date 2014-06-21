package cat.projects.mediaplayer_ui;

import java.util.ArrayList;
import java.util.List;

import tv.luxs.rcassistant.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dlna.datadefine.DLNA_DeviceData;

public class DMCRenderListActivity extends ListActivity {

	private List<String> dmrlistString = new ArrayList<String>();
	private int mCurrentPosition = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		ArrayList<DLNA_DeviceData> dmrlist = intent
				.getParcelableArrayListExtra("renderlist");
		for (int i = 0; i < dmrlist.size(); i++) {
			dmrlistString.add(dmrlist.get(i).devicename);
		}

		setContentView(R.layout.renderlist_activity);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.mylist_item, dmrlistString);
		setListAdapter(adapter);
//		this.setOnItemClickListener(this);
//		ListView listview = (ListView) findViewById(R.id.dmr_list);
//		listview.setOnItemClickListener(this);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent();
		intent.setAction("android.dmc.renderlist");  
		intent.putExtra("renderlist", position);
		mCurrentPosition = position;
		sendBroadcast(intent);  
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent();
		intent.setAction("android.dmc.renderlist");  
		intent.putExtra("renderlist", mCurrentPosition);
		sendBroadcast(intent);  
		finish();
	}
	
	

}
