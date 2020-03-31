package com.example.registrationform;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends BaseAdapter implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int CALL_REQUEST_CODE = 400;

    private Context context;
    private ArrayList<User> username ;

    private User user;
    private String[] callPermission;

    CustomAdapter(Context context, ArrayList<User> UserName) {
        this.context = context;
        this.username = UserName;
    }

    @Override
    public int getCount() {
        return username.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final viewHolder holder;
        LayoutInflater layoutInflater;
        if (convertView == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.row_item, null);
            holder = new viewHolder();
            holder.username = convertView.findViewById(R.id.name);
            holder.email = convertView.findViewById(R.id.email);
            holder.time = convertView.findViewById(R.id.time);
            holder.pic = convertView.findViewById(R.id.image);
            holder.call = convertView.findViewById(R.id.call);
            intiPermissions();
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        user = username.get(position);
        holder.username.setText(user.getUserName());
        holder.email.setText(user.getEmail());
        holder.time.setText(user.getTime());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCallPermission()) {
                    requestCallPermission();
                } else {
                    makeCall();
                }
            }
        });

        holder.pic.setImageBitmap(decodeImage(user.getImage()));
        return convertView;
    }

    private Bitmap decodeImage(byte[] outImage) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        return BitmapFactory.decodeStream(imageStream);
    }

    private boolean checkCallPermission() {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestCallPermission() {
        ActivityCompat.requestPermissions((ListViewActivity)context, callPermission, CALL_REQUEST_CODE);
    }

    private void intiPermissions() {
        callPermission = new String[]{Manifest.permission.CALL_PHONE};
    }

    private void makeCall() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + user.getMobile()));
        context.startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean callPermissionAccepted = grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED;
                if (callPermissionAccepted) {
                    makeCall();
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public static class viewHolder {
        TextView username;
        TextView email;
        TextView time;
        CircleImageView pic;
        ImageView call;

    }
}


