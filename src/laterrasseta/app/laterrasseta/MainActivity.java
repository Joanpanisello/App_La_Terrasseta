package laterrasseta.app.laterrasseta;

import elmeu.paquet.practica_15_actionbarcompat.R;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	private String[] opcionesMenu;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private CharSequence tituloSeccion; 
    private ActionBarDrawerToggle drawerToggle;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 getSupportActionBar().setDisplayShowTitleEnabled(false);
	

		opcionesMenu = new String[] {"                  La Sala", "                  Ofertas", "                  Galería", "                  Eventos", "                  Vídeos", "                Contacto"};
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); 
        drawerList = (ListView) findViewById(R.id.left_drawer);
 
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        
        drawerList.setAdapter(new ArrayAdapter<String>(
                   getSupportActionBar().getThemedContext(),
                   android.R.layout.simple_list_item_1, opcionesMenu));
        
        drawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	getSupportActionBar().setDisplayShowTitleEnabled(true);
                Fragment fragment = null;
     
                switch (position) {
                    case 0:
                        fragment = new Activity_LaSala();
                        break;
                    case 1:
                        fragment = new Activity_Ofertas();
                        break;
                    case 2:
                        fragment = new Activity_Galeria();
                        break;
                    case 3:
                        fragment = new Activity_Eventos();
                        break; 
                    case 4:
                    	fragment = new Activity_Videos();
                    	break;
                    case 5:
                    	fragment = new Activity_Contacto();
                        break;
                }
     
                FragmentManager fragmentManager =
                    getSupportFragmentManager();
     
                fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
     
                drawerList.setItemChecked(position+1, true);
                
               
                tituloSeccion = opcionesMenu[position];
                getSupportActionBar().setTitle(tituloSeccion);
     
                drawerLayout.closeDrawer(drawerList);
            }
        });
        
        
        
        drawerToggle = new ActionBarDrawerToggle(this,
            drawerLayout,
            R.drawable.ic_drawer,
            R.string.drawer_open,
            R.string.drawer_close) {
     
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(tituloSeccion);
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }
     
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("");
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }
        };
     
        drawerLayout.setDrawerListener(drawerToggle);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	 
	    boolean menuAbierto = drawerLayout.isDrawerOpen(drawerList);
	 
	    if(menuAbierto)
	        menu.findItem(R.id.action_search).setVisible(false);
	    else
	        menu.findItem(R.id.action_search).setVisible(false);
	    return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		switch(item.getItemId())
	    {
	        case R.id.action_settings:
	            break;
	        case R.id.action_search:
	            break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	 
	    return true;
	}
	
	class Adaptador extends ArrayAdapter<String> {
		 
		 Activity context;
		 String[] arrayLlista;
		
		 
		 Adaptador(Activity context, String[] dades) {
			    super(context, R.layout.listitem, opcionesMenu);
	            this.context = context;
	            this.arrayLlista=dades;
	            
	     }
		 
		 public View getView(int position, View convertView, ViewGroup parent) {
		        LayoutInflater inflater = context.getLayoutInflater();
		        View item = inflater.inflate(R.layout.listitem, null);
		        
		        Typeface font = Typeface.createFromAsset(getAssets(), "sig.ttf");
		 
		        TextView lblTitulo = (TextView)item.findViewById(R.id.item);
		        lblTitulo.setTextColor(Color.WHITE);
		        lblTitulo.setTypeface(font);
		        lblTitulo.setText("\n"+" "+arrayLlista[position]);
		 
		        return(item);
		    }
	 }	 
}