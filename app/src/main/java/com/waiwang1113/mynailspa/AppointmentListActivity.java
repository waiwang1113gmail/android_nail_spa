package com.waiwang1113.mynailspa;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IntRange;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.waiwang1113.mynailspa.entities.Appointment;
import com.waiwang1113.mynailspa.request.RequestDispatcher;
import com.waiwang1113.mynailspa.response.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.waiwang1113.mynailspa.constants.IntentConstants.INTENT_EXTRA_KEY_SHOP_ID;

/**
 * Activity for listing appointment for specified shop.
 * Current implementation is to list each appointments into ordered
 * list from most recent to order. Also the list displays the appointments
 * in different sections and each section has a header with title of
 * date that all appointments in.
 *
 */
public class AppointmentListActivity extends AppCompatActivity {
    @BindView(R.id.list_appointments)
    StickyListHeadersListView mAppointmentList;
    @BindView(R.id.fab)
    FloatingActionButton mAddButton;
    @Inject
    RequestDispatcher mRequestDispatcher;

    @IntRange(from=1,to=Integer.MAX_VALUE)
    private int mShopID;
    private StickyListHeadersAdapter mAdapter;
    private RetrieveAppointmentListTask mRetrieveAppointmentTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        ButterKnife.bind(this);
        ((MainApplication)getApplication()).getApplicationComponent().inject(this);
        mShopID = getIntent().getExtras().getInt(INTENT_EXTRA_KEY_SHOP_ID);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentListActivity.this,AddAppointmentActivity.class);
                intent.putExtra(INTENT_EXTRA_KEY_SHOP_ID,mShopID);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume(){
        super.onResume();
        setShopID(mShopID);
    }
    /*
        Set shop id for creating the list of appointments
     */
    private void setShopID(int mShopID) {
        this.mShopID = mShopID;

        if(mShopID==0){
            showErrorMessage(R.string.error_empty_shop_id);
        }else{
            if(mRetrieveAppointmentTask!=null){
                mRetrieveAppointmentTask.cancel(true);
            }
            mRetrieveAppointmentTask = new RetrieveAppointmentListTask();
            mRetrieveAppointmentTask.execute();
        }
    }

    private void showErrorMessage(@StringRes int msgid){
        Snackbar.make(this.mAppointmentList, msgid, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    private class AppointmentListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
        private LayoutInflater inflater;
        private List<Appointment> mAppointmentList;
        public AppointmentListAdapter(Context context,List<Appointment> mAppointmentList){
            inflater = LayoutInflater.from(context);
            this.mAppointmentList=mAppointmentList;

        }
        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tmpl_appointment_list_category, parent, false);
            }
            TextView header = (TextView) convertView.findViewById(R.id.text_date);
            Appointment appointment = mAppointmentList.get(position);
            String headerTitle = getHeader(appointment);
            header.setText(headerTitle);
            return convertView;
        }

        /*
         * Return the header title for given appointment
         * basically this method return the format date string
         * fot the date that appointment scheduled.
         * For example: Tue, Sep 1 2016
         * @param appointment
         * @return
         */
        private String getHeader(Appointment appointment){
            DateFormat inputDF = new SimpleDateFormat("EEE, MMM dd yyyy");
            return inputDF.format(appointment.getTime());
        }
        @Override
        public long getHeaderId(int position) {
            Appointment appointment = mAppointmentList.get(position);
            return getHeader(appointment).hashCode();
        }

        @Override
        public int getCount() {
            return mAppointmentList.size();
        }

        @Override
        public Object getItem(int i) {
            return mAppointmentList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tmpl_appointment_list_item, viewGroup, false);
            }
            TextView text = (TextView) convertView.findViewById(R.id.text_appointment_detail);
            Appointment appointment = mAppointmentList.get(i);
            text.setText(getFormattedAppoinmentDetail(appointment));
            return convertView;
        }
        private String getFormattedAppoinmentDetail(Appointment appointment){
            Date date = appointment.getTime();
            DateFormat inputDF = new SimpleDateFormat("hh:mm a");
            StringBuilder str=new StringBuilder();
            for(int i=0;i<appointment.getServices().size();i++){
                str.append(appointment.getServices().get(i).getName());
                if(i!=appointment.getServices().size()-1){
                    str.append(",");
                }

            }
            return String.format("%s - %s",inputDF.format(date),str.toString());
        }
    }
    private class RetrieveAppointmentListTask extends AsyncTask<Void,Void,Void> {
        Response<Appointment> mResponse;
        @Override
        protected Void doInBackground(Void... voids) {
            mResponse = mRequestDispatcher.getAllAppointment(mShopID);
            return null;
        }
        @Override
        protected void onPostExecute(Void v){
            if(mResponse.getResponseCode()== Response.ResponseCode.SUCCESS) {
                mAdapter=new AppointmentListAdapter(AppointmentListActivity.this,mResponse.getPayload());
                mAppointmentList.setAdapter(mAdapter);
            }else{
                Snackbar.make(mAppointmentList, R.string.error_retrieve_shop_list, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

}
