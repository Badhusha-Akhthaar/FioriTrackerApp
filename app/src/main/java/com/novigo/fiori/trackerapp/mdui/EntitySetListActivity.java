package com.novigo.fiori.trackerapp.mdui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.novigo.fiori.trackerapp.mdui.customers.CustomersActivity;
import com.novigo.fiori.trackerapp.mdui.productcategories.ProductCategoriesActivity;
import com.novigo.fiori.trackerapp.mdui.producttexts.ProductTextsActivity;
import com.novigo.fiori.trackerapp.mdui.products.ProductsActivity;
import com.novigo.fiori.trackerapp.mdui.purchaseorderheaders.PurchaseOrderHeadersActivity;
import com.novigo.fiori.trackerapp.mdui.purchaseorderitems.PurchaseOrderItemsActivity;
import com.novigo.fiori.trackerapp.mdui.salesorderheaders.SalesOrderHeadersActivity;
import com.novigo.fiori.trackerapp.mdui.salesorderitems.SalesOrderItemsActivity;
import com.novigo.fiori.trackerapp.mdui.stock.StockActivity;
import com.novigo.fiori.trackerapp.mdui.suppliers.SuppliersActivity;
import com.sap.cloud.mobile.fiori.object.ObjectCell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.novigo.fiori.trackerapp.R;


/*
 * An activity to display the list of all entity types from the OData service
 */
public class EntitySetListActivity extends AppCompatActivity {

    private static final int SETTINGS_SCREEN_ITEM = 200;
    private static final Logger LOGGER = LoggerFactory.getLogger(EntitySetListActivity.class);
    private static final int BLUE_ANDROID_ICON = R.drawable.ic_android_blue;
    private static final int WHITE_ANDROID_ICON = R.drawable.ic_android_white;

    public enum EntitySetName {
        Customers("Customers", R.string.eset_customers,BLUE_ANDROID_ICON),
        ProductCategories("ProductCategories", R.string.eset_productcategories,WHITE_ANDROID_ICON),
        ProductTexts("ProductTexts", R.string.eset_producttexts,BLUE_ANDROID_ICON),
        Products("Products", R.string.eset_products,WHITE_ANDROID_ICON),
        PurchaseOrderHeaders("PurchaseOrderHeaders", R.string.eset_purchaseorderheaders,BLUE_ANDROID_ICON),
        PurchaseOrderItems("PurchaseOrderItems", R.string.eset_purchaseorderitems,WHITE_ANDROID_ICON),
        SalesOrderHeaders("SalesOrderHeaders", R.string.eset_salesorderheaders,BLUE_ANDROID_ICON),
        SalesOrderItems("SalesOrderItems", R.string.eset_salesorderitems,WHITE_ANDROID_ICON),
        Stock("Stock", R.string.eset_stock,BLUE_ANDROID_ICON),
        Suppliers("Suppliers", R.string.eset_suppliers,WHITE_ANDROID_ICON);

        private int titleId;
        private int iconId;
        private String entitySetName;

        EntitySetName(String name, int titleId, int iconId) {
            this.entitySetName = name;
            this.titleId = titleId;
            this.iconId = iconId;
        }

        public int getTitleId() {
                return this.titleId;
        }

        public String getEntitySetName() {
                return this.entitySetName;
        }
    }

    private final List<String> entitySetNames = new ArrayList<>();
    private final Map<String, EntitySetName> entitySetNameMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entity_set_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        entitySetNames.clear();
        entitySetNameMap.clear();
        for (EntitySetName entitySet : EntitySetName.values()) {
            String entitySetTitle = getResources().getString(entitySet.getTitleId());
            entitySetNames.add(entitySetTitle);
            entitySetNameMap.put(entitySetTitle, entitySet);
        }

        final ListView listView = findViewById(R.id.entity_list);
        final EntitySetListAdapter adapter = new EntitySetListAdapter(this, R.layout.element_entity_set_list, entitySetNames);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            EntitySetName entitySetName = entitySetNameMap.get(adapter.getItem(position));
            Context context = EntitySetListActivity.this;
            Intent intent;
            switch (entitySetName) {
                case Customers:
                    intent = new Intent(context, CustomersActivity.class);
                    break;
                case ProductCategories:
                    intent = new Intent(context, ProductCategoriesActivity.class);
                    break;
                case ProductTexts:
                    intent = new Intent(context, ProductTextsActivity.class);
                    break;
                case Products:
                    intent = new Intent(context, ProductsActivity.class);
                    break;
                case PurchaseOrderHeaders:
                    intent = new Intent(context, PurchaseOrderHeadersActivity.class);
                    break;
                case PurchaseOrderItems:
                    intent = new Intent(context, PurchaseOrderItemsActivity.class);
                    break;
                case SalesOrderHeaders:
                    intent = new Intent(context, SalesOrderHeadersActivity.class);
                    break;
                case SalesOrderItems:
                    intent = new Intent(context, SalesOrderItemsActivity.class);
                    break;
                case Stock:
                    intent = new Intent(context, StockActivity.class);
                    break;
                case Suppliers:
                    intent = new Intent(context, SuppliersActivity.class);
                    break;
                    default:
                        return;
            }
            context.startActivity(intent);
        });
            
    }

    public class EntitySetListAdapter extends ArrayAdapter<String> {

        EntitySetListAdapter(@NonNull Context context, int resource, List<String> entitySetNames) {
            super(context, resource, entitySetNames);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            EntitySetName entitySetName = entitySetNameMap.get(getItem(position));
            if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_entity_set_list, parent, false);
            }
            String headLineName = getResources().getString(entitySetName.titleId);
            ObjectCell entitySetCell = convertView.findViewById(R.id.entity_set_name);
            entitySetCell.setHeadline(headLineName);
            entitySetCell.setDetailImage(entitySetName.iconId);
            return convertView;
        }
    }
                
    @Override
    public void onBackPressed() {
            moveTaskToBack(true);
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, SETTINGS_SCREEN_ITEM, 0, R.string.menu_item_settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LOGGER.debug("onOptionsItemSelected: " + item.getTitle());
        switch (item.getItemId()) {
            case SETTINGS_SCREEN_ITEM:
                LOGGER.debug("settings screen menu item selected.");
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivityForResult(intent, SETTINGS_SCREEN_ITEM);
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LOGGER.debug("EntitySetListActivity::onActivityResult, request code: " + requestCode + " result code: " + resultCode);
        if (requestCode == SETTINGS_SCREEN_ITEM) {
            LOGGER.debug("Calling AppState to retrieve settings after settings screen is closed.");
        }
    }

}
