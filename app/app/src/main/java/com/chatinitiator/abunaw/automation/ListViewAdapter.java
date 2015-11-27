package com.chatinitiator.abunaw.automation;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/*
Custom list view adapter class
  The event object is passed to this class
  so it can be displayed in main list view on screen.
  Also is makes a list of events searchable by the events title.
 */
public class ListViewAdapter extends ArrayAdapter<Event> {
	// Declare Variables
	Context context;
	LayoutInflater inflater;
	List<Event> eventlist;
	private SparseBooleanArray mSelectedItemsIds;

	public ListViewAdapter(Context context, int resourceId, List<Event> eventlist) {
		
		super(context, resourceId, eventlist);
		
		mSelectedItemsIds = new SparseBooleanArray();
		this.context = context;
		this.eventlist = eventlist;
		inflater = LayoutInflater.from(context);

	}

	private class ViewHolder {
		TextView title;
		TextView time;
		TextView str;
		TextView frequency;
		ImageView flag;
		TextView timeStr;
	}

	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item, null);
			// Locate the TextViews in listview_item.xml
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.time = (TextView) view.findViewById(R.id.time);
			holder.frequency = (TextView) view.findViewById(R.id.frequency);
			// Locate the ImageView in listview_item.xml
			//holder.flag = (ImageView) view.findViewById(R.id.flag);
			holder.str = (TextView)view.findViewById(R.id.timelabel);
			holder.timeStr = (TextView)view.findViewById(R.id.time);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Capture position and set to the TextViews
		holder.title.setText(eventlist.get(position).getTitle());
		holder.time.setText(eventlist.get(position).getTime());
		holder.frequency.setText(eventlist.get(position)
				.getFrequency());
		// Capture position and set to the ImageView
		//holder.flag.setImageResource(eventlist.get(position)
				//.getFlag());

		if(eventlist.get(position).getIsActive().equalsIgnoreCase("true"))
		{
			holder.str.setText("Started: ");
			holder.str.setBackgroundColor(Color.rgb(102, 204, 0));
			holder.timeStr.setBackgroundColor(Color.rgb(102,204,0));
				//view.setBackgroundColor(Color.TRANSPARENT);
		}else{
			holder.str.setText("Starts: ");
			holder.str.setBackgroundColor(Color.rgb(255, 255, 51));
			holder.timeStr.setBackgroundColor(Color.rgb(255, 255, 51));
			   //view.setBackgroundColor(Color.TRANSPARENT);
		}

		return view;
	}

	@Override
	public void remove(Event object) {
		eventlist.remove(object);
		notifyDataSetChanged();
	}

	public List<Event> getEventList() {
		return eventlist;
	}

	public void toggleSelection(int position) {
		selectView(position, !mSelectedItemsIds.get(position));
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


}