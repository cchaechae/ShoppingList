package hu.ait.shoppinglist;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.ait.shoppinglist.adapter.ShoppingAdapter;
import hu.ait.shoppinglist.data.Item;
import hu.ait.shoppinglist.touch.ShoppingListTouchHelper;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {


    public static final int REQUEST_NEW_ITEM = 101;
    public static final int REQUEST_EDIT_ITEM = 102;
    private ShoppingAdapter shoppingAdapter;
    private CoordinatorLayout layoutContent;
    public static final String KEY_EDIT = "KEY_EDIT";
    private int itemToEditPosition = -1;
    List<Item> itemsResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();

        //saving the realm objects to an array
        RealmResults<Item> allItems = getRealm().where(Item.class).findAll();
        Item itemArray[] = new Item[allItems.size()];
        itemsResult = new ArrayList<Item>(Arrays.asList(allItems.toArray(itemArray)));

        shoppingAdapter = new ShoppingAdapter(itemsResult, this);

        //Create a recycler view
        RecyclerView rcvItems = (RecyclerView) findViewById(
                R.id.recyclerViewItems);
        rcvItems.setLayoutManager(new LinearLayoutManager(this));
        rcvItems.setAdapter(shoppingAdapter);

        ShoppingListTouchHelper shoppingcallback = new ShoppingListTouchHelper(shoppingAdapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(shoppingcallback);
        touchHelper.attachToRecyclerView(rcvItems);

        layoutContent = (CoordinatorLayout) findViewById(R.id.layoutContent);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                // User chose the "Add" item, show the app settings UI...
                showNewItemActivity();
                return true;

            case R.id.action_delete:
                // User chose the "Delete" action, delete
                //shoppingAdapter.removePurchased();
                shoppingAdapter.removeAll();
                //deleteAll();
                return true;

            case R.id.action_buy:
                shoppingAdapter.removePurchased();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                showNewItemActivity();
                return true;

        }
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmPlaces();
    }

    public void showNewItemActivity(){

        Intent intentStart = new Intent(MainActivity.this, NewItemActivity.class);
        startActivityForResult(intentStart, REQUEST_NEW_ITEM);
    }


    public void showEditItemActivity(String placeID, int position) {
        Intent intentStart = new Intent(MainActivity.this,
                NewItemActivity.class);
        itemToEditPosition = position;

        intentStart.putExtra(KEY_EDIT, placeID);
        startActivityForResult(intentStart, REQUEST_EDIT_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                String itemID = data.getStringExtra(NewItemActivity.KEY_ITEM);

                Item item = getRealm().where(Item.class).equalTo("itemID",itemID).findFirst();

                if(requestCode == REQUEST_NEW_ITEM){
                    shoppingAdapter.addItem(item);
                    showSnackBarMessage("Item added");
                }
                else if (requestCode == REQUEST_EDIT_ITEM){

                    shoppingAdapter.updateItem(itemToEditPosition, item);
                    showSnackBarMessage("Item edited!");
                }

                break;

            case RESULT_CANCELED:
                showSnackBarMessage("place adding cancelled");
                break;
        }
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction("action hide", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    public void deleteItem(Item item) {
        getRealm().beginTransaction();
        item.deleteFromRealm();
        getRealm().commitTransaction();
    }

    public void purchaseItem(Item item){
        getRealm().beginTransaction();
        item.setPurchased(true);
        getRealm().commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }
}
