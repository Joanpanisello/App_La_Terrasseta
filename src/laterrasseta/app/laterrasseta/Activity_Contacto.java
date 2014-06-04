package laterrasseta.app.laterrasseta;

import elmeu.paquet.practica_15_actionbarcompat.R;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class Activity_Contacto extends Fragment{


	 private TextView telefon01, telefon02, direccio01, direccio02, localitat01, localitat02, email01, email02;
     private Button boto;
	 private ImageView face, twit;
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
		    View V = inflater.inflate(R.layout.activity_contacto, container, false);
		    
		    return V;
	 }
	 
  	 @Override
 	 public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		telefon01 = (TextView) getView().findViewById(R.id.telefon01);
		telefon02 = (TextView) getView().findViewById(R.id.telefon02);
		direccio01 = (TextView) getView().findViewById(R.id.direccio01);
		direccio02 = (TextView) getView().findViewById(R.id.direccio02);
		localitat01 = (TextView) getView().findViewById(R.id.localitat01);
		localitat02 = (TextView) getView().findViewById(R.id.localitat02);
		email01 = (TextView) getView().findViewById(R.id.email01);
		email02 = (TextView) getView().findViewById(R.id.email02); 
		boto = (Button) getView().findViewById(R.id.button1);
		face = (ImageView)getView().findViewById(R.id.imageView150);
		twit = (ImageView)getView().findViewById(R.id.imageView250);
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "sig.ttf");
		
		telefon01.setTypeface(font);
		telefon02.setTypeface(font);
		direccio01.setTypeface(font);
		direccio02.setTypeface(font);
		localitat01.setTypeface(font);
		localitat02.setTypeface(font);
		email01.setTypeface(font);
		email02.setTypeface(font);
	
		telefon01.setTextColor(Color.parseColor("#00FFFF"));
		telefon02.setTextColor(Color.WHITE);
		direccio01.setTextColor(Color.parseColor("#00FFFF"));
		direccio02.setTextColor(Color.WHITE);
		localitat01.setTextColor(Color.parseColor("#00FFFF"));
		localitat02.setTextColor(Color.WHITE);
		email01.setTextColor(Color.parseColor("#00FFFF"));
		email02.setTextColor(Color.WHITE);
		boto.setTextColor(Color.WHITE);
		boto.setBackgroundColor(Color.GRAY);
		
		face.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Uri uri = Uri.parse("https://www.facebook.com/laterrassetapage");
				Intent i = new Intent(Intent.ACTION_VIEW, uri);                        //Intents implicits encarregats de obrir el navegador amb la ruta desitjada
				startActivity(i);
				
			}
		});
		
		twit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Uri uri = Uri.parse("https://twitter.com/Laterrassetacam");
				Intent i = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(i);
				
			}
		});
		
		boto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {


				Intent i = new Intent(getActivity(), Activity_Mapa.class);            // Intent per obrir l'activitat on es mostra el mapa.
				
				startActivity(i);
				
			}
		});
	 }
}
