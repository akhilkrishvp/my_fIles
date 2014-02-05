package Adapter;

import leavemanagement.spectrum.net.in.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {
	private Activity activity;
	private String[] phone;
	private String[] name;
	private String[] status;
	// private String[] phone;

	LayoutInflater inflater;

	public HistoryAdapter(Activity a, String[] phone, String[] name,
			String[] status) {
		activity = a;
		this.phone = phone;
		this.name = name;
		this.status = status;
		// phone=Phone;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {

		return name.length;

	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView tphone;
		public TextView text;
		public TextView tstatus;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.demo, null);
			holder = new ViewHolder();
			holder.tphone = (TextView) vi.findViewById(R.id.Phone);
			holder.text = (TextView) vi.findViewById(R.id.leave);
			holder.tstatus = (TextView) vi.findViewById(R.id.stat);
			// holder.tPhone=(TextView)vi.findViewById(R.id.Phone);

			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();
		try{
		holder.tphone.setText(phone[position]);

		holder.text.setText(name[position]);

		holder.tstatus.setText(status[position]);
		
		if(status[position].equals("Pending"))
		{
			holder.tstatus.setTextColor(Color.YELLOW);
			
		}
		else if(status[position].equals("Rejected"))
		{
			holder.tstatus.setTextColor(Color.RED);
			
		}else if(status[position].equals("Approved"))
		{
			holder.tstatus.setTextColor(Color.GREEN);
			
		}
		else if(status[position].equals("Cancelled"))
		{
			holder.tstatus.setTextColor(Color.CYAN);
			
		}
		
		// holder.tPhone.setText(phone[position]);
		// holder.text.setOnClickListener((OnClickListener) this);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return vi;
	}
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	//
	// Toast.makeText(activity, "cliki", 3000);
	// System.out.println("...clicked view is...."+arg0);
	// }
}
