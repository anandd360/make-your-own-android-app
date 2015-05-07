package in.data360.anand.alllanguagetranslator;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.detect.Detect;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class LTranslator extends Fragment implements OnInitListener {
    OnSavedClickedListener mCallback;

    // Container Activity must implement this interface
    public interface OnSavedClickedListener {
        public void onSaved(int position);
    }



    Language[] languages = Language.values();
    private TextToSpeech tts;

    Spinner EnterLan, TransLan;
    ImageView speakEntered, speakTranslated;
    TextView languageEntered, textEntered, languageTranslated, textTranslated;
    EditText userText;
    ImageButton send;
    Button shFav;
    ImageButton shareBtn, copyBtn, adFavourites;
    //ProgressBar loading;
    DatabaseHelper db;
    private ProgressDialog progressDialog;

    private LinearLayout        ll;
    private FragmentActivity    faActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSavedClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSavedClickedListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        faActivity  = (FragmentActivity)    super.getActivity();
        ll    = (LinearLayout)    inflater.inflate(R.layout.trans_fragment, container, false);

        db = new DatabaseHelper(getActivity().getApplicationContext());
        tts = new TextToSpeech(super.getActivity(), this);

        initViews();
        //playSound();

        Locale loc = new Locale("en");
        Log.i("-------------", Arrays.toString(loc.getAvailableLocales()));

        return ll;
    }

    public void initViews(){
        //work with spinners
        EnterLan = (Spinner) ll.findViewById(R.id.sTobTranslated);
        TransLan = (Spinner) ll.findViewById(R.id.sTranslateto);

        EnterLan.setAdapter(new ArrayAdapter<String>(super.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, GetAllValues()));
        EnterLan.setSelection(9);

        TransLan.setAdapter(new ArrayAdapter<String>(super.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, GetAllValues()));
        TransLan.setSelection(17);


        send = (ImageButton) ll.findViewById(R.id.ivSend);

        speakEntered = (ImageView) ll.findViewById(R.id.ivSpeakEntered);
        speakTranslated = (ImageView) ll.findViewById(R.id.ivSpeakTranslated);
        shareBtn = (ImageButton) ll.findViewById(R.id.ShareBtn);
        copyBtn = (ImageButton) ll.findViewById(R.id.CopyBtn);
        languageEntered = (TextView) ll.findViewById(R.id.tvLanguageEntered);
        textEntered = (TextView) ll.findViewById(R.id.tvTextEntered);
        languageTranslated = (TextView) ll.findViewById(R.id.tvLanguageTranslated);
        textTranslated = (TextView) ll.findViewById(R.id.tvTextTranslated);
        adFavourites = (ImageButton) ll.findViewById(R.id.SaveBtn);
        shFav = (Button) ll.findViewById(R.id.ViewFav);
        userText = (EditText) ll.findViewById(R.id.etEnteredText);

        speakEntered.setVisibility(ImageView.INVISIBLE);
        speakTranslated.setVisibility(ImageView.INVISIBLE);
        shareBtn.setVisibility(ImageButton.INVISIBLE);
        copyBtn.setVisibility(ImageButton.INVISIBLE);
        languageEntered.setVisibility(TextView.INVISIBLE);
        languageTranslated.setVisibility(TextView.INVISIBLE);
        textEntered.setVisibility(TextView.INVISIBLE);
        textTranslated.setVisibility(TextView.INVISIBLE);
        adFavourites.setVisibility(ImageButton.INVISIBLE);

        //loading = (ProgressBar) findViewById(R.id.pbLoading);
        //loading.setVisibility(ProgressBar.INVISIBLE);
        ((View) ll.findViewById(R.id.view1)).setVisibility(View.INVISIBLE);
        ((View) ll.findViewById(R.id.view2)).setVisibility(View.INVISIBLE);

        speakEntered.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SpeakText(textEntered.getText().toString());
            }
        });

        speakTranslated.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SpeakText(textTranslated.getText().toString());
            }
        });

        adFavourites.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                FavModel trans = new FavModel();
                trans.input = EnterLan.getSelectedItem().toString()+" : "+ textEntered.getText().toString();
                trans.output = TransLan.getSelectedItem().toString()+" : " + textTranslated.getText().toString();
                db.addDetail(trans);



                Toast.makeText(getActivity().getApplicationContext(),
                        "Saved !", Toast.LENGTH_LONG).show();

                //for notification


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setContentTitle("Language Translator");
                mBuilder.setContentText("Click to view saved translations");
                mBuilder.setTicker("Translation saved !");
                Intent resultIntent = new Intent(getActivity(), ShowActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
                stackBuilder.addParentStack(ShowActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//to cancel notification on click
                mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
// notificationID allows you to update the notification later on.
                mNotificationManager.notify(0, mBuilder.build());


                //refresh other fragment for two page ui

                int position = 1;
               mCallback.onSaved(position);


            }

        });

        shFav.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Log.i("clicks", "You Clicked B1");
                Intent i = new Intent(
                        LTranslator.super.getActivity(),
                        ShowActivity.class);
                startActivity(i);

            }
        });

        shareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                String shText = textTranslated.getText().toString();
                //i.putExtra(android.content.Intent.EXTRA_SUBJECT,shText);
                i.putExtra(android.content.Intent.EXTRA_TEXT, shText);
                startActivity(Intent.createChooser(i, "Share via"));


            }
        });


        copyBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (textTranslated.getText() != null) {
                    String copyTxt = textTranslated.getText().toString();
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(copyTxt);
                    } else {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("label",copyTxt);
                        clipboard.setPrimaryClip(clip);
                    }

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Text Copied.", Toast.LENGTH_LONG).show();


                }
            }
        });




        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               String inText = userText.getText().toString();
                if (inText.matches("")) {

                    //TODO hide keyboard When press button
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Oops..Please enter text..", Toast.LENGTH_LONG).show();
                }
                else
                {



                // TODO Auto-generated method stub
                class bgStuff extends AsyncTask<Void, Void, Void> {


                    String translatedText = "";

                    @Override
                    protected void onPreExecute() {

                        // TODO Auto-generated method stub
                        //loading.setVisibility(ProgressBar.VISIBLE);

                        //showing pls wait
                        progressDialog = ProgressDialog.show(LTranslator.super.getActivity(), "Translating", "Please Wait..");


                        super.onPreExecute();




                        //TODO hide keyboard When press button
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    }

                    @Override
                    protected Void doInBackground(Void... params) {


                        // TODO Auto-generated method stub
                        try {
                            translatedText = translateText();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            translatedText = "Unable to translate";
                        }

                        return null;


                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        // TODO Auto-generated method stub
                        speakEntered.setVisibility(ImageView.VISIBLE);
                        speakTranslated.setVisibility(ImageView.VISIBLE);

                        languageEntered.setVisibility(TextView.VISIBLE);
                        languageTranslated.setVisibility(TextView.VISIBLE);
                        textEntered.setVisibility(TextView.VISIBLE);
                        textTranslated.setVisibility(TextView.VISIBLE);
                        adFavourites.setVisibility(ImageButton.VISIBLE);
                        shareBtn.setVisibility(ImageButton.VISIBLE);
                        copyBtn.setVisibility(ImageButton.VISIBLE);

                        ((View) ll.findViewById(R.id.view1)).setVisibility(View.VISIBLE);
                        ((View) ll.findViewById(R.id.view2)).setVisibility(View.VISIBLE);

                        textEntered.setText(userText.getText().toString());
                        textTranslated.setText(translatedText);

                        languageEntered.setText(detectedLanguage);
                        languageTranslated.setText(languages[TransLan.getSelectedItemPosition()].name());
                       // loading.setVisibility(ProgressBar.INVISIBLE);
                        progressDialog.dismiss();

                        super.onPostExecute(result);
                    }

                }



                new bgStuff().execute();

            }
            }
        });


    }

    String detectedLanguage = "";

    public String translateText() throws Exception {


        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Translate.setClientId("dummy_Client_ID");//CHANGE THIS
        Translate.setClientSecret("dummy_secretcode"); //CHANGE THIS

        String translatedText = Translate.execute(userText.getText().toString(),languages[EnterLan.getSelectedItemPosition()], languages[TransLan.getSelectedItemPosition()]);

        Language detectedLanguage = Detect.execute(userText.getText().toString());
        this.detectedLanguage = detectedLanguage.getName(Language.ENGLISH);

        return translatedText;

    }
    public String[] GetAllValues(){
        String lan[] = new String[languages.length];
        for(int i = 0; i < languages.length; i++){
            lan[i] = languages[i].name();
        }
        return lan;
    }

    public void playSound(){
        MediaPlayer player = new MediaPlayer();
        try {
            player.setVolume(10, 10);
            player.setDataSource("http://api.microsofttranslator.com/V2/http.svc/Speak?appId=Bearer+http%253a%252f%252fschemas.xmlsoap.org%252fws%252f2005%252f05%252fidentity%252fclaims%252fnameidentifier%3dgilokimu%26http%253a%252f%252fschemas.microsoft.com%252faccesscontrolservice%252f2010%252f07%252fclaims%252fidentityprovider%3dhttps%253a%252f%252fdatamarket.accesscontrol.windows.net%252f%26Audience%3dhttp%253a%252f%252fapi.microsofttranslator.com%26ExpiresOn%3d1360142778%26Issuer%3dhttps%253a%252f%252fdatamarket.accesscontrol.windows.net%252f%26HMACSHA256%3dBzK2I18ZSFu0lkV88oCNZUDZzt9QwmVaaDLQKyhhpjs%253d&text=Did+you+enjoy+the+2011+Cricket+World+Cup%3f&language=en-in");
            player.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(super.getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(super.getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(super.getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(super.getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void SpeakText(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {

                //speakOut("Ich");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

}
