package com.snapcrap.bmanica.portal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.snapcrap.bmanica.portal.Calc;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calculadora extends AppCompatActivity {

    private EditText editText;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private Button calcular;
    private ListView listView;
    private ListView indexView;

    private ArrayList<CalcArray> calcData;
    //private ArrayList<PostData> indexData;
    private Calc data = new Calc();

    private CalcAdapter postAdapter;
   // private PostItemAdapter indexAdapter;

    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        overridePendingTransition(R.animator.fadein, R.animator.fadeout);

        calcData = new ArrayList<CalcArray>();
        //indexData = new ArrayList<PostData>();

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);

        calcular = (Button) findViewById((R.id.button));

        listView = (ListView) this.findViewById(R.id.listView);
        postAdapter = new CalcAdapter(this, R.layout.calcitem, calcData);
        listView.setAdapter(postAdapter);

        /*indexView = (ListView) this.findViewById(R.id.listView2);
        indexAdapter = new PostItemAdapter(this, R.layout.postitem, indexData);
        listView.setAdapter(indexAdapter);*/

        editText4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Calculadora.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        calcular.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        data.val_parcela = Integer.parseInt(editText.getText().toString());
                        data.juros = Double.parseDouble(editText2.getText().toString());
                        data.dia_venc = Integer.parseInt(editText3.getText().toString());

/*                        try {
                            data.quitacao = editText4.getText());
                            Log.d("Data", String.valueOf(data.quitacao));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/

                        data.quant_parcelas = Integer.parseInt(editText5.getText().toString());
                        calcData.clear();
                        mostraCalculo();
                    }
                }
        );
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        editText4.setText(sdf.format(myCalendar.getTime()));
        data.quitacao = myCalendar.getTime();
        Log.d("SDF", sdf.format(myCalendar.getTime()));
    }

    private ArrayList<CalcArray> mostraCalculo() {
        CalcArray pdData = null;

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        Date hoje = data.quitacao;
        Log.d("hoje", String.valueOf(hoje));
        int umDia = 86400000;

        int dia = hoje.getDate();
        int mes = hoje.getMonth() + 1;
        int ano = hoje.getYear() + 1900;

        Log.d("DIA", String.valueOf(dia));
        Log.d("MES", String.valueOf(mes));
        Log.d("ANO", String.valueOf(ano));

        Log.d("VALOR PARCELA", String.valueOf(data.val_parcela));
        Log.d("TAXA JUROS", String.valueOf(data.juros));
        Log.d("VENCIMENTO PARCELA", String.valueOf(data.dia_venc));
        Log.d("QUITAÇÃO", String.valueOf(data.quitacao));
        Log.d("QUANTIDADE PARCELAS", String.valueOf(data.quant_parcelas));

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        double valor = data.val_parcela;
        double acumulado = 0;
        Date _date = null;
        double val0 = 0;
        double val1 = 0;

        if (dia > data.dia_venc) {
            mes = mes + 1;
            if (mes == 12) {
                mes = 0;
                ano = ano + 1;
            }
        }

        for (int i = 0; i < data.quant_parcelas; i++) {
            pdData = new CalcArray();

            String vencimento = data.dia_venc + "/" + mes + "/" + ano;

            try {
                _date = sdf.parse(vencimento);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            pdData._vencimento = vencimento;

            int dias = (int) ((_date.getTime() - data.quitacao.getTime()) / umDia);
            Log.d("_DATE", String.valueOf(_date));
            Log.d("DATA.QUITACAO", String.valueOf(data.quitacao));

            val0 = Math.pow((1.0 + data.juros/100.0), 1.0/30.0);
            val1 = Math.pow(val0, dias);
            valor = data.val_parcela / val1;

            Log.d("VALOR", String.valueOf(valor));
            pdData._valor = Math.round(valor*100.0)/100.0;


            acumulado = acumulado + valor;
            pdData._acumulador = Math.round(acumulado*100.0)/100.0;

            calcData.add(pdData);

            mes++;
            if (mes == 12) {
                mes = 0;
                ano = ano + 1;
            }

        }
        postAdapter.notifyDataSetChanged();
        return calcData;
    }

}
