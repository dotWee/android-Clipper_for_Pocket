package de.dotwee.pocketclipper.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.dotwee.pocketclipper.R;

public class StartAuthFragment extends Fragment implements View.OnClickListener {

    private Callback callback;

    public StartAuthFragment() {
    }

    public static StartAuthFragment newInstance(Callback callback) {
        StartAuthFragment fragment = new StartAuthFragment();
        fragment.setCallback(callback);
        return fragment;
    }

    private void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonStart).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {

                case R.id.buttonStart:
                    if (callback != null) {
                        callback.onClickStart();
                        return;
                    } else throw new IllegalStateException("Callback is null");
            }
        } else throw new IllegalStateException("View is null");
    }

    public interface Callback {

        void onClickStart();
    }
}
