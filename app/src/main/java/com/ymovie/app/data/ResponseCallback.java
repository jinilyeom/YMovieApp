package com.ymovie.app.data;

public interface ResponseCallback<T> {
    public abstract void onSuccess(T data);
    public abstract void onFailure(Throwable t);
}
