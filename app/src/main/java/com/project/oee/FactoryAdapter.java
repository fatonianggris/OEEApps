package com.project.oee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ravi on 13/05/15.
 */
public class FactoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Factory> factoryList;
    private Context mContext;
    private static final String KEY_ID = "id_factory";

    public FactoryAdapter(Context c, List<Factory> factoryList) {
        this.mContext= c;
        this.factoryList = factoryList;
        notifyDataSetChanged();
   }
    @Override
    public int getCount() {
        return factoryList.size();
    }

    @Override
    public Object getItem(int location) {
        return factoryList.get(location);
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
            convertView = inflater.inflate(R.layout.list_history,null);


        TextView factory = (TextView) convertView.findViewById(R.id.factory_name);
        TextView officer = (TextView) convertView.findViewById(R.id.officer);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        factory.setText(factoryList.get(position).factory);
        officer.setText(factoryList.get(position).username);
        date.setText(factoryList.get(position).date);
        // getting movie data for the row
        final Factory m = factoryList.get(position);

        ImageView imageClick = (ImageView) convertView.findViewById(R.id.row_click_factory);
        try {
            imageClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.row_click_factory:

                            PopupMenu popup = new PopupMenu(mContext, v);
                            popup.getMenuInflater().inflate(R.menu.menu_factory, popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.export:
                                            ShareController s = new ShareController(mContext);
                                            s.export(m.getIdFactory().toString());
                                            break;
                                        case R.id.share:
                                            ShareController sh = new ShareController(mContext);
                                            sh.shareFile(m.getIdFactory().toString());
                                            break;
                                        case R.id.delete:
                                            deleteDialog(m.getIdFactory().toString(), m.getFactory());
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

    public void deleteDialog(final String id, final String fn){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete.");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                SQLiteHandler r = new SQLiteHandler(mContext);
                HashMap<String, String> fac = r.getFacIDLast();
                String idF = fac.get(KEY_ID);
                if(id.equals(idF)){
                    Toast.makeText(mContext, "Opps.. not working, this task still on progress" , Toast.LENGTH_LONG).show();
                } else{
                    r.deleteFac(id);
                    Toast.makeText(mContext, fn+" task has been deleted" , Toast.LENGTH_LONG).show();
                    ((FactoryActivity) mContext).onRefresh();
                }

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