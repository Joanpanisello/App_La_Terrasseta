package laterrasseta.app.laterrasseta;

import httprequest.JSONParser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import classes.Titular;
import elmeu.paquet.practica_15_actionbarcompat.R;


public class Activity_Eventos extends Fragment {

	private ListView llista;
	
	private Titular[] datos;
	
	private static String url = "http://www.laterrasseta.com/json/index.php/jsoncontrol/evento";   //URL que conté el JSON de esdeveniments 
	
	private static final String TAG_JSON = "json";
	private static final String TAG_TITOL = "titol";
	private static final String TAG_CONTINGUT = "contingut";
	private static final String TAG_FECHA = "fecha";
	private static final String TAG_ID = "id";
	
	
	private JSONArray eventos = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
	    View V = inflater.inflate(R.layout.activity_eventos, container, false);
	    
	    return V; 
     }
	 
	@Override
	 public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
	        llista = (ListView)getView().findViewById(R.id.llistaEvents);
	  
	     JSONParse json = (JSONParse) new JSONParse().execute(url);
	 }
	
	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		 
	       private ProgressDialog pDialog;
	       
		   private ArrayList<String> titolsProxims = new ArrayList<String>();     //Ja que a la WEB a pareixen tant els events següents com els passats he tingut que controlar-ho
		   private ArrayList<String> titols = new ArrayList<String>();            //per tal de nomes mostrar els següents.
		   
		   private ArrayList<Integer> id = new ArrayList<Integer>();
		   private ArrayList<Integer> idProxims = new ArrayList<Integer>();
		   
		   private ArrayList<String> contingut = new ArrayList<String>();
		   private ArrayList<String> contingutProxims = new ArrayList<String>();
		   
		   private ArrayList<String> dates = new ArrayList<String>();
		   private ArrayList<String> datesProxims = new ArrayList<String>();
	       
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
			protected JSONObject doInBackground(String... params) {
				
				JSONParser jParser = new JSONParser();
		        JSONObject json =(JSONObject) jParser.getJSONFromUrl(url);
		        
		    try{    
			        eventos = json.getJSONArray(TAG_JSON);
			        JSONObject c = null;
			
			        for(int i = 0; i < eventos.length(); i++){
		            	
		            	c = eventos.getJSONObject(i);
		            	titols.add(c.getString(TAG_TITOL));
		            	contingut.add(c.getString(TAG_CONTINGUT));
		            	id.add(Integer.parseInt(c.getString(TAG_ID)));
		            	dates.add(c.getString(TAG_FECHA).replace("/","-"));   //Per tal de poder comparar les dates he tingut que canviar les '/' per '-'
		            }
			        
			        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
			        Date now = new Date();
			        Date fechaEvento = null;
			        
			        for(int i = 0; i < dates.size(); i++){    //Recorrem tots els esdeveniments
			        	
			        	try {
							fechaEvento = sdfDate.parse(dates.get(i));   //passem la data al format adequat per tal de poder-la comparar amb la data d'avuí
						} catch (ParseException e) {}
			        	
			        	if(fechaEvento.compareTo(now)>0 | fechaEvento.compareTo(now)==0){      //Si la fecha de l'esdeveniment és després de la data d'avuí guardem la seva informació
			        		titolsProxims.add(titols.get(i));
			        		idProxims.add(id.get(i));
			        		contingutProxims.add(contingut.get(i));
			        		datesProxims.add(dates.get(i));
			        	}
			        }
		        
			    }catch (JSONException e) {}
		    
		    
		        datos = new Titular[titolsProxims.size()];

		        String dia = "";
		        String mes = "";
		        String any = "";
		        String dataNo = ""; 
				
				for(int i = 0; i<titolsProxims.size(); i++){         //Bucle per tal de posar totes les dates en el format correcte
					
					dataNo = datesProxims.get(i);
			        dia = dataNo.substring(8, dataNo.length());
			        mes = dataNo.substring(5, 7);
			        any = dataNo.substring(0, 4);
			         
					Titular e = new Titular("  "+titolsProxims.get(i), "   "+dia+"/"+mes+"/"+any);				
					datos[i]=e;
				}

		        return json; 
			}

			@Override
			protected void onPostExecute(JSONObject result) {
		
				AdaptadorTitulares adaptador = new AdaptadorTitulares(getActivity());
				
				pDialog.dismiss();
			 
				llista.setAdapter(adaptador);
		        
				llista.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long ide) {

						Intent i = new Intent(getActivity(), Activity_Detall_Event.class);
				    	
				    	Bundle b = new Bundle();   //Objecte per passar tota la informació necessària a la següent activitat 
				    	
				    	b.putInt("id", idProxims.get(position));      
				    	b.putString("titol", titolsProxims.get(position));
				    	b.putString("contingut", contingutProxims.get(position));
				    	b.putString("fecha", datesProxims.get(position));
						i.putExtras(b);
						startActivity(i);		//Iniciem la següent activitat
					}
				 });
			}	
	 } 
	
	class AdaptadorTitulares extends ArrayAdapter {     //Adaptador personalitzat per tal de poder afegir el titol i la data de l'event a cada element de la llista
		 
	    Activity context;
	 
			AdaptadorTitulares(Activity context) {
	            super(context, R.layout.listitem_titular, datos);
	            this.context = context;
	        }
	 
	        public View getView(int position, View convertView, ViewGroup parent) {
	        LayoutInflater inflater = context.getLayoutInflater();
	        View item = inflater.inflate(R.layout.listitem_titular, null);
	        
	        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "sig.ttf");
	 
	        TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
	        lblTitulo.setTextColor(Color.WHITE);
	        lblTitulo.setTypeface(font);
	        lblTitulo.setText("\n"+datos[position].getTitulo());  //Asignem el titol de l'esdeveniment
	 
	        TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
	        lblSubtitulo.setTextColor(Color.parseColor("#00FFFF"));
	        lblSubtitulo.setTypeface(font);
	        lblSubtitulo.setText(datos[position].getSubtitulo()+"\n");  //Assignem la data de l'esdeveniment
	        
	 
	        return(item);
	    }
	}
	
}
