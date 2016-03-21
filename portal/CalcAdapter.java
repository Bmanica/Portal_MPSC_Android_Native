package com.snapcrap.bmanica.portal;

/**
 * PostItemAdapter.java
 *
 * Adapter Class which configs and returns the View for ListView
 *
 */

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import com.snapcrap.bmanica.portal.PostData;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalcAdapter extends ArrayAdapter<CalcArray> {
    private Activity myContext;
    private ArrayList<CalcArray> datas;

    public CalcAdapter(Context context, int textViewResourceId,ArrayList<CalcArray> objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        datas = objects;
    }

    static class ViewHolder {
        TextView postVencimento;
        TextView postParcela;
        TextView postAcumulado;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        CalcArray post = datas.get(position);
        if (convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.calcitem, null);
            viewHolder = new ViewHolder();
            viewHolder.postVencimento = (TextView) convertView.findViewById(R.id.vencimento);
            viewHolder.postParcela = (TextView) convertView.findViewById(R.id.parcela);
            viewHolder.postAcumulado = (TextView) convertView.findViewById(R.id.acumulado);
            convertView.setTag(viewHolder);
            convertView.setBackgroundResource(R.drawable.calc_corners);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.postVencimento.setText((post._vencimento).toString());
        viewHolder.postParcela.setText(String.valueOf(post._valor));
        viewHolder.postAcumulado.setText(String.valueOf(post._acumulador));

        if(datas.get(position) != null ) {
            viewHolder.postVencimento.setTextColor(Color.DKGRAY);
            // viewHolder.postTitleView.setText(datas.get(position));

            //int color = Color.argb( 200, 255, 64, 64 );
			/* viewHolder.postTitleView.setBackgroundColor(color);
            viewHolder.postDateView.setBackgroundColor(color);*/

        }
        return convertView;
    }
}
