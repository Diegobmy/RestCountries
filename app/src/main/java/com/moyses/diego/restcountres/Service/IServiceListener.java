package com.moyses.diego.restcountres.Service;

public interface IServiceListener<T> {


    void onSuccess(T response);
    void onError();
}
