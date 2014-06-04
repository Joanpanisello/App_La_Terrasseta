package laterrasseta.app.laterrasseta;

import httprequest.JSONParser;
import imageloader.ImageLoader;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import elmeu.paquet.practica_15_actionbarcompat.R;


public class Activity_Album extends Activity {
	
	private GridView listview;
	private ListViewAdapter adapter;
	private Bundle bundle;
	private String location = "http://www.laterrasseta.com/json/index.php/jsoncontrol/fotosalbum/";     //URL del JSON de les imatges d'un album 
	static final String TAG_JSON = "json";
	static final String TAG_URL = "url";        
	private String[] urlsOk;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_album);

	        
	       bundle = Activity_Album.this.getIntent().getExtras();
	       int id = bundle.getInt("id");                              //recuperem el id del album per tal de poder recuperar les seves fotos
	       location = location + id; 
	        
	       Download_JSON_Images a = (Download_JSON_Images) new Download_JSON_Images().execute(location);  
	        
	        try {
				urlsOk = a.get();
			} catch (InterruptedException e) {} catch (ExecutionException e) {}
	        

	        adapter = new ListViewAdapter(this);
			listview = (GridView) findViewById(R.id.listview);
			listview.setAdapter(adapter);
			
			listview.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {        //Al fer clik a una foto es passa a la seguent activitat l'array de fotos i
		        																					   //la posició de la foto per tal de poder anar a les seguents i les anteriors.
		        	Intent i = new Intent(Activity_Album.this, Activity_Detall_Foto.class);
		        	Bundle b = new Bundle();
		        	b.putString("url", urlsOk[position]);
		        	b.putStringArray("array", urlsOk);
		        	b.putInt("position", position);
		        	i.putExtras(b);
		        	startActivity(i);
		        }
			});
	  }      
	  

	  class Download_JSON_Images extends AsyncTask<String, Void, String[]> {     //Classe encarregada de descargar el JSON des de la Web
		  
		  private String urls[];
		  
		  @Override
		  protected void onPreExecute() {
				super.onPreExecute();
		  }
		  
	        @Override
	        protected String[] doInBackground(String... params) {
	        	
	            JSONParser jsonp=new JSONParser();
	            JSONObject json = jsonp.getJSONFromUrl(location);   //Recuperem el JSON de la url indicada

	            try {
	                JSONArray jarray;
	                jarray = json.getJSONArray(TAG_JSON);
	                urls = new String[jarray.length()];
	                
	                for (int i = 0; i < jarray.length(); i++) {
	                    JSONObject gridImages = jarray.getJSONObject(i);   
	                    urls[i] = gridImages.getString(TAG_URL);             //Afegim a cada posició de l'array la url d'una imatge
	                }

	            } catch (JSONException e) {}

	            return urls;
	        }

	  }
	  
	  class ListViewAdapter extends ArrayAdapter<String> {        //Adaptador on descarreguem les imatges i les assignem a la seva corresponent vista
		  
		    Activity context;
			int layoutResourceId;

			ListViewAdapter(Activity context) {
				super(context, R.layout.detall_gridview, urlsOk);
				this.context = context;
				layoutResourceId = R.layout.detall_gridview;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				
				NewsHolder holder = null;
				View row = convertView;

				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new NewsHolder();
				
				
				holder.img_view = (ImageView)row.findViewById(R.id.img_minfied);
				
				
				ImageLoader imgLoader = new ImageLoader(getApplicationContext());       //Classe encarregada de descarregar les imatges       
				int loader = R.drawable.loader;
				
				imgLoader.DisplayImage(urlsOk[position], loader, holder.img_view);      //Funció de la classe ImageLoader que descarrega la imatge i la mostra a la vista indicada.
				
				return row;
			}
		}

		static class NewsHolder {

			ImageView img_view;
		}
}
