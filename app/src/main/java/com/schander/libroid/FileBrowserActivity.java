package com.schander.libroid;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class FileBrowserActivity extends Activity {
	
	private String currentPath;
	private ArrayList<FileEntry> fileList;
	private ListView fileListView;
	private FileBrowseAdapter adapter;
	private String storageDir = "/storage/sdcard0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_browser);
		File extStore = Environment.getExternalStorageDirectory();
		storageDir = extStore.getAbsolutePath();
		currentPath = storageDir;
		
		EditText pathText = (EditText) this.findViewById(R.id.pathText);
		pathText.setText(currentPath);

		fileList = updateFileList(currentPath);
		adapter = new FileBrowseAdapter(this, fileList);
        fileListView = (ListView) findViewById(R.id.listFilesView);
        fileListView.setAdapter(adapter);
        fileListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {             
            	refreshFileList(position);
            	adapter.clear();
            	adapter.addAll(fileList);
            	adapter.notifyDataSetChanged();
            }
       }); 
	}
	
	private void refreshFileList(int pos){
		String oldPath = currentPath;
		FileEntry entry = fileList.get(pos);
		// If a db file is selected, go back to main activity
		if(entry.getFile().isFile()){
			Intent returnIntent = new Intent();
			returnIntent.putExtra("result", entry.getFile().getAbsolutePath());
			setResult(RESULT_OK,returnIntent);
			finish();
		}
		// Otherwise it is a directory. We have to go in it
		if((entry.getFile() != null) && entry.getFile().isDirectory()){
			if(entry.getFileName().equals("..")){
				currentPath = entry.getFile().getParent();
			} else {
				currentPath = entry.getFile().getAbsolutePath();
			}
			if(!currentPath.contains(storageDir)){
				currentPath = storageDir;
			}
			ArrayList<FileEntry> localList = updateFileList(currentPath);
			if(localList != null){
				fileList = localList;
			} else {
				currentPath = oldPath;
			}
			EditText pathText = (EditText) this.findViewById(R.id.pathText);
			pathText.setText(currentPath);

		}
	}
	
	private ArrayList<FileEntry> updateFileList(String currentPath){
		ArrayList<FileEntry> localList = null;
		File file = new File(currentPath);
 		File[] files = file.listFiles();
		if(files != null){
			int fileIcon;
			localList = new ArrayList<FileEntry>();
			if(!currentPath.equals(storageDir)){
				fileIcon = R.drawable.folder_icon;
				localList.add(new FileEntry(fileIcon, "..", file.getParentFile()));
			}
			for(int i=0; i<files.length; i++){
				if(files[i].isDirectory()){
					fileIcon = R.drawable.folder_icon;
					localList.add(new FileEntry(fileIcon, files[i].getName(), files[i]));
				}
			}
			fileIcon = R.drawable.blank;
			FilenameFilter fileNameFilter = new FilenameFilter() {
	            @Override
	            public boolean accept(File dir, String name) {
	               if(name.lastIndexOf('.')>0)
	               {
	                  // get last index for '.' char
	                  int lastIndex = name.lastIndexOf('.');
	                  
	                  // get extension
	                  String str = name.substring(lastIndex);
	                  
	                  // match path name extension
	                  if(str.equals(".db"))
	                  {
	                     return true;
	                  }
	               }
	               return false;
	            }
	         };
			files = file.listFiles(fileNameFilter);
			for(int i=0; i<files.length; i++){
				localList.add(new FileEntry(fileIcon, files[i].getName(), files[i]));
			}
		}else{
			Log.w("updateFileList", " files is null !!");
		}
		return localList;
	}
	
}
