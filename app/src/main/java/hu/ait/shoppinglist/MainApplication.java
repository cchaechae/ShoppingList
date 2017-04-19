package hu.ait.shoppinglist;

import android.app.Application;
import android.content.res.Configuration;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

/**
 * Created by chaelimseo on 4/11/17.
 */

public class MainApplication extends Application{

    private Realm realmItems;

    @Override
    public void onCreate() {
        super.onCreate();

        //create database
        Realm.init(this);
    }

    public void openRealm(){

        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmItems = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmItems.close();
    }

    public Realm getRealmPlaces() {
        return realmItems;
    }
}
