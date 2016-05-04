package com.wu.listviewdelete;
import java.util.ArrayList;
import java.util.List;

import com.wu.listviewdelete.MyDeleteListView.OnDeleteCallback;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	float x = 0;
	float y = 0;
	PopupWindow popupWindow;
	private List<String> infoList;
	public static int statusBarHeight = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		infoList = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			infoList.add("item" + i);
		}
		//�������Դ

		MyDeleteListView listview = new MyDeleteListView(this);
		final MyAdapter adapter = new MyAdapter(this, infoList);
		listview.setAdapter(adapter);
		listview.setOnItemDeleteCallback(new OnDeleteCallback() {

			@Override
			public void onDelete(AdapterView<?> parent, int deletePosition) {
				parent.getChildAt(deletePosition);
				Toast.makeText(MainActivity.this,
						infoList.get(deletePosition) + "被删除了",
						Toast.LENGTH_SHORT).show();
				infoList.remove(deletePosition);
				adapter.notifyDataSetChanged();
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this,
						infoList.get(position) + "被点击了", Toast.LENGTH_SHORT)
						.show();
			}
		});
		setContentView(listview);
	}


	public class MyAdapter extends BaseAdapter implements ListAdapter {

		private List<String> infoList;
		private LayoutInflater inflater;

		/**
		 * @param mainActivity
		 * @param infoList
		 */
		public MyAdapter(Context context, List<String> infoList) {
			this.infoList = infoList;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return infoList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView)
				convertView = inflater.inflate(R.layout.item, null);
			final TextView textview = (TextView) convertView
					.findViewById(R.id.item_tv);
			textview.setText(infoList.get(position));
			return convertView;
		}
	}

}
