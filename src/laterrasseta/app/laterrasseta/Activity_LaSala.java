package laterrasseta.app.laterrasseta;

import java.util.ArrayList;

import httprequest.HttpRequest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import classes.La_sala_contingut;

import com.google.gson.Gson;


import elmeu.paquet.practica_15_actionbarcompat.R;

public class Activity_LaSala extends Fragment {
	
	 private TextView titol1, titol2, titol3, contingut1, contingut2, contingut3, contingut4;
	 private ImageView imatge1, imatge2, imatge3, imatgelogo;
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		   
		 View V = inflater.inflate(R.layout.activity_la_sala, container, false);
         
		 return V;
     }
	 
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
	        
	        titol1 = (TextView)getView().findViewById(R.id.titol1);
	        titol2 = (TextView)getView().findViewById(R.id.titol2);
	        titol3 = (TextView)getView().findViewById(R.id.titol3);
	        contingut1 = (TextView)getView().findViewById(R.id.content1);
	        contingut2 = (TextView)getView().findViewById(R.id.content2);
	        contingut3 = (TextView)getView().findViewById(R.id.content3);
	        contingut4 = (TextView)getView().findViewById(R.id.content4);
	        imatge1 = (ImageView)getView().findViewById(R.id.imageView1);
	        imatge2 = (ImageView)getView().findViewById(R.id.imageView2);
	        imatge3 = (ImageView)getView().findViewById(R.id.imageView3);
	        imatgelogo = (ImageView)getView().findViewById(R.id.imageevent);
	        
	        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "sig.ttf");
	        
	        titol1.setTypeface(font);
	        titol2.setTypeface(font);
	        titol3.setTypeface(font);
	        contingut1.setTypeface(font);
	        contingut2.setTypeface(font);
	        contingut3.setTypeface(font);
	        contingut4.setTypeface(font);
	  
	        titol1.setTextSize(22);
	        titol2.setTextSize(22);
	        titol3.setTextSize(22);
	        
	        titol1.setText("");
	        titol2.setText("");
	        titol3.setText("");
	        contingut1.setText(""); 
	        contingut2.setText("");
	        contingut3.setText("");
	        contingut4.setText("");
	        
	        DownloadTask baixa = (DownloadTask) new DownloadTask().execute("http://www.laterrasseta.com/la-sala/?json=1");
	        
	        
	  }  
	 
	 public class DownloadTask extends AsyncTask<String, Long, String> {
		 
		 private ProgressDialog pDialog;
		 
		 @Override
	     protected void onPreExecute() {
	            super.onPreExecute();
	            
	            pDialog = new ProgressDialog(getActivity());
	            pDialog.setMessage("Cargando contenido ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	     } 
		 
		 @Override
		 protected String doInBackground(String... urls) {

			    	String string = null;
			    	string =  HttpRequest.get(urls[0]).body();

			    	return string;
			  }

			@Override
			protected void onPostExecute(String string) {
				
				  String jsonOk = string.substring(22);
				  jsonOk = jsonOk.substring(0, jsonOk.length()-1); 
				
				  Gson gson = new Gson();
				  
				  La_sala_contingut laSala =  gson.fromJson(jsonOk, La_sala_contingut.class);
				  
				  String contingutHtml = laSala.getContingut();
				  String contingutHtml2 = laSala.getContingut();
				  
				  String aBuscar = contingutHtml;
				  String aBuscar2 = contingutHtml2;
				  
				  String conta = "<p>";
				  String conta2 = "<h3";
 				  
				  int contador = -1;
				  int contador2 = -1;
				  
				  while(aBuscar.indexOf(conta)> -1){
					  aBuscar = aBuscar.substring(aBuscar.indexOf(conta)+conta.length(), aBuscar.length());
					  contador++;
				  }
				  
				  while(aBuscar2.indexOf(conta2)> -1){
					  aBuscar2 = aBuscar2.substring(aBuscar2.indexOf(conta2)+conta2.length(), aBuscar2.length());
					  contador2++;
				  }
				  
			      ArrayList<String> titols = new ArrayList<String>();
				  ArrayList<String> contingut = new ArrayList<String>();
				  
				  int inici = contingutHtml.indexOf("<p>");
				  int fi = contingutHtml.indexOf("</p>");
				  
				  int inici2 = contingutHtml2.indexOf("<h3");
				  int fi2 = contingutHtml2.indexOf("</h3>");
				  
				  String paragraf = contingutHtml.substring(inici+3, fi);
				  paragraf = paragraf.replace("<br />", "");
				  
				  String paragraf2 = contingutHtml2.substring(inici2+27, fi2);
				 			  
				  contingut.add(paragraf);
				  titols.add(paragraf2);
				  
				  while(contador2!=0){
					  
					  inici2 = contingutHtml2.indexOf("<h3", inici2+1);
					  fi2 = contingutHtml2.indexOf("</h3>", fi2+1);
					 
					  if(inici2==0){
						  break;
					  }else{
						  paragraf2 = contingutHtml2.substring(inici2+27, fi2);
						  titols.add(paragraf2);
					  }
					  contador2--;
				  }
				  
				  while(contador!=0){
					  
					  inici = contingutHtml.indexOf("<p>", inici+1);
					  fi = contingutHtml.indexOf("</p>", fi+1);
					 
					  if(inici==0){
						  break;
					  }else{
						  paragraf = contingutHtml.substring(inici+3, fi);
						  if(paragraf.contains("</br>")){
							  
						  }else{
							  paragraf = paragraf.replace("<br />", "");
							  paragraf = paragraf.replace("&#8217;", "'");
							  paragraf = paragraf.replace("&#8230;", "...");
							  contingut.add(paragraf);
						  }
					  }
					  contador--;
				  }
				  
				  
				  pDialog.dismiss();
				  
				  titol1.setTextColor(Color.parseColor("#00FFFF"));
				  titol2.setTextColor(Color.parseColor("#00FFFF"));
				  titol3.setTextColor(Color.parseColor("#00FFFF"));
				  
				  contingut1.setTextColor(Color.WHITE);
				  contingut2.setTextColor(Color.WHITE);
				  contingut3.setTextColor(Color.WHITE);
				  contingut4.setTextColor(Color.WHITE);
				  
				  
				  titol1.setText(titols.get(0)); 
				  titol2.setText(titols.get(1));
				  titol3.setText(titols.get(2));
				  
				  contingut1.setText(contingut.get(0));
				  contingut2.setText(contingut.get(1));
				  contingut3.setText(contingut.get(2));
				  contingut4.setText(contingut.get(3));	
				  
				  imatge1.setImageResource(R.drawable.chillout);
			      imatge2.setImageResource(R.drawable.pis);
			      imatge3.setImageResource(R.drawable.otrosok);
			      imatgelogo.setImageResource(R.drawable.logoterrasseta);
			  }
		}
}