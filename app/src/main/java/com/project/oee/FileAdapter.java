package com.project.oee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Ravi on 13/05/15.
 */
public class FileAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Files> filesList;
    private Context mContext;

    public FileAdapter(Context c, List<Files> filesList) {
        this.mContext= c;
        this.filesList = filesList;
        notifyDataSetChanged();
   }
    @Override
    public int getCount() {
        return filesList.size();
    }

    @Override
    public Object getItem(int location) {
        return filesList.get(location);
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
            convertView = inflater.inflate(R.layout.list_file,null);


        TextView filename = (TextView) convertView.findViewById(R.id.file_name);
        TextView size = (TextView) convertView.findViewById(R.id.size);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        filename.setText(filesList.get(position).nameFile);
        size.setText(filesList.get(position).size + " byte");
        date.setText(filesList.get(position).date);
        // getting movie data for the row
        final Files f = filesList.get(position);

        ImageView imageClick = (ImageView) convertView.findViewById(R.id.row_click_file);
        try {
            imageClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.row_click_file:

                            PopupMenu popup = new PopupMenu(mContext, v);
                            popup.getMenuInflater().inflate(R.menu.menu_file, popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.share:
                                            ShareController sh = new ShareController(mContext);
                                            sh.shareFiles(f.getNameFile().toString());
                                            break;
                                        case R.id.delete:
                                            deleteDialog(f.getNameFile().toString());
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
    public void deleteDialog(final String fn){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete.");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                ((FileActivity) mContext).delFile(fn);
                ((FileActivity) mContext).onRefresh();
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