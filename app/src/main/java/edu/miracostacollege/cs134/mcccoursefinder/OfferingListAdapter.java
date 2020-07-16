package edu.miracostacollege.cs134.mcccoursefinder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.miracostacollege.cs134.mcccoursefinder.model.Course;
import edu.miracostacollege.cs134.mcccoursefinder.model.Instructor;
import edu.miracostacollege.cs134.mcccoursefinder.model.Offering;

/**
 * Helper class to provide custom adapter for the <code>Offering</code> list.
 */
public class OfferingListAdapter extends ArrayAdapter<Offering> {

    private Context mContext;
    private List<Offering> mOfferingsList;
    private int mResourceId;

    /**
     * Creates a new <code>OfferingListAdapter</code> given a mContext, resource id and list of offerings.
     *
     * @param c The mContext for which the adapter is being used (typically an activity)
     * @param rId The resource id (typically the layout file name)
     * @param offerings The list of offerings to display
     */
    public OfferingListAdapter(Context c, int rId, List<Offering> offerings) {
        super(c, rId, offerings);
        mContext = c;
        mResourceId = rId;
        mOfferingsList = offerings;
    }

    /**
     * Gets the view associated with the layout.
     * @param pos The position of the Offering selected in the list.
     * @param convertView The converted view.
     * @param parent The parent - ArrayAdapter
     * @return The new view with all content set.
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        final Offering selectedOffering = mOfferingsList.get(pos);
        // DONE (1): Get a reference to the selectedCourse and the selectedInstructor
        final Course selectedCourse = selectedOffering.getCourse();
        final Instructor selectedInstructor = selectedOffering.getInstructor();

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout offeringListLinearLayout =
                view.findViewById(R.id.offeringListLinearLayout);

        TextView offeringListFullNameTextView =
                view.findViewById(R.id.offeringListFullNameTextView);
        TextView offeringListTitleTextView =
                view.findViewById(R.id.offeringListTitleTextView);
        TextView offeringListCRNTextView =
                view.findViewById(R.id.offeringListCRNTextView);

        // DONE (2): Set the text of each of the 3 views accordingly.
        offeringListFullNameTextView.setText(selectedCourse.getFullName());
        offeringListTitleTextView.setText(selectedInstructor.getFullName());
        offeringListCRNTextView.setText(String.valueOf(selectedOffering.getCRN()));

        // Sets the tag, so when user clicks a specific offering, we can respond to that object specifically.
        offeringListLinearLayout.setTag(selectedOffering);
        return view;
    }
}
