package in.alertmeu.a4a.adapter;


import android.content.Context;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import java.util.List;


import in.alertmeu.a4a.R;
import in.alertmeu.a4a.models.SendNotificationModelDAO;
import in.alertmeu.a4a.utils.Listener;


public class FCMSIDsListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<SendNotificationModelDAO> data;
    SendNotificationModelDAO current;
    String id, id1;
    int ID;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    private static Listener mListener;


    // create constructor to innitilize context and data sent from MainActivity
    public FCMSIDsListAdpter(Context context, List<SendNotificationModelDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();


    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_fcmids_details, parent, false);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);

        myHolder.notes.setText(current.getName());
        myHolder.notes.setTag(position);

        if (!current.getImagepath().equals("")) {

            myHolder.subimage.setTag(position);

            try {
                Picasso.get().load(current.getImagepath()).into(myHolder.subimage);
            } catch (Exception e) {

            }
        } else {

            // myHolder.subimage.setImageDrawable(context.getResources().getDrawable(R.drawable.default_sub_category));
            //myHolder.subimage.setTag(position);

        }
        myHolder.id.setText(current.getId());
        myHolder.id.setTag(position);
        myHolder.chkBox.setTag(position);
        myHolder.chkBox.setChecked(data.get(position).isSelected());
        myHolder.chkBox.setTag(data.get(position));

        myHolder.chkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                SendNotificationModelDAO contact = (SendNotificationModelDAO) cb.getTag();
                contact.setSelected(cb.isChecked());
                data.get(pos).setSelected(cb.isChecked());
            }
        });
    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView txt_date, notes, id;
        CheckBox chkBox;
        ImageView subimage;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            notes = (TextView) itemView.findViewById(R.id.comments);
            notes = (TextView) itemView.findViewById(R.id.comments);
            id = (TextView) itemView.findViewById(R.id.id);
            chkBox = (CheckBox) itemView.findViewById(R.id.chkBox);
            subimage = (ImageView) itemView.findViewById(R.id.subimage);

        }

    }


    // method to access in activity after updating selection
    public List<SendNotificationModelDAO> getSservicelist() {
        return data;
    }

    public static void bindListener(Listener listener) {
        mListener = listener;
    }


}
