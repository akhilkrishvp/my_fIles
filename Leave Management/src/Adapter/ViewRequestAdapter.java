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

public class ViewRequestAdapter extends BaseAdapter{
	private Activity activity;
	private String[] name;
	private String[] dept;
	
	 private String[] leavestatus;

	LayoutInflater inflater;

	public ViewRequestAdapter(Activity a, String[] nameArray, String[] deptArray,String[] leaveArray) {
		activity = a;
		name = nameArray;
		dept = deptArray;
		
		leavestatus=leaveArray;
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
		public TextView text;
		public TextView tstatus;
		
		 public TextView leavests;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.reqst_list_adpter, null);
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.name);
			holder.tstatus = (TextView) vi.findViewById(R.id.dept);
			 holder.leavests=(TextView)vi.findViewById(R.id.status);

			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();
		try{
			
		
		holder.text.setText(name[position]);

		holder.tstatus.setText(dept[position]);
	
		
		if(leavestatus[position].equals("Pending"))
		{
			holder.leavests.setTextColor(Color.YELLOW);
			
		}
		else if(leavestatus[position].equals("Rejected"))
		{
			holder.leavests.setTextColor(Color.RED);
			
		}else if(leavestatus[position].equals("Approved"))
		{
			holder.leavests.setTextColor(Color.GREEN);
			
		}
		else if(leavestatus[position].equals("Cancelled"))
		{
			holder.leavests.setTextColor(Color.CYAN);
			
		}
		holder.leavests.setText(leavestatus[position]);
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


