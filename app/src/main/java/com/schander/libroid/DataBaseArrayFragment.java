package com.schander.libroid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class DataBaseArrayFragment extends Fragment {
	
	ListView booksList;
	BookListAdapter bookListAdapter = null;
	private static LibraryBrowser context; 
	
	public DataBaseArrayFragment(){
	}
	
	public static final DataBaseArrayFragment newInstance(LibraryBrowser context){
		DataBaseArrayFragment instance = new DataBaseArrayFragment();
		DataBaseArrayFragment.context = context;
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState) {
		// Create, or inflate the Fragment’s UI, and return it.
		// If this Fragment has no UI then return null.
		View view = inflater.inflate(R.layout.data_array_with_book_list, container, false);
		booksList = (ListView) view.findViewById(R.id.bookListView);
		bookListAdapter = new BookListAdapter(DataBaseArrayFragment.context, DataStore.getInstance().getStoredBooks());
		booksList.setAdapter(bookListAdapter);
		registerForContextMenu(booksList);
		return view;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getActivity().getMenuInflater();
	    if(DataBaseArrayFragment.context.mountedDevices == null){
	    	inflater.inflate(R.menu.floating_menu_books_processing, menu);
	    }else{
	    	inflater.inflate(R.menu.floating_menu_books_processing_with_device, menu);
	    }
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.sendToDevice:
	        	copyToDevice(info.position);
	            return true;
	        case R.id.editMetadata:
	            //deleteNote(info.id);
	            return true;
	        case R.id.removeFromLibrary:
	            //deleteNote(info.id);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	private void copyToDevice(int position){
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		ArrayList<BookEntry> booksList = DataStore.getInstance().getStoredBooks();
		BookEntry selectedBook = booksList.get(position);
		try{
			File bookFile = selectedBook.getFile();
			if(bookFile != null){
				String fileName = bookFile.getName();
				String devicePath = DataBaseArrayFragment.context.mountedDevices[0].getAbsolutePath();
				File destFile = new File(devicePath + "/documents/" + fileName);
				inStream = new FileInputStream(bookFile);
				outStream = new FileOutputStream(destFile);
				FileChannel inChannel = inStream.getChannel();
				FileChannel outChannel = outStream.getChannel();
				inChannel.transferTo(0, inChannel.size(), outChannel);
				Toast.makeText(DataBaseArrayFragment.context, "The file " + fileName + " is successfully transferred to your device !",
						Toast.LENGTH_LONG).show();
				bookListAdapter.setTransferredToDevice(position, true);
				bookListAdapter.notifyDataSetChanged();
			}
		}catch(Exception e){}
		try{
			inStream.close();
			outStream.close();
		}catch(Exception e){}
	}
	
	public void onClickBook(View view){
		return;
	}

}
