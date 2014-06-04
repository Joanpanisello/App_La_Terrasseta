package laterrasseta.app.laterrasseta;

import httprequest.JSONParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import laterrasseta.app.laterrasseta.Activity_Videos.Adaptador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import elmeu.paquet.practica_15_actionbarcompat.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class Activity_Galeria extends Fragment {
	
	private ListView llista;
	
	private static String url = "http://www.laterrasseta.com/json/index.php/jsoncontrol/album/";   //URL on es troba el JSON amb els albums (per recuperar un album has de inficar seguidament el seu ID)
	
	private static final String TAG_JSON = "json";
	private static final String TAG_TITOL = "titol";
	private static final String TAG_ID = "ID";
	
	private JSONArray albums = null;
	private String[] array;
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
	    View V = inflater.inflate(R.layout.activity_galeria, container, false);
	    
	    return V;
     }
	 
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
	        llista = (ListView)getView().findViewById(R.id.listView2);
	  
	        JSONParse json = (JSONParse) new JSONParse().execute(url);
	     
	        try {
				array=json.get();                                                //Necessitem tenir aquí l'array ple per tal de poder passar-li al adaptador
			} catch (InterruptedException e) {} catch (ExecutionException e) {}
	 }
	 
	 private class JSONParse extends AsyncTask<String, String, String[]> {
		 
	       private ProgressDialog pDialog;
	       private String[] arrayLlista;
		   private ArrayList<String> album = new ArrayList<String>();
		   private ArrayList<String> id = new ArrayList<String>();
	       
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
			protected String[] doInBackground(String... params) {
				
				JSONParser jParser = new JSONParser();
		        JSONObject json =(JSONObject) jParser.getJSONFromUrl(url);
		       
		        try {
		        	
					 albums = json.getJSONArray(TAG_JSON);
					 JSONObject c=null;
		             for(int i = 0; i < albums.length(); i++){
		            	
		            	c = albums.getJSONObject(i);
		            	album.add(c.getString(TAG_TITOL));
		            	id.add(c.getString(TAG_ID));
		             }
					
		             arrayLlista = new String[album.size()];
					
					 String titolOk="";
					
					 for (int i =0; i < album.size(); i ++){
						titolOk=album.get(i);
						arrayLlista[i]="  "+titolOk+"\n";
					 }
					 
		         }catch (JSONException e) {
			          e.printStackTrace();
		         }

		        return arrayLlista;
			}
	        
			@Override
	        protected void onPostExecute(String[] json) {
					
					Adaptador adaptador = new Adaptador(getActivity(), array);
					
					pDialog.dismiss();
					
					llista.setAdapter(adaptador);
					
					llista.setOnItemClickListener(new OnItemClickListener(){

							@Override
							public void onItemClick(AdapterView<?> parent, View view,int position, long ide) {

								Intent i = new Intent(getActivity(), Activity_Album.class);
						    	
						    	Bundle b = new Bundle();
						    	
						    	b.putInt("id", Integer.parseInt(id.get(position))); 
								i.putExtras(b);
								startActivity(i);		
							}
					});
			}
	 } 
	 
	 class Adaptador extends ArrayAdapter<String> {
		 
		 Activity context;
		 String[] arrayLlista;
		
		 
		 Adaptador(Activity context, String[] dades) {
			    super(context, R.layout.listitem, array);
	            this.context = context;
	            this.arrayLlista=dades;
	            
	     }
		 
		 public View getView(int position, View convertView, ViewGroup parent) {
		        LayoutInflater inflater = context.getLayoutInflater();
		        View item = inflater.inflate(R.layout.listitem, null);
		        
		        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "sig.ttf");
		 
		        TextView lblTitulo = (TextView)item.findViewById(R.id.item);
		        lblTitulo.setTextColor(Color.WHITE);
		        lblTitulo.setTypeface(font);
		        lblTitulo.setText("\n"+" "+arrayLlista[position]);
		 
		        return(item);
		    }
	 }
}
