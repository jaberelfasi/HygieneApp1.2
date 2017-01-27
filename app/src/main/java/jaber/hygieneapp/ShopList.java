package jaber.hygieneapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ShopList extends ArrayAdapter<Shop>{


    private Activity context;
    List<Shop> allShops = new ArrayList<Shop>();
    int tag;
    public ShopList(Activity context, List<Shop> allShops, int tag) {
        super(context, 0, allShops);
        this.context = context;
        this.allShops = allShops;
        this.tag=tag;//to determine whether the search includes distance or not 1 or 0
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_view_layout, null, true);//this produces the content in the list view
        TextView tvName = (TextView) listViewItem.findViewById(R.id.tvName);
        TextView tvLine1 = (TextView) listViewItem.findViewById(R.id.tvLine1);
        TextView tvLine2 = (TextView) listViewItem.findViewById(R.id.tvLine2);
        TextView tvLine3 = (TextView) listViewItem.findViewById(R.id.tvLine3);
        TextView tvPostCode = (TextView) listViewItem.findViewById(R.id.tvPostCode);
        TextView tvRatingDate = (TextView) listViewItem.findViewById(R.id.tvRatingDate);
        TextView tvKm=(TextView)listViewItem.findViewById(R.id.tvKm);
        ImageView tvRatingVal = (ImageView)listViewItem.findViewById(R.id.ivRatingVal);


            //all text views get assigned with their corresponding values
            tvName.setText(allShops.get(position).getBusinessName());
            tvLine1.setText(allShops.get(position).getAddressLine1());
            tvLine2.setText(allShops.get(position).getAddressLine2());
            tvLine3.setText(allShops.get(position).getAddressLine3());
            tvPostCode.setText(allShops.get(position).getPostCode());

            //the rating value here is displayed in an ImageView
            //and this switch statement decides which image to be displayed in a particular record
            switch (allShops.get(position).getRatingValue()){
                case "-1": tvRatingVal.setImageResource(R.drawable.awaiting);
                    break;
                case "0": tvRatingVal.setImageResource(R.drawable.zero);
                    break;
                case "1": tvRatingVal.setImageResource(R.drawable.one);
                    break;
                case "2": tvRatingVal.setImageResource(R.drawable.two);
                    break;
                case "3": tvRatingVal.setImageResource(R.drawable.three);
                    break;
                case "4": tvRatingVal.setImageResource(R.drawable.four);
                    break;
                case "5": tvRatingVal.setImageResource(R.drawable.five);
                    break;
                default: tvRatingVal.setImageResource(R.drawable.awaiting);
            }

            //this text view again gets assigned with its corresponding value
            tvRatingDate.setText(allShops.get(position).getRatingDate());

            //if the tag was equal to 1 the distance textview will be displayed, if not the app will crash
            //if we allowed the text view to access allShops value of the distance since it will be null.
            //we only allow access to it if we know it is not null and that is when the distance is returned
            //in the url query
            if(tag==1){
                String str= String.format("%.2f", Double.parseDouble(allShops.get(position).getDistanceKM()));
                tvKm.setText((str)+" Km");
            }

        return listViewItem;
    }

}
