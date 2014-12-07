package com.javaproject.mathgeniuses;

import java.util.List;

import com.javaproject.mathgeniuses.AbstractExerciseFragment.ExerciseEvents;
import com.javaproject.mathgeniuses.database.MathGeniusesDbAdapter;
import com.javaproject.mathgeniuses.entities.ExerciseObject;
import com.javaproject.mathgeniuses.entities.LessonObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

/**
 * This class loads all the exercises for the lesson and then, loads a fragment per exercise.
 * @author MariaLuisa
 *
 */
public class PlayExercisesActivity extends Activity implements ExerciseEvents {
	
	public static final String KEY_LESSON_ID = "keyLessonId";
	public static final int TOTAL_NUMBER_OF_EXERCISES = 10;

	private long mLessonId;
	private List<ExerciseObject> mExerciseObjectsList;
	private int mCurrentExercise;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_exercises);
		
		// Get intent data
		Intent intent = getIntent();
		mLessonId = intent.getLongExtra(KEY_LESSON_ID, -1);
		
		mCurrentExercise = 0;
		mExerciseObjectsList = getExercises(mLessonId);	
		
		if (savedInstanceState == null) {
			
			AbstractExerciseFragment exerciseFragment = getNewExerciseFragment();
			
			getFragmentManager().beginTransaction()
					.add(R.id.container, exerciseFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_exercises, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private List<ExerciseObject> getExercises(long lessonId) {
		MathGeniusesDbAdapter mathAdapter = new MathGeniusesDbAdapter(this);
		mathAdapter.open();
		
		List<ExerciseObject> exerciseObjectsList;
		exerciseObjectsList = mathAdapter.fetchExercises(lessonId);
		mathAdapter.close();
		
		return exerciseObjectsList;
	}

	@Override
	public void onExerciseEnd() {
		if (!exercisesCompleted()) {
			mCurrentExercise++;
			
			AbstractExerciseFragment exerciseFragment = getNewExerciseFragment();

	        FragmentTransaction transaction = getFragmentManager().beginTransaction();
	        transaction.replace(R.id.container, exerciseFragment);
	        // Add the transaction to the back stack so the user can navigate back
	        //transaction.addToBackStack(null);

	        transaction.commit();
		}
	}
	
	private AbstractExerciseFragment getNewExerciseFragment() {
		AbstractExerciseFragment fragmentToReturn;
		
		fragmentToReturn = new DragAndDropExerciseFragment();
		
		Bundle args = new Bundle();
        args.putString(AbstractExerciseFragment.KEY_EXERCISE, 
        		mExerciseObjectsList.get(mCurrentExercise).getExercise());
        fragmentToReturn.setArguments(args);
		
		return fragmentToReturn;
	}
	
	private boolean exercisesCompleted() {
		return (mCurrentExercise == TOTAL_NUMBER_OF_EXERCISES - 1);
	}

}