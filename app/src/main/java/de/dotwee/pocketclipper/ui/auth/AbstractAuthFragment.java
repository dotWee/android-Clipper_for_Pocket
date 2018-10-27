package de.dotwee.pocketclipper.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.dotwee.pocketclipper.R;
import timber.log.Timber;

public abstract class AbstractAuthFragment<M extends AbstractAuthViewModel> extends Fragment implements AbstractAuthViewModel.Callback {

    protected TextView textViewTitle;
    protected TextView textViewMessage;
    protected ProgressBar progressBar;
    protected Button buttonStart;
    protected Button buttonNext;

    protected M viewModel;

    public M getViewModel() {
        return viewModel;
    }

    protected void setViewModel(final M viewModel) {
        Timber.i("setViewModel");

        this.viewModel = viewModel;

        textViewTitle.setText(viewModel.title);
        textViewMessage.setText(viewModel.message);
        progressBar.setVisibility(viewModel.progressBarVisibility ? View.VISIBLE : View.INVISIBLE);
        buttonStart.setEnabled(viewModel.buttonStartEnabled);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("onClick buttonStart");
                viewModel.onStart();
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("onClick buttonNext");
                viewModel.onStart();
            }
        });

        buttonNext.setVisibility(viewModel.buttonNextVisibility ? View.VISIBLE : View.INVISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth_base, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewMessage = view.findViewById(R.id.textViewMessage);
        progressBar = view.findViewById(R.id.progressBar);
        buttonStart = view.findViewById(R.id.buttonStart);
        buttonNext = view.findViewById(R.id.buttonNext);
    }

    @Override
    public void onDisplayStartup() {
        progressBar.setVisibility(View.VISIBLE);
        buttonStart.setEnabled(false);
        buttonNext.setVisibility(View.INVISIBLE);
        textViewMessage.setText("Performing request...");
    }

    @Override
    public void onDisplaySuccess() {
        progressBar.setVisibility(View.INVISIBLE);
        buttonStart.setEnabled(false);

        buttonStart.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        textViewMessage.setText("Success!");
    }

    @Override
    public void onDisplayError(Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);
        buttonStart.setEnabled(true);
        buttonNext.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDisplayCompleted() {
        progressBar.setVisibility(View.INVISIBLE);
        buttonNext.setEnabled(true);

        if (buttonNext.getVisibility() == View.VISIBLE) {
            buttonStart.setEnabled(false);
        }
    }
}
