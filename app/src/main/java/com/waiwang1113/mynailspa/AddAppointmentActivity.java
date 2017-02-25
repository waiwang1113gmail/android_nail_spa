package com.waiwang1113.mynailspa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.waiwang1113.mynailspa.entities.Appointment;
import com.waiwang1113.mynailspa.entities.Service;
import com.waiwang1113.mynailspa.entities.Shop;
import com.waiwang1113.mynailspa.fragments.DatePickerFragment;
import com.waiwang1113.mynailspa.fragments.TimePickerFragment;
import com.waiwang1113.mynailspa.request.RequestDispatcher;
import com.waiwang1113.mynailspa.response.Response;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.waiwang1113.mynailspa.constants.IntentConstants.INTENT_EXTRA_KEY_SHOP_ID;

public class AddAppointmentActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener ,TimePickerDialog.OnTimeSetListener{
    private static final String TAG = AddAppointmentActivity.class.getCanonicalName();
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////Injected variables///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    @BindView(R.id.list_services)
    ListView mSelectedService;

    @BindView(R.id.select_date)
    EditText mDateField;
    @BindView(R.id.select_time)
    EditText mTimeField;

    @BindView(R.id.spinner_service_list)
    Spinner mSpinnerServices;

    @Inject
    RequestDispatcher mRequestDispatcher;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////Private Variables////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private Calendar mAppointmentDate;
    private int mShopID;

    //A list for all available servces
    //Note first item must be a hint.
    private ServicesSpinnerAdapter mServiceSpinnerAdapter;
    private SelectedServiceListAdapter mSelectedServicesAdapter;
    private RetrieveShopTask mRetrieveShopTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        //Injection code here
        ButterKnife.bind(this);
        ((MainApplication)getApplication()).getApplicationComponent().inject(this);
        Log.d(TAG,"Creating add appintment activity.");
        initializingAddAppointmentComponeents(getIntent().getExtras().getInt(INTENT_EXTRA_KEY_SHOP_ID));
    }

    private Menu mActionBarMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_appointment, menu);
        mActionBarMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            addAppointment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //Verify if appointment date is valid and the selected services
    //list is not empty. Then add appointment
    private void addAppointment(){
        if(mAppointmentDate.before(new Date())){
            Log.d(TAG,"Appointment date is not valid: " + mAppointmentDate);
            showErrorMessage(R.string.error_selected_date_is_not_valid);
            return;
        }
        if(mSelectedServicesAdapter.getSelectedServices().size()==0){
            Log.d(TAG,"No service is selected");
            showErrorMessage(R.string.error_no_service_is_selected);
            return;
        }
        Appointment appointment = new Appointment();
        appointment.setShopID(mShopID);
        appointment.setServices(mSelectedServicesAdapter.getSelectedServices());
        appointment.setTime(mAppointmentDate.getTime());
        Log.d(TAG,"Adding new Appointment: " + appointment);
        mRequestDispatcher.addAppointment(mShopID,appointment);
        finish();

    }
    private void toggleAddAppiontmentMenuItem(boolean enabled){
        MenuItem item = (MenuItem) findViewById(R.id.action_done);
        item.setEnabled(enabled);
    }
    /*
        method for initializing appointment page
        Basically it executes an asynctask to retrieve shop data and
        initialize appointment activity
     */
    private void initializingAddAppointmentComponeents(int shopid){
        Log.d(TAG,"Getting shop data "+shopid);
        this.mShopID = shopid;
        //default appointment time
        mAppointmentDate=Calendar.getInstance();
        updateTime();
        updateDate();
        if(mShopID==0){
            showErrorMessage(R.string.error_empty_shop_id);
        }else{
            if(mRetrieveShopTask!=null){
                mRetrieveShopTask.cancel(true);
            }
            mRetrieveShopTask = new RetrieveShopTask(mShopID);
            mRetrieveShopTask.execute();
        }
    }
    /*
        TODO find a better way to show error message
     */
    @MainThread
    private void showErrorMessage(@StringRes int msgid){
        Snackbar.make(this.mSelectedService, msgid, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    //Update time field from appointment date
    @MainThread
    private void updateTime() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        mTimeField.setText(dateFormat.format(mAppointmentDate.getTime()));
    }
    //Update date field from appointment date
    @MainThread
    private void updateDate() {
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy");
        mDateField.setText(dateFormat.format(mAppointmentDate.getTime()));
    }
    //Setup services spinner and listview for selected services
    @MainThread
    private void setUpSpinnerAndSelectedServicesList(Shop shop){
        Log.d(TAG,"Adding Services: "+shop.getServices());
        mServiceSpinnerAdapter = new ServicesSpinnerAdapter(new ArrayList<>(shop.getServices()));
        mSpinnerServices.setAdapter(mServiceSpinnerAdapter);
        mSpinnerServices.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0) return;
        Service selectedItem=(Service)mServiceSpinnerAdapter.getItem(i);
        mSpinnerServices.setSelection(0);
        Log.d(TAG, "Select Service: "+selectedItem.getName());
        mServiceSpinnerAdapter.remove(i);
        mSelectedServicesAdapter.addService(selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
    @MainThread
    private void setShop(Shop shop) {
        Log.d(TAG,"Set shop: "+shop);
        mSelectedServicesAdapter=new SelectedServiceListAdapter();
        mSelectedService.setAdapter(mSelectedServicesAdapter);
        setUpSpinnerAndSelectedServicesList(shop);
        setUpDateTimeFields();
    }
    @MainThread
    private void setUpDateTimeFields() {
        mDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerFragment editNameDialogFragment = DatePickerFragment.newInstance(AddAppointmentActivity.this);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });
        mTimeField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                TimePickerFragment editNameDialogFragment = TimePickerFragment.newInstance(AddAppointmentActivity.this);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mAppointmentDate.set(year,month,day);
        updateDate();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int minutes, int seconds) {
        mAppointmentDate.set(Calendar.HOUR,minutes);
        mAppointmentDate.set(Calendar.MINUTE,seconds);
        updateTime();
    }

    private class RetrieveShopTask extends AsyncTask<Void,Void,Response<Shop>>{
        private int mShopID;

        public RetrieveShopTask(int mShopID) {
            this.mShopID = mShopID;
        }

        @Override
        protected Response<Shop> doInBackground(Void... voids) {
            return mRequestDispatcher.getShop(mShopID);
        }
        @Override
        protected void onPostExecute(Response<Shop> result){
            if(result.getResponseCode()== Response.ResponseCode.FAIL
                    ||result.getPayload()==null||result.getPayload().size()==0){
                Log.e(TAG,"Failed to fetch shop data for id: " + mShopID);
                Snackbar.make(mSelectedService, R.string.error_retrieve_shop_data, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else{
                setShop(result.getPayload().get(0));
            }
        }
    }
    private class SelectedServiceListAdapter extends BaseAdapter{
        private List<Service> mSelectedService;
        public SelectedServiceListAdapter(){
            this.mSelectedService=new ArrayList<>();
        }
        public List<Service> getSelectedServices(){
            return mSelectedService;
        }
        public void addService(Service s){
            mSelectedService.add(s);
            this.notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return mSelectedService.size();
        }
        @Override
        public Object getItem(int i) {
            return mSelectedService.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(AddAppointmentActivity.this);
            if(view == null){
                view = inflater.inflate(R.layout.tmpl_service_type_list_item,null);
            }
            final Service s= mSelectedService.get(i);
            TextView title = (TextView)view.findViewById(R.id.text_service_title);
            title.setText(s.getName());
            View deleteButton = view.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedService.remove(s);
                    notifyDataSetChanged();
                    mServiceSpinnerAdapter.add(s);

                }
            });
            return view;
        }
    }

    /*
        Adapter class for service spinner
        Note: there is always an extra item for displaying hint
     */
    private class ServicesSpinnerAdapter extends BaseAdapter{
        private List<Service> mAvailableService;
        public ServicesSpinnerAdapter(@NotNull List<Service> list){
            this.mAvailableService=list;
        }
        public void remove(int i){
            mAvailableService.remove(i-1);
            notifyDataSetChanged();
        }
        public void add(Service s){
            mAvailableService.add(s);
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return mAvailableService.size()+1;
        }
        @Override
        public Object getItem(int i) {
            return i==0?null:mAvailableService.get(i-1);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(AddAppointmentActivity.this);
            if(view == null){
                view = inflater.inflate(android.R.layout.simple_spinner_item,null);
            }
            String str = i==0?getString(R.string.hint_select_service):mAvailableService.get(i-1).getName();

            TextView title = (TextView)view.findViewById(android.R.id.text1);
            title.setText(str);
            return view;
        }
    }


}
