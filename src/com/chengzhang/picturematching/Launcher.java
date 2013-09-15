/**
 * 
 */
package com.chengzhang.picturematching;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author Jackie
 *
 */
public class Launcher extends Activity {
  private Button _startButton, _exitButton;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.launcher);
	  initComponent();
  }

private void initComponent() {
	_startButton = (Button)findViewById(R.id.Start);
	_exitButton = (Button)findViewById(R.id.Exit);
	_startButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Launcher.this, PictureMatching.class);
			startActivity(intent);
		}
	});
	
	_exitButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
			System.exit(RESULT_OK);
		}
	});
	
}
}
