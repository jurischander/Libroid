package com.schander.libroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

public class PermissionBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	     String action = intent.getAction();
	     if (LibraryBrowser.ACTION_USB_PERMISSION.equals(action)) {
	      
//	      Toast.makeText(MainActivity.this, "ACTION_USB_PERMISSION", Toast.LENGTH_LONG).show();
//	      textStatus.setText("ACTION_USB_PERMISSION");
	      
	                  synchronized (this) {
	                      UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

	                      if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
	                    	  if(device != null){
//	                           doReadRawDescriptors(device);
	                    	  }
	                      } 
	                      else {
//	                          Toast.makeText(MainActivity.this, "permission denied for device " + device, Toast.LENGTH_LONG).show();
//	                          textStatus.setText("permission denied for device " + device);
	                      }
	                  }
	              }
	    }

}
