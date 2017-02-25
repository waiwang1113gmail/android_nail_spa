package com.waiwang1113.mynailspa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.waiwang1113.mynailspa.entities.Shop;
import com.waiwang1113.mynailspa.request.RequestDispatcher;
import com.waiwang1113.mynailspa.response.Response;
import com.waiwang1113.mynailspa.utilities.Helper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.waiwang1113.mynailspa.constants.IntentConstants.INTENT_EXTRA_KEY_SHOP_ID;

public class ShopListActivity extends AppCompatActivity {

    @BindView(R.id.list_shops)
    ListView wShopList;

    @Inject
    RequestDispatcher mRequestDispatcher;
    ShopListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ButterKnife.bind(this);
        ((MainApplication)getApplication()).getApplicationComponent().inject(this);
        new RetrieveShopList().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * adapter for providing shop list
     */
    private class ShopListAdapter extends BaseAdapter{
        private List<Shop> shops;

        public ShopListAdapter(List<Shop> shops) {
            this.shops=shops;
        }

        @Override
        public int getCount() {
            return shops.size();
        }

        @Override
        public Object getItem(int i) {
            return shops.get(i);
        }

        @Override
        public long getItemId(int i) {
            return shops.get(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(ShopListActivity.this);
            if(view == null){
                view = inflater.inflate(R.layout.tmpl_shop_list_item,null);
            }
            final Shop shop = shops.get(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ShopListActivity.this,AppointmentListActivity.class);
                    intent.putExtra(INTENT_EXTRA_KEY_SHOP_ID,shop.getId());
                    startActivity(intent);
                }
            });
            Helper.setTextViewValue(R.id.textShopName, (ViewGroup) view,shop.getName());
            Helper.setTextViewValue(R.id.textShopDetail, (ViewGroup) view,shop.getDetail());
            return view;
        }
    }

    private class RetrieveShopList extends AsyncTask<Void,Void,Void> {
        Response<Shop> mResponse;
        @Override
        protected Void doInBackground(Void... voids) {
            mResponse = mRequestDispatcher.getShopList();
            return null;
        }
        @Override
        protected void onPostExecute(Void v){
            if(mResponse.getResponseCode()== Response.ResponseCode.SUCCESS) {
                mAdapter = new ShopListAdapter(mResponse.getPayload());
                wShopList.setAdapter(mAdapter);
            }else{
                Snackbar.make(ShopListActivity.this.wShopList, R.string.error_retrieve_shop_list, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }
}
