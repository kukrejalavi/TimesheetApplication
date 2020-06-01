package com.example.timesheetapplication.Admin.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timesheetapplication.Firebaselinks;
import com.example.timesheetapplication.R;
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
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
public class Profile extends Fragment {
View view;
TextView passwordtext;
EditText txtname,txtnumber,txtemailid,txtpassword;
Spinner txtprofile;
ImageButton editbtn;
    ImageView imagebtn;
Button Savebtn,inactivebtn;
    Uri filePath;
    StorageReference mstorageRef;
    String activityresult;
    private int GALLERY = 1, CAMERA = 2;
public String empid,type;
    private StorageTask mUploadTask;
    final List<String> role = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "Edit Profile";
        Objects.requireNonNull(getActivity()).setTitle(title);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.activity_profile, container, false);
        mstorageRef = FirebaseStorage.getInstance().getReference("Images");

txtprofile = view.findViewById(R.id.txtprofile);
passwordtext = view.findViewById(R.id.passwordtxt);
txtname = view.findViewById(R.id.txtname);
        txtnumber = view.findViewById(R.id.txtnumber);
        txtemailid = view.findViewById(R.id.txtemailid);
        txtpassword = view.findViewById(R.id.txtpassword);
Savebtn = view.findViewById(R.id.Savebtn);
inactivebtn = view.findViewById(R.id.inactivebtn);
editbtn = view.findViewById(R.id.editbtn);
        imagebtn = view.findViewById(R.id.imagebtn);


        final Bundle bundle = this.getArguments();
        if (bundle != null) {

txtname.setText(bundle.getString("name"));

            imagebtn.setEnabled(false);
            txtprofile.setEnabled(false);
            txtname.setEnabled(false);
            txtnumber.setEnabled(false);
            txtemailid.setEnabled(false);
            txtpassword.setEnabled(false);

Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getChildrenCount() > 0){
            for(DataSnapshot child : dataSnapshot.getChildren()){
                try{
                if(child.child("id").getValue().toString().equals(bundle.getString("id"))) {
                    if (role.size() > 0)
                        role.clear();
                    role.add(child.child("profile").getValue().toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, role);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    txtprofile.setAdapter(adapter);
                    txtnumber.setText(child.child("number").getValue().toString());
                    txtemailid.setText(child.child("emailid").getValue().toString());
                    txtpassword.setVisibility(View.GONE);
                    passwordtext.setVisibility(View.GONE);
                    String image = child.child("imgpath").getValue().toString();
                    if (image != "")
                        Picasso.with(getContext()).load(image).into(imagebtn);
                }
                }catch(Exception e){}
            }
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
});




            Savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Savebtn.setVisibility(View.INVISIBLE);
                    inactivebtn.setVisibility(View.GONE);
                    editbtn.setVisibility(View.VISIBLE);
                    imagebtn.setEnabled(false);
                    onupload(bundle.getString("id"));


                }
            });
        }



else {
            if (type.equals("Employee")) {
                editbtn.setVisibility(View.INVISIBLE);
                inactivebtn.setVisibility(View.INVISIBLE);
                Savebtn.setVisibility(View.INVISIBLE);
            }

            imagebtn.setEnabled(false);
            txtprofile.setEnabled(false);
            txtname.setEnabled(false);
            txtnumber.setEnabled(false);
            txtemailid.setEnabled(false);
            txtpassword.setEnabled(false);

            Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.child("id").getValue().toString().equals(empid)) {

                                if(role.size() > 0)
                                    role.clear();
                                role.add(child.child("profile").getValue().toString());
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                        android.R.layout.simple_spinner_item, role);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                txtprofile.setAdapter(adapter);
                                txtname.setText(child.child("name").getValue().toString());
                                txtnumber.setText(child.child("number").getValue().toString());
                                txtemailid.setText(child.child("emailid").getValue().toString());
                                int length = child.child("password").getValue().toString().length();
                                String chhar="" ;
                                for(int i=0;i<length;i++){
                                    chhar+= "*";
                                }
                                txtpassword.setText(chhar);
                                String image = child.child("imgpath").getValue().toString();
                                if(image!="")
                                    Picasso.with(getContext()).load(image).into(imagebtn);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


            Savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Savebtn.setVisibility(View.INVISIBLE);
                    inactivebtn.setVisibility(View.GONE);
                    editbtn.setVisibility(View.VISIBLE);
                    imagebtn.setEnabled(false);
                    onupload(empid);


                }
            });



            txtprofile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });



        }
            imagebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select();
                }
            });

            editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Savebtn.setVisibility(View.VISIBLE);
                   // inactivebtn.setVisibility(View.VISIBLE);
                    editbtn.setVisibility(View.INVISIBLE);
                    txtprofile.setEnabled(true);

                    if(txtprofile.getSelectedItem().toString().equals("Employee")) {
                        if(role.size() > 0)
                            role.clear();
                        role.add("Employee");
                        role.add("Admin");

                        role.add("Supervisor");
                    }
                    else if(txtprofile.getSelectedItem().toString().equals("Admin")){
                        if(role.size() > 0)
                            role.clear();

                        role.add("Admin");
                        role.add("Employee");
                        role.add("Supervisor");
                    }
                    else{
                        if(role.size() > 0)
                            role.clear();

                        role.add("Supervisor");
                        role.add("Admin");
                        role.add("Employee");

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, role);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    txtprofile.setAdapter(adapter);


                    imagebtn.setEnabled(true);
                    txtname.setEnabled(true);
                    txtnumber.setEnabled(true);
                    txtemailid.setEnabled(true);
                    txtpassword.setEnabled(true);
                }
            });

inactivebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {



        Firebaselinks.myaccountfirebaselink().orderByChild("emailid").equalTo(txtemailid.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("status").getValue().toString().equals("Active")){
                            String key = child.getKey();
                            Firebaselinks.myaccountfirebaselink().child(key + "/status").setValue("Inactive");
                        }
                    }
                    inactivebtn.setVisibility(View.GONE);
                    Savebtn.setVisibility(View.GONE);
                    editbtn.setVisibility(View.GONE);

                    imagebtn.setEnabled(false);
                    txtprofile.setEnabled(false);
                    txtname.setEnabled(false);
                    txtnumber.setEnabled(false);
                    txtemailid.setEnabled(false);
                    txtpassword.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
});

        return view;
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


    public void onupload(final String empid) {
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




                            Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            if (child.child("id").getValue().toString().equals(empid)) {
                                                String clubkey = child.getKey();
                                                Firebaselinks.myaccountfirebaselink().child(clubkey + "/profile").setValue(txtprofile.getSelectedItem().toString());
                                                Firebaselinks.myaccountfirebaselink().child(clubkey + "/name").setValue(txtname.getText().toString());
                                                Firebaselinks.myaccountfirebaselink().child(clubkey + "/number").setValue(txtnumber.getText().toString());
                                                Firebaselinks.myaccountfirebaselink().child(clubkey + "/emailid").setValue(txtemailid.getText().toString());
                                                if (txtpassword.getVisibility() == View.VISIBLE) {
                                                    if (!txtpassword.getText().toString().contains("*"))
                                                        Firebaselinks.myaccountfirebaselink().child(clubkey + "/password").setValue(txtpassword.getText().toString());
                                                }
                                              downloadUri[0] = task.getResult();
                                                    Firebaselinks.myaccountfirebaselink().child(clubkey + "/imgpath").setValue(downloadUri[0].toString());
                                            }
                                        }

                                        txtname.setEnabled(false);
                                        txtnumber.setEnabled(false);
                                        txtemailid.setEnabled(false);
                                        txtpassword.setEnabled(false);
                                        txtprofile.setEnabled(false);

                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });






                        }
                    }
                });
            }



            else{

                Firebaselinks.myaccountfirebaselink().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                if (child.child("id").getValue().toString().equals(empid)) {
                                    String clubkey = child.getKey();
                                    Firebaselinks.myaccountfirebaselink().child(clubkey + "/profile").setValue(txtprofile.getSelectedItem().toString());
                                    Firebaselinks.myaccountfirebaselink().child(clubkey + "/name").setValue(txtname.getText().toString());
                                    Firebaselinks.myaccountfirebaselink().child(clubkey + "/number").setValue(txtnumber.getText().toString());
                                    Firebaselinks.myaccountfirebaselink().child(clubkey + "/emailid").setValue(txtemailid.getText().toString());

                                    if (txtpassword.getVisibility() == View.VISIBLE) {
                                        if (!txtpassword.getText().toString().contains("*"))
                                            Firebaselinks.myaccountfirebaselink().child(clubkey + "/password").setValue(txtpassword.getText().toString());
                                    }
                                }
                            }

                            txtname.setEnabled(false);
                            txtnumber.setEnabled(false);
                            txtemailid.setEnabled(false);
                            txtpassword.setEnabled(false);
                            txtprofile.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }


                             }catch (Exception e){}

                        }
                    }






