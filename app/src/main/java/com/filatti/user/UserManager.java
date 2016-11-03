package com.filatti.user;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

public final class UserManager {
    private static final String DB_KEY_USERS = "users";

    private static UserManager sUserManager = null;

    public static UserManager getInstance() {
        if (sUserManager == null) {
            sUserManager = new UserManager();
        }
        return sUserManager;
    }

    private String mUid;
    private User mUser;

    private UserManager() {
    }

    public String getUid() {
        return mUid;
    }

    public User getUser() {
        return mUser;
    }

    public void setFirebaseUser(final FirebaseUser firebaseUser) {
        mUid = firebaseUser.getUid();

        final DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference().child(DB_KEY_USERS).child(mUid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mUser = dataSnapshot.getValue(User.class);
                } else {
                    User user = User.create(firebaseUser);
                    Task<Void> task = databaseReference.setValue(user);
                    if (task.isSuccessful()) {
                        Timber.d("Successfully created a user");
                        mUser = user;
                    } else {
                        Timber.e("Unsuccessfully created a user");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e("Unsuccessfully got an user: %s", databaseError.getMessage());
            }
        });
    }
}
