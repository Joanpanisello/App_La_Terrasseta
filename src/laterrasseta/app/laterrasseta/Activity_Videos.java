package laterrasseta.app.laterrasseta;

import httprequest.JSONParser;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import elmeu.paquet.practica_15_actionbarcompat.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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

public class Activity_Videos extends Fragment {

	
	 private ListView llista;
	 
	 private static String url = "http://www.laterrasseta.com/json/index.php/jsoncontrol/videos/";
		
	 private static final String TAG_JSON = "json";
	 private static final String TAG_TITOL = "titol";
	 private static final String TAG_URL = "url";
	 
	 private JSONArray videos = null;
	 private String[] array;
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
		    View V = inflater.inflate(R.layout.activity_videos, container, false);
		
		    return V;
	     }

		 @Override
		 public void onActivityCreated(Bundle savedInstanceState) {
		        super.onActivityCreated(savedInstanceState);
		        
		     llista = (ListView)getView().findViewById(R.id.listViewVideo);
		     
		     JSONParse json = (JSONParse) new JSONParse().execute(url);
		     
		     try {
				array=json.get();
			} catch (InterruptedException e) {} catch (ExecutionException e) {}
		     
		 }

		 private class JSONParse extends AsyncTask<String, String, String[]> {
			 
		       private ProgressDialog pDialog;
		       private String[] arrayLlista;
		       private ArrayList<String> titols = new ArrayList<String>();
			   private ArrayList<String> urls = new ArrayList<String>();
		       
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
		        protected String[] doInBackground(String... args) {
		        	
			        JSONParser jParser = new JSONParser();
			        JSONObject json =(JSONObject) jParser.getJSONFromUrl(args[0]);
			        
			         try{
				            videos = json.getJSONArray(TAG_JSON);
				            JSONObject c=null;
				            for(int i = 0; i < videos.length(); i++){
				            	
				            c = videos.getJSONObject(i);
				         
				            String titol = c.getString(TAG_TITOL);
				            String urlok = c.getString(TAG_URL);
				           
				       
				            titols.add(titol);
				            urls.add(urlok);
				           
				            }
				            
				             arrayLlista = new String[titols.size()];
							
							String titolOk="";
							
							for (int i =0; i < titols.size(); i ++){
								titolOk=titols.get(i);
								arrayLlista[i]="\n"+titolOk+"\n";
							}
							
							return arrayLlista;
			        
				        } catch (JSONException e) {
					          return null;
				        }
		       }
		        
	           @Override
	           protected void onPostExecute(String[] array) {
		       
					
	        	    Adaptador adaptador = new Adaptador(getActivity(), array);
					
					pDialog.dismiss();
					
					llista.setAdapter(adaptador);
					
				    llista.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

							Uri uri = Uri.parse(urls.get(position));
							Intent i = new Intent(Intent.ACTION_VIEW, uri);
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
