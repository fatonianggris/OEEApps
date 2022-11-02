package com.project.oee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Ravi on 13/05/15.
 */
public class RecordAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Record> recordList;
    private SwipeRefreshLayout swipe;
    private Context mContext;

    public RecordAdapter(Context c, List<Record> recordList, SwipeRefreshLayout s) {
        this.mContext= c;
        this.recordList = recordList;
        this.swipe = s;
        notifyDataSetChanged();
   }
    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int location) {
        return recordList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_record, null);

        TextView no = (TextView) convertView.findViewById(R.id.no);
        TextView oee = (TextView) convertView.findViewById(R.id.oee);
        TextView mch = (TextView) convertView.findViewById(R.id.machine);
        TextView ot = (TextView) convertView.findViewById(R.id.ot);
        TextView pa = (TextView) convertView.findViewById(R.id.pa);
        TextView gc = (TextView) convertView.findViewById(R.id.gc);
        TextView tt = (TextView) convertView.findViewById(R.id.tt);
        TextView ict = (TextView) convertView.findViewById(R.id.ict);
        TextView tc = (TextView) convertView.findViewById(R.id.tc);
        TextView ava = (TextView) convertView.findViewById(R.id.ava);
        TextView per = (TextView) convertView.findViewById(R.id.per);
        TextView roq = (TextView) convertView.findViewById(R.id.roq);
        TextView dtime = (TextView) convertView.findViewById(R.id.dt);
        TextView tdtime = (TextView) convertView.findViewById(R.id.td);
        TextView pdtime = (TextView) convertView.findViewById(R.id.pd);
        TextView ltime = (TextView) convertView.findViewById(R.id.lt);
        TextView tdelay = (TextView) convertView.findViewById(R.id.tdel);
        TextView whours = (TextView) convertView.findViewById(R.id.wh);

        no.setText(recordList.get(position).getNo()+".");
        oee.setText(recordList.get(position).getOEE());
        mch.setText(recordList.get(position).getMachine().substring(0, 1).toUpperCase() + recordList.get(position).getMachine().substring(1));
        ot.setText(recordList.get(position).getOTime());
        pa.setText(recordList.get(position).getPAmount());
        gc.setText(recordList.get(position).getGCount());
        tt.setText(recordList.get(position).getTTime());
        ict.setText(recordList.get(position).getICTime());
        tc.setText(recordList.get(position).getTCount());
        ava.setText(recordList.get(position).getAvailability());
        per.setText(recordList.get(position).getPerformance());
        roq.setText(recordList.get(position).getROQuality());
        dtime.setText(recordList.get(position).getDtime());
        tdtime.setText(recordList.get(position).getTdtime());
        pdtime.setText(recordList.get(position).getPdtime());
        ltime.setText(recordList.get(position).getLtime());
        tdelay.setText(recordList.get(position).getTdelay());
        whours.setText(recordList.get(position).getWhours());

        final Record m = recordList.get(position);

        ImageView imageClick = (ImageView) convertView.findViewById(R.id.row_click_record);
        try {
            imageClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.row_click_record:

                            PopupMenu popup = new PopupMenu(mContext, v);
                            popup.getMenuInflater().inflate(R.menu.menu_record,
                            popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.share:
                                            ShareController s = new ShareController(mContext);
                                            s.shareTextById(m.getIDCal().toString(),m.getIDFac().toString());
                                            break;
                                        case R.id.delete:
                                            deleteDialog(m.getIDCal().toString(),m.getMachine());
                                            break;
                                        default:
                                            break;
                                    }
                                    return true;
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
    public void deleteDialog(final String id, final  String rn){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete.");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                SQLiteHandler r = new SQLiteHandler(mContext);
                r.deleteCal(id);
                Toast.makeText(mContext, rn.toUpperCase()+" machine has been deleted" , Toast.LENGTH_LONG).show();
                ((RecordActivity) mContext).onRefresh();
            }
        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,	int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }
}