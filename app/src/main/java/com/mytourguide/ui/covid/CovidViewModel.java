package com.mytourguide.ui.covid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CovidViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CovidViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Covid-19 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}