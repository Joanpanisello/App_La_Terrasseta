package laterrasseta.app.laterrasseta;

import httprequest.HttpRequest;
import httprequest.JSONParser;
import httprequest.HttpRequest.HttpRequestException;
import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import elmeu.paquet.practica_15_actionbarcompat.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class Activity_Detall_Event extends Activity {
	
	private TextView textTitol, textContingut, textData, textLloc, textDireccio;
	private ImageView imatgeEvent;
	
	private JSONArray imatge = null;
	private JSONArray direccio = null;
	private JSONArray lloc = null;
	
	private static final String TAG_JSON = "json";
	private static final String TAG_URL = "url";
	
	private Bundle bundle; 
	private int id; 
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detall_event);
		
		textTitol = (TextView)findViewById(R.id.texttitol);
		textContingut = (TextView)findViewById(R.id.textcontingut);
		textData = (TextView)findViewById(R.id.textdata);
		textLloc = (TextView)findViewById(R.id.textlloc);
		textDireccio = (TextView)findViewById(R.id.textdireccio);
		imatgeEvent = (ImageView)findViewById(R.id.imageevento);
		
		bundle = this.getIntent().getExtras();
		id = bundle.getInt("id");                            //recuperem el ID del event per tal del poder recuperar la seva informació
		
		Typeface font = Typeface.createFromAsset(getAssets(), "sig.ttf");
		
		textTitol.setTypeface(font);
		textContingut.setTypeface(font);
		textData.setTypeface(font);
		textDireccio.setTypeface(font);
		textLloc.setTypeface(font);
		
		textTitol.setText("");
		textContingut.setText("");
		textData.setText("");
		textDireccio.setText("");
		textLloc.setText("");
		
		String titol = bundle.getString("titol");                   //La imatge de l'event amb l'event només es relaciona a la BD per el seu titol
		String delimitador= " ";
		String[] separades = titol.split(delimitador);
		String url = "http://www.laterrasseta.com/json/index.php/jsoncontrol/fotoevento/"+separades[0];   //
		
		DownloadTask baixa = (DownloadTask) new DownloadTask().execute(url);
		
	}	
	
	public class DownloadTask extends AsyncTask<String, Long, File> {
		 
		private ProgressDialog pDialog;
		private String direccioOk="", llocOk="";
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(Activity_Detall_Event.this);
            pDialog.setMessage("Cargando contenido ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected File doInBackground(String... urls) {
		
			String urlImage="";
			String urlDireccio ="http://www.laterrasseta.com/json/index.php/jsoncontrol/direccionevento/";
			String urlLloc ="http://www.laterrasseta.com/json/index.php/jsoncontrol/lugarevento/";
			
			JSONParser jParser = new JSONParser();
			
	        JSONObject json =(JSONObject) jParser.getJSONFromUrl(urls[0]);            //Recuperem dades de l'event com el lloc, direcció i imatge
	        JSONObject json2 =(JSONObject) jParser.getJSONFromUrl(urlDireccio+id);
	        JSONObject json3 =(JSONObject) jParser.getJSONFromUrl(urlLloc+id);
	        
	        try{
	        
	        	direccio = json2.getJSONArray("json");
	        	JSONObject t = null;
	        	
	        	t = direccio.getJSONObject(0);
	        	direccioOk = t.getString("direccion");
	        	
	        	lloc = json3.getJSONArray("json");
	        	JSONObject m = null;
	        	
	        	m = lloc.getJSONObject(0);
	        	llocOk = m.getString("lugar");
	        	
	        	
			}catch (JSONException e) {
		          e.printStackTrace();
		    }
		
	        try {
	        	imatge = json.getJSONArray(TAG_JSON);
	        	JSONObject c=null;
	            
	            c = imatge.getJSONObject(0);
	            urlImage = c.getString(TAG_URL);
	            	
	        }catch (JSONException e) {
		          e.printStackTrace();
		    }
			
			try {
		      HttpRequest request =  HttpRequest.get(urlImage);   //Descarreguem la imatge
		      File file = null;
		      if (request.ok()) {
		        try {
					file = File.createTempFile("download", ".tmp"); 
				} catch (IOException e) {
				
					e.printStackTrace();
				}
		        request.receive(file);
		        publishProgress(file.length());
		      }
		      return file;
		    } catch (HttpRequestException exception) {
		      return null;
		    }
		  }

		  protected void onPostExecute(File file) {
			
		        pDialog.dismiss();	  
			    
				if(file!=null){  
				    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());	      //Assignem la imatge a un objecte Bitmap i la pintem al ImageView
				    imatgeEvent.setImageBitmap(myBitmap);
				}
				
				String titol = bundle.getString("titol");
				String contingut = bundle.getString("contingut");
				String fecha = bundle.getString("fecha");
				
				textTitol.setTextColor(Color.parseColor("#00FFFF"));
				textContingut.setTextColor(Color.WHITE);
				textDireccio.setTextColor(Color.parseColor("#DF7401"));
				textLloc.setTextColor(Color.parseColor("#DF7401"));
				textData.setTextColor(Color.parseColor("#DF7401"));
				
				textDireccio.setText(direccioOk);
				textLloc.setText(llocOk);
				textTitol.setText(titol);
				textContingut.setText(contingut.subSequence(0, contingut.indexOf("<iframe")));
				
				String dia = "";
		        String mes = "";
		        String any = "";
		        String dataNo = ""; 
		        
		        dataNo = fecha;
		        dia = dataNo.substring(8, dataNo.length());
		        mes = dataNo.substring(5, 7);
		        any = dataNo.substring(0, 4);
				
				textData.setText(dia+"/"+mes+"/"+any);	      //Cambiem el format de la data 
		  }	  
	}
}
