package laterrasseta.app.laterrasseta;

import httprequest.HttpRequest;
import httprequest.HttpRequest.HttpRequestException;
import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import elmeu.paquet.practica_15_actionbarcompat.R;

public class Activity_Detall_Foto extends Activity {

	private Bundle bundle;
	private String url;
	private Integer position;
	private String[] album;
	private ImageView foto, dreta, esquerra;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detall_foto);
	
		bundle = this.getIntent().getExtras();      //Obtenim les dades necessaries de l'activitat anterior per tal de poder descarregar la imatge i accedir a les seguents i anteriors.
		url = bundle.getString("url");
		album = bundle.getStringArray("array");
		position = bundle.getInt("position");
		 
		foto = (ImageView)findViewById(R.id.imageView100);
		dreta = (ImageView)findViewById(R.id.imageViewdreta);
		esquerra = 	(ImageView)findViewById(R.id.imageViewesquerra);
		
		DownloadTask a = (DownloadTask) new DownloadTask().execute(url);
		
		dreta.setOnClickListener(new View.OnClickListener() {        //Listener del botó per passar a la següent imatge
			@Override
			public void onClick(View v) {

				if(!position.equals(album.length-1)){                                                 //Comprobem que no sigui la última imatge de l'album
					DownloadTask a = (DownloadTask) new DownloadTask().execute(album[position+1]);
					position++;
				}
				
			}
		});
		 
		esquerra.setOnClickListener(new View.OnClickListener() {     //Listener del botó per passar a la imatge anterior
			@Override
			public void onClick(View v) {

				if(!position.equals(0)){                                                              //Comprobem que no sigui la primera imatge de l'album
					DownloadTask a = (DownloadTask) new DownloadTask().execute(album[position-1]);
					position--;
				}
				
			}
		});
			
	}
	

	public class DownloadTask extends AsyncTask<String, Long, File> {         //Classe encarregada de descarregar les imatges en un altre fil d'execució
		 
		private ProgressDialog pDialog;
		private String direccioOk="", llocOk="";
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(Activity_Detall_Foto.this);
            pDialog.setMessage("Cargando imagen ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected File doInBackground(String... urls) {

			try {
		      HttpRequest request =  HttpRequest.get(urls[0]);   //Descarreguem la imatge
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
				    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());	     //La assignem a un objcte Bitmap i la assignem al ImageView.
				    foto.setImageBitmap(myBitmap);
				}	
		  }		
	}
}
