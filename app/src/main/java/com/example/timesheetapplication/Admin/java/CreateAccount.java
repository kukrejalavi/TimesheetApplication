package com.example.timesheetapplication.Admin.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.InternetConnection;
import com.example.timesheetapplication.R;
import com.example.timesheetapplication.SetDate;
import com.example.timesheetapplication.Validations;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CreateAccount extends Fragment {
EditText txtname,txtnumber,txtemailid,txtpassword,txtpasswordhint;
int EMpid;
Button createbtn,clearbtn;
AppCompatSpinner selectprofilespinner;
    View view;
    ImageView imagebtn;
    Uri filePath;
    StorageReference mstorageRef;
    String activityresult;
    private int GALLERY = 1, CAMERA = 2;
    public String empid,type;
    private StorageTask mUploadTask;
    private  ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private long fileSize = 0;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Add Employee";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.activity_create_account, container, false);
        mstorageRef = FirebaseStorage.getInstance().getReference("Images");
        txtname = view.findViewById(R.id.name);
        txtnumber  = view.findViewById(R.id.number);
        txtemailid   = view.findViewById(R.id.emailid);
        txtpassword   = view.findViewById(R.id.password);
        txtpasswordhint   = view.findViewById(R.id.passwordhint);
        imagebtn = view.findViewById(R.id.imagebtn);
        selectprofilespinner   = view.findViewById(R.id.selectprofile);
createbtn = view.findViewById(R.id.Createbtn);
clearbtn = view.findViewById(R.id.clear);

        final List<String> spinneritems = new ArrayList<>();
        spinneritems.add("Employee");
        spinneritems.add("Admin");
spinneritems.add("Supervisor");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, spinneritems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectprofilespinner.setAdapter(adapter);

       maxid();
        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
            }
        });
        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validations.ClearDataWithoutHint(txtname,txtnumber,txtpassword,txtpasswordhint,txtemailid);
imagebtn.setImageDrawable(null);
            }
        });

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!txtname.getText().toString().isEmpty() && !txtemailid.getText().toString().isEmpty() && !txtpassword.getText().toString().isEmpty() && !txtpasswordhint.getText().toString().isEmpty()) {
                    if(txtemailid.getText().toString().contains("revolvespl.com") && !txtemailid.getText().toString().contains("gmail.com")) {
                        if (txtnumber.getText().toString().length() == 10) {

                            createbtn.setEnabled(false);
                            onupload(v);

                        } else
                            Validations.Toast(getContext(), "Please enter correct mobileno");
                    }
                    else
                        Validations.Toast(getContext(), "Please enter revolve's emailid");

                    }else
                        Validations.Toast(getContext(), "Please enter all fields");

                } catch (Exception e) {
                    //showAlertDialogButtonClicked();
                }
            }
        });

        return view;
    }

public void maxid(){
    Firebaselinks.myaccountfirebaselink().
            addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            EMpid = Integer.parseInt(childSnapshot.child("id").getValue().toString());
                        }
                        EMpid =    EMpid + 1;
                        //   txtprojid.setText(Projid + 1 + "");
                    }
                    else
                        EMpid = 1 ;
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }


            });

}
    public void select(){
        try{
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
            pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Select photo from gallery",
                    "Capture photo from camera" };
            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    loadImageFromGallery();
                                    break;
                                case 1:
                                    takePhotoFromCamera();
                                    break;
                            }
                        }
                    });
            pictureDialog.show();



        }catch(Exception e){}
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    public void loadImageFromGallery() {
        try{
            Intent chooseImageIntent = new Intent(Intent.ACTION_PICK);
            chooseImageIntent.setType("image/*");
            startActivityForResult(chooseImageIntent, GALLERY);
        }catch(Exception e){}
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY) {
            if (data == null) return;
            try {
                filePath = data.getData();
                Picasso.with(getContext()).load(filePath).into(imagebtn);
                imagebtn.setImageURI(filePath);
                activityresult = "GALLERY";
            }catch(Exception e){}
        }



        else if (requestCode == CAMERA) {
            if (data == null) return;
            try {
                Bitmap thumbnail = (Bitmap)  data.getExtras().get("data");
                filePath = bitmapToUriConverter(thumbnail);
                imagebtn.setImageBitmap(thumbnail);
                activityresult = "CAMERA";
            }catch (Exception e) {
            }
        }
    }


    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);
            File file = new File(getActivity().getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_PRIVATE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            //   Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return uri;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }



    public void onupload(final View v) {
        try {
            final Uri[] downloadUri = {null};
            StorageReference fileReference = null;

                if (activityresult == "GALLERY")
                    fileReference = mstorageRef.child(System.currentTimeMillis() + "");

                if (activityresult == "CAMERA")
                    fileReference = mstorageRef.child(System.currentTimeMillis() + "");


if(filePath != null) {
    mUploadTask = fileReference.putFile(filePath);
    final StorageReference finalFileReference = fileReference;
    Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return finalFileReference.getDownloadUrl();
        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull final Task<Uri> task) {
            if (task.isSuccessful()) {


                final String id = Firebaselinks.myaccountfirebaselink().push().getKey();
                if (Validations.gettexttostring(txtname) != "" && Validations.gettexttostring(txtnumber) != "" &&
                        Validations.gettexttostring(txtemailid) != "" && Validations.gettexttostring(txtpassword) != "" &&
                        Validations.gettexttostring(txtpasswordhint) != ""
                ) {



                    Firebaselinks.myaccountfirebaselink().orderByChild("emailid").equalTo(txtemailid.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount() > 0){
                                Toast.makeText(getContext(), "Email id already exists", Toast.LENGTH_SHORT).show();
                                createbtn.setEnabled(true);
                                return;
                            }
                            else{

                                progressBar = new ProgressDialog(v.getContext());
                                progressBar.setCancelable(true);
                                progressBar.setMessage("Creating your account ...");
                                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressBar.setProgress(0);
                                progressBar.setMax(100);
                                progressBar.show();
                                //reset progress bar and filesize status
                                progressBarStatus = 0;
                                fileSize = 0;
                                new Thread(new Runnable() {
                                    public void run() {
                                        while (progressBarStatus < 100) {
                                            progressBarStatus = downloadFile();

                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            progressBarbHandler.post(new Runnable() {
                                                public void run() {
                                                    progressBar.setProgress(progressBarStatus);
                                                }
                                            });
                                        }

                                        if (progressBarStatus >= 100) {
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            progressBar.dismiss();
                                        }
                                    }
                                }).start();
                                Firebaselinks.myaccountfirebaselink().child(id).child("date").setValue(SetDate.Currentdateinyyyymmdd());
                                Firebaselinks.myaccountfirebaselink().child(id).child("id").setValue(EMpid);
                                Firebaselinks.myaccountfirebaselink().child(id).child("name").setValue(txtname.getText().toString());
                                Firebaselinks.myaccountfirebaselink().child(id).child("number").setValue(txtnumber.getText().toString());
                                Firebaselinks.myaccountfirebaselink().child(id).child("emailid").setValue(txtemailid.getText().toString());
                                Firebaselinks.myaccountfirebaselink().child(id).child("password").setValue(txtpassword.getText().toString());
                                Firebaselinks.myaccountfirebaselink().child(id).child("passwordhint").setValue(txtpasswordhint.getText().toString());
                                Firebaselinks.myaccountfirebaselink().child(id).child("status").setValue("Active");
                                Firebaselinks.myaccountfirebaselink().child(id).child("profile").setValue(selectprofilespinner.getSelectedItem().toString());
                                downloadUri[0] = task.getResult();
                                if(filePath!=null)
                                    Firebaselinks.myaccountfirebaselink().child(id).child("imgpath").setValue(downloadUri[0].toString());
                                else
                                    Firebaselinks.myaccountfirebaselink().child(id).child("imgpath").setValue("");






                                Validations.ClearDataWithoutHint(txtname,txtnumber,txtpassword,txtpasswordhint,txtemailid);
                                imagebtn.setImageDrawable(null);
                                maxid();
                                createbtn.setEnabled(true);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });



                }
            }
        }
    });
}
else{
    final String id = Firebaselinks.myaccountfirebaselink().push().getKey();
    if (Validations.gettexttostring(txtname) != "" && Validations.gettexttostring(txtnumber) != "" &&
            Validations.gettexttostring(txtemailid) != "" && Validations.gettexttostring(txtpassword) != "" &&
            Validations.gettexttostring(txtpasswordhint) != ""
    ) {



        Firebaselinks.myaccountfirebaselink().orderByChild("emailid").equalTo(txtemailid.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    Toast.makeText(getContext(), "Email id already exists", Toast.LENGTH_SHORT).show();
                    createbtn.setEnabled(true);
                    return;
                }
                else{

                    progressBar = new ProgressDialog(v.getContext());
                    progressBar.setCancelable(true);
                    progressBar.setMessage("Creating your account ...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();
                    //reset progress bar and filesize status
                    progressBarStatus = 0;
                    fileSize = 0;
                    new Thread(new Runnable() {
                        public void run() {
                            while (progressBarStatus < 100) {
                                progressBarStatus = downloadFile();

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                progressBarbHandler.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(progressBarStatus);
                                    }
                                });
                            }

                            if (progressBarStatus >= 100) {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                progressBar.dismiss();
                            }
                        }
                    }).start();
                    Firebaselinks.myaccountfirebaselink().child(id).child("date").setValue(SetDate.Currentdateinyyyymmdd());
                    Firebaselinks.myaccountfirebaselink().child(id).child("id").setValue(EMpid);
                    Firebaselinks.myaccountfirebaselink().child(id).child("name").setValue(txtname.getText().toString());
                    Firebaselinks.myaccountfirebaselink().child(id).child("number").setValue(txtnumber.getText().toString());

                    Firebaselinks.myaccountfirebaselink().child(id).child("emailid").setValue(txtemailid.getText().toString());
                    Firebaselinks.myaccountfirebaselink().child(id).child("password").setValue(txtpassword.getText().toString());
                    Firebaselinks.myaccountfirebaselink().child(id).child("passwordhint").setValue(txtpasswordhint.getText().toString());
                    Firebaselinks.myaccountfirebaselink().child(id).child("status").setValue("Active");
                    Firebaselinks.myaccountfirebaselink().child(id).child("profile").setValue(selectprofilespinner.getSelectedItem().toString());
                        Firebaselinks.myaccountfirebaselink().child(id).child("imgpath").setValue("");






                    Validations.ClearDataWithoutHint(txtname,txtnumber,txtpassword,txtpasswordhint,txtemailid);
                    imagebtn.setImageDrawable(null);
                    maxid();
                    createbtn.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }
}



        }catch (Exception e){}
    }

    public int downloadFile() {
        while (fileSize <= 1000000) {
            fileSize++;

            if (fileSize == 100000) {
                return 10;
            }else if (fileSize == 200000) {
                return 20;
            }else if (fileSize == 300000) {
                return 30;
            }else if (fileSize == 400000) {
                return 40;
            }else if (fileSize == 500000) {
                return 50;
            }else if (fileSize == 700000) {
                return 70;
            }else if (fileSize == 800000) {
                return 80;
            }
        }
        return 100;
    }




}
