package laterrasseta.app.laterrasseta;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import elmeu.paquet.practica_15_actionbarcompat.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class Activity_Mapa extends FragmentActivity {

	private GoogleMap mapa = null;
	private int vista = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);
		
		mapa = ((SupportMapFragment) getSupportFragmentManager()
				   .findFragmentById(R.id.map)).getMap();

		CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(40.767530, 0.650284)).zoom(15).build();

		mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
		mapa.addMarker(new MarkerOptions().position(new LatLng(40.767530, 0.650284)).title("La Terrasseta"));

	}
}