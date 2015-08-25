package com.schander.libroid;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileBrowseAdapter extends ArrayAdapter<FileEntry> {

	private final Context context;
	private final ArrayList<FileEntry> filesList;
	
	public FileBrowseAdapter(Context context, ArrayList<FileEntry> files) {
		super(context, R.layout.file_browser_line, files);
		this.context = context; 
		this.filesList = files;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		
		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(convertView == null){
			// 2. Get rowView from inflater
			convertView = inflater.inflate(R.layout.file_browser_line, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.fileIconView);
			viewHolder.fileNameView = (TextView) convertView.findViewById(R.id.fileNameTextView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.imageView.setImageResource(filesList.get(position).getIcon());
		viewHolder.fileNameView.setText(filesList.get(position).getFileName());
		return convertView;
	}
	
	static class ViewHolder {
		public ImageView imageView;
		public TextView fileNameView;
	}
	
}
