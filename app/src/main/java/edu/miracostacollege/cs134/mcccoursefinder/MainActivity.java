package edu.miracostacollege.cs134.mcccoursefinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import edu.miracostacollege.cs134.mcccoursefinder.model.Course;
import edu.miracostacollege.cs134.mcccoursefinder.model.DBHelper;
import edu.miracostacollege.cs134.mcccoursefinder.model.Instructor;
import edu.miracostacollege.cs134.mcccoursefinder.model.Offering;

public class MainActivity extends AppCompatActivity {

    private DBHelper db;
    private static final String TAG = "MCC Course Finder";

    private List<Instructor> allInstructorsList;
    private List<Course> allCoursesList;
    private List<Offering> allOfferingsList;
    private List<Offering> filteredOfferingsList;
    private OfferingListAdapter offeringsListAdapter;

    private EditText courseTitleEditText;
    private Spinner instructorSpinner;
    private ListView offeringsListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importCoursesFromCSV("courses.csv");
        db.importInstructorsFromCSV("instructors.csv");
        db.importOfferingsFromCSV("offerings.csv");

        allCoursesList = db.getAllCourses();
        allInstructorsList = db.getAllInstructors();
        allOfferingsList = db.getAllOfferings();
        filteredOfferingsList = new ArrayList<>(allOfferingsList);

        courseTitleEditText = findViewById(R.id.courseTitleEditText);
        courseTitleEditText.addTextChangedListener(courseTitleTextWatcher);

        // Wire up offeringsListView
        offeringsListView = findViewById(R.id.offeringsListView);
        offeringsListAdapter =
                new OfferingListAdapter(this, R.layout.offering_list_item, filteredOfferingsList);
        offeringsListView.setAdapter(offeringsListAdapter);

        //DONE (1): Construct instructorSpinnerAdapter using the method getInstructorNames()
        //DONE: to populate the spinner.
        instructorSpinner = findViewById(R.id.instructorSpinner);
        ArrayAdapter<String> instructorSpinnerAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getInstructorNames());
        instructorSpinner.setAdapter(instructorSpinnerAdapter);

        //DONE (5): Create an AdapterView.OnItemSelectedListener named instructorSpinnerListener and implement
        //DONE: the onItemSelected method to do the following:
        //DONE: If the selectedInstructorName != "[Select Instructor]", clear the offeringListAdapter,
        //DONE: then rebuild it with every Offering that has an instructor whose full name equals the one selected.
        // Create an event to handle when user clicks on item in the Spinner
        instructorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // If the position is 0 ("Selected Instructor"), call reset and return
                if (position == 0) {
                    reset(view);
                    return;
                }

                // Get the selected name
                String selectedName = String.valueOf(adapterView.getItemAtPosition(position));

                // Clear out the list adapter for Offerings
                offeringsListAdapter.clear();

                // Rebuild it with courses offered by the selected instructor
                for (int i = 0; i < allOfferingsList.size(); i++) {
                    if (allOfferingsList.get(i).getInstructor().getFullName().equalsIgnoreCase(selectedName)) {
                        offeringsListAdapter.add(allOfferingsList.get(i));
                    }
                }
                offeringsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //DONE (2): Create a method getInstructorNames that returns a String[] containing the entry
    //DONE: "[SELECT INSTRUCTOR]" at position 0, followed by all the full instructor names in the
    //DONE: allInstructorsList
    public String[] getInstructorNames() {
        String[] names = new String[allInstructorsList.size() + 1];
        names[0] = "[Select Instructor]";
        for (int i = 1; i < names.length; i++) {
            names[i] = allInstructorsList.get(i - 1).getFullName();
        }
        return names;
    }

    //DONE (3): Create a void method named reset that sets the test of the edit text back to an
    //DONE: empty string, sets the selection of the Spinner to 0 and clears out the offeringListAdapter,
    //DONE: then rebuild it with the allOfferingsList
    public void reset(View v) {
        courseTitleEditText.setText("");
        instructorSpinner.setSelection(0);
        offeringsListAdapter.clear();

        // rebuild it from allOfferingsList
        for (Offering o: allOfferingsList)
            offeringsListAdapter.add(o);
    }


    //DONE (4): Create a TextWatcher named courseTitleTextWatcher that will implement the onTextChanged method.
    //DONE: In this method, set the selection of the instructorSpinner to 0, then
    //DONE: Clear the offeringListAdapter
    //DONE: If the entry is an empty String "", the offeringListAdapter should addAll from the allOfferingsList
    //DONE: Else, the offeringListAdapter should add each Offering whose course title starts with the entry.
    public TextWatcher courseTitleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence text, int i, int i1, int i2) {
            String cleanText = text.toString().toLowerCase();
            if (!cleanText.isEmpty()) {
                offeringsListAdapter.clear();
                for (Offering o : allOfferingsList) {
                    Course c = o.getCourse();
                    if (c.getFullName().toLowerCase().contains(cleanText)) {
                        offeringsListAdapter.add(o);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
