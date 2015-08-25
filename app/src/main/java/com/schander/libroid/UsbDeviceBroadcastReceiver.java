package com.schander.libroid;

import java.io.File;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class UsbDeviceBroadcastReceiver extends BroadcastReceiver {
	private static final int countLoops = 10000;
	UsbDevice deviceFound = null;
	UsbDeviceConnection usbDeviceConnection;
	UsbInterface usbInterface;
	private final String mountPath = "/sdcard/usbStorage/";
	private LibraryBrowser parent = null;
	private AsyncTask<Void, Void, Void> devicePollingTask = null;
	
	public UsbDeviceBroadcastReceiver(LibraryBrowser parent){
		this.parent = parent;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
	     String action = intent.getAction();
	     if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
	    	 deviceFound = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
	    	 Log.i("USB_SJX", "USB Device attached !" + deviceFound.toString());
//	    	 Toast.makeText(context, "ACTION_USB_DEVICE_ATTACHED: \n" + deviceFound.toString(), Toast.LENGTH_LONG).show();
	    	 devicePollingTask = new MonitorDeviceTask().execute();
	     }else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
	    	 UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
	    	 Log.i("USB_SJX", "USB Device detached !");
//	    	 Toast.makeText(context, "ACTION_USB_DEVICE_DETACHED: \n" + device.toString(), Toast.LENGTH_LONG).show();
	    	 if(device!=null){
	    		 if(device == deviceFound){
	    			 releaseUsb();
	    		 }
	    	 }
	    	 this.parent.mountedDevices = null;
	    	 if(devicePollingTask != null){
	    		 devicePollingTask.cancel(true);
	    		 devicePollingTask = null;
	    	 }
	    	 FragmentTransaction transactionManager = this.parent.getFragmentManager().beginTransaction();
	    	 transactionManager.replace(R.id.action_bar_fragment, new IconsBarFragment());
	    	 transactionManager.commit();
	     }		
	}
	
	public void addDevice(){
		File mountDir = new File(mountPath);
		this.parent.mountedDevices = mountDir.listFiles();
		FragmentTransaction transactionManager = this.parent.getFragmentManager().beginTransaction();
		transactionManager.replace(R.id.action_bar_fragment, new ActionBarFragment());
		transactionManager.commit();
	}
	
	 private void releaseUsb(){
		  
		 if(usbDeviceConnection != null){
			 if(usbInterface != null){
				 usbDeviceConnection.releaseInterface(usbInterface);
				 usbInterface = null;
			 }
			 usbDeviceConnection.close();
			 usbDeviceConnection = null;
		 }
		 	deviceFound = null;
	}
	 
	 private class MonitorDeviceTask extends AsyncTask<Void, Void, Void> {

			@Override
			protected Void doInBackground(Void... params) {
				String[] devs = null;
				File mountDir = new File(mountPath);
				while((devs == null) || (devs.length == 0)){
					devs = mountDir.list();
			    	 if((devs != null) && (devs.length !=0)){
			    		 addDevice();
			    		 break;
			    	 }
				}
				return null;
			}

		}
}
