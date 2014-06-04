package laterrasseta.app.laterrasseta;

import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import httprequest.JSONParser;
import httprequest.HttpRequest;
import httprequest.HttpRequest.HttpRequestException;
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

public class Activity_Detall_Oferta extends Activity{

	private TextView tv1, tv2;
	
	private JSONArray imatge = null;
	
	private static final String TAG_JSON = "json";
	private static final String TAG_URL = "url";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detall_oferta);
		
		tv1 = (TextView)findViewById(R.id.textView1111);
		tv2 = (TextView)findViewById(R.id.textView222);
		
		Bundle bundle = Activity_Detall_Oferta.this.getIntent().getExtras();
		String titol = bundle.getString("titol");                               //Recuperem el titol de l'oferta seleccionada a l'activitat anterior
		
		
		String delimitador= " ";
		String[] separades = titol.split(delimitador);
		String url = "http://www.laterrasseta.com/json/index.php/jsoncontrol/fotooferta/"+separades[0];     //La imatge de l'oferta amb l'oferta només es relaciona a la BD per el seu titol
		
		DownloadTask baixa = (DownloadTask) new DownloadTask().execute(url);
		
	}	
		
	public class DownloadTask extends AsyncTask<String, Long, File> {
		 
		private ProgressDialog pDialog;
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(Activity_Detall_Oferta.this);
            pDialog.setMessage("Cargando contenido ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected File doInBackground(String... urls) {
		
			String urlImage="";
			
			JSONParser jParser = new JSONParser();
	        JSONObject json =(JSONObject) jParser.getJSONFromUrl(urls[0]);
			
	        try {
	        	imatge = json.getJSONArray(TAG_JSON);
	        	JSONObject c=null;
	            
	            c = imatge.getJSONObject(0);
	            urlImage = c.getString(TAG_URL);      //asignem al String la URL de la imatge
	            	
	        }catch (JSONException e) {
		          e.printStackTrace();
		    }
			
			try {
		      HttpRequest request =  HttpRequest.get(urlImage);    //Descarreguem la imatge
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
			    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		
			    ImageView myImage = (ImageView) findViewById(R.id.imageevent);    //Assignem la imatge a un objecte Bitmap i la pintem al ImageView
			    myImage.setImageBitmap(myBitmap);
			}
			
			
			Bundle bundle = Activity_Detall_Oferta.this.getIntent().getExtras();   //Recuperem informació de l'oferta seleccionada a l'activitat anterior
			String titol = bundle.getString("titol");
			String contingut = bundle.getString("contingut");
			
			Typeface font = Typeface.createFromAsset(getAssets(), "sig.ttf");
			
			tv1.setTypeface(font);
			tv2.setTypeface(font);
			
			tv1.setTextColor(Color.parseColor("#DF7401"));
			tv2.setTextColor(Color.WHITE);
			
			tv1.setText("\n"+titol+"\n\n");
			tv2.setText(contingut);
		 }	  
	}
}
