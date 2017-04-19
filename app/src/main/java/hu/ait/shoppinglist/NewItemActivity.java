package hu.ait.shoppinglist;

import android.content.Intent;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.UUID;

import hu.ait.shoppinglist.data.Item;
import io.realm.Realm;

public class NewItemActivity extends AppCompatActivity {

    static final String KEY_ITEM = "KEY_ITEM";
    private Spinner spinnerItemType;
    private EditText etName;
    private EditText etDescribe;
    private EditText etPrice;
    private Item itemToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        setupUI();

        if (getIntent().getSerializableExtra(MainActivity.KEY_EDIT) != null) {
            initEdit();
        } else {
            initCreate();
        }
    }

    private void initCreate(){
        getRealm().beginTransaction();
        itemToEdit = getRealm().createObject(Item.class, UUID.randomUUID().toString());
        //should i put set pick up date here?
        getRealm().commitTransaction();
    }

    private void initEdit(){
        String itemID = getIntent().getStringExtra(MainActivity.KEY_EDIT);
        itemToEdit = getRealm().where(Item.class).equalTo("itemID", itemID).findFirst();

        etName.setText(itemToEdit.getItemName());
        etDescribe.setText(itemToEdit.getDescription());
        spinnerItemType.setSelection(itemToEdit.getItemType().getValue());
        etPrice.setText(itemToEdit.getItemPrice());

    }

    private void setupUI() {

        spinnerItemType = (Spinner) findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               R.array.itemtypes_array, android.R.layout.simple_spinner_item );
        spinnerItemType.setAdapter(adapter);

        etName = (EditText) findViewById(R.id.etName);
        etDescribe = (EditText) findViewById(R.id.etDesc);
        etPrice = (EditText) findViewById(R.id.etPrice);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

    }

    public void saveItem(){

        Intent intentResult = new Intent();

        getRealm().beginTransaction();
        itemToEdit.setItemName(etName.getText().toString());
        itemToEdit.setDescription(etDescribe.getText().toString());
        itemToEdit.setItemPrice(etPrice.getText().toString());
        itemToEdit.setItemType(spinnerItemType.getSelectedItemPosition());
        itemToEdit.setPurchased(false);
        getRealm().commitTransaction();

        intentResult.putExtra(KEY_ITEM, itemToEdit.getItemID());
        setResult(RESULT_OK, intentResult);
        finish();
    }


    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmPlaces();
    }

}
