package com.toch.utils;

import java.util.ArrayList;

import com.toch.spotalarm.R;



import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListItemAdapter extends BaseAdapter {

	private Context context;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    int i=0;
    
    ItemModel tempValues = null;
    
    public ListItemAdapter(Context c, ArrayList d,Resources resLocal){
    	context = c;
        data=d;
        res = resLocal;
     
        /***********  Layout inflator to call external xml layout () ***********/
         inflater = ( LayoutInflater )context.
                                     getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	@Override
	public int getCount() {
		if(data.size()<=0)
            return 1;
        return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public static class ViewHolder{
        
        public TextView textViewLocation;
        public TextView textViewLat;
        public TextView textViewLong;
 
    }
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
        ViewHolder holder;
         
        if(convertView==null){
             
            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_layout, null);
             
            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.textViewLocation = (TextView) vi.findViewById(R.id.textViewLocation);
            holder.textViewLat = (TextView)vi.findViewById(R.id.item_lat);
            holder.textViewLong = (TextView)vi.findViewById(R.id.item_lng);
             
           /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else 
            holder=(ViewHolder)vi.getTag();
         
        if(data.size()<=0)
        {
            holder.textViewLocation.setText("No Data");
             
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (ItemModel) data.get( position );
             
            /************  Set Model values in Holder elements ***********/

             holder.textViewLocation.setText( tempValues.getLocation() );
             holder.textViewLat.setText( tempValues.getLatitude() );
             holder.textViewLong.setText( tempValues.getLongitude());
//             holder.image.setImageResource(R.drawable.ic_launcher);
              
             /******** Set Item Click Listner for LayoutInflater for each row *******/

        }
        return vi;
	}

}
