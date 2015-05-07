package in.data360.anand.alllanguagetranslator;

/**
 * Created by ANAND on 4/7/2015.
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SavedDisplay extends Fragment implements OnClickListener {

    List<FavModel> list = new ArrayList<FavModel>();
    DatabaseHelper db;
    TextView tv;
    EditText id;
    ImageButton delete;

    private LinearLayout ll;
    private FragmentActivity faActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        faActivity  = (FragmentActivity)    super.getActivity();
        ll    = (LinearLayout)    inflater.inflate(R.layout.saved_fragment, container, false);


        db = new DatabaseHelper(getActivity().getApplicationContext());
        tv = (TextView) ll.findViewById(R.id.tv);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        id = (EditText) ll.findViewById(R.id.editText);
        delete = (ImageButton) ll.findViewById(R.id.btnDel);
        //back = (Button) findViewById(R.id.btnBack);
        //back.setOnClickListener(this);
        delete.setOnClickListener(this);
        list = db.getAllList();
        print(list);



        return ll;
    }

    private void print(List<FavModel> list) {
        // TODO Auto-generated method stub
        String value = "";
        for(FavModel sm : list){
//            value = value +sm.id+". English : "+sm.input+"\n    Output : "+sm.output+"\n";
            value = value +sm.id+". "+sm.input+"\n"+"     "+sm.output+"\n\n";
        }
        tv.setText(value);

        //if no favourites found
        if (value.matches(""))
        {
            tv.setText(" ---- Saved list empty ! ----\n\n\n\n\n");
            id.setVisibility(EditText.INVISIBLE);
            delete.setVisibility(Button.INVISIBLE);
        }

    }

    @Override
    public void onClick(View v) {



        if(v == ll.findViewById(R.id.btnDel)){

            //TODO hide keyboard When press button
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            //delete code follows

            String student_id = id.getText().toString();
            if (student_id.matches(""))
            {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Invalid entry !", Toast.LENGTH_LONG).show();
            }
            else {
                tv.setText("");
                db.deleteEntry(Integer.parseInt(student_id));
                list = db.getAllList();
                print(list);
                id.setText("");
                Toast.makeText(getActivity().getApplicationContext(),
                        "Deleted !", Toast.LENGTH_LONG).show();
            }
        }
    }


}
