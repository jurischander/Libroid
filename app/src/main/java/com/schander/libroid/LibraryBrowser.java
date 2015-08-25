package com.schander.libroid;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class LibraryBrowser extends Activity {

	public static final String ACTION_USB_PERMISSION = "de.schander.libroid.USB_PERMISSION";
	public static final String CURRENT_DATABASE = "currentDatabase";
	PendingIntent mPermissionIntent;
	UsbDevice deviceFound = null;
	BroadcastReceiver mUsbReceiver;
	BroadcastReceiver mUsbDeviceReceiver;
	BookListAdapter bookListAdapter = null;
	ListView booksList;

	public File[] mountedDevices = null;
	
	private String dataBase = null;
	
	private DataBaseArrayFragment dataBaseFragment = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        
        dataBase = sharedPref.getString(CURRENT_DATABASE, null);
        if(!isDatabaseAvailable()){
       		dataBase = null;
       		SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(CURRENT_DATABASE, dataBase);
            editor.commit();
        }
        try{
        	ArrayList<BookEntry> books = DataStore.getInstance(this, dataBase).getStoredBooks();
        	if(books == null){
        		throw new IllegalArgumentException("Wrong database !");
        	}
        }catch(Exception e){
        	dataBase = null;
       		SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(CURRENT_DATABASE, dataBase);
            editor.commit();
        	Toast.makeText(this, "Database " + dataBase + " is not available", Toast.LENGTH_LONG).show();
        }
        
        FragmentTransaction transactionManager = getFragmentManager().beginTransaction();
        transactionManager.replace(R.id.action_bar_fragment, new IconsBarFragment());
        if(dataBase == null){
        	transactionManager.replace(R.id.data_array_fragment, new NoDatabaseDataArrayFragment());
        }else{
        	if(dataBaseFragment == null){
        		dataBaseFragment = DataBaseArrayFragment.newInstance(this);
        	}
       		transactionManager.replace(R.id.data_array_fragment, dataBaseFragment);
        }
    	transactionManager.commit();

        setContentView(R.layout.dynamic_main_layout);
    	
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        mUsbReceiver = new PermissionBroadcastReceiver();
        mUsbDeviceReceiver = new UsbDeviceBroadcastReceiver(this);
        registerReceiver(mUsbReceiver, filter);
        registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
        registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));
        
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        FragmentTransaction transactionManager = getFragmentManager().beginTransaction();
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	if(dataBaseFragment == null){
        		dataBaseFragment = DataBaseArrayFragment.newInstance(this);
        	}
       		transactionManager.replace(R.id.data_array_fragment, dataBaseFragment);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        	if(dataBaseFragment == null){
        		dataBaseFragment = DataBaseArrayFragment.newInstance(this);
        	}
       		transactionManager.replace(R.id.data_array_fragment, dataBaseFragment);
        }
    	transactionManager.commit();
    }    
    
    @Override
    protected void onDestroy() {
    	unregisterReceiver(mUsbReceiver);
    	unregisterReceiver(mUsbDeviceReceiver);
    	super.onDestroy();
    }

    public void onConnectToServerClicked(View view){
    	Intent intent = new Intent(this, EnterServerUrlDialog.class);
    	startActivity(intent);
    }

    public void onBrowseFileSystemClicked(View view){
    	Intent intent = new Intent(this, FileBrowserActivity.class);
    	startActivityForResult(intent, 1);
    }
    
    public void onCreateNewDatabaseClicked(View view){
    	
    }
    
    private boolean isDatabaseAvailable(){
    	if(dataBase == null){
    		return false;
    	}else{
    		int lastIndex = dataBase.lastIndexOf('/');
    		File dbDir = new File(dataBase.substring(0, lastIndex));
    		try{
    			String[] files = dbDir.list();
    			if((files == null) || (files.length == 0)){
    				return false;
    			}
    		}catch(Exception e){
    			return false;
    		}
    		return true;
    	}
    }
    
}
