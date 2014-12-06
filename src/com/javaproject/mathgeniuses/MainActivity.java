package com.javaproject.mathgeniuses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.plus.Plus;
import com.javaproject.mathgeniuses.database.MathGeniusesDbAdapter;
import com.javaproject.mathgeniuses.util.PlusBaseActivity;

public class MainActivity extends PlusBaseActivity
{

	public static final String KEY_FIRST_RUN = "firstRun";
	private static final String TAG = MainActivity.class.getSimpleName();

	// Variables for input/output controls
	private TextView mTxtWelcome;
	private String mUsername;
	private Button mAdd, mSubtract, mDivide, mMultiply, mAll;

	// Variables for data
	MathGeniusesDbAdapter mDb;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		 
		// Open database
		mDb = new MathGeniusesDbAdapter(getApplicationContext());
		mDb.open();

		// Check if this is the first time the application runs
		SharedPreferences sharedPreferences = getSharedPreferences(
				"com.example.mathgeniuses", MODE_PRIVATE);
		if (sharedPreferences.getBoolean(KEY_FIRST_RUN, true))
		{
			mDb.bulkInsertCatalogs();
			sharedPreferences.edit().putBoolean(KEY_FIRST_RUN, false).commit();
		}

		// Create references to input/output controls
		mTxtWelcome = ((TextView) findViewById(R.id.txtWelcome));
		
		//initialize the buttons and add the listeners
		initializeButtons();

		// Reconnect with the user's account to obtain the user's name
		// This does the callback to onPlusClientSignIn()
		signIn();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		} else if (id == R.id.plus_sign_out)
		{
			signOut();
		} else if (id == R.id.plus_disconnect)
		{
			revokeAccess();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPlusClientSignIn()
	{
		Log.d(TAG, "Entered onPlusClientSignIn");

		try
		{
			String givenName = Plus.PeopleApi
					.getCurrentPerson(getGoogleApiClient()).getName()
					.getGivenName();
			mUsername = givenName != null ? givenName : "";
			mTxtWelcome.setText(mUsername + ", " + getString(R.string.welcome));

			// adding listeners to the buttons

		} catch (Exception e)
		{
			Log.e(TAG, "Given name not found.\n" + e.getMessage());
		}

	}

	@Override
	protected void onPlusClientBlockingUI(boolean show)
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "Entered onPlusClientBlockingUI");
	}

	@Override
	protected void updateConnectButtonState()
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "Entered updateConnectButtonState");
	}

	@Override
	protected void onPlusClientRevokeAccess()
	{
		returnToLogin();
	}

	@Override
	protected void onPlusClientSignOut()
	{
		returnToLogin();
	}

	private void returnToLogin()
	{
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();
	}

	/**
	 * This method initializes all the operation buttons and store them in an
	 * arrayList of Button objects
	 */
	public void initializeButtons()
	{
	
		mAdd = (Button) findViewById(R.id.btnAdd);
		mSubtract = (Button) findViewById(R.id.btnSubtract);
		mMultiply = (Button) findViewById(R.id.btnMultiply);
		mDivide = (Button) findViewById(R.id.btnDivide);
		mAll = (Button) findViewById(R.id.btnAll);

		// add Listeners
		addListeners();

	}
	
	// This method adds the listener to all the buttons
	public void addListeners(){
				
		mAdd.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				mDb.fetchLessons(1);
				mDb.fetchLessonsWithScore(1);
				mDb.fetchOperations(); 	
				
				Intent intent =new Intent(MainActivity.this,LessonChoiceActivity.class);
				startActivity(intent);		 

			}

		});
		
		mSubtract.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				mDb.fetchLessons(2);
				// Go to the subtraction selection screen
			}
			
		});
		
		mMultiply.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				mDb.fetchLessons(3);
				// Go to the Multiplication selection screen
			}
			
		});
		
		mDivide.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				mDb.fetchLessons(4);
				// Go to the Division selection screen
			}
			
		});
		
		mAll.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				mDb.fetchLessons(5);
				// Go to the All selection screen
			}
			
		});
		
		
		
		
	}

}