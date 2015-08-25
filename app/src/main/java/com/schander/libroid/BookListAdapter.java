package com.schander.libroid;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.BitmapFactory;;


public class BookListAdapter extends ArrayAdapter<BookEntry> {

	private final Context context;
	private final ArrayList<BookEntry> booksList;
	
	public BookListAdapter(Context context,	ArrayList<BookEntry> books) {
		super(context, R.layout.book_entry_line, books);
		this.context = context;
		this.booksList = books;
	}
	
	public void setTransferredToDevice(int pos, boolean set){
		this.booksList.get(pos).setTransferred(set);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// 2. Get rowView from inflater
		View rowView = inflater.inflate(R.layout.book_entry_line, parent, false);
		
		// 3. Get icon, author & title views from rowView
		ImageView transferredView = (ImageView) rowView.findViewById(R.id.transferred_book_icon);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.book_icon);
		TextView authorView = (TextView) rowView.findViewById(R.id.textBookAuthor);
		TextView titleView = (TextView) rowView.findViewById(R.id.textBookName);
		TextView annotationView = (TextView) rowView.findViewById(R.id.annotationText);
		// Set the text for textView
		if(booksList.get(position).getTransferred()){
			transferredView.setImageResource(R.drawable.btn_check_on);
		}else{
			transferredView.setImageResource(R.drawable.btn_check_off);
		}
		if(booksList.get(position).getPathToImage().equals("")){
			imageView.setImageResource(R.drawable.book);
		}else{
			Bitmap bitmap = BitmapFactory.decodeFile(booksList.get(position).getPathToImage());
			imageView.setImageBitmap(bitmap);
		}
		authorView.setText(booksList.get(position).getAuthor());
		titleView.setText(booksList.get(position).getTitle());
		annotationView.setText(booksList.get(position).getAnnotation());
		
		return rowView;
	}	
}
