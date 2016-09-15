package rohitnahata.mailingsystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import rohitnahata.mailingsystem.Models.StudentDetails;

public class SplashScreen extends AppCompatActivity {

    private ArrayList<StudentDetails> studentDetailsList;
    private ArrayList<String> className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        className = ((App) this.getApplication()).getClassName();
        studentDetailsList = ((App) this.getApplication()).getStudentDetailsList();
        new PrefetchData().execute(App.BASE_URL);
    }

    private class PrefetchData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            new Firebase(strings[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    deleteData();
                    for (DataSnapshot alert : dataSnapshot.getChildren()) {
                        String strClass = alert.getKey();
                        className.add(alert.getKey());
                        for (DataSnapshot recipient : alert.getChildren()) {
                            String strEmail = (String) recipient.child("email_id").getValue();
                            String strName = (String) recipient.child("student_name").getValue();
                            String strUID = (String) recipient.child("id").getValue();
                            addData(strUID, strName, strEmail, strClass);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            ((App) getApplication()).setStudentDetailsList(studentDetailsList);
            ((App) getApplication()).setClassName(className);


//
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);

        }


        public void deleteData() {
            if (studentDetailsList != null) {
                studentDetailsList.clear();
            }
            if (className != null) {
                className.clear();
            }
        }

        public void addData(String n1, String n2, String n3, String n4) {
            StudentDetails studentDetails = new StudentDetails(n1, n2, n3, n4);
            studentDetailsList.add(studentDetails);
        }


    }
}


